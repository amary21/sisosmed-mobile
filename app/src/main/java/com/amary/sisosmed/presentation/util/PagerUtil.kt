package com.amary.sisosmed.presentation.util

import androidx.paging.LoadState

fun pagerResponseError(loadState: LoadState, response: (codeResponse: String, message: String) -> Unit){
    val errorState = loadState as? LoadState.Error
    val splitError = errorState?.error?.message?.split(" - ")
    response(splitError?.get(0).toString(), splitError?.get(1).toString())
}