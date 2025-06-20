package com.digital.payment.domain

interface PaymentInterface {
    suspend fun pay(cardData : CardData) : Boolean
}