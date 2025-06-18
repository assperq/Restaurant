package com.digital.profile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("id") val id : String,
    @SerialName("email") val email : String,
    @SerialName("full_name") val fullName : String,
    @SerialName("role") val role : String,
)
