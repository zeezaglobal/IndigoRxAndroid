package ca.zeezaglobal.indigorx.Presentation.screens.base

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseFragment : Fragment() {
    protected fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    protected fun navigateBack() {
        findNavController().popBackStack()
    }
}