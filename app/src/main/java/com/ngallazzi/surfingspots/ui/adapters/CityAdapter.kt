package com.ngallazzi.surfingspots.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ngallazzi.surfingspots.R
import com.ngallazzi.surfingspots.data.cities.City
import com.ngallazzi.surfingspots.data.temperatures.WeatherCondition


class CityAdapter(private val context: Context) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val cities: ArrayList<City> = arrayListOf()

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

    fun submitUpdate(citiesUpdate: List<City>) {
        val callback = CityItemDiffCallback(cities, citiesUpdate)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)

        cities.clear()
        cities.addAll(citiesUpdate)
        diffResult.dispatchUpdatesTo(this)
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
                            R.string.weather_condition,
                            context.getString(R.string.sunny),
                            degrees
                        )
                        holder.clCityInfoContainer.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorCityInfoBackground
                            )
                        )
                    }
                    is WeatherCondition.Cloudy -> {
                        holder.tvCityForecast.text = context.getString(
                            R.string.weather_condition,
                            context.getString(R.string.cloudy),
                            degrees
                        )
                    }
                }
            } ?: run {
                holder.tvCityForecast.text = context.getString(R.string.no_weather_condition_yet)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val clCityInfoContainer: ConstraintLayout = view.findViewById(R.id.clCityInfoContainer)
        val ivCity: ImageView = view.findViewById(R.id.ivCity)
        val tvCityName: TextView = view.findViewById(R.id.tvCityName)
        val tvCityForecast: TextView = view.findViewById(R.id.tvCityForecast)
    }

    class CityItemDiffCallback(
        private val oldCitiesList: List<City>,
        private val newCitiesList: List<City>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldCitiesList.size
        }

        override fun getNewListSize(): Int {
            return newCitiesList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCitiesList[oldItemPosition].name == newCitiesList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCitiesList[oldItemPosition].temperature == newCitiesList[newItemPosition].temperature
        }
    }
}