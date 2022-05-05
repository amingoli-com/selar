package amingoli.com.selar.activity.first_open.mvp

import amingoli.com.selar.model.ResponseBusinessSample

interface FirstOpenView {
    fun onResponse(arrayListBusinessSample: ArrayList<ResponseBusinessSample.item>?)
    fun onError(message: String)
    fun stopResponse()
}