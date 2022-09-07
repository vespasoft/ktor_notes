package com.example.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Note (
    val title: String,
    val content: String,
    val date: Long,
    val owners: List<String>,
    val color: String,
    @BsonId
    val id: String = ObjectId().toString()
)