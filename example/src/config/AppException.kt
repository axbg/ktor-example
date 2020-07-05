package com.axbg.ctd.config

import java.lang.RuntimeException

class AppException(override val message: String, val status: Int) : RuntimeException(message)