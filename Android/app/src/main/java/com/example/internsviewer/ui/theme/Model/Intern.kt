package com.example.internsviewer.ui.theme

import kotlinx.serialization.Serializable

@Serializable
data class Intern(
    val Id: Int,
    val Name: String,
    val Surname: String,
    val amount: Double,
    val boss: String
)