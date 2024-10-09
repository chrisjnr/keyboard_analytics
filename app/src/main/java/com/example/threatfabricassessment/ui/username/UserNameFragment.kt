package com.example.threatfabricassessment.ui.username

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.threatfabricassessment.R
import com.example.threatfabricassessment.databinding.FragmentUserNameBinding

const val USERNAME = "userName"

class UserNameFragment : Fragment(R.layout.fragment_user_name) {
    private var _binding: FragmentUserNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() = binding.apply {
        btnContinue.setOnClickListener {
            if (etUsername.text.toString().length >= 3) {
                findNavController().navigate(
                    R.id.action_userNameFragment_to_typingFragment, bundleOf(
                        USERNAME to etUsername.text.toString()
                    )
                )
            } else {
                tilUserName.error = getString(R.string.error_please_enter_at_least_3_letters)
            }
        }
    }
}
