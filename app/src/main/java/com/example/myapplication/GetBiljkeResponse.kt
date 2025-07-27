/*package com.example.myapplication

import com.google.gson.annotations.SerializedName
data class GetBiljkeResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("image_url") val image_url: String
)*/
package com.example.myapplication

import com.google.gson.annotations.SerializedName


data class GetBiljkeResponse(
    @SerializedName("data") val biljke: List<TrefleBiljka>
)
data class GetBiljkeResponse2(
    @SerializedName("data") val biljka: TrefleBiljka2
)