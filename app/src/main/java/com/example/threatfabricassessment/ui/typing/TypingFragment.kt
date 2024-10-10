package com.example.threatfabricassessment.ui.typing

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.threatfabricassessment.R
import com.example.threatfabricassessment.databinding.FragmentTypingBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TypingFragment : Fragment(R.layout.fragment_typing) {
    private var _binding: FragmentTypingBinding? = null
    private val binding get() = _binding!!
    private val args: TypingFragmentArgs by navArgs()
    private val viewModel: TypingViewModel by inject()

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
        setupUi()
        viewModel.setReferenceText(getString(R.string.reference_text))
        collectEffect()
    }

    private fun collectEffect() {
        lifecycleScope.launch {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is TypingEffect.UpdateReferenceText -> {
                        underlineErrors(effect.errorPositions)
                    }
                }
            }
        }
    }

    private fun setupUi() = binding.apply {
        binding.userName.text = args.userName
        etType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let { userInput ->
                    viewModel.handleEvent(TypingEvent.TextTyped(userInput.toString()))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun underlineErrors(errorPositions: List<Int>) {
        val editable = binding.referenceTextView.text.toSpannable()
        val spans = editable.getSpans(0, editable.length, ForegroundColorSpan::class.java)
        for (span in spans) {
            editable.removeSpan(span)
        }

        errorPositions.forEach { index ->
            if (index in editable.indices) {
                editable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    index,
                    index + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.referenceTextView.text = editable
    }
}
