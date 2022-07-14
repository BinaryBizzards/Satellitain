package com.example.satellite_predictor.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.satellite_predictor.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed(Runnable {
            val i = Intent(
                this@SplashScreen,
                AuthActivity::class.java
            )
            startActivity(i)
            finish()
        }, 1200)
    }
}