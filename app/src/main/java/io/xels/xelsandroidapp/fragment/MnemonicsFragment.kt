package io.xels.xelsandroidapp.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.adapter.WordGridViewAdapter
import io.xels.xelsandroidapp.interfaces.ToolBarControll
import io.xels.xelsandroidapp.response_model.WordResponseModel
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.Utils
import kotlinx.android.synthetic.main.fragment_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MnemonicsFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_create_wallet -> {
                b = Bundle()
                b.putString("word4", separate1?.get(3).toString())
                b.putString("word8", separate1?.get(7).toString())
                b.putString("word12", separate1?.get(11).toString())
                b.putString("name",name)
                b.putString("password", password)
                b.putString("mnemonics", mnemonics)
                fragment = ConfirmWordFragment()
                fragment?.arguments = b

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frameLayout, fragment as ConfirmWordFragment, "ConfirmWordFragment")
                    ?.addToBackStack("ConfirmWordFragment")?.commit()


            }
        }
    }


    var name: String? = null
    var password: String? = null
    var separate1: List<String>? = null
    var mnemonics: String? = null
    var gridview: GridView? = null
    var btn_create_wallet: Button? = null

    var toolBarControll: ToolBarControll? = null
    var apiInterface: ApiInterface? = null
    var TAG: String = "MnemonicsFragment"
    var adapter: WordGridViewAdapter? = null
    var fragment: Fragment? = null
    lateinit var b: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = arguments?.getString("name")
        password = arguments?.getString("pass")
        toolBarControll?.showDialog(true)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mnemonics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridview = view.findViewById(R.id.gridview)
        btn_create_wallet = view.findViewById(R.id.btn_create_wallet)
        btn_create_wallet?.setOnClickListener(this)
        showHistory()


    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ToolBarControll) {
            toolBarControll = context as ToolBarControll?
        } else {
            throw IllegalArgumentException("Containing activity must implement OnSearchListener interface")
        }
    }


    private fun showHistory() {

        apiInterface = ApiClient.getClient()?.create(ApiInterface::class.java)


        apiInterface?.getMnemonics(
            AppConstance.mnemonicsUrl,
            "English",
            12
        )?.enqueue(object :
            Callback<WordResponseModel?> {
            override fun onFailure(call: Call<WordResponseModel?>, t: Throwable) {
                println(t.printStackTrace())
                Utils.showError(activity)
                toolBarControll?.showDialog(false)

            }

            override fun onResponse(
                call: Call<WordResponseModel?>,
                response: Response<WordResponseModel?>
            ) {


                if (response.isSuccessful) {

                    separate1 = response.body()?.innerMsg?.split(" ")
                    mnemonics = response.body()?.innerMsg
                    Log.d(TAG, "word: " + separate1!![2])
                    adapter = WordGridViewAdapter(separate1, activity)
                    gridview?.setAdapter(adapter)
                    toolBarControll?.showDialog(false)
                } else {

                }

                toolBarControll?.showDialog(false)

            }

        })
    }


}