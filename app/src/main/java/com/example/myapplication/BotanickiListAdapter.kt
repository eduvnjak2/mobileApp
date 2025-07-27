package com.example.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BotanickiListAdapter(
    private var botBiljke: List<Biljka>
) : RecyclerView.Adapter<BotanickiListAdapter.BotanickiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotanickiViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.botanicki, parent, false)
        return BotanickiViewHolder(view)
    }
    private var onItemClickListener: ((Biljka) -> Unit)? = null
    override fun getItemCount(): Int = botBiljke.size

    override fun onBindViewHolder(holder: BotanickiViewHolder, position: Int) {
        val biljka: Biljka = botBiljke[position]
        holder.biljkaTitle.text = botBiljke[position].naziv
        holder.porodica.text=biljka.porodica
        val klimatskiTipovi = botBiljke[position].klimatskiTipovi
        val klimatskiOpis = if (klimatskiTipovi.isNotEmpty()) klimatskiTipovi[0].opis else ""
        holder.klimatskiTip.text = klimatskiOpis

        val zemljisniTip = botBiljke[position].zemljisniTipovi
        //holder.zemljisniTip.text=botBiljke[position].zemljisniTipovi[0].naziv
        val zemljisniOpis = if (zemljisniTip.isNotEmpty()) zemljisniTip[0].naziv else ""
        holder.zemljisniTip.text = zemljisniOpis
        val trefle= TrefleDAO(/*holder.biljkaImage.getContext()*/)
        trefle.setContext(holder.biljkaImage.getContext())
        CoroutineScope(Dispatchers.Main).launch {
            val imageUrl = withContext(Dispatchers.IO) {
                trefle.getImage(biljka)
            }
            Glide.with(holder.itemView)
                .load(imageUrl)
                .placeholder(R.drawable.slika1)
                .error(R.drawable.slika1)
                .into(holder.biljkaImage)
        }
        /*
        CoroutineScope(Dispatchers.Main).launch {
            val context = holder.biljkaImage.context
            val database = BiljkaDatabase.getInstance(context)
            val biljkaDao = database.biljkaDao()

            // Check if image exists in the database
            val bitmap = withContext(Dispatchers.IO) {
                biljkaDao.getImageForBiljka(biljka.id!!)
            }

            if (bitmap != null) {
                // If image exists in the database, use it
                holder.biljkaImage.setImageBitmap(bitmap)
            } else {
                // Otherwise, download the image and save it
                val trefle = TrefleDAO()
                trefle.setContext(context)
                val imageUrl = withContext(Dispatchers.IO) {
                    trefle.getImage(biljka)
                }
                Glide.with(holder.itemView)
                    .load(imageUrl)
                    .placeholder(R.drawable.slika1)
                    .error(R.drawable.slika1)
                    .into(holder.biljkaImage)

                // Save the downloaded image to the database
                val downloadedBitmap = withContext(Dispatchers.IO) {
                    Glide.with(holder.itemView.context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()
                }
                withContext(Dispatchers.IO) {
                    biljkaDao.addOrUpdateImage(biljka.id!!, downloadedBitmap)
                }
            }
        }*/

    }
    fun updateBotBiljke(botBiljke: List<Biljka>) {
        this.botBiljke = botBiljke
        notifyDataSetChanged()
    }
    fun setItemClickListener(listener: (Biljka) -> Unit) {
        this.onItemClickListener = listener
    }
    inner class BotanickiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val biljkaImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val biljkaTitle: TextView = itemView.findViewById(R.id.nazivItem)
        val porodica: TextView = itemView.findViewById(R.id.porodicaItem)
        val klimatskiTip: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        val zemljisniTip: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(botBiljke[adapterPosition])
            }
        }
    }
}