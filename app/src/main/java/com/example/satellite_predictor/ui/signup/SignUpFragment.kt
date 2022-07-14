package com.example.satellite_predictor.ui.signup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.satellite_predictor.activities.Dashboard
import com.example.satellite_predictor.activities.MainActivity
import com.example.satellite_predictor.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnSignup.setOnClickListener {
            viewModel.signup(
                binding.signupName.text.toString(),
                binding.signupEmail.text.toString(),
                binding.signupPassword.text.toString()
            )
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.signupProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null)
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.isRegistered.observe(viewLifecycleOwner) { isRegistered ->
            if (isRegistered) {
                val intent = Intent(activity, Dashboard::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }
        }

        viewModel.SignUpStatus.observe(viewLifecycleOwner) { status ->
            if (status != null)
                Toast.makeText(activity, status, Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}