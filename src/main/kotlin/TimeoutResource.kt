import kotlinx.coroutines.*

var acquired = 0

class Resource {
    init { acquired++ } // Acquire the resource
    fun close() { acquired-- } // Release the resource
}

fun main() {
    runBlocking {
//        unSafeResource()
        safeResource()
    }


    // Outside of runBlocking all coroutines have completed
    println(acquired) // Print the number of resources still acquired
}

private fun CoroutineScope.safeResource() {
    repeat(10_000) { // Launch 10K coroutines
        launch {
            var resource: Resource? = null // Not acquired yet
            try {
                withTimeout(60) { // Timeout of 60 ms
                    delay(70) // Delay for 50 ms
                    resource = Resource() // Store a resource to the variable if acquired
                }
                // We can do something else with the resource here
            } finally {
                resource?.close() // Release the resource if it was acquired
            }
        }
    }
}

private fun CoroutineScope.unSafeResource() {
    repeat(10_000) { // Launch 10K coroutines
        launch {
            val resource = withTimeout(60) { // Timeout of 60 ms
                delay(35) // Delay for 50 ms
                Resource() // Acquire a resource and return it from withTimeout block
            }
            resource.close() // Release the resource
        }
    }
}
