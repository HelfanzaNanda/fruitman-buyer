package com.one.fruitmanbuyer.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import com.one.fruitmanbuyer.R
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.models.Preference
import com.one.fruitmanbuyer.ui.login.LoginActivity
import com.one.fruitmanbuyer.ui.update_password.UpdatePasswordActivity
import com.one.fruitmanbuyer.ui.update_profil.UpdateProfilActivity
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.extensions.gone
import com.one.fruitmanbuyer.utils.extensions.showToast
import com.one.fruitmanbuyer.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.profile_email
import kotlinx.android.synthetic.main.fragment_profile.profile_image
import kotlinx.android.synthetic.main.fragment_profile.profile_name
import kotlinx.android.synthetic.main.fragment_profile.profile_telp
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val profileViewModel: ProfileViewModel by viewModel()
    private var prefs = mutableListOf<Preference>().apply {
        add(Preference(1, R.string.update_profile, R.drawable.ic_person_black_24dp))
        add(Preference(2, R.string.update_password, R.drawable.ic_key))
        add(Preference(3, R.string.logout, R.drawable.ic_navigate_next))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        observe()
    }

    private fun observer() = profileViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })

    private fun handleUiState(it: ProfileState) {
        when (it) {
            is ProfileState.Loading -> handleLoading(it.state)
            is ProfileState.ShowToast -> requireActivity().showToast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) = if (state) loading.visible() else loading.gone()

    private fun observe() = profileViewModel.listenToUser().observe(requireActivity(), Observer { handleUser(it) })

    private fun handleUser(buyer: Buyer) {
        with(requireView()){
            buyer.image?.let { imageUrl -> profile_image.load(imageUrl){ transformations(CircleCropTransformation()) } } ?: profile_image.load(R.drawable.ic_person){ transformations(
                CircleCropTransformation()
            ) }
            profile_name.text = buyer.name
            profile_email.text = buyer.email
            profile_telp.text = buyer.phone

            requireView().rv_pref.apply {
                layoutManager = LinearLayoutManager(activity).apply { orientation = LinearLayoutManager.HORIZONTAL }
                adapter = ProfileAdapter(requireActivity(), prefs)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        profileViewModel.profile(Constants.getToken(requireActivity()))
    }
}