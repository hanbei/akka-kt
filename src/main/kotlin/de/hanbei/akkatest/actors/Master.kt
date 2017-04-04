package de.hanbei.akkatest.actors

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.UntypedActor
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Routee
import akka.routing.Router
import de.hanbei.akkatest.messages.Calculate
import de.hanbei.akkatest.messages.PiApproximation
import de.hanbei.akkatest.messages.Result
import de.hanbei.akkatest.messages.Work
import java.math.BigDecimal
import java.time.Duration

class Master(val nrOfWorkers: Int, val nrOfMessages: Int, val nrOfElements: Int, val listener: ActorRef) : UntypedActor() {

    private var pi: BigDecimal = BigDecimal.ZERO
    private var nrOfResults: Int = 0
    private val start = System.currentTimeMillis()
    private val workerRouter = Router(RoundRobinRoutingLogic(), createRouter(nrOfWorkers))

    private fun createRouter(nrOfWorkers: Int): List<Routee> {
        return (0..nrOfWorkers).map {
            val workerActor = context.actorOf(Props.create(Worker::class.java), "worker-${it}")
            context.watch(workerActor)
            ActorRefRoutee(workerActor)
        }
    }

    override fun onReceive(message: Any?): Unit {
        when (message) {
            is Calculate -> {
                (0..nrOfMessages).forEach { workerRouter.route(Work(it, nrOfElements), self) }
            }
            is Result -> {
                pi += message.value
                if (++nrOfResults >= nrOfMessages) {
                    listener.tell(PiApproximation(pi, Duration.ofMillis(System.currentTimeMillis() - start)), self)
                    context.stop(self)
                }
            }
            is Terminated -> {
                workerRouter.removeRoutee(message.actor)
            }
            else -> unhandled(message)
        }
    }
}