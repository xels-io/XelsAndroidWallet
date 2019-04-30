package io.xels.xelsandroidapp.response_model

import com.google.gson.annotations.SerializedName

  data class HistoryApiResponseModel(
    @SerializedName("InnerMsg")
    val innerMsg: InnerMsg,
    @SerializedName("statusCode")
    val statusCode: Int, // 200
    @SerializedName("statusText")
    val statusText: String // OK
) {
    data class InnerMsg(
        @SerializedName("history")
        val history: List<History>
    ) {
        data class History(
            @SerializedName("accountHdPath")
            val accountHdPath: String, // m/44'/105'/0'
            @SerializedName("accountName")
            val accountName: String, // account 0
            @SerializedName("coinType")
            val coinType: Int, // 105
            @SerializedName("transactionsHistory")
            val transactionsHistory: List<TransactionsHistory>
        ) {
            data class TransactionsHistory(
                @SerializedName("amount")
                val amount: Long, // 60000000000
                @SerializedName("confirmedInBlock")
                val confirmedInBlock: Int, // 7784
                @SerializedName("fee")
                val fee: Int, // 10000
                @SerializedName("id")
                val id: String, // 3096bbe163b467ddf7af2ad75765921348e1de10e47a7a0cf0dd35e3d27156f6
                @SerializedName("payments")
                val payments: List<Payment>,
                @SerializedName("timestamp")
                val timestamp: String, // 1556604864
                @SerializedName("toAddress")
                val toAddress: String, // XK8E3CJu1wQu4B3Dd5HziNxGZUcgiviMdy
                @SerializedName("type")
                val type: String // received
            ) {
                data class Payment(
                    @SerializedName("amount")
                    val amount: Long, // 54300000000
                    @SerializedName("destinationAddress")
                    val destinationAddress: String // XG3AnKuKvdn4L7Tfum8fjCn15LjRKqWUtP
                )
            }
        }
    }
}