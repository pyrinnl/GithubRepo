package com.pyrinnl.githubrepo.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pyrinnl.githubrepo.databinding.ItemRepoBinding
import com.pyrinnl.githubrepo.model.entities.Repo

class RepoAdapter(private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<RepoAdapter.ReposViewHolder>() {

    var reposData: List<Repo> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(inflater, parent, false)
        return ReposViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val repo = reposData[position]
        with(holder.binding) {
            repoNameTV.text = repo.name
            repoDescriptionTV.text = repo.description
            repoLanguageTV.text = repo.language
        }
        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount(): Int = reposData.size

    class ReposViewHolder(val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root)


}