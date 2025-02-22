package com.example.retrofitforecaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofitforecaster.Utils.WhetherUtils
import com.example.retrofitforecaster.whether.DayPrognosis

const val VIEW_TYPE_HOT = 0;
const val VIEW_TYPE_COLD = 1;

class DayListAdapter(private var whetherUtils : WhetherUtils) : ListAdapter<DayPrognosis, RecyclerView.ViewHolder>(DayDiffCallback()) {
    inner class DayViewHolderHot(view: View) : RecyclerView.ViewHolder(view) {
        val datetime: TextView = view.findViewById(R.id.datetime)
        val plusTxt: TextView = view.findViewById(R.id.txt_plus_temperature)
        val icon: ImageView = view.findViewById(R.id.icon)

        fun bind(day: DayPrognosis) {
            datetime.text = day.dt_txt
            plusTxt.text = whetherUtils.getTemperatureAsString(day.main.temp)
            val iconUrl = "https://openweathermap.org/img/wn/${day.weather[0].icon}@2x.png"
            Glide.with(icon).load(iconUrl).into(icon)
        }
    }

    inner class DayViewHolderCold(view: View) : RecyclerView.ViewHolder(view) {
        val datetime: TextView = view.findViewById(R.id.datetime)
        val minusTxt: TextView = view.findViewById(R.id.txt_minus_temperature)
        val icon: ImageView = view.findViewById(R.id.icon)

        fun bind(day: DayPrognosis) {
            datetime.text = day.dt_txt
            minusTxt.text = whetherUtils.getTemperatureAsString(day.main.temp);
            val iconUrl = "https://openweathermap.org/img/wn/${day.weather[0].icon}@2x.png"
            Glide.with(icon).load(iconUrl).into(icon)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val day = currentList[position]
        return if (day.main.temp > 0) VIEW_TYPE_HOT else VIEW_TYPE_COLD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HOT) {
            DayViewHolderHot(LayoutInflater.from(parent.context).inflate(R.layout.r_item_hot, parent, false))
        } else {
            DayViewHolderCold(LayoutInflater.from(parent.context).inflate(R.layout.r_item_cold, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val day = currentList[position]
        if (holder is DayViewHolderHot) {
            holder.bind(day)
        } else if (holder is DayViewHolderCold) {
            holder.bind(day)
        }
    }

    override fun getItemCount(): Int = currentList.size
}

class DayDiffCallback : DiffUtil.ItemCallback<DayPrognosis>() {
    override fun areItemsTheSame(oldItem: DayPrognosis, newItem: DayPrognosis): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: DayPrognosis, newItem: DayPrognosis): Boolean {
        return oldItem == newItem
    }
}
