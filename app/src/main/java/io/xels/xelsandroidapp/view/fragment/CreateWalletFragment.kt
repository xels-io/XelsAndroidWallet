package io.xels.xelsandroidapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.interfaces.ToolBarControll
import io.xels.xelsandroidapp.ulits.Utils

class CreateWalletFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btn_create_wallet -> {
                if (name?.text.toString().isEmpty() && password?.text.toString().isEmpty() && confirmPassword?.text.toString().isEmpty()) {
                    Toast.makeText(activity, "Field missing", Toast.LENGTH_SHORT).show()
                } else {
                    if (password?.text.toString().equals(confirmPassword?.text.toString())) {

                        if (toolBarControll!!.internetCheck(activity)) {

                            b = Bundle()
                            b.putString("name", name?.text.toString())
                            b.putString("pass", password?.text.toString())
                            fragment = MnemonicsFragment()
                            fragment?.arguments = b

                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.frameLayout, fragment as MnemonicsFragment, "MnemonicsFragment")
                                ?.addToBackStack("MnemonicsFragment")?.commit()
                        }


                    } else {
                        Toast.makeText(activity, "Password mismatch", Toast.LENGTH_SHORT).show()

                    }

                }
            }

            R.id.linearLayout -> {
                Utils.hideKeyBoard(activity)
            }


        }


    }

    var name: EditText? = null
    var password: EditText? = null
    var confirmPassword: EditText? = null
    var createWallet: Button? = null
    var linearLayout: LinearLayout? = null
    lateinit var b: Bundle
    var toolBarControll: ToolBarControll? = null

    var fragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        name = view.findViewById(R.id.et_name)
        password = view.findViewById(R.id.et_password)
        confirmPassword = view.findViewById(R.id.confirmPassord)
        createWallet = view.findViewById(R.id.btn_create_wallet)
        linearLayout = view.findViewById(R.id.linearLayout)
        createWallet!!.setOnClickListener(this)
        linearLayout!!.setOnClickListener(this)


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