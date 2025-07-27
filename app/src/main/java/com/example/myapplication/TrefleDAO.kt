package com.example.myapplication

//Log
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrefleDAO {

    private var context: Context? = null

    constructor()

    constructor(context: Context) {
        this.context = context
    }

    fun setContext(context: Context) {
        this.context = context
    }
    fun defaultBitmap(): Bitmap? {
        val ctx = context ?: throw IllegalStateException("Context not set")
        return BitmapFactory.decodeResource(ctx.resources, R.drawable.slika1)
    }

    fun extractTextInParentheses(input: String): String {
        val regex = "\\(([^)]+)\\)".toRegex()
        val match = regex.find(input)
        if (match != null) {
            return match.groupValues.get(1)
        }
        return null.toString()
    }

    private suspend fun traziPoLatinskom(latinskiNaziv: String): Int? {
        return try {
            val result= BiljkeRepository.GetLatBiljke(latinskiNaziv)
            result?.biljke?.firstOrNull()?.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun getBiljkaPoID(id: Int): TrefleBiljka2? {
        return try {
            //Log.d("TrefleDAO", "Fetching plant with ID: $id")
            val result= BiljkeRepository.GetIDBiljke(id)
            //Log.d("TrefleDAO", "API result:hello")
            result?.biljka
        } catch (e: Exception) {
            ////Log.e("TrefleDAO", "Exception fetching plant by ID: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun getImage(biljka: Biljka): Bitmap{
        val latinskiNaziv: String =extractTextInParentheses(biljka.naziv)
       val id=traziPoLatinskom(latinskiNaziv)

        //Log.d(TAG, "Extracted Latin name: $latinskiNaziv")
        //Log.d(TAG, "Searched ID: $id")

       if(id!=null)
       {
           val detaljno= getBiljkaPoID(id)
           if(detaljno!=null){
           return withContext(Dispatchers.IO) {
               try {
                   Glide.with(context!!)
                       .asBitmap()
                       .load(detaljno.imageUrl)
                       .submit()
                       .get()
               }catch (e: Exception){
                   e.printStackTrace()
                   defaultBitmap()
               }!!
           }
           }
       }
        return defaultBitmap()!!
    }

    suspend fun fixData(biljka: Biljka):Biljka{
        val id=traziPoLatinskom(extractTextInParentheses(biljka.naziv))
        var naziv=biljka.naziv
        var porodica="Porodica"
        var medUpozorenje=biljka.medicinskoUpozorenje
        var medKoristi=biljka.medicinskeKoristi
        var profOkusa=biljka.profilOkusa
        var jela=biljka.jela
        var klimTipovi=biljka.klimatskiTipovi
        var zemljisniTipovi=biljka.zemljisniTipovi.toMutableList()
        val zt= emptyList<Zemljiste>()
        if(id!=null) {
            val uvezenaBiljka = getBiljkaPoID(id)
            if (uvezenaBiljka != null) {
                if(biljka.porodica!=uvezenaBiljka.family.name) porodica=uvezenaBiljka.family.name
                if(uvezenaBiljka.mainSpecies.edible==false) {
                    jela= emptyList()
                    medUpozorenje+=" NIJE JESTIVO"
                }
                if(!provjeriToksicnost(biljka.medicinskoUpozorenje) && uvezenaBiljka.mainSpecies.specifications?.toxicity !="none" && uvezenaBiljka.mainSpecies.specifications?.toxicity!=null){
                   medUpozorenje+=" TOKSIČNO"
                }


                val zemljiste=uvezenaBiljka.mainSpecies.growth.soilTecture
                if((zemljiste==1 || zemljiste==2) && !biljka.zemljisniTipovi.contains(Zemljiste.GLINENO)){
                    zemljisniTipovi=zt.toMutableList()
                    zemljisniTipovi+=Zemljiste.GLINENO
                }
                if (zemljiste == 9 && !biljka.zemljisniTipovi.contains(Zemljiste.SLJUNOVITO)) {
                    zemljisniTipovi=zt.toMutableList()
                    zemljisniTipovi.add(Zemljiste.SLJUNOVITO)
                }
                if (zemljiste == 10 && !biljka.zemljisniTipovi.contains(Zemljiste.KRECNJACKO)) {
                    zemljisniTipovi=zt.toMutableList()
                    zemljisniTipovi.add(Zemljiste.KRECNJACKO)
                }
                if ((zemljiste == 3 || zemljiste == 4) && !biljka.zemljisniTipovi.contains(Zemljiste.PJESKOVITO)) {
                    zemljisniTipovi=zt.toMutableList()
                    zemljisniTipovi.add(Zemljiste.PJESKOVITO)
                }
                if ((zemljiste == 5 || zemljiste == 6) && !biljka.zemljisniTipovi.contains(Zemljiste.ILOVACA)) {
                    zemljisniTipovi=zt.toMutableList()
                    zemljisniTipovi.add(Zemljiste.ILOVACA)
                }
                if ((zemljiste == 7 || zemljiste == 8) && !biljka.zemljisniTipovi.contains(Zemljiste.CRNICA)) {
                    zemljisniTipovi=zt.toMutableList()
                    zemljisniTipovi.add(Zemljiste.CRNICA)
                }
                val light=uvezenaBiljka.mainSpecies.growth.light
                val ah=uvezenaBiljka.mainSpecies.growth.atmosphericHumidity
                val kt= emptyList<KlimatskiTip>()
                if (light in 6..9 && ah in 1..5 && !biljka.klimatskiTipovi.contains(KlimatskiTip.SREDOZEMNA)) {
                    klimTipovi=kt.toMutableList()
                    klimTipovi.add(KlimatskiTip.SREDOZEMNA)
                }
                if (light in 8..10 && ah in 7..10 && !biljka.klimatskiTipovi.contains(KlimatskiTip.TROPSKA)) {
                    klimTipovi=kt.toMutableList()
                    klimTipovi.add(KlimatskiTip.TROPSKA)
                }
                if (light in 6..9 && ah in 5..8 && !biljka.klimatskiTipovi.contains(KlimatskiTip.SUBTROPSKA)) {
                    klimTipovi=kt.toMutableList()
                    klimTipovi.add(KlimatskiTip.SUBTROPSKA)
                }
                if (light in 4..7 && ah in 3..7 && !biljka.klimatskiTipovi.contains(KlimatskiTip.UMJERENA)) {
                    klimTipovi=kt.toMutableList()
                    klimTipovi.add(KlimatskiTip.UMJERENA)
                }
                if (light in 7..9 && ah in 1..2 && !biljka.klimatskiTipovi.contains(KlimatskiTip.SUHA)) {
                    klimTipovi=kt.toMutableList()
                    klimTipovi.add(KlimatskiTip.SUHA)
                }
                if (light in 0..5 && ah in 3..7 && !biljka.klimatskiTipovi.contains(KlimatskiTip.PLANINSKA)) {
                    klimTipovi=kt.toMutableList()
                    klimTipovi.add(KlimatskiTip.PLANINSKA)
                }
            }
        }
        val biljka1=Biljka(null,naziv,porodica,medUpozorenje,medKoristi,profOkusa,jela,klimTipovi,zemljisniTipovi)
        //Log.d("TAG", "Saljem biljku $biljka1")
        return biljka1
    }

    fun provjeriToksicnost(tekst: String): Boolean {
        return "toksično" in tekst.toLowerCase()
    }

    private suspend fun traziPoBoji(flowerColor: String, substr: String):GetBiljkeResponse?{
        return try{
            val rez=BiljkeRepository.GetFlowerColor(flowerColor,substr)
            rez
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }
    suspend fun getPlantsWithFlowerColor(flower_color: String, substr: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            val response = traziPoBoji(flower_color, substr)
            // Provjera da li je odgovor uspješan i da li sadrži podatke
            response?.let { getBiljkeFromResponse(it) } ?: emptyList()
        }
    }


    private fun getBiljkeFromResponse(response: GetBiljkeResponse): List<Biljka> {
        return response.biljke.map { trefleBiljka ->
            val naziv = trefleBiljka.scientificName
            val porodica = trefleBiljka.family
            val medicinskoUpozorenje: String? = null
            val medicinskeKoristi: List<MedicinskaKorist> = emptyList()
            val profilOkusa: ProfilOkusaBiljke = ProfilOkusaBiljke.SLATKI
            val jela: List<String> = emptyList()
            val klimatskiTipovi: List<KlimatskiTip> = emptyList<KlimatskiTip>()
            val zemljisniTipovi: List<Zemljiste> = emptyList<Zemljiste>()

            Biljka(
                naziv = "("+naziv+")",
                porodica = porodica,
                medicinskoUpozorenje = " ",
                medicinskeKoristi = medicinskeKoristi,
                profilOkusa = ProfilOkusaBiljke.MENTA,
                jela = jela,
                klimatskiTipovi = klimatskiTipovi,
                zemljisniTipovi = zemljisniTipovi
            )
        }
    }
}