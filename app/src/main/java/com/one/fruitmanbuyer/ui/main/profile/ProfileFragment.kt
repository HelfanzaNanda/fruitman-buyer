package com.one.fruitmanbuyer.ui.main.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.models.Preference
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val profileViewModel: ProfileViewModel by viewModel()

    private var prefs = mutableListOf<Preference>().apply {
        add(Preference(1, R.string.update_profile, R.drawable.ic_person_black_24dp))
        add(Preference(2, R.string.update_password, R.drawable.ic_key))
        add(Preference(3, R.string.premium, R.drawable.ic_premium))
        add(Preference(4, R.string.logout, R.drawable.ic_navigate_next))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe(){
        observeState()
        observeCurrentUser()
    }

    private fun observeState() = profileViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })
    private fun observeCurrentUser() = profileViewModel.listenToUser().observe(requireActivity(), Observer { handleUser(it) })

    private fun handleUiState(it: ProfileState) {
        when (it) {
            is ProfileState.Loading -> handleLoading(it.state)
            is ProfileState.ShowToast -> requireActivity().showToast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) = if (state) requireView().loading.visible() else requireView().loading.gone()

    private fun handleUser(buyer: Buyer) {
        with(requireView()){
            buyer.image?.let { imageUrl -> profile_image.load(imageUrl){ transformations(CircleCropTransformation()) } } ?: profile_image.load(R.drawable.ic_person){ transformations(
                CircleCropTransformation()
            ) }
            profile_name.text = buyer.name
            profile_email.text = buyer.email
            profile_telp.text = buyer.phone

            requireView().rv_pref.apply {
                layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                adapter = ProfileAdapter(requireActivity(), prefs)
            }
        }

    }

    private fun fetchProfile() = profileViewModel.profile(Constants.getToken(requireActivity()))

    override fun onResume() {
        super.onResume()
        fetchProfile()
    }
}