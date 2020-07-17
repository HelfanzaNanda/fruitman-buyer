package com.one.fruitmanbuyer.webservices

import com.google.gson.annotations.SerializedName
import com.one.fruitmanbuyer.models.Buyer
import com.one.fruitmanbuyer.models.Order
import com.one.fruitmanbuyer.models.Product
import com.one.fruitmanbuyer.models.RegisterBuyer
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<Buyer>>

    @Headers("Content-Type: application/json")
    @POST("api/user/register")
    fun register(
        @Body body: RequestBody
    ) : Call<WrappedResponse<RegisterBuyer>>


    @GET("api/user/product")
    fun fetchProducts(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Product>>

    @GET("api/user/product/{name}/search")
    fun searchProducts(
        @Header("Authorization") token : String,
        @Path("name") name : String
    ) : Call<WrappedListResponse<Product>>

    @FormUrlEncoded
    @POST("api/user/order/store")
    fun createOrder(
        @Header("Authorization") token : String,
        @Field("seller_id") sellerId : Int,
        @Field("product_id") productId : Int,
        @Field("offer_price") offerPrice : Int
    ) : Call<WrappedResponse<Order>>
}
data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)