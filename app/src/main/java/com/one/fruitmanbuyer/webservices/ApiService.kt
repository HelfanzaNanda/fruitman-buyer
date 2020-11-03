package com.one.fruitmanbuyer.webservices

import com.google.gson.annotations.SerializedName
import com.one.fruitmanbuyer.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("fcm_token") fcmToken : String
    ) : Call<WrappedResponse<Buyer>>

    @Headers("Content-Type: application/json")
    @POST("api/user/register")
    fun register(
        @Body body: RequestBody
    ) : Call<WrappedResponse<RegisterBuyer>>

    @GET("api/user/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<Buyer>>

    @Headers("Content-Type: application/json")
    @POST("api/user/profile/update")
    fun updateProfile(
        @Header("Authorization") token : String,
        @Body body: RequestBody
    ) : Call<WrappedResponse<Buyer>>

    @Multipart
    @POST("api/user/profile/update/photo")
    fun updatePhotoProfile(
        @Header("Authorization") token : String,
        @Part image : MultipartBody.Part
    ) :Call<WrappedResponse<Buyer>>

    @FormUrlEncoded
    @POST("api/user/profile/update/password")
    fun updatePassword(
        @Header("Authorization") token : String,
        @Field("password") pass : String
    ) :Call<WrappedResponse<Buyer>>

    @GET("api/user/subdistrict")
    fun fetchSubDistricts(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<SubDistrict>>

    @GET("api/user/fruit/{sub_district_id}")
    fun fetchFruitsByDistrict(
        @Header("Authorization") token : String,
        @Path("sub_district_id") sub_district_id : Int
    ) : Call<WrappedListResponse<Fruit>>

    @FormUrlEncoded
    @POST("api/user/product/search")
    fun fetchProductsByCriteria(
        @Header("Authorization") token : String,
        @Field("sub_district_id") sub_district_id : Int,
        @Field("fruit_id") fruit_id : Int
    ) : Call<WrappedListResponse<Product>>

    @GET("api/user/product/{sub_district_id}")
    fun fetchProductsBySubDistrct(
        @Header("Authorization") token : String,
        @Path("sub_district_id") sub_district_id : Int
    ) : Call<WrappedListResponse<Product>>

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

    @FormUrlEncoded
    @POST("api/user/password/email")
    fun forgotPassword(
        @Field("email") email : String
    ) :Call<WrappedResponse<Buyer>>

    @GET("api/user/order/waiting")
    fun orderOrderIn(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/user/order/inprogress")
    fun orderInProgress(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/user/order/complete")
    fun orderComplete(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/user/order/{id}/decline")
    fun orderCancel(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @GET("api/user/order/{id}/arrived")
    fun orderArrived(
        @Header("Authorization") token : String,
        @Path("id") id : Int
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