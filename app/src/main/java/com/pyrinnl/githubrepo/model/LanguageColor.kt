package com.pyrinnl.githubrepo.model

import androidx.annotation.ColorRes
import com.pyrinnl.githubrepo.R


enum class LanguageColor(var language: String, @ColorRes val color: Int) {
    Java("Java", R.color.java),
    Kotlin("Kotlin", R.color.kotlin),
    JavaScript("JavaScript", R.color.javascript),
    Swift("Swift", R.color.swift),
    Python("Python", R.color.python),
    ObjectiveC("Objective-C", R.color.objective_c),
    TypeScript("TypeScript", R.color.typeScript),
    PHP("PHP", R.color.php),
    Handlebars("Handlebars", R.color.handlebars),
    UncoveredCase("", R.color.uncovered_case),
}