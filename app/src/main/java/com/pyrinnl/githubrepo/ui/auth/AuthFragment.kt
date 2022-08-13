package com.pyrinnl.githubrepo.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.*
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.databinding.FragmentAuthBinding
import com.pyrinnl.githubrepo.ui.auth.AuthViewModel.State.Loading
import com.pyrinnl.githubrepo.ui.dialog.ErrorDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var binding: FragmentAuthBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAuthBinding.bind(view)
        this.bindProgressButton(binding.signInButton)

        binding.tokenEditText.bindTextTwoWay(viewModel.token, viewLifecycleOwner)

        binding.signInButton.setOnClickListener { viewModel.onSignButtonPressed() }

        observeState()
        observeActions()
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) { state ->
        binding.tokenTextInput.error =
            if (state is AuthViewModel.State.InvalidInput) getString(state.reason) else null

        binding.tokenTextInput.isEnabled = state !is Loading

        binding.signInButton.apply {
            if (state is Loading) {
                showProgress{
                    progressColor = Color.WHITE
                }
            } else {
                hideProgress(R.string.sign_in_button)
            }
        }
    }

    private fun observeActions() = lifecycleScope.launch {
        viewModel.actions.collect { handleActions(it) }
    }

    private fun handleActions(action: AuthViewModel.Action) {
        when (action) {
            AuthViewModel.Action.RouteToMain -> routeToRepositoriesListFragment()
            is AuthViewModel.Action.ShowError -> showErrorDialogFragment(action.message)
        }
    }

    private fun showErrorDialogFragment(message: Int) {
        val errorMessage = getString(message)
        ErrorDialogFragment.show(this.requireActivity().supportFragmentManager, errorMessage)
    }

    private fun EditText.bindTextTwoWay(
        liveData: MutableLiveData<String>,
        lifecycleOwner: LifecycleOwner
    ) {
        this.doOnTextChanged { s, _, _, _ ->
            liveData.value = s.toString()
        }

        liveData.observe(lifecycleOwner) { text ->
            if (this.text.toString() == text) return@observe

            this.setText(text)
        }
    }
    private fun routeToRepositoriesListFragment() {
        findNavController().navigate(R.id.action_authFragment_to_repositoriesListFragment)
    }
}

