package com.sansan.fitpal

data class bmi(
    val info: Info
)

data class Info(
    val bmi: Double,
    val health: String,
    val healthy_bmi_range: String
)