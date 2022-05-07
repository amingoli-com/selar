package amingoli.com.selar.activity.business_add.mvp

import amingoli.com.selar.api.ApiClient
import amingoli.com.selar.model.ResponseBusinessSample
import android.util.Log
import ir.trano.keeper.model.ResponseData
import retrofit2.Call
import retrofit2.Response

class FirstOpenModel(val view: FirstOpenView) : FirstOpenPresenter {
    override fun getBusinessSample() {
        ApiClient.getClient().getBusinessSample()
            .enqueue(object : retrofit2.Callback<ResponseData<ResponseBusinessSample>> {
                override fun onFailure(call: Call<ResponseData<ResponseBusinessSample>>, t: Throwable) {
                    Log.e("qqqq", "getBusinessSample onFailure" ,t)
                    view.stopResponse()
                    view.onError(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ResponseData<ResponseBusinessSample>>,
                    response: Response<ResponseData<ResponseBusinessSample>>) {
                    Log.e("qqqq", "getBusinessSample onResponse")
                    view.stopResponse()
                    if (response.isSuccessful) {
                        Log.e("qqqq", "getBusinessSample isSuccessful")
                        view.onResponse(response.body()?.data?.business)
                    } else {
                        Log.e("qqqq", "getBusinessSample notSuccessful")
                        view.onError("getData")
                    }
                }
            })
    }

    override fun getBusinessSample(url: String) {
        ApiClient.getClient().getBusinessSample(url)
            .enqueue(object : retrofit2.Callback<ResponseData<ResponseBusinessSample>> {
                override fun onFailure(call: Call<ResponseData<ResponseBusinessSample>>, t: Throwable) {
                    Log.e("qqqq", "getBusinessSample url onFailure" ,t)
                    view.stopResponse()
                    view.onError(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ResponseData<ResponseBusinessSample>>,
                    response: Response<ResponseData<ResponseBusinessSample>>) {
                    Log.e("qqqq", "getBusinessSample onResponse")
                    view.stopResponse()
                    if (response.isSuccessful) {
                        Log.e("qqqq", "getBusinessSample isSuccessful")
                        if (response.body()?.data?.sample_data != null){
                            view.onResponseSampleData(response.body()?.data?.sample_data!!)
                        }else{
                            view.onEmpty()
                        }
                    } else {
                        Log.e("qqqq", "getBusinessSample notSuccessful")
                        view.onError("getData")
                    }
                }
            })
    }
}