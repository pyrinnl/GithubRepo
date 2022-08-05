package com.pyrinnl.githubrepo.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.databinding.FragmentDetailInfoBinding
import com.pyrinnl.githubrepo.ui.detail.RepositoryInfoViewModel.*
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {

    private val args: DetailInfoFragmentArgs by navArgs()

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    private lateinit var markwon: Markwon

    private lateinit var binding: FragmentDetailInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailInfoBinding.bind(view)

        markwon = Markwon.create(requireContext())

        getRepository()

        viewModel.state.observe(viewLifecycleOwner) {
            binding.observeViewState(it) }
    }

    private fun getRepository() {
        val repoName = args.repoName
        binding.toolbar.textViewToolBar.text = repoName
        viewModel.getRepository(repoName)
    }


    private fun FragmentDetailInfoBinding.observeViewState(state: State) {


        when (state) {
            is State.Error -> {}
            is State.Loaded -> {
                linkTV.text = state.repoDetails.url.substringAfter("https://").trim()
                starsCountTV.text = state.repoDetails.stars.toString()
                forksCountTV.text = state.repoDetails.forks.toString()
                watchersCountTV.text = state.repoDetails.watchers.toString()
                licenseTypeTV.text = state.repoDetails.license

                readmeProgressBar.visibility =
                    if (state.readmeState is ReadmeState.Loading) View.VISIBLE else View.GONE

               if (state.readmeState is ReadmeState.Loaded){
                  val markdown =  state.readmeState.markdown
                   markwon.setMarkdown(readmeTV, markdown)
               } else return
            }
            is State.Loading -> {}
        }
        detailInfoLayoutIn.visibility =
            if (state is State.Loading)
                View.GONE
            else View.VISIBLE

        startProgressBar.visibility =
            if (state is State.Loading)
                View.VISIBLE
            else View.GONE

    }
}