package com.test.ali.testingkm

import com.test.ali.testingkm.shared.cache.Database
import com.test.ali.testingkm.shared.cache.Configurations
import com.test.ali.testingkm.shared.entity.RocketLaunch
import com.test.ali.testingkm.shared.network.RestAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UgoKM(private val dataReceivedListener: DataReceivedListener, configurations: Configurations? = null) {

    interface DataReceivedListener{
        fun onRequestCompleted(listOfData : ArrayList<RocketLaunch>)
        fun onRequestIncomplete(failureReason: String)
    }
    private val ioCoroutineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var db : Database
    init {
        if(configurations != null){
            db = Database(configurations)
        }
    }
    //private val database = Database(databaseDriverFactory)
    private val api = RestAPI()

    fun fetchData(loadDataFromDB : Boolean){
        ioCoroutineScope.launch{
            val list = fetchAPIData(loadDataFromDB)
            if(list.isNullOrEmpty()){
                dataReceivedListener.onRequestIncomplete("No data found")
            }else{
                dataReceivedListener.onRequestCompleted(listOfData = list as ArrayList<RocketLaunch>)
            }
        }
    }

    @Throws(Exception::class) private suspend fun fetchAPIData(forceReload: Boolean): List<RocketLaunch> {

        if(db != null){
            val cachedLaunches = db.getAllLaunches()
            return if (cachedLaunches.isNotEmpty() && !forceReload) {
                cachedLaunches
            } else {
                return api.getAllLaunches().also {
                    db.clearDatabase()
                    db.createLaunches(it)
                }
            }
        }else{
            return api.getAllLaunches()
        }
        /*val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }*/
    }
}