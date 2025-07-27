package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import androidx.core.database.getStringOrNull
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.greaterThan
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class BiljkeDB4test {

    private val countBiljka = "SELECT COUNT(*) AS broj_biljaka FROM Biljka"
    private val countBiljkaBitmaps = "SELECT COUNT(*) AS broj_bitmapa FROM BiljkaBitmap"

    private val describeBiljka = "pragma table_info('Biljka')"
    private val describeBiljkaBitmap = "pragma table_info('BiljkaBitmap')"

    private val kolone = mapOf(
        "BiljkaBitmap" to arrayListOf("idBiljke", "bitmap"),
        "Biljka" to arrayListOf(
            "id",
            "naziv",
            "family",
            "medicinskoUpozorenje",
            "jela",
            "klimatskiTipovi",
            "zemljisniTipovi",
            "medicinskeKoristi"
        )
    )

    companion object {
        lateinit var db: SupportSQLiteDatabase
        lateinit var context: Context
        lateinit var roomDb: BiljkaDatabase
        lateinit var biljkaDAO: BiljkaDAO

        @BeforeClass
        @JvmStatic
        fun createDB() = runBlocking {
            val scenarioRule = ActivityScenario.launch(MainActivity::class.java)
            context = ApplicationProvider.getApplicationContext()
            roomDb = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
            biljkaDAO = roomDb.biljkaDao()
            biljkaDAO.getAllBiljkas()
            db = roomDb.openHelper.readableDatabase

        }
    }

    private fun executeCountAndCheck(query: String, column: String, value: Long) {
        var rezultat = db.query(query)
        rezultat.moveToFirst()
        var brojOdgovora = rezultat.getLong(0)
        MatcherAssert.assertThat(brojOdgovora, `is`(equalTo(value)))
    }

    private fun checkColumns(query: String, naziv: String) {
        var rezultat = db.query(query)
        val list = (1..rezultat.count).map {
            rezultat.moveToNext()
            rezultat.getString(1)
        }
        assertThat(list, hasItems(*kolone[naziv]!!.toArray()))
    }

    @get:Rule
    val intentsTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun a0_insertFirstBiljka() = runBlocking {
        biljkaDAO.saveBiljka(
            Biljka(
                naziv = "Bosiljak (Ocimum basilicum)",
                porodica = "Lamiaceae (usnate)",
                medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE,
                    MedicinskaKorist.REGULACIJAPROBAVE
                ),
                profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
                jela = listOf("Salata od paradajza", "Punjene tikvice"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            )
        )

        assertThat(biljkaDAO.getAllBiljkas().size, `is`(1))
    }

    @Test
    fun a1_tableBiljkaHasAllColumns() = runBlocking {
        checkColumns(describeBiljka, "Biljka")
    }

    @Test
    fun a2_tableBiljkaBitmapHasAllColumns() = runBlocking {
        checkColumns(describeBiljkaBitmap, "BiljkaBitmap")
    }

    @Test
    fun a3_insertZaistaUpisaoUBazu() = runBlocking {
        executeCountAndCheck(countBiljka, "broj_biljaka", 1)
    }

    @Test
    fun a4_dodajJosJednuBiljkuIGetAllBiljkas() = runBlocking {
        biljkaDAO.saveBiljka(
            Biljka(
                naziv = "Kamilica (Matricaria chamomilla)",
                porodica = "Asteraceae (glavočike)",
                medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE,
                    MedicinskaKorist.PROTUUPALNO
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Čaj od kamilice"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
            )
        )
        assertThat(biljkaDAO.getAllBiljkas().size, `is`(2))
        executeCountAndCheck(countBiljka, "broj_biljaka", 2)
    }

    @Test
    fun a5_provjeriDaUSvimBiljkamaPostojiKoristSmirenje() = runBlocking {
        var biljke = biljkaDAO.getAllBiljkas()
        for (biljka in biljke) {
            assertThat(biljka.medicinskeKoristi.contains(MedicinskaKorist.SMIRENJE), `is`(true))
        }
    }

    @Test
    fun a6_addSlikaAndCheckItsAdded() = runBlocking {
        executeCountAndCheck(countBiljkaBitmaps, "broj_bitmapa", 0)
        var biljka1 = biljkaDAO.getAllBiljkas().get(0).id
        var bitmap = Bitmap.createBitmap(200, 300, Bitmap.Config.ARGB_8888)
        //napravite da je prvi parametar BiljkaBitmap id koji je PrimaryKey(autoGenerate=true)
        biljkaDAO.addImage(biljka1 ?: 0, bitmap)
        executeCountAndCheck(countBiljkaBitmaps, "broj_bitmapa", 1)
        var bitmapCursor = db.query("SELECT bitmap FROM BiljkaBitmap")
        bitmapCursor.moveToFirst()
        assertThat(bitmapCursor.getStringOrNull(0)?.length ?: 0, greaterThan(100))
    }

    @Test
    fun a7_deleteAllAndCheckIfItsEmpty() = runBlocking {
        biljkaDAO.clearData()
        executeCountAndCheck(countBiljkaBitmaps, "broj_bitmapa", 0)
        executeCountAndCheck(countBiljka, "broj_biljaka", 0)
    }

    @Test
    fun testDodajBiljkuUBazu() = runBlocking {
        // Definiramo biljku koju ćemo dodati u bazu
        val biljka = Biljka(
            naziv = "Ružmarin",
            porodica = "Lamiaceae",
            medicinskoUpozorenje = "Može pomoći kod digestivnih problema.",
            medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Piletina s ružmarinom"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
        )

        // Dodajemo biljku u bazu
        biljkaDAO.saveBiljka(biljka)

        // Provjeravamo da li je biljka dodana u bazu
        val sveBiljke = biljkaDAO.getAllBiljkas()
        assertThat(sveBiljke.size, `is`(equalTo(1)))

        // Provjeravamo da li je dodana biljka ista kao biljka koju smo pokušali dodati
        assertThat(sveBiljke.size, `is`(equalTo(1)))
        assertThat(sveBiljke[0].naziv, `is`(equalTo(biljka.naziv)))
        assertThat(sveBiljke[0].porodica, `is`(equalTo(biljka.porodica)))
        assertThat(sveBiljke[0].medicinskoUpozorenje, `is`(equalTo(biljka.medicinskoUpozorenje)))
        assertThat(sveBiljke[0].medicinskeKoristi, `is`(equalTo(biljka.medicinskeKoristi)))
        assertThat(sveBiljke[0].profilOkusa, `is`(equalTo(biljka.profilOkusa)))
        assertThat(sveBiljke[0].jela, `is`(equalTo(biljka.jela)))
        assertThat(sveBiljke[0].klimatskiTipovi, `is`(equalTo(biljka.klimatskiTipovi)))
        assertThat(sveBiljke[0].zemljisniTipovi, `is`(equalTo(biljka.zemljisniTipovi)))
    }


    @Test
    fun testPrazanNizNakonClearData() = runBlocking {
        // Dodavanje biljke kako bi baza bila inicijalno popunjena
        val biljka = Biljka(
            naziv = "Lavanda",
            porodica = "Lamiaceae",
            medicinskoUpozorenje = "Općenito sigurna biljka za upotrebu.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od lavande"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        )
        biljkaDAO.saveBiljka(biljka)

        // Provjera da li su biljke dodane prije brisanja
        var sveBiljke = biljkaDAO.getAllBiljkas()
        assertThat(sveBiljke.size, not(equalTo(0)))

        // Brisanje svih biljaka iz baze
        biljkaDAO.clearData()

        // Provjera da getAllBiljkas vraća prazan niz nakon clearData
        sveBiljke = biljkaDAO.getAllBiljkas()
        assertThat(sveBiljke.size, `is`(equalTo(0)))
    }
}