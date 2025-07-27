package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromMedicinskeKoristi(value: List<MedicinskaKorist>): String {
        val gson = Gson()
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMedicinskeKoristi(value: String): List<MedicinskaKorist> {
        val gson = Gson()
        val type = object : TypeToken<List<MedicinskaKorist>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromProfilOkusa(value: ProfilOkusaBiljke): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toProfilOkusa(value: String): ProfilOkusaBiljke {
        return Gson().fromJson(value, ProfilOkusaBiljke::class.java)
    }

    @TypeConverter
    fun fromJela(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toJela(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromKlimatskiTipovi(value: List<KlimatskiTip>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toKlimatskiTipovi(value: String): List<KlimatskiTip> {
        val listType = object : TypeToken<List<KlimatskiTip>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromZemljisniTipovi(value: List<Zemljiste>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toZemljisniTipovi(value: String): List<Zemljiste> {
        val listType = object : TypeToken<List<Zemljiste>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromZemljiste(value: Zemljiste): String {
        return value.name
    }

    @TypeConverter
    fun toZemljiste(value: String): Zemljiste {
        return Zemljiste.valueOf(value)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(encodedString: String): Bitmap {
        val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
