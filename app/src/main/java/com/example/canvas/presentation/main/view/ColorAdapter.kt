package com.example.canvas.presentation.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.canvas.R
import com.google.android.material.card.MaterialCardView

class ColorAdapter(
    private val colors: List<Int>,
    private var selectedColor: Int,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // Исправлено здесь
        private val cardColor: MaterialCardView = itemView.findViewById(R.id.cardColor)

        fun bind(color: Int, isSelected: Boolean) {
            cardColor.apply {
                setCardBackgroundColor(color)
                strokeWidth = if (isSelected) 4 else 0
                strokeColor = ContextCompat.getColor(context, R.color.black)
                setOnClickListener {
                    onColorSelected(color)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ColorViewHolder(inflater.inflate(R.layout.item_color, parent, false))
    }
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.bind(color, color == selectedColor)
    }

    override fun getItemCount() = colors.size

    fun updateSelected(newColor: Int) {
        val prevPosition = colors.indexOf(selectedColor)
        selectedColor = newColor
        prevPosition.takeIf { it != -1 }?.let { notifyItemChanged(it) }
        colors.indexOf(newColor).takeIf { it != -1 }?.let { notifyItemChanged(it) }
    }
}
