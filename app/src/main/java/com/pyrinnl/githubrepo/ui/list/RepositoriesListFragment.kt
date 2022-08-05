package com.pyrinnl.githubrepo.ui.list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.adapters.RepoAdapter
import com.pyrinnl.githubrepo.databinding.FragmentRepositoriesListBinding
import com.pyrinnl.githubrepo.model.ConnectionException
import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State.Error
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State.Loaded
import com.pyrinnl.githubrepo.ui.list.RepositoriesListViewModel.State.Loading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {

    private val viewModel by viewModels<RepositoriesListViewModel>()
    private lateinit var binding: FragmentRepositoriesListBinding
    private lateinit var adapter: RepoAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepositoriesListBinding.bind(view)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state is Loading) View.VISIBLE else View.INVISIBLE
            binding.emptyReposItem.root.visibility = if(state is State.Empty) View.VISIBLE else View.INVISIBLE
            renderError(state)
            prepareAdapter(state)
        }
        prepareToolbar()
    }

    private fun prepareAdapter(state: State) {
        if (state !is Loaded) return

        val repos = state.repos
        adapter = RepoAdapter { onItemClick(repos[it]) }
        adapter.reposData = repos
        binding.recyclerView.adapter = adapter
    }

    private fun onItemClick(repo: Repo) {
        val destination = RepositoriesListFragmentDirections.actionRepositoriesListFragmentToDetailInfoFragment(repo.name) //Пиздец
        findNavController().navigate(destination)
    }

    private fun renderError(state: State) {
        if(state !is Error) return

        if(state.e is ConnectionException)
            binding.connectionErrorItem.root.visibility = View.VISIBLE
        else
            binding.somethingErrorItem.root.visibility = View.VISIBLE

    }

    private fun prepareToolbar() {
        binding.toolbar.textViewToolBar.text = "Repositories"
        binding.toolbar.logoutIcon.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_repositoriesListFragment_to_authFragment)
        }
    }
}
