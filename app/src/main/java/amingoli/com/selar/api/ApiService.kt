package amingoli.com.selar.api

import amingoli.com.selar.model.ResponseBusinessSample
import ir.trano.keeper.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("business_sample.json")
    fun getBusinessSample(): Call<ResponseData<ResponseBusinessSample>>

    @GET("{url}")
    fun getBusinessSample(@Path("url") url: String ): Call<ResponseData<ResponseBusinessSample>>
}