package io.xels.xelsandroidapp.response_model

import com.google.gson.annotations.SerializedName

data class HistoryApiResponseModel(
    @SerializedName("statusCode")
    var statusCode: Int, // 200
    @SerializedName("statusText")
    var statusText: String, // OK
    @SerializedName("InnerMsg")
    var innerMsg: InnerMsg
) {
    data class InnerMsg(
        @SerializedName("history")
        var history: List<History>
    ) {
        data class History(
            @SerializedName("accountName")
            var accountName: String, // account 0
            @SerializedName("accountHdPath")
            var accountHdPath: String, // m/44'/105'/0'
            @SerializedName("coinType")
            var coinType: Int, // 105
            @SerializedName("transactionsHistory")
            var transactionsHistory: List<TransactionsHistory>
        ) {
            data class TransactionsHistory(
                @SerializedName("type")
                var type: String, // staked
                @SerializedName("toAddress")
                var toAddress: String, // XL2nwvVA9xNCyzPkEuSdeDcCCruDsXLBXp
                @SerializedName("id")
                var id: String, // 13f56513eea6e3df603ce6f41e623091a198cd6983736857a2ff9d98f75cf44c
                @SerializedName("amount")
                var amount: Long, // 37500000000
                @SerializedName("payments")
                var payments: List<Any>,
                @SerializedName("confirmedInBlock")
                var confirmedInBlock: Int, // 2290
                @SerializedName("timestamp")
                var timestamp: String, // 1547414272
                @SerializedName("fee")
                var fee: Int // 10000
            )
        }
    }
}