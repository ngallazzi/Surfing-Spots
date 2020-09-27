package com.ngallazzi.surfingspots.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ngallazzi.surfingspots.R
import com.ngallazzi.surfingspots.data.cities.City
import com.ngallazzi.surfingspots.data.temperatures.WeatherCondition

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
            holder.tvCityName.text = this.name

            this.temperature?.let { degrees ->
                when (WeatherCondition.getWeather(degrees)) {
                    is WeatherCondition.Sunny -> {
                        Glide.with(context)
                            .load(this.imageUrl)
                            .into(holder.ivCity)
                        holder.tvCityForecast.text = context.getString(
                            R.string.wheater_condition,
                            context.getString(R.string.sunny),
                            degrees
                        )
                        holder.clCityInfoContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorCityInfoBackground))
                    }
                    is WeatherCondition.Cloudy -> {
                        holder.tvCityForecast.text = context.getString(
                            R.string.wheater_condition,
                            context.getString(R.string.cloudy),
                            degrees
                        )
                    }
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val clCityInfoContainer: ConstraintLayout = view.findViewById(R.id.clCityInfoContainer)
        val ivCity: ImageView = view.findViewById(R.id.ivCity)
        val tvCityName: TextView = view.findViewById(R.id.tvCityName)
        val tvCityForecast: TextView = view.findViewById(R.id.tvCityForecast)
    }
}