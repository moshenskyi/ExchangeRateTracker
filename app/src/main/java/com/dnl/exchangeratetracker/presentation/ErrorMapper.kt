package com.dnl.exchangeratetracker.presentation

import com.dnl.exchangeratetracker.R
import com.dnl.exchangeratetracker.domain.Resource

fun Resource.ErrorType.messageResource(): Int = when(this) {
    Resource.ErrorType.EmptyResponse -> R.string.error_empty_response
    Resource.ErrorType.Unknown -> R.string.error_unknown
    Resource.ErrorType.Network -> R.string.error_network
}