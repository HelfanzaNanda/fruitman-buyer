package com.one.fruitmanbuyer.ui.premium


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanbuyer.models.Bank
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.repositories.BankRepository
import com.one.fruitmanbuyer.repositories.BuyerRepository
import com.one.fruitmanbuyer.utils.SingleLiveEvent
import com.one.fruitmanbuyer.utils.SingleResponse

class PremiumViewModel (private val bankRepository: BankRepository,
                        private val buyerRepository: BuyerRepository
) : ViewModel(){
    private val state : SingleLiveEvent<BankState> = SingleLiveEvent()
    private val bank = MutableLiveData<Bank>()

    private fun isLoading(b : Boolean){ state.value = BankState.Loading(b) }
    private fun toast(m : String){ state.value = BankState.ShowToast(m) }
    private fun success(){ state.value = BankState.Success }

    fun fetchBank(){
        isLoading(true)
        bankRepository.fetchBank(object : SingleResponse<Bank> {
            override fun onSuccess(data: Bank?) {
                data?.let {
                    isLoading(false)
                    bank.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun premium(token : String, image : String){
        isLoading(true)
        buyerRepository.premium(token, image, object : SingleResponse<Buyer>{
            override fun onSuccess(data: Buyer?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToBank() = bank
}

sealed class BankState{
    data class ShowToast(var message : String) : BankState()
    data class Loading(var state : Boolean = false) : BankState()
    object Success : BankState()
}