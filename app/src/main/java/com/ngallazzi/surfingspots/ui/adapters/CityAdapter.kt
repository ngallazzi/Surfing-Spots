package com.ngallazzi.surfingspots.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ngallazzi.surfingspots.R
import com.ngallazzi.surfingspots.data.cities.City

class CityAdapter(
    private val cities: List<City>,
    private val context: Context
) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_city,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cities[position].run {
            Glide.with(context)
                .load(this.imageUrl)
                .into(holder.ivCity)

            holder.tvCityName.text = this.name
            holder.tvCityForecast.text = this.temperature.toString()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCity: ImageView = view.findViewById(R.id.ivCity)
        val tvCityName: TextView = view.findViewById(R.id.tvCityName)
        val tvCityForecast: TextView = view.findViewById(R.id.tvCityForecast)
    }
}