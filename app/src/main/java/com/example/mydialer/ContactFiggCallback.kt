package com.example.mydialer

import androidx.recyclerview.widget.DiffUtil

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        // Сравнение уникальных идентификаторов (здесь используем имя как пример)
        return oldItem.phone == newItem.phone
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        // Сравнение содержимого
        return oldItem == newItem
    }
}