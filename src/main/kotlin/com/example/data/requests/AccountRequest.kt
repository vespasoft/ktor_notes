package com.example.data.requests

@kotlinx.serialization.Serializable
data class AccountRequest(
    val email: String,
    val password: String
)