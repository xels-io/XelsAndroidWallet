package io.xels.xelsandroidapp.view.activity

import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.response_model.LoadApiResponseModel
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.Utils
import retrofit2.Call
import retrofit2.Response
import java.util.*

class RestoreWalletActivity : FragmentActivity(), View.OnClickListener {
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.restoreBtn -> {

                if (date?.text.toString().isEmpty() ||
                    word?.text.toString().isEmpty() ||
                    name?.text.toString().isEmpty() ||
                    password?.text.toString().isEmpty()
                ) {

                    Toast.makeText(this@RestoreWalletActivity, "Field missing", Toast.LENGTH_SHORT).show()

                } else {

                    if (Utils.isNetworkAvailable(this@RestoreWalletActivity, typeNetwork)) {
                        restoreWallet(apiInterface)
                    } else {
                        Toast.makeText(this@RestoreWalletActivity, "Internet not available", Toast.LENGTH_SHORT).show()
                    }
                }


            }

            R.id.date -> {
                showCalendar()
            }
            R.id.layout -> {
                Utils.hideKeyBoard(this@RestoreWalletActivity)
            }


        }


    }

    private fun showCalendar() {
        var now: Calendar = Calendar.getInstance()
        val dialog = DatePickerFragmentDialog.newInstance({ view, year, monthOfYear, dayOfMonth ->

            month = monthOfYear + 1;

            if (month < 10) {
                date?.text = year.toString() + "-0" + month + "-" + dayOfMonth

            } else {
                date?.text = year.toString() + "-" + month + "-" + dayOfMonth
            }

        })

        dialog.show(supportFragmentManager, "tag")
        dialog.setAccentColor("#62B04F")
    }

    var name: EditText? = null
    var date: TextView? = null
    var word: EditText? = null
    var password: EditText? = null
    var restoreBtn: Button? = null
    var layout: LinearLayout? = null
    var apiInterface: ApiInterface? = null
    var month: Int = 0
    var typeNetwork: IntArray = intArrayOf(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore_wallet)
        apiInterface = ApiClient.getClient()?.create(ApiInterface::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        val title: TextView = toolbar.findViewById(R.id.text_screen_title) as TextView
        title.setText(R.string.restore_a_wallet)


        init()


    }

    private fun init() {
        name = findViewById(R.id.walletName)
        date = findViewById(R.id.date)
        word = findViewById(R.id.words)
        password = findViewById(R.id.password)
        restoreBtn = findViewById(R.id.restoreBtn)
        layout = findViewById(R.id.layout)
        restoreBtn?.setOnClickListener(this)
        layout?.setOnClickListener(this)
        date?.setOnClickListener(this)


    }


    private fun restoreWallet(apiInterface: ApiInterface?) {


        apiInterface?.restoreWalletApi(
            AppConstance.recoverWallet,
            date?.text.toString(),
            word?.text.toString(),
            name?.text.toString(),
            password?.text.toString()
        )?.enqueue(object : retrofit2.Callback<LoadApiResponseModel?> {
            override fun onFailure(call: Call<LoadApiResponseModel?>, t: Throwable) {
                println(t.printStackTrace())
                Toast.makeText(this@RestoreWalletActivity, "Something went wrong. please try later", Toast.LENGTH_SHORT)
                    .show()

            }

            override fun onResponse(
                call: Call<LoadApiResponseModel?>,
                response: Response<LoadApiResponseModel?>
            ) {

                if (response.isSuccessful) {

                    finish()

                    Toast.makeText(this@RestoreWalletActivity, "Wallet successfully recovered", Toast.LENGTH_SHORT)
                        .show()


                } else {

                    if (response.code() == 400) {
                        Toast.makeText(this@RestoreWalletActivity, "Invalid input", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this@RestoreWalletActivity,
                            "Something went wrong. please try later",
                            Toast.LENGTH_SHORT
                        )

                    }


                }
            }
        })
    }


}