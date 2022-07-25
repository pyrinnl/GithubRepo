package com.pyrinnl.githubrepo.data.retrofit.entities

import com.pyrinnl.githubrepo.model.entities.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SignInResponseEntity(
    private val login: String,
    private val id: Int,
    private val company: String?,
    private val location: String?,
    private val email: String?,
    private val bio: String?,
    @SerialName("public_repos") private val publicRepos: Int,
    @SerialName("public_gists") private val publicGists: Int,
    private val followers: Int,
    private val following: Int,
    @SerialName("created_at") private val createdAt: String?,
    @SerialName("updated_at") private val updatedAt: String?,
) {
    fun mapToUserInfo(): UserInfo {
        return UserInfo(
            ownerName = login
        )
    }
}