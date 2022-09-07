package com.example.routes

import com.example.data.checkPasswordForEmail
import io.ktor.server.auth.*

fun AuthenticationConfig.configureAuth() {
    basic {
        realm = "Note Server"
        validate {credentials ->
            val email = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}
