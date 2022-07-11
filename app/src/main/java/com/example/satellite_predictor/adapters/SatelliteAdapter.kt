package com.example.satellite_predictor.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.satellite_predictor.R
import com.example.satellite_predictor.models.Satellite
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class SatelliteAdapter(
    private val context: Context,
    private val satellite: List<Satellite>
) : RecyclerView.Adapter<SatelliteAdapter.SatelliteViewHolder>() {

    inner class SatelliteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val img = itemView.findViewById<ImageView>(R.id.imageView)
        private val chipGroup = itemView.findViewById<ChipGroup>(R.id.chipGroup)
        fun bind(satellite: Satellite) {
            var encoder = satellite.img
            encoder=encoder.substring(2,encoder.length-1)
            val prediction = satellite.predicted_values
            val list = prediction.split(",").toTypedArray()
            Log.i("Encoder",encoder)
            val bytes = Base64.decode(encoder, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            img.setImageBitmap(bitmap)
            for (tag in list) {
                val chip = Chip(context)
                chip.text = tag
                chipGroup.addView(chip)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.satellite_item, parent, false)
        return SatelliteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        holder.bind(satellite[position])
    }

    override fun getItemCount() = satellite.size
}