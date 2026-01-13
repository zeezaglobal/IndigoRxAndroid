package ca.zeezaglobal.indigorx.Presentation.screens.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ca.zeezaglobal.indigorx.R
import ca.zeezaglobal.indigorx.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupInitialState() {
        // Set initial alpha to 0 for animation
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

            // Set initial translation
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

        // Logo animation
        binding.logoSection.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        // Welcome text
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

        // Email field slides from left
        binding.tilEmail.animate()
            .alpha(1f)
            .translationX(0f)
            .setStartDelay(300)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        // Password field slides from right
        binding.tilPassword.animate()
            .alpha(1f)
            .translationX(0f)
            .setStartDelay(400)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        // Forgot password
        binding.tvForgotPassword.animate()
            .alpha(1f)
            .setStartDelay(500)
            .setDuration(duration)
            .start()

        // Login button with scale
        binding.btnLogin.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setStartDelay(550)
            .setDuration(duration)
            .setInterpolator(interpolator)
            .start()

        // Divider and Google button
        binding.dividerSection.animate()
            .alpha(1f)
            .setStartDelay(650)
            .setDuration(duration)
            .start()

        // Register section
        binding.registerSection.animate()
            .alpha(1f)
            .setStartDelay(750)
            .setDuration(duration)
            .start()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            animateClickAndNavigate(it) {
                // Navigate to forgot password
            }
        }

        binding.tvRegister.setOnClickListener {
            animateClickAndNavigate(it) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://indigorx.me/register"))
                startActivity(intent)
            }
        }
    }

    private fun setupTextWatchers() {
        binding.etEmail.doAfterTextChanged {
            binding.tilEmail.error = null
        }

        binding.etPassword.doAfterTextChanged {
            binding.tilPassword.error = null
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            shakeView(binding.tilEmail)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Invalid email format"
            shakeView(binding.tilEmail)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            shakeView(binding.tilPassword)
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            shakeView(binding.tilPassword)
            isValid = false
        }

        return isValid
    }

    private fun performLogin() {
        showLoading(true)

        // Animate button
        binding.btnLogin.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                binding.btnLogin.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()

        // TODO: Call your login use case here
        // For now, simulate network delay
        binding.root.postDelayed({
            showLoading(false)
            // Navigate to dashboard on success
            // findNavController().navigate(R.id.action_login_to_dashboard)
        }, 2000)
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !show
            btnLogin.text = if (show) "" else "Sign In"
            etEmail.isEnabled = !show
            etPassword.isEnabled = !show
        }
    }

    private fun shakeView(view: View) {
        val shake = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
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