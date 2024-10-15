package com.example.threatfabricassessment.ui.typing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.domain.models.TypingEvent
import com.example.threatfabricassessment.R
import com.example.threatfabricassessment.databinding.FragmentTypingBinding
import com.example.threatfabricassessment.utils.CustomKeyListener
import com.example.threatfabricassessment.utils.isCustomKeyboardActive
import com.example.threatfabricassessment.utils.setCustomOnKeyListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TypingFragment : Fragment(R.layout.fragment_typing), CustomKeyListener {
    private var _binding: FragmentTypingBinding? = null
    private val binding get() = _binding!!
    private val args: TypingFragmentArgs by navArgs()
    private val viewModel: TypingViewModel by viewModel()

    var keydownTime = 0L
    var keyupTime = 0L
    var hardwareKeyboard = false


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
        viewModel.setUserName(args.userName)
        viewModel.setReferenceText(getString(R.string.reference_text))
        collectEffect()
        collectState()
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    state.wpmState.let {
                        binding.feedbackTextView.text = "${state.wpmState} WPM"
                        binding.tvAdjustedWpm.text = "${state.adjustedWpmState} WPM"
                    }
                }
            }
        }
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
        etType.setCustomOnKeyListener(this@TypingFragment)

        etType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let { userInput ->
                    viewModel.handleEvent(TypingUIEvent.TextTyped(userInput.toString(), keydownTime))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(isSoftWareOnScreenKeyboard(hardwareKeyboard)){
                    keydownTime = System.currentTimeMillis()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                keyupTime = System.currentTimeMillis()
                if (isSoftWareOnScreenKeyboard(hardwareKeyboard)) {
                    if (count > 0) {
                        val addedChar = s?.subSequence(start + count -1, start + count).toString()
                        if (addedChar.isNotBlank()) {
                            viewModel.handleEvent(TypingUIEvent.CollectAnalytics(
                                TypingEvent(
                                    keyCode = addedChar,
                                    keyPressedTime = keydownTime,
                                    keyReleasedTime = keyupTime,
                                    resources.configuration.orientation
                                )
                            ))
                        }
                }
                }
                if (etType.text?.trim()?.length == referenceTextView.text.trim().length) {
                    etType.isEnabled = false
                    Snackbar.make(etType, "You have exceeded the length of the reference text", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }


    fun isSoftWareOnScreenKeyboard(isHardWareKeyboard: Boolean): Boolean {
        return !requireContext().isCustomKeyboardActive("com.example.threatfabricassessment/.utils.MyInputMethodService") && !isHardWareKeyboard
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


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra("tf_analytics", TypingEvent::class.java)
                } else {
                    intent.getSerializableExtra("tf_analytics") as TypingEvent
                }
                event?.let { viewModel.handleEvent(TypingUIEvent.CollectAnalytics(it)) }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("com.example.threatfabricassessment")
        ContextCompat.registerReceiver(requireContext(),receiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(receiver)
    }

    override fun onTypingEvent(typingEvent: TypingUIEvent.CollectAnalytics) {
        viewModel.handleEvent(typingEvent)
    }

    override fun isHardWareKeyboard(fromHardware: Boolean) {
        hardwareKeyboard = fromHardware
    }
}
