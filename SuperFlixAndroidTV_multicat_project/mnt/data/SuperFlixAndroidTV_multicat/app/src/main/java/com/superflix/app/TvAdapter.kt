package com.superflix.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TvAdapter(
    private val context: Context,
    private val items: List<TvCanal>,
    private val onItemClick: (TvCanal) -> Unit
) : RecyclerView.Adapter<TvAdapter.TvVH>() {

    inner class TvVH(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgPoster)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return TvVH(view)
    }

    override fun onBindViewHolder(holder: TvVH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.name ?: "-"
        Glide.with(context)
            .load(item.logo)
            .centerCrop()
            .into(holder.imgPoster)

        holder.itemView.isFocusable = true
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
