package com.julia.imp.common.network

import io.ktor.client.*
import io.ktor.client.engine.js.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(Js) { config(this) }