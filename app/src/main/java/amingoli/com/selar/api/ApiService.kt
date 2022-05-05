package ir.trano.keeper.api

import ir.trano.keeper.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field("mobile") mobile: String): Call<ResponseData<Login>>

    @FormUrlEncoded
    @POST("auth/confirm")
    fun confirmCode(
        @Field("mobile") mobile: String,
        @Field("code") code: String
    ): Call<ResponseData<ConfirmCode>>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("family") family: String
    ): Call<ResponseData<Register>>

    @FormUrlEncoded
    @POST("keeper/search")
    fun search(@Query("page") page: Int,
               @Field("q") value: String,
               @Field("in") type: String): Call<ResponseData<SearchResults>>

    /** Order */
    @GET("keeper/orders")
    fun listKeeperOrder(@QueryMap map: Map<String, String>
    ): Call<ResponseData<Data>>

    @POST("keeper/orders")
    fun newOrder(@Body data: RequestBody): Call<ResponseData<Data>>

    @POST("keeper/orders/{id}")
    fun updateOrder(@Path("id") id: Int ,
                    @Body data: RequestBody
    ): Call<ResponseData<Status>>

    @GET("keeper/orders/{id}")
    fun view(@Path("id") id: Int ): Call<ResponseData<Orders>>

    @POST("orders/{id}/cancel")
    fun cancel(@Path("id") id: Int ): Call<ResponseData<Status>>

    @POST("keeper/orders/{id}/store-in-stock")
    fun storeInStock(@Path("id") id: Int ): Call<ResponseData<Status>>

    @GET("keeper/filters")
    fun filters(): Call<ResponseData<Data>>

    @GET("keeper/info")
    fun info(): Call<ResponseData<Splash>>

    /** Truck request*/
    @GET("keeper/truck-requests")
    fun truckList(@Query("page") page: Int): Call<ResponseData<Data>>

    @POST("keeper/truck-requests")
    fun newTruck(@QueryMap map: Map<String, String>): Call<ResponseData<Status>>

    @POST("keeper/truck-requests/{id}/cancel")
    fun cancelTruck(@Path("id") id: Int): Call<ResponseData<Status>>

    /** loaded trucks */
    @GET("keeper/loaded-trucks")
    fun loadedTrucksList(): Call<ResponseData<Data>>

    @POST("keeper/loaded-trucks/{id_driver}/assign")
    fun loadedTrucksAssign(@Path("id_driver") id: Int,
                           @QueryMap orders: Map<String,String>):
            Call<ResponseData<Data>>
}