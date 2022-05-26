package com.revature.workitout.viewmodel.providers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

data class CoroutineProvider(
    val main : CoroutineDispatcher = Main,
    val io : CoroutineDispatcher = IO

)
