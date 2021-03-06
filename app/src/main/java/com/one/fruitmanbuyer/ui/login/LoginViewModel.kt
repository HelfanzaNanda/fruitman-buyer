package com.one.fruitmanbuyer.ui.login

import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.repositories.FirebaseRepository
import com.one.fruitmanbuyer.utils.Constants
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class LoginViewModel (private val buyerRepository: BuyerRepository,
                      private val firebaseRepository: FirebaseRepository) : ViewModel(){
    private val state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun setLoading() { state.value = LoginState.Loading(true) }
    private fun hideLoading() { state.value = LoginState.Loading(false) }
    private fun toast(message: String){ state.value = LoginState.ShowToast(message) }
    private fun success(token: String, premium: Boolean, overload: Boolean){ state.value = LoginState.Success(token, premium, overload) }

    fun validate(email: String, password : String) : Boolean{
        state.value = LoginState.Reset
        if (email.isEmpty()){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "Email tidak valid")
            return false
        }
        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }
        return true
    }

    fun login(email: String, password: String){
        setLoading()
        generateTokenFirebase(email, password)
    }

    private fun generateTokenFirebase(email : String, password: String) {
        firebaseRepository.generateToken(object : SingleResponse<String> {
            override fun onSuccess(data: String?) {
                data?.let { fcmToken ->
                    buyerRepository.login(email, password, fcmToken, object : SingleResponse<Buyer> {
                        override fun onSuccess(data: Buyer?) {
                            hideLoading()
                            data?.let { success(it.token!!, it.premium, it.overload) }
                        }

                        override fun onFailure(err: Error) {
                            hideLoading()
                            toast(err.message.toString())
                        }
                    })
                }
            }

            override fun onFailure(err: Error) {
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class LoginState{
    data class Loading(var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class Success(var token : String, var premium : Boolean = false, var overload : Boolean = false) : LoginState()
    data class Validate(var email : String? = null, var password : String? = null) : LoginState()
    object Reset : LoginState()
}