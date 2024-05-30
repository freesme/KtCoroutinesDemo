import kotlinx.coroutines.*
import javax.print.attribute.PrintRequestAttributeSet

fun main(): Unit = runBlocking {
    println("START")
//    doWorld()
//    explicitJob()

    /**
     * async 启动一个新的协程并返回一个Deferred对象
     * Deferred 表示一个以其他名称（例如Future或 ）为人所知的概念Promise。
     * 它存储计算结果，但会延迟获得最终结果的时间；它承诺在未来的某个时候得到结果。
     *
     * async和launch之间的主要区别在于，launch用于启动不期望返回特定结果的计算。
     * launch返回一个代表协程的Job。可以通过调用Job.join()等待它完成。
     */
    // val deferred: Deferred<Int>
//    val deferred = async {
//        loadData()
//    }
//    println(deferred.await())


    // 如果有一个延迟对象列表，你可以调用awaitAll()来等待所有对象的结果:
    val deferreds: List<Deferred<Int>> = (1..3).map {
        async {
            delay(1000L * it)
            println("Loading $it")
            it
        }
    }
    val sum = deferreds.awaitAll().sum()
    println("$sum")

    println("DONE")

}

private suspend fun CoroutineScope.explicitJob() {
    val job = launch {
        println("Do explicit job")
        delay(1000L)
    }
    println("等待任务完成")
    job.join()
    println("任务完成")
}

private suspend fun hello() {
    delay(1000L)
    println("world")
}

/**
 * coroutineScope构建器可以在任何挂起函数中使用，
 * 以执行多个并发操作。在doWorld挂起函数中启动两个并发协程:
 */
suspend fun doWorld() = coroutineScope {
    launch {
        delay(2000L)
        println("World 2")
    }

    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}

suspend fun loadData(): Int{
    println("loading...")
    delay(1000L)
    println("loaded!")
    return 42
}
