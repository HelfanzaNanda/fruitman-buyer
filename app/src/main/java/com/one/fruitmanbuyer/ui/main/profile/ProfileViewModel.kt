package com.one.fruitmanbuyer.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class ProfileViewModel (private val buyerRepository: BuyerRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProfileState> = SingleLiveEvent()
    private val user  = MutableLiveData<Buyer>()

    private fun setLoading() { state.value = ProfileState.Loading(true) }
    private fun hideLoading() { state.value = ProfileState.Loading(false) }
    private fun toast(message: String){ state.value = ProfileState.ShowToast(message) }

    fun profile(token : String){
        setLoading()
        buyerRepository.profile(token, object : SingleResponse<Buyer> {
            override fun onSuccess(data: Buyer?) {
                hideLoading()
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToUser() = user

}

sealed class ProfileState{
    data class Loading(var state : Boolean = false) : ProfileState()
    data class ShowToast(var message : String) : ProfileState()
}