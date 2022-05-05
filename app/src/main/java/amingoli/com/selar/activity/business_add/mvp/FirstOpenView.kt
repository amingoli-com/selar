package amingoli.com.selar.activity.business_add.mvp

import amingoli.com.selar.model.ResponseBusinessSample

interface FirstOpenView {
    fun onResponse(arrayListBusinessSample: ArrayList<ResponseBusinessSample.item>?)
    fun onResponseSampleData(sampleData: ResponseBusinessSample.SampleData)
    fun onError(message: String)
    fun onEmpty()
    fun stopResponse()
}