package com.example.routes

import com.example.data.checkPasswordForEmail
import com.example.data.requests.AccountRequest
import com.example.data.responses.SimpleResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.loginRouting() {
    routing {
        route("/login") {
            post {
                val request = try {
                    call.receive<AccountRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val isPasswordCorrect = checkPasswordForEmail(request.email, request.password)
                if (isPasswordCorrect) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "You are now logged"))
                } else {
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "Email or Password is incorrect"))
                }
            }
        }
    }
}