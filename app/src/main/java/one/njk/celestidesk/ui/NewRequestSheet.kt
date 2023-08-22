package one.njk.celestidesk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import one.njk.celestidesk.databinding.NewRequestSheetBinding

class NewRequestSheet: BottomSheetDialogFragment() {

    private var _binding: NewRequestSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewRequestSheetBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    companion object {
        const val TAG = "NewRequestBottomSheet"
    }
}