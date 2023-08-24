package one.njk.celestidesk.ui

import android.content.Context
import android.os.Bundle
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

        _binding.normal.performClick()
        _binding.durationEdit.setOnFocusChangeListener { editText, hasFocus ->
            if(hasFocus){
                datePicker()
            }
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
        }
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