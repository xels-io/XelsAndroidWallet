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
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import io.xels.xelsandroidapp.response_model.ErrorApiResponse
import java.io.IOException


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

        Toast.makeText(context, "Something went wrong, Please try later", Toast.LENGTH_LONG).show()


    }


    fun showDialog(context: FragmentActivity) {
        KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setDimAmount(0.5f)
            .setLabel("Loading...")
    }

    fun handleErrorResponse(resonse: String, fragmentActivity: FragmentActivity, code: Int) {

        Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()

        /*       when (code) {



                       400 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   401 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   403 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   404 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   406 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   500 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   502 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   404 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   404 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
                   404 ->
                       Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()
               }
       */

    }

    fun isNetworkAvailable(context: Context, typeNetworks: IntArray): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            for (typeNetwork in typeNetworks) {
                val networkInfo = connectivityManager.getNetworkInfo(typeNetwork)
                if (networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } catch (ex: Exception) {
            return false
        }

        return false
    }


/*    fun hideKeyBoard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus.windowToken, 0
        )
    }*/

}



