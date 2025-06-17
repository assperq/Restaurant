package com.digital.registration.domain

interface AuthRepository {
    suspend fun signIn(email : String, password : String)
    suspend fun signUp(email : String, password : String, contactInfo : String)
    suspend fun signOut()
}