package com.example.data

import com.example.data.collections.Note
import com.example.data.collections.User
import org.litote.kmongo.*

private val client = KMongo.createKMongoClient()
private val database = client.getDatabase("notes")
private val notesDao = database.getCollection<Note>()
private val usersDao = database.getCollection<User>()

fun registerUser(user: User): Boolean {
    return usersDao.insertOne(user).wasAcknowledged()
}

fun checkIfUserExists(email: String): Boolean {
    return usersDao.findOne(User::email eq email) != null
}

fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
    val actualPassword = usersDao.findOne ( User::email eq email )?.password ?: return false
    return actualPassword == passwordToCheck
}

fun getNotesForUser(email: String): List<Note> {
    return notesDao.find(Note::owners contains email).toList()
}

fun saveNote(note: Note): Boolean {
    val noteExists = notesDao.findOneById(note.id) != null
    return if (noteExists) {
        notesDao.updateOneById(note.id, note).wasAcknowledged()
    } else {
        notesDao.insertOne(note).wasAcknowledged()
    }
}

fun isOwnerOfNote(noteId: String, owner: String): Boolean {
    val notes = notesDao.findOneById(noteId) ?: return false
    return owner in notes.owners
}

fun addOwnerToNote(noteId: String, owner: String): Boolean {
    val owners = notesDao.findOneById(noteId)?.owners ?: return false
    return notesDao.updateOneById(noteId, setValue(Note::owners, owners + owner)).wasAcknowledged()
}

fun deleteNoteForUser(email: String, noteId: String): Boolean {
    val note = notesDao.findOne(Note::id eq noteId, Note::owners contains email)
    note?.let { note ->
        if (note.owners.size > 1) {
            val newOwners = note.owners - email
            return notesDao.updateOne(Note::id eq note.id, setValue(Note::owners, newOwners)).wasAcknowledged()
        }
        return notesDao.deleteOneById(note.id).wasAcknowledged()
    } ?: return false
}