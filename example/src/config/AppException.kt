package com.axbg.ctd.config

class AppException(override val message: String, val status: Int) : RuntimeException(message)
