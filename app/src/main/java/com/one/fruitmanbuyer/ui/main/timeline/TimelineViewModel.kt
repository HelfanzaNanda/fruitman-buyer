package com.one.fruitmanbuyer.ui.main.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.one.fruitmanbuyer.models.Fruit
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.models.SubDistrict
import com.one.fruitmanbuyer.repositories.FruitRepository
import com.one.fruitmanbuyer.repositories.ProductRepository
import com.one.fruitmanbuyer.repositories.SubDistrictRepository
import com.one.fruitmanbuyer.utils.ArrayResponse
import com.one.fruitmanbuyer.utils.SingleLiveEvent

class TimelineViewModel (private val productRepository: ProductRepository,
                         private val subDistrictRepository: SubDistrictRepository,
                         private val fruitRepository: FruitRepository) : ViewModel(){
    private val state : SingleLiveEvent<TimelineState> = SingleLiveEvent()
    private val products = MutableLiveData<List<Product>>()
    private val fruits = MutableLiveData<List<Fruit>>()
    private val subDistricts = MutableLiveData<List<SubDistrict>>()
    private var fruitId = MutableLiveData<String>()

    private fun isLoading(b : Boolean){ state.value = TimelineState.Loading(b) }
    private fun toast(message: String){ state.value = TimelineState.ShowToast(message) }

    fun fetchProducts(token : String){
        isLoading(true)
        productRepository.fetchProducts(token, object : ArrayResponse<Product>{
            override fun onSuccess(datas: List<Product>?) {
                isLoading(false)
                datas?.let { products.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun fetchFruitsByDistrict(token: String, sub_district_id : String) {
        isLoading(true)
        fruitRepository.fetchFruitsByDistrict(token, sub_district_id, object : ArrayResponse<Fruit>{
            override fun onSuccess(datas: List<Fruit>?) {
                isLoading(false)
                datas?.let { fruits.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun fetchSubdistricts(token: String){
        isLoading(true)
        subDistrictRepository.fetchSubdistricts(token, object : ArrayResponse<SubDistrict>{
            override fun onSuccess(datas: List<SubDistrict>?) {
                isLoading(false)
                datas?.let { subDistricts.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun searchProduct(token: String, name : String){
        isLoading(true)
        productRepository.searchProducts(token, name, object : ArrayResponse<Product>{
            override fun onSuccess(datas: List<Product>?) {
                isLoading(false)
                datas?.let {
                    products.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun fetchProductsByCriteria(token: String, sub_district_id: String, fruit_id : String){
        isLoading(true)
        productRepository.fetchProductsByCriteria(token, sub_district_id, fruit_id, object : ArrayResponse<Product>{
            override fun onSuccess(datas: List<Product>?) {
                isLoading(false)
                datas?.let { products.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun fetchProductsBySubDistrict(token: String, sub_district_id: String) {
        isLoading(true)
        productRepository.fetchProductsBySubDistrict(token, sub_district_id, object : ArrayResponse<Product> {
            override fun onSuccess(datas: List<Product>?) {
                isLoading(false)
                datas?.let { products.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun setIdSubDistrict(id : String) = fruitId.postValue(id)

    fun listenToState() = state
    fun listenToProducts() = products
    fun listenToSubDistricts() = subDistricts
    fun listenToFruits() = fruits
    fun getIdSubDistrict() = fruitId
}

sealed class TimelineState{
    data class Loading(var state : Boolean = false) : TimelineState()
    data class ShowToast(var message : String) : TimelineState()
}