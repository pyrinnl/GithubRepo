package com.pyrinnl.githubrepo.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.databinding.FragmentDetailInfoBinding
import com.pyrinnl.githubrepo.ui.detail.RepositoryInfoViewModel.*
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {

    private val args: DetailInfoFragmentArgs by navArgs()

    private var repoName by Delegates.notNull<String>()

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    private lateinit var markwon: Markwon

    private lateinit var binding: FragmentDetailInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailInfoBinding.bind(view)
        markwon = Markwon.create(requireContext())
        repoName = args.repoName
        prepareToolbar(repoName)

        viewModel.state.observe(viewLifecycleOwner) { binding.observeViewState(it) }
    }

    private fun prepareToolbar(repoName: String) {
        binding.toolbar.textViewToolBar.text = repoName
        binding.toolbar.logoutIcon.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_detailInfoFragment_to_authFragment)
        }
        binding.toolbar.backIcon.apply {
            visibility = View.VISIBLE
            setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun FragmentDetailInfoBinding.observeViewState(state: State) {
        startProgressBar.visibility =
            if (state is State.Loading) View.VISIBLE else View.GONE
        errorItem.layout.visibility = if (state is State.Error) View.VISIBLE else View.GONE

        detailInfoLayout.visibility = if (state is State.Loaded) View.VISIBLE else View.GONE

        when (state) {
            is State.Error -> {
                binding.renderDetailInfoError(state)
            }
            is State.Loaded -> {
                renderDetailInfoPart(state)
                renderReadmePartWithError(state.readmeState)
            }
            State.Loading -> return
        }
    }

    private fun FragmentDetailInfoBinding.renderReadmePartWithError(state: ReadmeState) {
        errorItem.layout.visibility = if (state is ReadmeState.Error) View.VISIBLE else View.GONE
        readmeProgressBar.visibility = if (state is ReadmeState.Loading) View.VISIBLE else View.GONE
        when (state) {
            is ReadmeState.Empty -> {
                readmeTV.text = getString(R.string.no_readme)
            }
            is ReadmeState.Error -> {
                state.errorTexts.apply {
                    errorItem.errorTitle.text = getString(title)
                    errorItem.errorTitle.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            colorTitle
                        )
                    )
                    errorItem.errorDescription.text = getString(text)
                    errorItem.errorIcon.setImageResource(errorIcon)
                    errorItem.button.setText(buttonText)
                    errorItem.button.setOnClickListener {
                        viewModel.onRetryReadmeButtonPressed(
                            repoName
                        )
                    }
                }
            }
            is ReadmeState.Loaded -> {
                val markdown = state.markdown
                markwon.setMarkdown(readmeTV, markdown)
            }
            ReadmeState.Loading -> return
        }
    }

    private fun FragmentDetailInfoBinding.renderDetailInfoError(state: State.Error) {

        state.error.apply {
            errorItem.errorTitle.text = getString(title)
            errorItem.errorTitle.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    colorTitle
                )
            )
            errorItem.errorDescription.text = getString(text)
            errorItem.errorIcon.setImageResource(errorIcon)
            errorItem.button.setText(buttonText)
            errorItem.button.setOnClickListener { viewModel.onRetryRepoDetailsPressed(repoName) }
        }
    }

    private fun FragmentDetailInfoBinding.renderDetailInfoPart(state: State.Loaded) {
        linkTV.text = state.repoDetails.url.substringAfter("https://").trim()
        starsCountTV.text = state.repoDetails.stars.toString()
        forksCountTV.text = state.repoDetails.forks.toString()
        watchersCountTV.text = state.repoDetails.watchers.toString()
        licenseTypeTV.text = state.repoDetails.license
    }

}