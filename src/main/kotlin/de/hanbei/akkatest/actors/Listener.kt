package de.hanbei.akkatest.actors

import akka.actor.UntypedActor
import de.hanbei.akkatest.messages.PiApproximation
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class Listener : UntypedActor() {
    override fun onReceive(message: Any?) {
        when (message) {
            is PiApproximation -> {
                println("Pi approx ${message.pi} in ${message.duration}")
                Await.result(context.system().terminate(), Duration.create(10, TimeUnit.SECONDS))
            }
            else -> unhandled(message)
        }
    }

}