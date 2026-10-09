package com.abcode.quran

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform