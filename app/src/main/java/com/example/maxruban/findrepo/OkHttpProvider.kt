package com.example.maxruban.findrepo

import okhttp3.OkHttpClient

object OkHttpProvider {
    // Should be provided by Dagger in a real application
    val okHttpClient by lazy {
        OkHttpClient()
    }
}