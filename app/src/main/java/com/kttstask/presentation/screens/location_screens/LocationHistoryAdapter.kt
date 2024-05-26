package com.kttstask.presentation.screens.location_screens

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.kttstask.R
import com.kttstask.data.models.Location
import com.kttstask.util.getFormattedDate

class LocationHistoryAdapter(
    var history: List<Location>,
    val navController: NavController
): RecyclerView.Adapter<LocationHistoryAdapter.LocationHistoryViewHolder>() {

    inner class LocationHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val latitude = itemView.findViewById<TextView>(R.id.tv_latitude)
        val longitude = itemView.findViewById<TextView>(R.id.tv_longitude)
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        val historyCard = itemView.findViewById<CardView>(R.id.history_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHistoryViewHolder {
        return LocationHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_location_history, parent, false))
    }

    override fun getItemCount(): Int {
        return history.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: LocationHistoryViewHolder, position: Int) {
        holder.latitude.text = history.get(position).latitude.toString()
        holder.longitude.text = history.get(position).longitude.toString()
        holder.time.text = getFormattedDate(history.get(position).time)

        holder.historyCard.setOnClickListener {
            val selectedLocation = history[position]
            val processedHistoryList = history.subList(position, history.size)
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToMapFragment(selectedLocation, processedHistoryList.toTypedArray()))
        }
    }

}