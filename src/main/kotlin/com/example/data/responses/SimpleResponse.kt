package com.example.data.responses

@kotlinx.serialization.Serializable
data class SimpleResponse(
    val successful: Boolean,
    val message: String
)