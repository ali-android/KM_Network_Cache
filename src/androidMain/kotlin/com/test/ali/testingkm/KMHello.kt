package com.test.ali.testingkm

import android.util.Log

actual class KMHello {
    actual fun doSomethingCommon(message: String) {
        Log.d("KMHello ::: on Android",message)
    }
}