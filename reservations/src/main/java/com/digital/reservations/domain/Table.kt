package com.digital.reservations.domain

data class Table(
    val id : Int,
    val waiterId : Int,
    val status : TableStatus,
    val x : Int,
    val y : Int
)
