package com.example.routes

import com.example.data.*
import com.example.data.collections.Note
import com.example.data.requests.AddOwnerRequest
import com.example.data.requests.DeleteNoteRequest
import com.example.data.responses.SimpleResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.notesRouting() {
    routing {
        route("/notes") {
            authenticate {
                get {
                    val email = call.principal<UserIdPrincipal>()!!.name
                    val notes = getNotesForUser(email)

                    call.respond(HttpStatusCode.OK, notes)
                }
            }
        }
        route("/AddNote") {
            authenticate {
                post {
                    val noteRequest = try {
                        call.receive<Note>()
                    } catch (e: ContentTransformationException) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    if (saveNote(noteRequest)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Conflict)
                    }
                }
            }
        }
        route("/addOwnerToNote") {
            authenticate {
                post {
                    val request = try {
                        call.receive<AddOwnerRequest>()
                    } catch (e: ContentTransformationException) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    if (!checkIfUserExists(request.owner)) {
                        call.respond(HttpStatusCode.OK, SimpleResponse(false, "No user with this E-Mail exists"))
                        return@post
                    }
                    if (isOwnerOfNote(request.noteId, request.owner)) {
                        call.respond(HttpStatusCode.OK, SimpleResponse(false, "this user is already owner of this note"))
                        return@post
                    }
                    if (addOwnerToNote(request.noteId, request.owner)) {
                        call.respond(
                            HttpStatusCode.OK,
                            SimpleResponse(true, "${request.owner} can now see this note")
                        )
                        return@post
                    } else {
                        call.respond(HttpStatusCode.Conflict)
                    }
                }
            }
        }
        route("/notes") {
            authenticate {
                delete {
                    val email = call.principal<UserIdPrincipal>()!!.name
                    val request = try {
                        call.receive<DeleteNoteRequest>()
                    } catch (e: ContentTransformationException) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@delete
                    }
                    if (deleteNoteForUser(email, request.id)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Conflict)
                    }
                }
            }
        }
    }
}