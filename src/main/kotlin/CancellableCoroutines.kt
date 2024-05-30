import kotlinx.coroutines.*

class CancellableCoroutines

fun main() = runBlocking {
//    cancellable()
//    timeOutCor()
    timeOutCorNull()
}


suspend fun cancellable() {
    coroutineScope {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            // isActive是协程内部通过CoroutineScope对象提供的扩展属性
            while (isActive) { // cancellable computation loop
                // print a message twice a second
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L)
        println("main: I'm tired of waiting!")
        job.cancelAndJoin()
        println("main: Now I can quit.")
    }
}

/**
 * 超时处理
 */
suspend fun timeOutCor(){
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}


suspend fun timeOutCorNull(){
    val result =  withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    println("Result is $result")
}
