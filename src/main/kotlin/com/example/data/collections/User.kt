package com.example.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

class User (
    val email: String,
    val password: String,
    @BsonId
    val id: String = ObjectId().toString()
)