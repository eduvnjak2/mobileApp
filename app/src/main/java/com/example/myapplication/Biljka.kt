package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "biljka")
@TypeConverters(Converters::class)
data class Biljka(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") var naziv: String,
    @ColumnInfo(name = "family") @SerializedName("porodica") var porodica: String,
    @ColumnInfo(name = "medicinskoUpozorenje") @SerializedName("medicinskoUpozorenje") var medicinskoUpozorenje: String,
    @ColumnInfo(name = "medicinskeKoristi") @SerializedName("medicinskeKoristi") var medicinskeKoristi: List<MedicinskaKorist>,
    @ColumnInfo(name = "profilOkusaBiljke") @SerializedName("profilOkusa") var profilOkusa: ProfilOkusaBiljke,
    @ColumnInfo(name = "jela") @SerializedName("jela") var jela: List<String>,
    @ColumnInfo(name = "klimatskiTipovi") @SerializedName("klimatskiTipovi") var klimatskiTipovi: List<KlimatskiTip>,
    @ColumnInfo(name = "zemljisniTipovi") @SerializedName("zemljisniTipovi") var zemljisniTipovi: List<Zemljiste>,
    @ColumnInfo(name = "onlineChecked") var onlineChecked: Boolean = false
) : Serializable
