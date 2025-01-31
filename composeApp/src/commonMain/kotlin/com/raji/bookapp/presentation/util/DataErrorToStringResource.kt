package com.raji.bookapp.presentation.util

import bookapp.composeapp.generated.resources.Res
import bookapp.composeapp.generated.resources.error_disk_full
import bookapp.composeapp.generated.resources.error_no_internet
import bookapp.composeapp.generated.resources.error_request_timeout
import bookapp.composeapp.generated.resources.error_serialization
import bookapp.composeapp.generated.resources.error_too_many_requests
import bookapp.composeapp.generated.resources.error_unknown
import com.raji.bookapp.data.remote.network.DataError

fun DataError.toUIText(): UiText {

    val stringRes = when (this) {
        DataError.LocalError.DISK_FULL -> Res.string.error_disk_full
        DataError.LocalError.UNKNOWN -> Res.string.error_unknown
        DataError.RemoteError.REQUEST_TIME_OUT -> Res.string.error_request_timeout
        DataError.RemoteError.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.RemoteError.NO_INTERNET -> Res.string.error_no_internet
        DataError.RemoteError.SERVER -> Res.string.error_unknown
        DataError.RemoteError.SERIALIZATION -> Res.string.error_serialization
        DataError.RemoteError.UNKNOWN -> Res.string.error_unknown
    }
    return UiText.StringResourceId(stringRes)
}