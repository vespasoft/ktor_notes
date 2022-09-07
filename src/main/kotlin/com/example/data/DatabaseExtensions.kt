package com.example.data

import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.typesafe.config.ConfigFactory
import org.litote.kmongo.KMongo

fun KMongo.createKMongoClient(): MongoClient {
    val conf = ConfigFactory.load()
    return createClient(ConnectionString(conf.getString("database.kmongoDatabaseUrl")))
}