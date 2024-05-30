import kotlinx.coroutines.*
import java.util.Random
import kotlin.coroutines.*
import kotlin.system.measureTimeMillis

@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulOneAsync() = GlobalScope.async {
    intFun(100)
}

@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulTwoAsync() = GlobalScope.async {
    intFun(40)
}

fun intFun(max: Int): Int {
    val random = Random()
    return random.nextInt(max)
}

fun main() {
    val time = measureTimeMillis {
//        // we can initiate async actions outside of a coroutine
//        val one = somethingUsefulOneAsync()
//        val two = somethingUsefulTwoAsync()
//        // but waiting for a result must involve either suspending or blocking.
//        // here we use `runBlocking { ... }` to block the main thread while waiting for the result
//        runBlocking {
//            println("The answer is ${one.await()} ${two.await()}   ${one.await() + two.await()}")
//        }

        runBlocking {
//            println("The answer is ${concurrentSum()}")
            // 结构化并发产生的异常仅在协程层次结构传播
            try {
                failedConcurrentSum()
            } catch(e: ArithmeticException) {
                println("Computation failed with ArithmeticException")
            }
        }
    }
    println("Completed in $time ms")
}

/**
 * 结构化并发调用
 * 这样，如果concurrentSum函数的代码出现问题，并且抛出异常，所有在其作用域中启动的协程都会被取消。
 */
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { intFun(40) }
    val two = async { intFun(50) }
    return@coroutineScope one.await() + two.await()
}

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            // 当结构内的其他协程发生异常，取消总是通过协程的层次结构传播
            // 当一个子进程(即两个)失败时，第一个async进程和等待中的父进程都将被取消:
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}
