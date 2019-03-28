package io.xels.xelsandroidapp.ulits

import android.content.Context
import android.content.Intent
import io.xels.xelsandroidapp.view.activity.LoginActivity
import java.util.*
import android.content.ClipData
import android.content.ClipboardManager
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import android.net.NetworkInfo
import android.net.ConnectivityManager




object Utils {

    fun convertTimeToDate(timeStamp: String): String {

        var cal: Calendar = Calendar.getInstance(Locale.ENGLISH)

        cal.timeInMillis = timeStamp.toLong() * 1000

        var date: String = android.text.format.DateFormat.format("dd-MM-yyyy", cal).toString()

        return date

    }


    fun logout(context: Context) {


        PreferenceManager.updateValue(AppConstance.walletName, "")
        PreferenceManager.updateValue(AppConstance.isLogin, false)
        val intent = Intent(context, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

    }


    fun copyToClipBoard(context: FragmentActivity?, msg: String, label: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(label, msg)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun showError(context: FragmentActivity?) {

        Toast.makeText(context, "Something went wrong, PLease try later", Toast.LENGTH_LONG).show()


    }


    fun showDialog(context: FragmentActivity) {
        KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

/*
    fun handleErrorResponse(response: Response<Any>, context: FragmentActivity, code: Int) {
        var error: ErrorApiResponse
        gson = GsonBuilder().create()

        error = ErrorApiResponse()


    }*/

    fun isNetworkAvailable(context: Context, typeNetworks: IntArray): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            for (typeNetwork in typeNetworks) {
                val networkInfo =connectivityManager.getNetworkInfo(typeNetwork)
                if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } catch (ex: Exception) {
            return false
        }

        return false
    }
}