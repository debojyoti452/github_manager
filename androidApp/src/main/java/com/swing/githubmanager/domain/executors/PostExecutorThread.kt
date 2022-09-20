package com.swing.githubmanager.domain.executors

import kotlinx.coroutines.CoroutineDispatcher

interface PostExecutorThread {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
