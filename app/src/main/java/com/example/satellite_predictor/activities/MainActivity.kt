package com.example.satellite_predictor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satellite_predictor.adapters.SatelliteAdapter
import com.example.satellite_predictor.databinding.ActivityMainBinding
import com.example.satellite_predictor.models.Satellite
import com.example.satellite_predictor.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SatelliteAdapter
    private val satelliteList = mutableListOf<Satellite>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.satelliteList.observe(this, Observer { list ->
            val size = satelliteList.size
            satelliteList.clear()
            satelliteList.addAll(list)
            adapter.notifyDataSetChanged()
            binding.recyclerView.smoothScrollToPosition(size)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage == null)
                binding.error.visibility = View.GONE
            else {
                binding.error.visibility = View.VISIBLE
                binding.error.setText(errorMessage)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        adapter = SatelliteAdapter(this, satelliteList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.loadMore.setOnClickListener {
            viewModel.getList()
        }
    }
}