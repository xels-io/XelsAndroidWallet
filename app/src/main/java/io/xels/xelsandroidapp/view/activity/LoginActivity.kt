package io.xels.xelsandroidapp.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.response_model.LoadApiResponseModel
import io.xels.xelsandroidapp.response_model.NodeStatusApiResponse
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.PreferenceManager
import io.xels.xelsandroidapp.ulits.Utils
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener, Callback<NodeStatusApiResponse> {
    override fun onFailure(call: Call<NodeStatusApiResponse>, t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResponse(call: Call<NodeStatusApiResponse>, response: Response<NodeStatusApiResponse>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private var TAG = "LoginActivity"


    private var progress: KProgressHUD? = null
    var MY_PERMISSIONS_REQUEST_USE_CAMERA: Int = 0x00AF
    var typeNetwork: IntArray = intArrayOf(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progress = KProgressHUD(this)
        Utils.showDialog(this)
        init()
        checkPermission()

    }

    private fun init() {
        val wallet: EditText = findViewById(R.id.walletEditTxt) as EditText
        val password: EditText = findViewById(R.id.passwordEditTxt) as EditText
        val decryptBtn: Button = findViewById(R.id.decryptBtn) as Button
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        val title: TextView = toolbar.findViewById(R.id.text_screen_title) as TextView
        val createWalletBtn: Button = findViewById(R.id.btn_create_wallet) as Button


        title.setText(R.string.login)


        decryptBtn.setOnClickListener(this)
        restoreWalletBtn.setOnClickListener(this)
        createWalletBtn.setOnClickListener(this)
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission not available requesting permission");
            ActivityCompat.requestPermissions(
                this,
                Array<String>(1) { Manifest.permission.CAMERA }, MY_PERMISSIONS_REQUEST_USE_CAMERA
            );
        } else {
            Log.d(TAG, "Permission has already granted");
        }
    }

    private fun gotoDashBoard() {
        val intent = Intent(this, BaseActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun callApi() {
        progress?.show()

        Log.d(TAG, "data: " + walletEditTxt.text.toString() + " " + passwordEditTxt.text.toString())

        val apiInterface: ApiInterface? = ApiClient.getClient()?.create(ApiInterface::class.java)


        Log.e(TAG, "request: " + AppConstance.load + walletEditTxt.text.toString() + passwordEditTxt.text.toString())


        apiInterface?.loadApiCall(AppConstance.load, walletEditTxt.text.toString(), passwordEditTxt.text.toString())
            ?.enqueue(
                object : Callback<LoadApiResponseModel> {
                    override fun onFailure(call: Call<LoadApiResponseModel>, t: Throwable) {
                        println(t?.localizedMessage)
                        progress?.dismiss()

                        Utils.showError(this@LoginActivity)


                    }

                    override fun onResponse(
                        call: Call<LoadApiResponseModel>,
                        response: Response<LoadApiResponseModel>
                    ) {


                        if (response.isSuccessful) {
                            println("got it")


                            PreferenceManager.updateValue(AppConstance.walletName, walletEditTxt.text.toString())
                            PreferenceManager.updateValue(AppConstance.isLogin, true)
                            PreferenceManager.updateValue(AppConstance.password, passwordEditTxt.text.toString())
                            gotoDashBoard()
                            progress?.dismiss()


                        } else {
                            progress?.dismiss()
                            println("try later")

                            Utils.handleErrorResponse(response.errorBody().toString(),this@LoginActivity,response.code())

                        }

                    }


                })
    }

    private fun checkNodeStatus(apiInterface: ApiInterface) {
        apiInterface.nodeStatusApi().enqueue(object : Callback<NodeStatusApiResponse?> {
            override fun onFailure(call: Call<NodeStatusApiResponse?>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<NodeStatusApiResponse?>,
                response: Response<NodeStatusApiResponse?>
            ) {


            }

        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.decryptBtn ->

                if (!walletEditTxt.text.toString().isEmpty() && !passwordEditTxt.text.toString().isEmpty()) {

                    if (Utils.isNetworkAvailable(this@LoginActivity, typeNetwork)) {
                        callApi()
                    }
                     else {
                        Toast.makeText(this@LoginActivity, "Internet not available", Toast.LENGTH_SHORT).show()

                    }


                } else {
                    Toast.makeText(this, "Field missing", Toast.LENGTH_LONG).show()
                }

            R.id.restoreWalletBtn -> {
                val intent: Intent = Intent(this@LoginActivity, RestoreWalletActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_create_wallet -> {
                val intent: Intent = Intent(this@LoginActivity, CreateWalletActivity::class.java)
                startActivity(intent)
            }


        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {


        when (requestCode) {
            MY_PERMISSIONS_REQUEST_USE_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission was granted! Do your stuff");

                } else {
                    Log.d(TAG, "permission denied! Disable the function related with permission.");

                }
            }
        }


    }


}



