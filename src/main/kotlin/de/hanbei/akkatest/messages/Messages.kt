package de.hanbei.akkatest.messages

import java.math.BigDecimal
import java.time.Duration

class Calculate()
data class Result(val value: BigDecimal)
data class Work(val start: Int, val nrOfElements: Int)
data class PiApproximation(val pi: BigDecimal, val duration: Duration)