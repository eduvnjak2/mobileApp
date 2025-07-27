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

class MedicinskiListAdapter(
    private var medBiljke: List<Biljka>
) : RecyclerView.Adapter<MedicinskiListAdapter.MedicinskiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinskiViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.medicinski2, parent, false)
        return MedicinskiViewHolder(view)
    }
    //novo
    private var onItemClickListener: ((Biljka) -> Unit)? = null
    override fun getItemCount(): Int = medBiljke.size

    override fun onBindViewHolder(holder: MedicinskiViewHolder, position: Int) {
        val biljka=medBiljke[position]
        holder.biljkaTitle.text = medBiljke[position].naziv
        holder.upozorenje.text=medBiljke[position].medicinskoUpozorenje
        holder.korist1.text= medBiljke[position].medicinskeKoristi.getOrNull(0)?.opis
        holder.korist2.text= medBiljke[position].medicinskeKoristi.getOrNull(1)?.opis
        holder.korist3.text= medBiljke[position].medicinskeKoristi.getOrNull(2)?.opis
        val trefle= TrefleDAO(/*holder.biljkaImage.getContext()*/)
        trefle.setContext(holder.biljkaImage.getContext())
        /*CoroutineScope(Dispatchers.Main).launch {
            val imageUrl = withContext(Dispatchers.IO) {
                trefle.getImage(medBiljke[position])
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
    fun updateMedBiljke(medBiljke: List<Biljka>) {
        this.medBiljke = medBiljke
        notifyDataSetChanged()
    }
    //novo
    fun setItemClickListener(listener: (Biljka) -> Unit) {
        this.onItemClickListener = listener
    }
    inner class MedicinskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val biljkaImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val biljkaTitle: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3: TextView = itemView.findViewById(R.id.korist3Item)
        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(medBiljke[adapterPosition])
            }
        }
    }
}