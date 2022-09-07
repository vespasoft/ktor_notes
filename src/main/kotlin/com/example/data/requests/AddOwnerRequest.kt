package com.example.data.requests

@kotlinx.serialization.Serializable
data class AddOwnerRequest(
    val noteId: String,
    val owner: String
)