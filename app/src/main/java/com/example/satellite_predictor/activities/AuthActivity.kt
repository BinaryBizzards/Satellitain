package com.example.satellite_predictor.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.satellite_predictor.R
import com.example.satellite_predictor.databinding.ActivityAuthBinding
import com.example.satellite_predictor.ui.login.LoginFragment
import com.example.satellite_predictor.ui.signup.SignUpFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.auth_fragment, LoginFragment.newInstance()).commit()

        binding.login.setOnClickListener(View.OnClickListener {
            binding.login.setTextColor(resources.getColor(R.color.light_teal))
            binding.signup.setTextColor(resources.getColor(R.color.teal))
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit()
        })

        binding.signup.setOnClickListener(View.OnClickListener {
            binding.signup.setTextColor(resources.getColor(R.color.light_teal))
            binding.login.setTextColor(resources.getColor(R.color.teal))
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment, SignUpFragment.newInstance())
                .addToBackStack(null)
                .commit()
        })
    }
}