package com.pyrinnl.githubrepo.ui.splash


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pyrinnl.githubrepo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSignedIn.observe(viewLifecycleOwner) { isSignIn ->
            launchMainScreen(isSignIn)
        }
    }

    private fun launchMainScreen(isSignIn: Boolean) {
        lifecycleScope.launch(Dispatchers.Main) {

            if (isSignIn) {
                viewModel.getUserInfo()
                routeToRepositoriesListFragment()
            } else {
                delay(300)
                routeToAuthFragment()

            }
        }
    }

    private fun routeToAuthFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_authFragment)
    }

    private fun routeToRepositoriesListFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_repositoriesListFragment)
    }
}