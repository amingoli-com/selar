package ir.trano.keeper.model

import com.google.gson.annotations.SerializedName

class ResponseData<E> {
    @SerializedName("data")
    val data: E? = null
}