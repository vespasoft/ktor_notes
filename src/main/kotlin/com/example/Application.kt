package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port) {
        install(Authentication) {
            configureAuth()
        }
        configureSecurity()
        configureSerialization()
        configureRouting()
        registerRouting()
        loginRouting()
        notesRouting()
    }.start(wait = true)
}
