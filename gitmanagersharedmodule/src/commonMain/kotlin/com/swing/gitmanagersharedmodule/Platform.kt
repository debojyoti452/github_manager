package com.swing.gitmanagersharedmodule

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform