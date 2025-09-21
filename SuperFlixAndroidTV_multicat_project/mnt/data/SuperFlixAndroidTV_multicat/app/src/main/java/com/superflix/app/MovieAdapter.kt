package com.superflix.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    private val context: Context,
    private var items: List<Filme>,
    private val onItemClick: (Filme) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieVH>() {

    inner class MovieVH(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgPoster)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieVH(view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title ?: "-"
        Glide.with(context)
            .load(item.poster)
            .centerCrop()
            .into(holder.imgPoster)

        holder.itemView.isFocusable = true
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Filme>) {
        items = newItems
        notifyDataSetChanged()
    }
}
