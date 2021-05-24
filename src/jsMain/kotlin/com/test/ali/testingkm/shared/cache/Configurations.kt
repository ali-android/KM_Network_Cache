package com.test.ali.testingkm.shared.cache

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.JsSqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.*
import kotlin.js.Promise
import kotlin.time.measureTime

actual class Configurations {

    val promise: Promise<SqlDriver> = initSqlDriver(AppDatabase.Schema)
    lateinit var sqlDriver : SqlDriver
    private val ioCoroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        ioCoroutineScope.launch { sqlDriver = getDriver() }
    }

    actual fun createDriver(): SqlDriver {

        // val abc = ioCoroutineScope.launch {  }
        /*var driverSQL : SqlDriver = JsSqlDriver(AppDatabase.invoke())
        promise.then { driver -> createDriver()
            driverSQL = driver}*/

        return sqlDriver
    }

    fun getSQLDriver() = CoroutineScope(Dispatchers.Default).async {
        return@async promise.await()
    }

    /*fun sum(): Int = runBlocking {
        funA().await() + funB().await()
    }*/
    private suspend fun getDriver(): SqlDriver = withContext(Dispatchers.Default) {
        return@withContext initSqlDriver(AppDatabase.Schema).await()
    }

    fun promiseMeSQLDriver(): Promise<SqlDriver> {
        // makes request and returns a promise that is completed later
        return initSqlDriver(AppDatabase.Schema)
    }

    suspend fun createSQLDriver() : SqlDriver{
        return initSqlDriver(AppDatabase.Schema).await()
        /* ... */
    }
}