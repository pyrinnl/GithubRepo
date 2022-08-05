package com.pyrinnl.githubrepo.data.retrofit.entities

import com.pyrinnl.githubrepo.model.entities.Readme
import kotlinx.serialization.Serializable

@Serializable
data class GetRepoReadmeResponseEntity(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val url: String,
    val html_url: String,
    val git_url: String,
    val download_url: String,
    val type: String,
    val content: String,
    val encoding: String,
    val _links: Links
) {

    fun mapToReadme(): Readme {
        return Readme(
            content = content
        )
    }

}

@Serializable
data class Links(
    val self: String,
    val git: String,
    val html: String
)