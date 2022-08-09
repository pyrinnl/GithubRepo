package com.pyrinnl.githubrepo.model.entities

import com.pyrinnl.githubrepo.model.LanguageColor


data class Repo(
    val id: Int,
    val name: String,
    val description: String?,
    val language: String?,
) {
    val languageColor: Int
        get() = when(language) {
            LanguageColor.Java.language -> LanguageColor.Java.color
            LanguageColor.Kotlin.language -> LanguageColor.Kotlin.color
            LanguageColor.JavaScript.language -> LanguageColor.JavaScript.color
            LanguageColor.Swift.language -> LanguageColor.Swift.color
            LanguageColor.Python.language -> LanguageColor.Python.color
            LanguageColor.ObjectiveC.language -> LanguageColor.ObjectiveC.color
            LanguageColor.TypeScript.language -> LanguageColor.TypeScript.color
            LanguageColor.PHP.language -> LanguageColor.PHP.color
            LanguageColor.Handlebars.language -> LanguageColor.Handlebars.color
            else ->  LanguageColor.UncoveredCase.color
        }
}