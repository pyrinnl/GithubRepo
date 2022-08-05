package com.pyrinnl.githubrepo.model.entities

data class Repo(
    val id: Int,
    val name: String,
    val description: String?,
    val language: String?
)