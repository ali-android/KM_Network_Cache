package com.test.ali.testingkm.shared.cache

import com.squareup.sqldelight.db.SqlDriver

expect class Configurations {
    fun createDriver(): SqlDriver
}