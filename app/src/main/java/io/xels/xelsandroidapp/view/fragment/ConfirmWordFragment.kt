package io.xels.xelsandroidapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.interfaces.ToolBarControll
import io.xels.xelsandroidapp.response_model.WordResponseModel
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmWordFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_confirm -> {
                if (te_word4?.text.toString().trim().isEmpty() && te_word8?.text.toString().trim().isEmpty() && te_word12?.text.toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Field missing", Toast.LENGTH_SHORT).show()
                } else {

                    if (te_word4?.text.toString().trim().equals(word4) && te_word8?.text.toString().trim().equals(word8) && te_word12?.text.toString().trim().equals(
                            word12
                        )
                    ) {

                        if (toolBarControll!!.internetCheck(activity)){
                            createWallet(mnemonics)
                        }

                    }

                else {
                        Toast.makeText(activity, "Words mismatch", Toast.LENGTH_SHORT).show()

                    }

                }
            }
        }

    }

    var apiInterface: ApiInterface? = null
    var name: String? = null

    var word4: String? = null
    var word8: String? = null
    var word12: String? = null
    var mnemonics: String? = null

    var password: String? = null
    var passPhase: String? = null
    var mnemonic: String? = null
    var toolBarControll: ToolBarControll? = null
    var createBtn: Button? = null
    var te_word4: EditText? = null
    var te_word8: EditText? = null
    var te_word12: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        word4 = arguments?.getString("word4")
        word8 = arguments?.getString("word8")
        word12 = arguments?.getString("word12")
        word12 = arguments?.getString("word12")
        name = arguments?.getString("name")
        password = arguments?.getString("password")
        mnemonics = arguments?.getString("mnemonics")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createBtn = view.findViewById(R.id.btn_confirm) as Button
        te_word4 = view.findViewById(R.id.et_word_no_4) as EditText
        te_word8 = view.findViewById(R.id.et_word_no_8) as EditText
        te_word12 = view.findViewById(R.id.et_word_no_12) as EditText
        createBtn?.setOnClickListener(this)

    }


    private fun createWallet(mnemonic: String?) {

        toolBarControll?.showDialog(true)

        val apiInterface: ApiInterface? = ApiClient.getClient()?.create(ApiInterface::class.java)

        apiInterface?.createWallet(AppConstance.createWallet, null,  mnemonic,name, password, password)
            ?.enqueue(
                object : Callback<WordResponseModel> {
                    override fun onFailure(call: Call<WordResponseModel>, t: Throwable) {
                        println(t?.localizedMessage)
                        toolBarControll?.showDialog(false)
                        Utils.showError(activity)
                    }
                    override fun onResponse(
                        call: Call<WordResponseModel>,
                        response: Response<WordResponseModel>
                    ) {


                        if (response.isSuccessful) {
                            toolBarControll?.showDialog(false)
                            responsefFun(response)


                        } else {
                            toolBarControll?.showDialog(false)
                            println("try later")
                            Utils.showError(activity)
                        }

                    }


                })




    }

    private fun responsefFun(response: Response<WordResponseModel>) {
        if (response.isSuccessful) {
            var fragment = CreateWalletComplete()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frameLayout, fragment, "CreateWalletComplete")
                ?.addToBackStack("CreateWalletComplete")?.commit()

            toolBarControll?.showDialog(false)


        } else {
            Toast.makeText(activity, "Something went wrong, please try later", Toast.LENGTH_SHORT)
                .show()
            toolBarControll?.showDialog(false)

        }
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