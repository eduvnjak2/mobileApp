package com.example.myapplication
import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    tableName = "biljkaBitmap",
    foreignKeys = [ForeignKey(
        entity = Biljka::class,
        parentColumns = ["id"],
        childColumns = ["idBiljke"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(Converters::class)
data class BiljkaBitmap(
    @PrimaryKey(autoGenerate=true) val id: Long=0,
    @ColumnInfo(name = "bitmap") val bitmap: Bitmap,
    @ColumnInfo(name = "idBiljke") val idBiljke: Long
)

