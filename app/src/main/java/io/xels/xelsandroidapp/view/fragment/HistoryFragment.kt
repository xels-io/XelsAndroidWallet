package io.xels.xelsandroidapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.adapter.HistoryRvAdapter
import io.xels.xelsandroidapp.interfaces.ToolBarControll
import io.xels.xelsandroidapp.response_model.HistoryApiResponseModel
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.PreferenceManager
import io.xels.xelsandroidapp.ulits.Utils
import kotlinx.android.synthetic.main.fragment_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {


    var historyAdapter: HistoryRvAdapter? = null
    val historyList: HistoryRvAdapter? = null

    var toolBarControll: ToolBarControll? = null


    var apiInterface: ApiInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarControll?.showDialog(true)
        toolBarControll?.showShareBtn(false)
        toolBarControll?.setTitle("History")

        showHistory()


    }

    private fun showHistory() {

        toolBarControll?.showDialog(true)


        apiInterface = ApiClient.getClient()?.create(ApiInterface::class.java)


        apiInterface?.getHistory(
            AppConstance.getHistory,
            PreferenceManager.getString(AppConstance.walletName),
            AppConstance.ACCOUNT_NAME
        )?.enqueue(object :
            Callback<HistoryApiResponseModel?> {
            override fun onFailure(call: Call<HistoryApiResponseModel?>, t: Throwable) {
                println(t.printStackTrace())
                Utils.showError(activity)
                toolBarControll?.showDialog(false)

            }

            override fun onResponse(
                call: Call<HistoryApiResponseModel?>,
                response: Response<HistoryApiResponseModel?>
            ) {


                if (response.isSuccessful) {

                    if (response.body()?.innerMsg?.history?.get(0)?.transactionsHistory?.size != 0) {
                        historyRv.visibility = View.VISIBLE
                        noData.visibility = View.GONE

                        historyAdapter = HistoryRvAdapter(response.body())
                        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        historyRv.setLayoutManager(mLayoutManager)
                        historyRv.setItemAnimator(DefaultItemAnimator())
                        historyRv.setAdapter(historyAdapter)
                        historyRv.setHasFixedSize(true)
                    } else {
                        historyRv.visibility = View.GONE
                        noData.visibility = View.VISIBLE

                    }





                    toolBarControll?.showDialog(false)

                } else {
                    toolBarControll?.showDialog(false)

                }


            }

        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ToolBarControll) {
            toolBarControll = context as ToolBarControll?
        } else {
            throw IllegalArgumentException("Containing activity must implement OnSearchListener interface")
        }
    }


}