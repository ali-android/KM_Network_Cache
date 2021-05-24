package com.test.ali.testingkm

actual class KMHello {
    actual fun doSomethingCommon(message: String) {
        console.log("KMHello ::: on JavaScript",message)
    }
}