package ir.trano.keeper.api

import android.util.Log
import com.google.gson.GsonBuilder
import ir.trano.keeper.helper.Session
import ir.trano.user.helper.Config.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        fun getClient(): ApiService {
            val client = OkHttpClient.Builder()
            client.readTimeout(20, TimeUnit.SECONDS)
            client.addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                builder.addHeader("Authorization", "Bearer "+ Session.getInstance().token)
                builder.addHeader("Accept", "application/json")
                Log.e("session api", "" + Session.getInstance().token)
                builder.method(original.method(), original.body())
                val request = builder.build()
                chain.proceed(request)
            }
            val retrofit = Retrofit.Builder()
                .baseUrl("$BASE_URL/api/user/v1/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(client.build())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}