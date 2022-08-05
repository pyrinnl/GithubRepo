package com.pyrinnl.githubrepo.model.entities

data class RepoDetails(
    val name: String,
    val stars: Int,
    val forks: Int,
    val watchers: Int,
    val url: String,
    val license: String?
)