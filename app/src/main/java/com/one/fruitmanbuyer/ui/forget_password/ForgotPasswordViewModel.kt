package com.one.fruitmanbuyer.ui.forget_password

import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class ForgotPasswordViewModel (private val buyerRepository: BuyerRepository) : ViewModel(){
    private val state : SingleLiveEvent<ForgotPasswordState> = SingleLiveEvent()

    private fun setLoading(){ state.value = ForgotPasswordState.Loading(true) }
    private fun hideLoading(){ state.value = ForgotPasswordState.Loading(false) }
    private fun toast(message: String){ state.value = ForgotPasswordState.ShowToast(message) }
    private fun success(email: String) { state.value = ForgotPasswordState.Success(email) }
    private fun reset() { state.value = ForgotPasswordState.Reset }

    fun validate(email: String) : Boolean {
        reset()
        if (email.isEmpty()){
            state.value = ForgotPasswordState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = ForgotPasswordState.Validate(email = "email tidak valid")
            return false
        }
        return true
    }

    fun forgotPassword(email: String){
        setLoading()
        buyerRepository.forgotPassword(email, object : SingleResponse<Buyer>{
            override fun onSuccess(data: Buyer?) {
                hideLoading()
                data?.let { success(email) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class ForgotPasswordState{
    data class Loading(var state : Boolean = false) : ForgotPasswordState()
    data class ShowToast(var message : String) : ForgotPasswordState()
    object Reset : ForgotPasswordState()
    data class Success(var email: String) : ForgotPasswordState()
    data class Validate (var email : String?= null) : ForgotPasswordState()
}