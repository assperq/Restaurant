package com.digital.profile.domain

data class Profile(
    val id : String,
    val email : String,
    val fullName : String,
    val role : UserRole
)
