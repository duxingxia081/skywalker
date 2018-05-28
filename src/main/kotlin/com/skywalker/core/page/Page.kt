package com.skywalker.core.page

interface Page<out T> {
    val totalPages: Int
    val content: List<T>
}