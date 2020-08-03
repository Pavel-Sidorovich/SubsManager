package com.pavesid.subsmanager.screens.add

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pavesid.subsmanager.R
import com.pavesid.subsmanager.models.Color

class ColorAdapter(private val colors: List<Color>, private val listener: ColorListener) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ColorViewHolder(inflater.inflate(R.layout.list_color_item, parent, false))
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var color: Color

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(color: Color) {
            this.color = color

            itemView.setBackgroundResource(color.color)
        }

        override fun onClick(v: View?) {
            listener.colorClicked(this.color)
        }
    }

    interface ColorListener {
        fun colorClicked(color: Color)
    }
}