package com.example.olib_retry.data.repository


import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> retryRequest(
    maxAttempts: Int = 3,
    initialDelayMs: Long = 1000L,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMs
    var lastError: Throwable? = null

    repeat(maxAttempts) { attempt ->
        try {
            return block()
        } catch (e: IOException) {
            lastError = e
        } catch (e: HttpException) {
            if (e.code() in 500..599) {
                lastError = e
            } else {
                throw e
            }
        }

        if (attempt < maxAttempts - 1) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
    }

    throw lastError ?: IllegalStateException("Unknown retry error")
}