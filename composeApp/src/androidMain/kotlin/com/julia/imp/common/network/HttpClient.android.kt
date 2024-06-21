package com.julia.imp.common.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(OkHttp) { config(this) }