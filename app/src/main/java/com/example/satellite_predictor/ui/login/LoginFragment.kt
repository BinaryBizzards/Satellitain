package com.example.satellite_predictor.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.satellite_predictor.activities.Dashboard
import com.example.satellite_predictor.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.loginEmail.text.toString(),
                binding.loginPassword.text.toString(),
                binding.remember.isChecked
            )
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loginProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null)
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(activity, Dashboard::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }
        }

        viewModel.LoginStatus.observe(viewLifecycleOwner) { status ->
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