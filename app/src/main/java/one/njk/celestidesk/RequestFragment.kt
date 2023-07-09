package one.njk.celestidesk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

        lifecycleScope.launch {
            _binding!!.buttonFirst.text = chooseLogic().name
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}