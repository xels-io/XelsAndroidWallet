package io.xels.xelsandroidapp.response_model

import com.google.gson.annotations.SerializedName

data class ErrorApiResponse(
    @SerializedName("InnerMsg")
    val innerMsg: List<InnerMsg>,
    @SerializedName("statusCode")
    val statusCode: Int, // 403
    @SerializedName("statusText")
    val statusText: String // Forbidden
) {
    data class InnerMsg(
        @SerializedName("description")
        val description: String,
        @SerializedName("message")
        val message: String, // Can't send transaction: sending transaction requires at least one connection!
        @SerializedName("status")
        val status: Int // 403
    )
}