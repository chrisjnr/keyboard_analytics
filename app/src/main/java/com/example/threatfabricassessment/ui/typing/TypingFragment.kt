package com.example.threatfabricassessment.ui.typing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.threatfabricassessment.R
import com.example.threatfabricassessment.databinding.FragmentTypingBinding

class TypingFragment : Fragment(R.layout.fragment_typing) {
    private var _binding: FragmentTypingBinding? = null
    private val binding get() = _binding!!
    private val args: TypingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTypingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userName.text = args.userName
    }
}
