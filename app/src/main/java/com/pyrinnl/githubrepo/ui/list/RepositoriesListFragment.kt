package com.pyrinnl.githubrepo.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.adapters.RepoAdapter
import com.pyrinnl.githubrepo.databinding.FragmentRepositoriesListBinding
import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State.Error
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State.Loaded
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State.Loading
import com.pyrinnl.githubrepo.utills.setTextColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {

    private val viewModel by viewModels<RepositoriesListViewModel>()
    private lateinit var binding: FragmentRepositoriesListBinding
    private lateinit var adapter: RepoAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepositoriesListBinding.bind(view)
        viewModel.state.observe(viewLifecycleOwner) { binding.observeViewState(it) }
        binding.errorItem.button.setOnClickListener { viewModel.onRetryButtonPressed() }
        prepareToolbar()
    }

    private fun onItemClick(repo: Repo) {
        routeToDetailInfoFragment(repo.name)
    }

    private fun prepareAdapter(state: State) {
        if (state !is Loaded) return
        val repos = state.repos
        adapter = RepoAdapter { onItemClick(repos[it]) }
        adapter.reposData = repos
        binding.recyclerView.adapter = adapter
    }

    private fun FragmentRepositoriesListBinding.observeViewState(state: State) {
        progressBar.visibility = if (state is Loading) View.VISIBLE else View.GONE
        errorItem.layout.visibility = if (state is Error) View.VISIBLE else View.GONE

        when (state) {
            is Error -> {
                with(errorItem) {
                    errorTitle.setText(state.error.title)
                    errorTitle.setTextColor(requireContext(), state.error.colorTitle)
                    errorDescription.setText(state.error.text)
                    errorIcon.setImageResource(state.error.errorIcon)
                    button.setText(state.error.buttonText)
                }
            }
            is Loaded -> {
                prepareAdapter(state)
            }
            Loading -> return
        }
    }

    private fun routeToDetailInfoFragment(repoName: String) {
        val destination =
            RepositoriesListFragmentDirections.actionRepositoriesListFragmentToDetailInfoFragment(
                repoName
            )
        findNavController().navigate(destination)
    }

    private fun prepareToolbar() {
        binding.toolbar.textViewToolBar.text = getString(R.string.repositories_toolbar_title)
        binding.toolbar.logoutIcon.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_repositoriesListFragment_to_authFragment)
        }
    }
}
