package one.njk.celestidesk.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.util.Pair
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.databinding.NewRequestSheetBinding
import one.njk.celestidesk.domain.NewBreakRequest

class NewRequestSheet(val datePicker: () -> Unit): BottomSheetDialogFragment() {

    private lateinit var _binding: NewRequestSheetBinding

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
                val newRequest = NewBreakRequest(
                    subject = subject.editText?.text.toString(),
                    message = reason.editText?.text.toString(),
                    emergency = isEmergency,
                    from = duration.editText?.text.toString().slice(0..9),
                    to = duration.editText?.text.toString().slice(13..22),
                )
                // Date value in edit field: 2023-08-17 - 2023-08-31
                mockSubmit(newRequest)
            }
        }
    }

    private fun mockSubmit(data: NewBreakRequest){
        Log.d("new", "$data")
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