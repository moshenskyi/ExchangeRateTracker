package com.dnl.exchangeratetracker.data.remote

import com.dnl.exchangeratetracker.domain.ConnectionException
import com.dnl.exchangeratetracker.domain.DnlException
import java.io.IOException

fun parseError(ex: Exception): Nothing {
    when (ex) {
        is IOException -> throw ConnectionException()
        else -> throw DnlException()
    }
}