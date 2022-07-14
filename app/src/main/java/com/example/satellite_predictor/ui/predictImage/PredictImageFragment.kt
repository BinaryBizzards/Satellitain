package com.example.satellite_predictor.ui.predictImage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.satellite_predictor.R

class PredictImageFragment : Fragment() {

    companion object {
        fun newInstance() = PredictImageFragment()
    }

    private lateinit var viewModel: PredictImageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_predict_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PredictImageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}