package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NovaBiljkaActivity : AppCompatActivity(){
    private lateinit var jeloET: EditText
    private lateinit var dodajJeloBtn: Button
    private lateinit var jelaLV: ListView
    private lateinit var jelaList: ArrayList<String>
    private lateinit var adapterJela: ArrayAdapter<String>
    private lateinit var uslikajBiljkuBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var listaMK: ArrayList<MedicinskaKorist>
    private lateinit var listaPO: ProfilOkusaBiljke
    private lateinit var listaZT: ArrayList<Zemljiste>
    private lateinit var listaKT: ArrayList<KlimatskiTip>
    private lateinit var biljkaDao: BiljkaDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_biljka)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biljkaDao = BiljkaDatabase.getInstance(this).biljkaDao()

        //punjenje listi enumima


        val medicinskeKoristi = MedicinskaKorist.values()
        val adapter =
            medicinskiEnumAdapter(this, android.R.layout.simple_list_item_1, medicinskeKoristi)
        val listView: ListView = findViewById(R.id.medicinskaKoristLV)
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.setOnItemClickListener { parent, view, position, id ->
            val checked = listView.checkedItemPositions
            val selectedItemsMK = arrayListOf<MedicinskaKorist>()
            for (i in 0 until checked.size()) {
                val key = checked.keyAt(i)
                if (checked.get(key)) {
                    selectedItemsMK.add(medicinskeKoristi[key])
                }
            }
            listaMK = ArrayList(selectedItemsMK)
            val message = "Odabrali ste: ${selectedItemsMK.joinToString(", ")}"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        val klimatskiTip = KlimatskiTip.values()
        val adapter2 = klimatskiEnumAdapter(this, android.R.layout.simple_list_item_1, klimatskiTip)
        val listView2: ListView = findViewById(R.id.klimatskiTipLV)
        listView2.adapter = adapter2
        listView2.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView2.setOnItemClickListener { parent, view, position, id ->
            val checked = listView2.checkedItemPositions
            val selectedItemsKT = arrayListOf<KlimatskiTip>()
            for (i in 0 until checked.size()) {
                val key = checked.keyAt(i)
                if (checked.get(key)) {
                    selectedItemsKT.add(klimatskiTip[key])
                }
            }
            listaKT = selectedItemsKT
            val message = "Odabrali ste: ${selectedItemsKT.joinToString(", ")}"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        val zemljisniTip = Zemljiste.values()
        val adapter3 = zemljisniEnumAdapter(this, android.R.layout.simple_list_item_1, zemljisniTip)
        val listView3: ListView = findViewById(R.id.zemljisniTipLV)
        listView3.adapter = adapter3
        listView3.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView3.setOnItemClickListener { parent, view, position, id ->
            val checked = listView3.checkedItemPositions
            val selectedItemsZT = arrayListOf<Zemljiste>()
            for (i in 0 until checked.size()) {
                val key = checked.keyAt(i)
                if (checked.get(key)) {
                    selectedItemsZT.add(zemljisniTip[key])
                }
            }
            listaZT = selectedItemsZT
            val message = "Odabrali ste: ${selectedItemsZT.joinToString(", ")}"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        val profilOkusa = ProfilOkusaBiljke.values()
        val adapter4 = ProfilOkusaAdapter(this, android.R.layout.simple_list_item_1, profilOkusa)
        val listView4: ListView = findViewById(R.id.profilOkusaLV)
        listView4.adapter = adapter4
        listView4.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView4.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = profilOkusa[position]
            listaPO = selectedItem

            val message = "Odabrali ste: $selectedItem"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        //dodavanje jela

        jeloET = findViewById(R.id.jeloET)
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
        jelaLV = findViewById(R.id.jelaLV)

        jelaList = arrayListOf()
        adapterJela = ArrayAdapter(this, android.R.layout.simple_list_item_1, jelaList)
        jelaLV.adapter = adapterJela

        dodajJeloBtn.setOnClickListener {
            val novoJelo = jeloET.text.toString()
            if (novoJelo.isNotEmpty()) {
                val indexOdabranogJela = jelaLV.checkedItemPosition
                if (indexOdabranogJela != ListView.INVALID_POSITION) {
                    // Ako je odabrano postojeće jelo
                    if (validacijaJela()) jelaList[indexOdabranogJela] = novoJelo
                    else validacijaJela()
                    adapterJela.notifyItemChanged(indexOdabranogJela)
                    jelaLV.clearChoices()
                } else {

                    if (validacijaJela() && validacijaListeJela()) jelaList.add(novoJelo)
                    else {
                        validacijaJela()
                        validacijaListeJela()
                    }
                    adapterJela.notifyItemInserted(jelaList.size - 1)
                }
                jeloET.setText("")
                dodajJeloBtn.text = "Dodaj jelo"
            } else if (novoJelo.isEmpty()) {
                // Ako je tekst u EditText-u prazan, obriši odabrano jelo (ako postoji)
                val indexOdabranogJela = jelaLV.checkedItemPosition
                if (indexOdabranogJela != ListView.INVALID_POSITION) {
                    jelaList.removeAt(indexOdabranogJela)
                    adapterJela.notifyItemRemoved(indexOdabranogJela)
                    jelaLV.adapter = adapterJela
                    jelaLV.clearChoices()
                    jeloET.setText("")
                    dodajJeloBtn.text = "Dodaj jelo"
                }
            }
        }

        // slikanje biljke

        jelaLV.setOnItemClickListener { _, _, position, _ ->

            val odabranoJelo = jelaList[position]
            jeloET.setText(odabranoJelo)
            dodajJeloBtn.text = "Izmijeni jelo"
        }
        imageView = findViewById(R.id.slikaIV)
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)
        uslikajBiljkuBtn.setOnClickListener {
            if (checkCameraPermission()) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
            }
        }

        //dodavanje biljke

        val dodajBiljkuBtn: Button = findViewById(R.id.dodajBiljkuBtn)
        dodajBiljkuBtn.setOnClickListener {
            if (provjeriValidacijuIPrikaziGreske()) {
                val nazivET: EditText = findViewById(R.id.nazivET)
                val porodicaET: EditText = findViewById(R.id.porodicaET)
                val medicinskoUpozorenjeET: EditText = findViewById(R.id.medicinskoUpozorenjeET)
                val biljka = Biljka(
                    null,
                    nazivET.text.toString(),
                    porodicaET.text.toString(),
                    medicinskoUpozorenjeET.text.toString(),
                    listaMK,
                    listaPO,
                    jelaList,
                    listaKT,
                    listaZT
                )
                val tf=TrefleDAO()
                tf.setContext(this)
                var biljka2=biljka

                ispravljanjeBiljke(biljka) { ispravljenaBiljka ->
                    biljka2=ispravljenaBiljka
                    saveBiljka(biljka2)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("kljuc", biljka2)
                    startActivity(intent)
                }

                }

            }
            //oncreate
        }

    //NOVO
   private fun saveBiljka(biljka: Biljka) {
       GlobalScope.launch(Dispatchers.Main) {
           val isSaved = withContext(Dispatchers.IO) {
               biljkaDao.saveBiljka(biljka)
           }
           if (isSaved) {
               finish()
           } else {
             TODO()
           }
       }
   }
    //NOVO
        private fun provjeriValidacijuIPrikaziGreske(): Boolean {
            val nazivET: EditText = findViewById(R.id.nazivET)
            val porodicaET: EditText = findViewById(R.id.porodicaET)
            val medicinskoUpozorenjeET: EditText = findViewById(R.id.medicinskoUpozorenjeET)
            val jeloET: EditText = findViewById(R.id.jeloET)

            // Validacija dužine teksta
            if (nazivET.text.length < 2 || nazivET.text.length > 40) {
                nazivET.error = "Naziv mora biti između 2 i 20 znakova."
                return false
            }

            if (porodicaET.text.length < 2 || porodicaET.text.length > 20) {
                porodicaET.error = "Porodica mora biti između 2 i 20 znakova."
                return false
            }

            if (medicinskoUpozorenjeET.text.length < 2 || medicinskoUpozorenjeET.text.length > 20) {
                medicinskoUpozorenjeET.error =
                    "Medicinsko upozorenje mora biti između 2 i 20 znakova."
                return false
            }

            if (jelaList.isEmpty()) {
                Toast.makeText(this, "Nije odabrano niti jedno jelo", Toast.LENGTH_SHORT).show()
                return false
            }

            if (!this::listaKT.isInitialized || !this::listaMK.isInitialized || !this::listaZT.isInitialized) {
                Toast.makeText(
                    this,
                    "U listama za odabir više vrijednosti bar jedna vrijednost treba biti odabrana",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            if (!this::listaPO.isInitialized) return false

            return true
        }

        private fun validacijaJela(): Boolean {
            val jeloET: EditText = findViewById(R.id.jeloET)

            // Validacija dužine teksta
            if (jeloET.text.length < 2 || jeloET.text.length > 20) {
                jeloET.error = "Jelo mora biti između 2 i 20 znakova."
                return false
            }

            return true
        }

        private fun validacijaListeJela(): Boolean {
            val unesenoJelo = jeloET.text.toString().toLowerCase()
            if (jelaList.any { it.toLowerCase() == unesenoJelo }) {
                jeloET.error = "Jelo već postoji u listi."
                return false
            }
            return true
        }

        val REQUEST_IMAGE_CAPTURE = 1

        private fun dispatchTakePictureIntent() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Kamera nije pronađena", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
                val imageBitmap = data.extras?.get("data") as? Bitmap
                imageView.setImageBitmap(imageBitmap)
            }
        }

        private fun checkCameraPermission(): Boolean {
            return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun requestCameraPermission() {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
            )
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchTakePictureIntent()
                } else {

                    Toast.makeText(this, "Dozvola za kameru nije odobrena", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


private fun <T> ArrayAdapter<T>.notifyItemRemoved(index: Int) {
    if (index >= 0 && index < count) {
        remove(getItem(index))
    }
}



private fun <T> ArrayAdapter<T>.notifyItemInserted(index: Int) {
    if (index >= 0 && index <= count) {
        notifyDataSetChanged()
    }
}

private fun <T> ArrayAdapter<T>.notifyItemChanged(indexOdabranogJela: Int) {
    if (indexOdabranogJela >= 0 && indexOdabranogJela < count) {
        notifyDataSetChanged()
    }
}
    fun ispravljanjeBiljke(biljka: Biljka, callback: (Biljka) -> Unit) {
        val tf = TrefleDAO(/*this*/)
        tf.setContext(this)
        CoroutineScope(Dispatchers.Main).launch {
            val ispravljenaBiljka = tf.fixData(biljka)
            callback(ispravljenaBiljka)
        }
    }

}