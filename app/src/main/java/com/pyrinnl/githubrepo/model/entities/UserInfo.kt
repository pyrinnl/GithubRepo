package com.pyrinnl.githubrepo.model.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val ownerName: String
)