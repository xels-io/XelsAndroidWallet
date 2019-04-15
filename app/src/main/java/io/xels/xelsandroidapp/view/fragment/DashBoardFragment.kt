package io.xels.xelsandroidapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.interfaces.ToolBarControll
import io.xels.xelsandroidapp.response_model.GetBalanceApiResponse
import io.xels.xelsandroidapp.response_model.GetStakingInfoResponse
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.PreferenceManager
import io.xels.xelsandroidapp.ulits.Utils
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call
import retrofit2.Response
import java.math.BigDecimal
import java.text.DecimalFormat

class DashBoardFragment : Fragment() {


    var amountConfirmed: BigDecimal? = null
    var unAmountConfirmed: BigDecimal? = null
    var totalAmount: Long? =null
    var toolBarControll:ToolBarControll?=null
    lateinit var df:DecimalFormat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarControll?.setTitle("DashBoard")
        toolBarControll?.showDialog(true)
        toolBarControll?.showShareBtn(false)
        val apiInterface: ApiInterface? = ApiClient.getClient()?.create(ApiInterface::class.java)


        if(toolBarControll!!.internetCheck(activity)){
            callBalance(apiInterface)

        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_dashboard, container, false)
    }

    private fun callBalance(apiInterface: ApiInterface?) {
        apiInterface?.getBalance(
            AppConstance.getBalance,
            PreferenceManager.getString(AppConstance.walletName),
            AppConstance.ACCOUNT_NAME
        )?.enqueue(object :
            retrofit2.Callback<GetBalanceApiResponse?> {
            override fun onFailure(call: Call<GetBalanceApiResponse?>, t: Throwable) {
                println(t.printStackTrace())
                Utils.showError(activity)
                toolBarControll?.showDialog(false)


            }

            override fun onResponse(call: Call<GetBalanceApiResponse?>, response: Response<GetBalanceApiResponse?>) {

                if (response.isSuccessful) {
                    amountConfirmed =  BigDecimal.valueOf(response.body()?.innerMsg?.balances?.get(0)!!.amountConfirmed?.div(AppConstance.shatoshi))
                    unAmountConfirmed = BigDecimal.valueOf(response.body()!!.innerMsg.balances.get(0).amountUnconfirmed.div(AppConstance.shatoshi))


                    Log.e("amount",""+ "%.8f".format(amountConfirmed))

                    confirmedBalanceTxtView.text = amountConfirmed.toString() + " XELS"
                    unConfirmedBalanceTxtView.text = unAmountConfirmed.toString() + " (unconfirmed)"

                    totalAmount=response.body()?.innerMsg?.balances?.get(0)!!.amountConfirmed?.plus(response.body()!!.innerMsg.balances.get(0).amountUnconfirmed)


                    PreferenceManager.updateValue(AppConstance.balance, totalAmount)



                    apiInterface.getStackingInfo(AppConstance.getStackingInfo)
                        .enqueue(object : retrofit2.Callback<GetStakingInfoResponse?> {
                            override fun onFailure(call: Call<GetStakingInfoResponse?>, t: Throwable) {
                                println(t.printStackTrace())
                                Toast.makeText(activity,"Something went wrong. please try later",Toast.LENGTH_SHORT).show()
                                toolBarControll?.showDialog(false)

                            }

                            override fun onResponse(
                                call: Call<GetStakingInfoResponse?>,
                                response: Response<GetStakingInfoResponse?>
                            ) {

                                if (response.isSuccessful) {
                                    weightTxtView.text = "Your weight is " + response.body()?.innerMsg?.weight?.toString() +
                                            " XELS"
                                    networkWeightTxtView.text = "Network weight is " +
                                            response.body()?.innerMsg?.netStakeWeight?.toString() + " Xels"
                                    expectedRewardTxtView.text = response.body()?.innerMsg?.expectedTime?.toString()
                                    toolBarControll?.showDialog(false)

                                }
                                else{
                                    toolBarControll?.showDialog(false)
                                    Utils.handleErrorResponse(response,activity,response.code())

                                }
                            }
                        })


                }
                else{
                    toolBarControll?.showDialog(false)
                    Utils.handleErrorResponse(response,activity,response.code())

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
