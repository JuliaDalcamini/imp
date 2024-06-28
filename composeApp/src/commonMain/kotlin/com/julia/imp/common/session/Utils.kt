package com.julia.imp.common.session

fun requireSession() = SessionManager.activeSession
    ?: throw IllegalStateException("Invalid session")