import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val channel = Channel<String>()
    val channel2 = Channel<Int>()
    launch {
        channel.send("A1")
        channel.send("A2")
        channel.send("A3")
        channel.send("A4")
        channel2.send(42)
        channel2.send(43)
        channel2.send(44)
        channel2.send(45)
        channel2.send(40)
        log("A done")
    }
    launch {
        channel.send("B1")
        log("B done")
    }
    launch {
        repeat(5) {
            val x = channel.receive()
            log(x)
        }
        log("EXIT")
    }
    launch {
        repeat(5) {
            val z = channel2.receive()
            log(z)
        }
    }
}

fun log(message: Any?) {
    println("[${Thread.currentThread().name}] $message")
}
