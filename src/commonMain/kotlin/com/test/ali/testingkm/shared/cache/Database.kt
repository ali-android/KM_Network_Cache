package com.test.ali.testingkm.shared.cache

import com.squareup.sqldelight.db.SqlDriver
import com.test.ali.testingkm.shared.entity.RocketLaunch
import com.test.ali.testingkm.shared.entity.Rocket
import com.test.ali.testingkm.shared.entity.Links

internal class Database(configurations: Configurations) {




    /*class SharedDatabase(
        private val driverProvider: suspend (SqlDriver.Schema) -> SqlDriver
    ) {
        private var database: Database? = null

        suspend fun initDatabase() {
            if (database == null) {
                database = driverProvider(AppDatabase.Schema).createDatabase()
            }
        }

        suspend operator fun <R> invoke(block: suspend (Database) -> R): R {
            initDatabase()
            return block(database!!)
        }

        private fun SqlDriver.createDatabase(): Database { *//* ... *//* }
    }

    val sharedDb = SharedDatabase(::createTestDbDriver)
    class DataRepository(
        private val withDatabase: SharedDatabase = sharedDb
    ) {
        suspend fun getData() = withDatabase { database ->
            *//* Do something with the database *//*
        }
    }*/














    private val database = AppDatabase(configurations.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRockets()
            dbQuery.removeAllLaunches()
        }
    }

    internal fun getAllLaunches(): List<RocketLaunch> {
        return dbQuery.selectAllLaunchesInfo(::mapLaunchSelecting).executeAsList()
    }

    private fun mapLaunchSelecting(
        flightNumber: Long,
        missionName: String,
        launchYear: Int,
        rocketId: String,
        details: String?,
        launchSuccess: Boolean?,
        launchDateUTC: String,
        missionPatchUrl: String?,
        articleUrl: String?,
        rocket_id: String?,
        name: String?,
        type: String?
    ): RocketLaunch {
        return RocketLaunch(
            flightNumber = flightNumber.toInt(),
            missionName = missionName,
            launchYear = launchYear,
            details = details,
            launchDateUTC = launchDateUTC,
            launchSuccess = launchSuccess,
            rocket = Rocket(
                id = rocketId,
                name = name!!,
                type = type!!
            ),
            links = Links(
                missionPatchUrl = missionPatchUrl,
                articleUrl = articleUrl
            )
        )
    }

    internal fun createLaunches(launches: List<RocketLaunch>) {
        dbQuery.transaction {
            launches.forEach { launch ->
                val rocket = dbQuery.selectRocketById(launch.rocket.id).executeAsOneOrNull()
                if (rocket == null) {
                    insertRocket(launch)
                }

                insertLaunch(launch)
            }
        }
    }

    private fun insertRocket(launch: RocketLaunch) {
        dbQuery.insertRocket(
            id = launch.rocket.id,
            name = launch.rocket.name,
            type = launch.rocket.type
        )
    }

    private fun insertLaunch(launch: RocketLaunch) {
        dbQuery.insertLaunch(
            flightNumber = launch.flightNumber.toLong(),
            missionName = launch.missionName,
            launchYear = launch.launchYear,
            rocketId = launch.rocket.id,
            details = launch.details,
            launchSuccess = launch.launchSuccess ?: false,
            launchDateUTC = launch.launchDateUTC,
            missionPatchUrl = launch.links.missionPatchUrl,
            articleUrl = launch.links.articleUrl
        )
    }
}