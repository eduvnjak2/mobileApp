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

class KuharskiListAdapter(
    private var kuhBiljke: List<Biljka>
) : RecyclerView.Adapter<KuharskiListAdapter.KuharskiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuharskiViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.kuharski2, parent, false)
        return KuharskiViewHolder(view)
    }
    private var onItemClickListener: ((Biljka) -> Unit)? = null
    override fun getItemCount(): Int = kuhBiljke.size

    override fun onBindViewHolder(holder: KuharskiViewHolder, position: Int) {
        val biljka=kuhBiljke[position]
        holder.biljkaTitle.text = kuhBiljke[position].naziv
        holder.profilOkusa.text= kuhBiljke[position].profilOkusa.opis
        holder.jelo1.text= kuhBiljke[position].jela.getOrNull(0)
        holder.jelo2.text= kuhBiljke[position].jela.getOrNull(1)
        holder.jelo3.text= kuhBiljke[position].jela.getOrNull(2)
        val trefle= TrefleDAO(/*holder.biljkaImage.getContext()*/)
        trefle.setContext(holder.biljkaImage.getContext())
        /*CoroutineScope(Dispatchers.Main).launch {
            val imageUrl = withContext(Dispatchers.IO) {
                trefle.getImage(kuhBiljke[position])
            }
            Glide.with(holder.itemView)
                .load(imageUrl)
                .placeholder(R.drawable.slika1)
                .error(R.drawable.slika1)
                .into(holder.biljkaImage)
        }*/
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
        }
    }
    fun updateKuhBiljke(medBiljke: List<Biljka>) {
        this.kuhBiljke = medBiljke
        notifyDataSetChanged()
    }
    fun setItemClickListener(listener: (Biljka) -> Unit) {
        this.onItemClickListener = listener
    }
    inner class KuharskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val biljkaImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val biljkaTitle: TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusa: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3: TextView = itemView.findViewById(R.id.jelo3Item)
        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(kuhBiljke[adapterPosition])
            }
        }
    }
}