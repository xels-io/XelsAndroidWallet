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
import io.xels.xelsandroidapp.response_model.AllAddressApiResponse
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

class ShowAllAddressFragment : Fragment() {
    var apiInterface: ApiInterface? = null
    var toolBarControll: ToolBarControll? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolBarControll?.setTitle("History")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun showHistory() {

        toolBarControll?.showDialog(true)


        apiInterface = ApiClient.getClient()?.create(ApiInterface::class.java)


        apiInterface?.getAllAddress(
            AppConstance.getAddress,
            PreferenceManager.getString(AppConstance.walletName),
            AppConstance.ACCOUNT_NAME
        )?.enqueue(object :
            Callback<AllAddressApiResponse?> {
            override fun onFailure(call: Call<AllAddressApiResponse?>, t: Throwable) {
                println(t.printStackTrace())
                Utils.showError(activity)
                toolBarControll?.showDialog(false)

            }

            override fun onResponse(
                call: Call<AllAddressApiResponse?>,
                response: Response<AllAddressApiResponse?>
            ) {


                if (response.isSuccessful) {

                    if (response.body()?.innerMessage?.addresses?.size != 0) {
                        historyRv.visibility = View.VISIBLE
                        noData.visibility = View.GONE

                        /*           historyAdapter = HistoryRvAdapter(response.body())
                                   val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                                   historyRv.setLayoutManager(mLayoutManager)
                                   historyRv.setItemAnimator(DefaultItemAnimator())
                                   historyRv.setAdapter(historyAdapter)
                                   historyRv.setHasFixedSize(true)*/
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