package one.njk.celestidesk.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.databinding.NewRequestSheetBinding
import one.njk.celestidesk.domain.NewBreakRequest
import one.njk.celestidesk.network.NetworkResult
import one.njk.celestidesk.viewmodels.RequestViewModel

@AndroidEntryPoint
class NewRequestSheet(val datePicker: () -> Unit): BottomSheetDialogFragment() {

    private lateinit var _binding: NewRequestSheetBinding
    private val viewModel: RequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewRequestSheetBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        _binding.apply {

            // Default value, coz android is headache
            var isEmergency = false
            emergency.setOnClickListener { _ ->
                isEmergency = true
            }
            normal.setOnClickListener { _ ->
                isEmergency = false
            }
            normal.performClick()

            durationEdit.setOnFocusChangeListener { editText, hasFocus ->
                if(hasFocus){
                    datePicker()
                }
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            }

            send.setOnClickListener { _ ->

                val subjectInput = subject.editText?.text.toString().trim()
                val reasonInput = reason.editText?.text.toString().trim()
                val durationInput = duration.editText?.text.toString().trim()

                // Error texts
                subject.error = validateSubject(subjectInput)
                reason.error = validateReason(reasonInput)
                duration.error = validateDuration(durationInput)

                if(subject.error == null && reason.error == null && duration.error == null){
                    val req = NewBreakRequest(
                        subject = subjectInput,
                        message = reasonInput,
                        emergency = isEmergency,
                        from = durationInput.slice(0..9),
                        to = durationInput.slice(13..22),
                    )
                    // Date value in edit field: 2023-08-17 - 2023-08-31

                    lifecycleScope.launch {
                        viewModel.newRequest(req)
                    }
                }
            }

            viewModel.state.observe(viewLifecycleOwner){
                if(it.isLoading) {
                    loading.visibility = View.VISIBLE
                    send.visibility = View.GONE
                } else {
                    loading.visibility = View.INVISIBLE
                    send.visibility = View.VISIBLE
                }

                if(it.status is NetworkResult.Failed){
                    Toast.makeText(requireContext(), "Please try again (or) You've used up all 5 requests this month! ", Toast.LENGTH_LONG).show()
                } else if (it.status is NetworkResult.Success) {
                    this@NewRequestSheet.dismiss()
                    Toast.makeText(requireContext(), "Request successfully raised", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun validateSubject(subject: String): String? {
        if(subject.isEmpty()) return "Subject is empty!"
        return null
    }
    private fun validateReason(subject: String): String? {
        if(subject.isEmpty()) return "Give a brief explanation"
        return null
    }
    private fun validateDuration(subject: String): String? {
        if(subject.isEmpty() || subject.length < 22) return "Please choose the dates again"
        return null
    }

    fun updateDateRange(range: Pair<Long, Long>?) {
        val from = Instant
            .fromEpochMilliseconds(
                range?.first ?: MaterialDatePicker.todayInUtcMilliseconds()
            ).toLocalDateTime(TimeZone.UTC).date.toString()

        val to = Instant
            .fromEpochMilliseconds(
                range?.second ?: (MaterialDatePicker.todayInUtcMilliseconds() + 86_400_000L)
            ).toLocalDateTime(TimeZone.UTC).date.toString()
        // (24 hours * 60 minutes * 60 seconds * 1000 milliseconds)

        _binding.duration.editText?.setText("$from - $to")
    }
    companion object {
        const val TAG = "NewRequestBottomSheet"
    }
}