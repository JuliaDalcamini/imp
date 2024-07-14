package com.julia.imp.common.session

fun requireSession() = SessionManager.activeSession
    ?: throw IllegalStateException("Invalid session")

fun requireTeam() = requireSession().team
    ?: throw IllegalStateException("Team not available")