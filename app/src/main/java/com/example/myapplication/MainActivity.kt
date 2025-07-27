package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var adapter1: RecyclerView
    private lateinit var medBiljkeAdapter: MedicinskiListAdapter
    private lateinit var kuharskiAdapter: KuharskiListAdapter
    private lateinit var botanickiAdapter: BotanickiListAdapter
    private lateinit var spinner: Spinner
    //private var biljke = getBiljke()
    private var biljke: MutableList<Biljka> = mutableListOf()
    private var filteredBiljke: MutableList<Biljka> = mutableListOf()
    private lateinit var dugme: Button
    private lateinit var brzaPretraga: Button
    private lateinit var bojaSPIN: Spinner
    private lateinit var pretragaET: EditText
    var filtriraj=true
    var filtriranje=0
    private lateinit var biljkaDao: BiljkaDAO



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        biljkaDao = BiljkaDatabase.getInstance(this).biljkaDao()
        dugme=findViewById(R.id.resetBtn)
        adapter1 = findViewById(R.id.biljkeRV)
        adapter1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        medBiljkeAdapter = MedicinskiListAdapter(listOf())
        kuharskiAdapter = KuharskiListAdapter(listOf())
        botanickiAdapter= BotanickiListAdapter(listOf())
        loadBiljkas()
        medBiljkeAdapter.setItemClickListener { clickedBiljka ->
            filtriraj(clickedBiljka, 0)
        }

        kuharskiAdapter.setItemClickListener { clickedBiljka ->
            filtriraj(clickedBiljka, 1)
        }
        if(filtriraj==true) {
            botanickiAdapter.setItemClickListener { clickedBiljka ->
                filtriraj(clickedBiljka, 2)
            }
        }
        dugme.setOnClickListener{
            loadBiljkas()
            filtriraj=true
            filtriranje=0
        }
        spinner = findViewById(R.id.modSpinner)
        bojaSPIN = findViewById(R.id.bojaSPIN)
        pretragaET = findViewById(R.id.pretragaET)
        brzaPretraga = findViewById(R.id.brzaPretraga)
        ArrayAdapter.createFromResource(this, R.array.modovi_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position == 0) {
                            adapter1.adapter = medBiljkeAdapter
                            if(filtriranje==0) loadBiljkas()
                            else medBiljkeAdapter.updateMedBiljke(biljke)
                            pretragaET.visibility = View.GONE
                            bojaSPIN.visibility = View.GONE
                            brzaPretraga.visibility = View.GONE
                        }
                        else if(position==1){
                            adapter1.adapter = botanickiAdapter
                            if(filtriranje==0) loadBiljkas()
                            else botanickiAdapter.updateBotBiljke(biljke)
                            pretragaET.visibility = View.VISIBLE
                            bojaSPIN.visibility = View.VISIBLE
                            brzaPretraga.visibility = View.VISIBLE
                    }
                        else if(position == 2) {
                            adapter1.adapter = kuharskiAdapter
                            if(filtriranje==0) loadBiljkas()
                            else kuharskiAdapter.updateKuhBiljke(biljke)
                            pretragaET.visibility = View.GONE
                            bojaSPIN.visibility = View.GONE
                            brzaPretraga.visibility = View.GONE
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
            }
        ArrayAdapter.createFromResource(this, R.array.boje, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bojaSPIN.adapter = adapter
            }
        val dugme = findViewById<Button>(R.id.novaBiljkaBtn)
        dugme.setOnClickListener {
            val intent = Intent(this, NovaBiljkaActivity::class.java)
            startActivity(intent)
        }


        brzaPretraga.setOnClickListener {
            val flowerColor = bojaSPIN.selectedItem.toString()
            filtriraj=false
            val substr = pretragaET.text.toString()
            if (substr.isNotEmpty()) {
                searchPlantsByColorAndQuery(flowerColor, substr)
            }
        }

    }
    private fun searchPlantsByColorAndQuery(flowerColor: String, substr: String) {
        val trefleDAO = TrefleDAO()

        CoroutineScope(Dispatchers.IO).launch {
            val plants = trefleDAO.getPlantsWithFlowerColor(flowerColor, substr)
            Log.d(TAG, "Primam biljke $plants")
            runOnUiThread {
                botanickiAdapter.updateBotBiljke(plants)
            }
        }
    }

    private fun loadBiljkas() {
        GlobalScope.launch(Dispatchers.Main) {
            val biljkas = withContext(Dispatchers.IO) {
                biljkaDao.getAllBiljkas()
            }
            medBiljkeAdapter.updateMedBiljke(biljkas)
            botanickiAdapter.updateBotBiljke(biljkas)
            kuharskiAdapter.updateKuhBiljke(biljkas)
        }
    }

    /*private fun filtriraj(clickedBiljka: Biljka, mode: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val biljkas = withContext(Dispatchers.IO) {
                biljkaDao.getAllBiljkas()
            }
            biljke = biljkas.toMutableList()

            filteredBiljke.clear()

            when (mode) {
                0 -> {  // Medicinski mod
                    filteredBiljke = biljke.filter { biljka ->
                        biljka.medicinskeKoristi.any { korist ->
                            clickedBiljka.medicinskeKoristi.contains(korist)
                        }
                    }.toMutableList()
                }
                1 -> {  // Botanički mod
                    if(filtriraj){
                    filteredBiljke = biljke.filter { biljka ->
                        biljka.zemljisniTipovi.any { zemljiste ->
                            clickedBiljka.zemljisniTipovi.contains(zemljiste)
                        } && biljka.klimatskiTipovi.any { klima ->
                            clickedBiljka.klimatskiTipovi.contains(klima)
                        }
                    }.toMutableList()}
                }
                2 -> {  // Kuharski mod
                    filteredBiljke = biljke.filter { biljka ->
                        biljka.jela.any { jelo ->
                            clickedBiljka.jela.contains(jelo)
                        }
                    }.toMutableList()
                }
            }

            biljke = filteredBiljke
            medBiljkeAdapter.updateMedBiljke(biljke)
            botanickiAdapter.updateBotBiljke(biljke)
            kuharskiAdapter.updateKuhBiljke(biljke)

        }
        filtriranje=1
    }*/
    private fun filtriraj(clickedBiljka: Biljka, mode: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val biljkas = withContext(Dispatchers.IO) {
                biljkaDao.getAllBiljkas()
            }
            biljke = biljkas.toMutableList()

            filteredBiljke.clear()

            when (mode) {
                0 -> {  // Medicinski mod
                    filteredBiljke = biljke.filter { biljka ->
                        biljka.medicinskeKoristi.any { korist ->
                            clickedBiljka.medicinskeKoristi.contains(korist)
                        }
                    }.toMutableList()
                }
                1 -> {  // Botanički mod
                    filteredBiljke = biljke.filter { biljka ->
                        biljka.porodica == clickedBiljka.porodica &&
                                biljka.zemljisniTipovi.any { zemljiste ->
                                    clickedBiljka.zemljisniTipovi.contains(zemljiste)
                                } && biljka.klimatskiTipovi.any { klima ->
                            clickedBiljka.klimatskiTipovi.contains(klima)
                        }
                    }.toMutableList()}
                2 -> {  // Kuharski mod
                    filteredBiljke = biljke.filter { biljka ->
                        biljka.profilOkusa == clickedBiljka.profilOkusa ||
                                biljka.jela.any { jelo ->
                                    clickedBiljka.jela.contains(jelo)
                                }
                    }.toMutableList()
                }
            }

            biljke = filteredBiljke
            medBiljkeAdapter.updateMedBiljke(biljke)
            botanickiAdapter.updateBotBiljke(biljke)
            kuharskiAdapter.updateKuhBiljke(biljke)
        }
        filtriranje = 1
    }

}

