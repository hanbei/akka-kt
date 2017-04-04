package de.hanbei.akkatest

import akka.actor.ActorSystem
import akka.actor.Props
import de.hanbei.akkatest.actors.Listener
import de.hanbei.akkatest.actors.Master
import de.hanbei.akkatest.messages.Calculate
import scala.concurrent.Await
import scala.concurrent.duration.Duration

fun main(args: Array<String>) {
    val system = ActorSystem.create("PiSystem")
    val listener = system.actorOf(Props.create(Listener::class.java), "listener")
    val master = system.actorOf(Props.create(Master::class.java, { Master(10, 10000, 10000, listener) }), "master")

    master.tell(Calculate(), listener)

    Await.result(system.whenTerminated(), Duration.Inf())
}