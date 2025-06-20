package com.digital.payment.domain

data class CardData(
    val cardNumber : String,
    val validityPeriod: String,
    val cvcCode : Int
) {
    fun equal(other : CardData) : Boolean {
        return this.cardNumber == other.cardNumber
                && this.validityPeriod == other.validityPeriod
                && this.cvcCode == other.cvcCode
    }
}
