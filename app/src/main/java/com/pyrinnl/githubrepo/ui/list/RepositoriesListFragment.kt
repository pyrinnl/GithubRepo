package com.pyrinnl.githubrepo.ui.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.databinding.FragmentRepositoriesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {

    private val viewModel by viewModels<RepositoriesListViewModel>()
    private lateinit var binding: FragmentRepositoriesListBinding
}