package de.hanbei.akkatest.actors

import akka.actor.UntypedActor
import de.hanbei.akkatest.messages.Result
import de.hanbei.akkatest.messages.Work
import java.math.BigDecimal

class Worker : UntypedActor() {

    override fun onReceive(message: Any?) =
            when (message) {
                is Work -> {
                    val result = calculatePiFor(message.start, message.nrOfElements)
                    sender.tell(Result(result), self)
                }
                else -> {
                    unhandled(message)
                }
            }

    private fun calculatePiFor(start: Int, nrOfElements: Int): BigDecimal {
        var acc = BigDecimal.ZERO
        for (i in start * nrOfElements..((start + 1) * nrOfElements - 1)) {
            acc += (4.0 * (1 - (i % 2) * 2) / (2 * i + 1))
        }
        return acc
    }
}

operator fun BigDecimal.plus(d: Double) = this.add(java.math.BigDecimal(d))
