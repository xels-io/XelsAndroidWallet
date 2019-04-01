package io.xels.xelsandroidapp.response_model

import com.google.gson.annotations.SerializedName

data class AllAddressApiResponse(
    @SerializedName("InnerMessage")
    val innerMessage: InnerMessage,
    @SerializedName("StatusCode")
    val statusCode: Int, // 200
    @SerializedName("StatusText")
    val statusText: String
) {
    data class InnerMessage(
        @SerializedName("addresses")
        val addresses: List<Addresse>
    ) {
        data class Addresse(
            @SerializedName("address")
            val address: String, // XT8RbyyHBy4GbPYavj8zGBm69Nry19w3vp
            @SerializedName("isChange")
            val isChange: Boolean, // true
            @SerializedName("isUsed")
            val isUsed: Boolean // false
        )
    }
}