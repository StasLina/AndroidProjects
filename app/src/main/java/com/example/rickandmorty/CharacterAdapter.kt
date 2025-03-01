package com.example.rickandmorty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.API.Character
import com.example.rickandmorty.API.CharacterResponse
import timber.log.Timber


class CharacterAdapter : ListAdapter<Character, RecyclerView.ViewHolder>(CharacterDiffUtil()) {
    companion object {
        const val VIEW_TYPE_HUMAN = 0;
        const val VIEW_TYPE_ALIEN = 1;
        const val VIEW_TYPE_OTHER = 2;
    }

    inner class HumanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.smthText);
        val textAdd: TextView = itemView.findViewById(R.id.addText)

        fun bind(character: Character) {
            textView.text = character.name;
            textAdd.text = character.species
        }
    }
    inner class AlienViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.smthText)
        val textAdd: TextView = itemView.findViewById(R.id.addText)

        fun bind(character: Character) {
            textView.text = character.name;
            textAdd.text = character.species
        }
    }
    inner class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.smthText)
        val textAdd: TextView = itemView.findViewById(R.id.addText)

        fun bind(character: Character) {
            textView.text = character.name;
            textAdd.text = character.species
        }
    }

    override fun getItemViewType(position: Int): Int {
        val character = currentList[position]
        when (character.species) {
             "Human"-> {
                 return VIEW_TYPE_HUMAN;
             }
            "Alien" -> {
                return  VIEW_TYPE_ALIEN;
            }
        }
        return  VIEW_TYPE_OTHER;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_HUMAN -> {
                return HumanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.r_item, parent, false))
            }
            VIEW_TYPE_OTHER -> {
                return AlienViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.r_item, parent, false))
            }
        }
        return OtherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.r_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = currentList[position]
        Timber.d("CharacterAdapter", "Binding character at position $position: ${character.name}")
        when (holder) {
            is HumanViewHolder -> holder.bind(character)
            is AlienViewHolder -> holder.bind(character)
            is OtherViewHolder -> holder.bind(character)
        }
    }

    override fun getItemCount(): Int = currentList.size
}

class CharacterDiffUtil : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}