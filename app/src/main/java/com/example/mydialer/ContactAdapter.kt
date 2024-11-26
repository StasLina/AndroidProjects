package com.example.mydialer

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private var contacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textPhone: TextView = itemView.findViewById(R.id.textPhone)
        val textType: TextView = itemView.findViewById(R.id.textType)

        fun bind(contact: Contact) {
            textName.text = contact.name
            textPhone.text = contact.phone

            // Обработчик клика для перехода в Dialer
            itemView.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${contact.phone}")
                }
                itemView.context.startActivity(dialIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.textName.text = contact.name
        holder.textPhone.text = contact.phone
        holder.textType.text = contact.type
        holder.bind(contact)
    }

    override fun getItemCount() = contacts.size

    fun filterContacts(filteredContacts: List<Contact>) {
        contacts = filteredContacts
        notifyDataSetChanged()
    }




}
