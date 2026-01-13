package ca.zeezaglobal.indigorx.Presentation.screens.auth.login

import android.animation.ObjectAnimator
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import ca.zeezaglobal.indigorx.R
import ca.zeezaglobal.indigorx.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialState()
        playEntryAnimations()
        setupClickListeners()
        setupTextWatchers()
        observeUiState()
        observeNavigationEvents()
    }

    private fun setupInitialState() {
        binding.apply {
            logoSection.alpha = 0f
            tvWelcome.alpha = 0f
            tvSubtitle.alpha = 0f
            tilEmail.alpha = 0f
            tilPassword.alpha = 0f
            tvForgotPassword.alpha = 0f
            btnLogin.alpha = 0f
            dividerSection.alpha = 0f
            registerSection.alpha = 0f

            logoSection.translationY = -50f
            tvWelcome.translationY = 30f
            tvSubtitle.translationY = 30f
            tilEmail.translationX = -100f
            tilPassword.translationX = 100f
            btnLogin.scaleX = 0.8f
            btnLogin.scaleY = 0.8f
        }
    }

    private fun playEntryAnimations() {
        val duration = 500L
        val interpolator = DecelerateInterpolator()

        binding.logoSection.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        binding.tvWelcome.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(150)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        binding.tvSubtitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(200)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        binding.tilEmail.animate()
            .alpha(1f)
            .translationX(0f)
            .setStartDelay(300)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        binding.tilPassword.animate()
            .alpha(1f)
            .translationX(0f)
            .setStartDelay(400)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        binding.tvForgotPassword.animate()
            .alpha(1f)
            .setStartDelay(500)
            .setDuration(duration)
            .start()

        binding.btnLogin.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(550)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        binding.dividerSection.animate()
            .alpha(1f)
            .setStartDelay(650)
            .setDuration(duration)
            .start()

        binding.registerSection.animate()
            .alpha(1f)
            .setStartDelay(750)
            .setDuration(duration)
            .start()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            animateButton(it) {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString()
                viewModel.login(email, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            animateClickAndNavigate(it) {
                // Navigate to forgot password
                // findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }
        }

        binding.tvRegister.setOnClickListener {
            animateClickAndNavigate(it) {
                val intent = Intent(Intent.ACTION_VIEW, "https://indigorx.me/register".toUri())
                startActivity(intent)
            }
        }
    }

    private fun setupTextWatchers() {
        binding.etEmail.doAfterTextChanged {
            binding.tilEmail.error = null
            viewModel.clearFieldErrors()
        }

        binding.etPassword.doAfterTextChanged {
            binding.tilPassword.error = null
            viewModel.clearFieldErrors()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: LoginUiState) {
        // Handle loading state
        showLoading(state.isLoading)

        // Handle field errors with shake animation
        state.usernameError?.let { error ->
            binding.tilEmail.error = error
            shakeView(binding.tilEmail)
        }

        state.passwordError?.let { error ->
            binding.tilPassword.error = error
            shakeView(binding.tilPassword)
        }

        // Handle general errors
        state.error?.let { errorMessage ->
            showErrorSnackbar(errorMessage)
            viewModel.clearError()
        }
    }

    private fun observeNavigationEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        is LoginNavigationEvent.NavigateToHome -> {
                            // Play success animation before navigating
                            playSuccessAnimation {
                               // findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                        }
                        is LoginNavigationEvent.NavigateToForgotPassword -> {
                            // findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
                        }
                        is LoginNavigationEvent.NavigateToRegister -> {
                            val intent = Intent(Intent.ACTION_VIEW,
                                "https://indigorx.me/register".toUri())
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !show
            btnLogin.text = if (show) "" else getString(R.string.login)
            etEmail.isEnabled = !show
            etPassword.isEnabled = !show
        }
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(R.color.error, null))
            .setTextColor(resources.getColor(R.color.white, null))
            .show()
    }

    private fun playSuccessAnimation(onComplete: () -> Unit) {
        // Scale down and fade out animation
        binding.root.animate()
            .alpha(0.8f)
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(200)
            .withEndAction {
                onComplete()
            }
            .start()
    }

    private fun shakeView(view: View) {
        val shake = ObjectAnimator.ofFloat(
            view,
            "translationX",
            0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f
        )
        shake.duration = 500
        shake.start()
    }

    private fun animateClickAndNavigate(view: View, action: () -> Unit) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { action() }
                    .start()
            }
            .start()
    }

    private fun animateButton(view: View, action: () -> Unit) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { action() }
                    .start()
            }
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}