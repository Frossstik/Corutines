import kotlinx.coroutines.*
import java.time.LocalTime
import kotlin.random.Random

//1

fun task1() = runBlocking {
    launch {
        while (true) {
            println(LocalTime.now())
            delay(1000L)
        }
    }
}

//2

suspend fun factorial(n: Int): Int {
    return withContext(Dispatchers.Default) {
        (1..n).fold(1) { acc, i -> acc * i }
    }
}

fun task2() = runBlocking {
    print("Число: ")
    val number = readln()
    val result = factorial(number.toInt())
    println("$number! = $result")
}

//3

suspend fun generateAndCheckEven(index: Int): Pair<Int, Boolean> {
    return withContext(Dispatchers.Default) {
        val number = Random.nextInt()
        Pair(index, number % 2 == 0)
    }
}

fun task3() = runBlocking {
    val results = mutableListOf<Pair<Int, Boolean>>()
    val jobs = List(100000) { index ->
        async {
            generateAndCheckEven(index)
        }
    }
    results.addAll(jobs.awaitAll())

    val evenCount = results.count { it.second }
    println("Четные числа: $evenCount")
}

//4

suspend fun <T> withTimeoutOrDefault(timeMillis: Long, default: T, block: suspend () -> T): T {
    return withTimeoutOrNull(timeMillis) {
        block()
    } ?: default
}

suspend fun sampleTask(duration: Long): String {
    delay(duration)
    return "$duration мс"
}

fun task4() = runBlocking {
    val default = "Too long body execution"

    val correctResult = withTimeoutOrDefault(1000, default) { sampleTask(500) }
    println(correctResult)

    val incorrectResult = withTimeoutOrDefault(1000, default) { sampleTask(1500) }
    println(incorrectResult)
}

//main

fun main(){
    //task1()
    //task2()
    //task3()
    task4()
}