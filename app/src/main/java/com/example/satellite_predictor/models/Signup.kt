package com.example.satellite_predictor.models

data class Signup(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
)

data class Login(
    val name: String? = null,
    val email: String? = null,
    val remember: Boolean
)