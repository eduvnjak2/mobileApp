package com.example.myapplication

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BiljkeRepository {

    suspend fun GetBiljke(query: String) : GetBiljkeResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.GetBiljke(query)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun GetLatBiljke(ime:String) : GetBiljkeResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.GetLatBiljke(ime)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun GetIDBiljke(id:Int) : GetBiljkeResponse2?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.GetBiljkeID(id)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun GetFlowerColor(a:String,b:String) : GetBiljkeResponse?{
        return withContext(Dispatchers.IO) {
            var response = ApiAdapter.retrofit.GetFlowerColor(a,b)
            val responseBody = response.body()
            return@withContext responseBody
        }
    }

    suspend fun getAllBiljkas(context: Context) : List<Biljka> {
        return withContext(Dispatchers.IO) {
            var db = BiljkaDatabase.getInstance(context)
            var biljke = db!!.biljkaDao().getAllBiljkas()
            return@withContext biljke
        }
    }
    /*suspend fun saveBiljka(context: Context,biljka:Biljka) : String?{
        return withContext(Dispatchers.IO) {
            try{
                var db = BiljkaDatabase.getInstance(context)
                db!!.biljkaDao().saveBiljka(biljka)
                return@withContext "success"
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }*/
}