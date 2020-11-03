package com.one.fruitmanbuyer.ui.update_password

import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class UpdatePasswordViewModel (private val buyerRepository: BuyerRepository) : ViewModel(){
    private val state : SingleLiveEvent<UpdatePasswordState> = SingleLiveEvent()

    private fun isLoading(b : Boolean){ state.value = UpdatePasswordState.Loading(b) }
    private fun toast(m : String){ state.value = UpdatePasswordState.ShowToast(m) }
    private fun success(){ state.value = UpdatePasswordState.Success }

    fun updatePassword(token : String, password : String){
        isLoading(true)
        buyerRepository.updatePassword(token, password, object : SingleResponse<Buyer>{
            override fun onSuccess(data: Buyer?) {
                isLoading(false)
                println("d $data")
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                println("e $err")
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class UpdatePasswordState{
    data class Loading(var state : Boolean = false) : UpdatePasswordState()
    data class ShowToast(var message : String) : UpdatePasswordState()
    object Success : UpdatePasswordState()
}