package com.example.myapplication

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.coroutineScope

@Dao
interface BiljkaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBiljka1(biljka: Biljka): Long
    fun saveBiljka(biljka:Biljka) : Boolean{
        return try{
            saveBiljka1(biljka)
            true
        } catch(error:Exception){
            false
        }
        return false
    }


    @Query("SELECT * FROM biljka WHERE onlineChecked = 0")
    fun getOfflineBiljkas(): List<Biljka>

    @Update
    fun updateBiljka(biljka: Biljka)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addImage1(biljkaBitmap: BiljkaBitmap): Long

    @Query("UPDATE biljkaBitmap SET bitmap = :image WHERE idBiljke = :id")
    fun updateImage(id: Long, image: Bitmap): Int

    fun addImage(id: Long, biljkaBitmap: Bitmap): Boolean {
        return try {
            val biljkaBitmapEntity = BiljkaBitmap(idBiljke = id, bitmap = biljkaBitmap)
            val result = addImage1(biljkaBitmapEntity)
            result != -1L
        } catch (e: Exception) {
            false
        }
    }
    suspend fun addOrUpdateImage(id: Long, image: Bitmap): Boolean {
        return try {
            val biljkaBitmapEntity = BiljkaBitmap(idBiljke = id, bitmap = image)
            val result = addImage1(biljkaBitmapEntity)
            if (result == -1L) { // Image already exists, update it
                updateImage(id, image)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    @Query("SELECT * FROM biljka")
    fun getAllBiljkas1(): List<Biljka>
    fun getAllBiljkas() : List<Biljka> {
        return try {
            val lista=getAllBiljkas1()
            Log.d(TAG,"Vracam listu $lista")
            lista
        }catch (e:Exception){
            emptyList()
        }
        return emptyList()
    }

    @Query("DELETE FROM biljka")
    fun clearData()

    @Query("SELECT * FROM biljka WHERE onlineChecked = 0")
    fun getOfflineBiljke(): List<Biljka>

    @Transaction
    suspend fun fixOfflineBiljka(): Int = coroutineScope {
        val biljke = getOfflineBiljkas()
        var updatedCount = 0
        var trefleDAO=TrefleDAO()
        for (biljka in biljke) {
            val fixedBiljka = trefleDAO.fixData(biljka)
            if (fixedBiljka != biljka) {
                updateBiljka(fixedBiljka)
                updatedCount++
            }
        }
        updatedCount
    }

    @Query("SELECT bitmap FROM biljkaBitmap WHERE idBiljke = :id LIMIT 1")
    fun getImageForBiljka(id: Long): Bitmap?

    @Query("SELECT * FROM biljka WHERE id = :id")
    suspend fun getBiljkaById(id: Long): Biljka?

}
