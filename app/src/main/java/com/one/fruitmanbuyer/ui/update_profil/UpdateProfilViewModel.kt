package com.one.fruitmanbuyer.ui.update_profil

import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class UpdateProfilViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private val state: SingleLiveEvent<UpdateProfilState> = SingleLiveEvent()
    private fun setLoading() { state.value = UpdateProfilState.Loading(true) }
    private fun hideLoading() { state.value = UpdateProfilState.Loading(false) }
    private fun toast(message: String) { state.value = UpdateProfilState.ShowToast(message) }
    private fun success() { state.value = UpdateProfilState.Success }


    fun validate(name : String, password : String, address : String, phone : String) : Boolean{
        if (name.isEmpty()){
            state.value = UpdateProfilState.Validate(name = "tidak boleh ksoosng")
            return false
        }

        if (!Constants.isAlpha(name)){
            state.value = UpdateProfilState.Validate(name = "tidak boleh ada angka")
            return false
        }

        if (password.isEmpty()){
            state.value = UpdateProfilState.Validate(pass = "tidak boleh ksoosng")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = UpdateProfilState.Validate(pass = "minimal 8 karakter")
            return false
        }

        if (address.isEmpty()){
            state.value = UpdateProfilState.Validate(address = "tidak boleh ksoosng")
            return false
        }

        if (phone.isEmpty()){
            state.value = UpdateProfilState.Validate(phone = "tidak boleh ksoosng")
            return false
        }

        return true
    }

    fun updateProfile(token: String, buyer : Buyer, pathImage: String) {
        setLoading()
        buyerRepository.updateProfile(token, buyer, object : SingleResponse<Buyer>{
            override fun onSuccess(data: Buyer?) {
                hideLoading()
                data?.let {
                    if (pathImage.isNotEmpty()) {
                        updatePhotoProfile(token, pathImage)
                    } else {
                        success()
                    }
                }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    private fun updatePhotoProfile(token: String, pathImage: String) {
        buyerRepository.updatePhotoProfile(token, pathImage, object : SingleResponse<Buyer> {
            override fun onSuccess(data: Buyer?) {
                hideLoading()
                success()
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class UpdateProfilState {
    data class Loading(var state: Boolean = false) : UpdateProfilState()
    data class ShowToast(var message: String) : UpdateProfilState()
    object Success : UpdateProfilState()
    data class Validate(
        var name : String? = null,
        var pass : String? = null,
        var address : String? = null,
        var phone : String? = null
    ) : UpdateProfilState()
    object Reset : UpdateProfilState()
}