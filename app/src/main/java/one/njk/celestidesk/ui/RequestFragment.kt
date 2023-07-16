package one.njk.celestidesk.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import one.njk.celestidesk.adapters.RequestListAdapter
import one.njk.celestidesk.data.BreakRequest
import one.njk.celestidesk.data.Role
import one.njk.celestidesk.data.RolesDataStore
import one.njk.celestidesk.databinding.FragmentRequestBinding
import one.njk.celestidesk.viewmodels.EmployeeViewModel
import one.njk.celestidesk.viewmodels.ManagerViewModel
import one.njk.celestidesk.viewmodels.RoleAgreement
import one.njk.celestidesk.viewmodels.TeamLeadViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

val data = listOf(
    BreakRequest("1", "1"),
    BreakRequest("2", "2"),
    BreakRequest("3", "3"),
    BreakRequest("4", "4"),
    BreakRequest("5", "5"),
    BreakRequest("6", "6"),
)

@AndroidEntryPoint
class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    @Inject
    lateinit var rolesDataStore: RolesDataStore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            _binding!!.role.text = chooseLogic().name
        }
        addFilterChips(binding.filter, listOf("Accepted", "Rejected", "Processing"), lifecycleScope)
        val adapter = RequestListAdapter()
        binding.requestList.adapter = adapter
        adapter.submitList(data)
        // TODO: Get from repository
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun chooseLogic(): RoleAgreement {
        return when(rolesDataStore.getRole()){
            Role.EMPLOYEE -> {
                val employeeViewModel: EmployeeViewModel by activityViewModels()
                employeeViewModel
            }
            Role.TEAM_LEAD -> {
                val teamLeadViewModel: TeamLeadViewModel by activityViewModels()
                teamLeadViewModel
            }
            Role.MANAGER -> {
                val managerLeadViewModel: ManagerViewModel by activityViewModels()
                managerLeadViewModel
            }
        }
    }

    private fun addFilterChips(
        chipGroup: ChipGroup,
        categories: List<String>,
        lifeCycleScope: CoroutineScope,
    ){
        // Define ChipGroup Behaviours
        chipGroup.apply {
            removeAllViews()
            isSelectionRequired = true
            isSingleSelection = true
        }

        // Generate Chip based on chosen Category (SFW/NSFW)
        for(category in categories){
            val chip = Chip(this.context)
            chip.apply {
                text = category
                isCheckable = true
                setOnClickListener {
                    // TODO: Filter list of requests using viewmodel
                }
            }

            chipGroup.addView(chip)
            lifeCycleScope.launch {
                // Check 1st Chip by default
                chipGroup.getChildAt(0).performClick()
            }
        }
    }
}