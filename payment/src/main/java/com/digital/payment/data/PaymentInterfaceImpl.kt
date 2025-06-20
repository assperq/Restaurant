package com.digital.payment.data

import com.digital.payment.domain.CardData
import com.digital.payment.domain.PaymentInterface
import kotlinx.coroutines.delay

class PaymentInterfaceImpl : PaymentInterface {
    private val paymentResults = mapOf(
        CardData(
            cardNumber = "0000 0000 0000 0000",
            validityPeriod = "08/30",
            cvcCode = 232
        ) to true,
        CardData(
            cardNumber = "0000 0020 0000 1111",
            validityPeriod = "08/34",
            cvcCode = 323
        ) to true
    )

    override suspend fun pay(cardData: CardData): Boolean {
        delay(1000)
        return try {
            println("CARD DATA: $cardData")
            paymentResults.getValue(cardData)
        }
        catch (_ : Throwable) {
            false
        }
    }
}