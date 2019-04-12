package io.xels.xelsandroidapp.ulits

import android.app.AlertDialog
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
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.address_dialog.view.*
import kotlinx.android.synthetic.main.fragment_recieve.*
import android.support.v4.content.ContextCompat.startActivity
import android.R
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Point
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import io.xels.xelsandroidapp.response_model.ErrorApiResponse
import kotlinx.android.synthetic.main.show_qr_code.view.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import retrofit2.Response


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


    fun copyToClipBoard(context: FragmentActivity?, msg: String?, label: String) {
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

    fun handleErrorResponse(
        response: Response<*>,
        fragmentActivity: FragmentActivity,
        code: Int,
        jsonObject: JSONObject
    ) {

        val gson = GsonBuilder().create()
        var mError = ErrorApiResponse()
        try {
            if (response != null) {
                mError =
                    gson.fromJson<ErrorApiResponse>(response.errorBody()!!.toString(), ErrorApiResponse::class.java!!)
            } else if (jsonObject != null) {
                mError = gson.fromJson<ErrorApiResponse>(jsonObject.toString(), ErrorApiResponse::class.java!!)
            }

            when (code) {


            /*    400 ->
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
                    Toast.makeText(fragmentActivity, resonse, Toast.LENGTH_SHORT).show()*/
            }

        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
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


    fun hideKeyBoard(activity: FragmentActivity?) {
        val inputMethodManager = activity?.getSystemService(
            FragmentActivity.INPUT_METHOD_SERVICE
        ) as InputMethodManager


        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus.windowToken, 0
            )
        }


    }

    fun showOptionDialog(activity: FragmentActivity?, address: String?) {
        val mDialogView = LayoutInflater.from(activity).inflate(io.xels.xelsandroidapp.R.layout.address_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.shareBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            shareInformation(activity, address)

        }
        //cancel button click of custom layout
        mDialogView.copyBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            Utils.copyToClipBoard(activity, address, "hello")
        }


        mDialogView.qrCodeBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            showQrCode(activity, address)


        }
    }

    private fun showQrCode(activity: FragmentActivity?, address: String?) {
        var qrEcode: QRGEncoder? = null
        var bitmap: Bitmap? = null
        val manager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val display = manager!!.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4

        qrEcode = QRGEncoder(address, null, QRGContents.Type.TEXT, smallerDimension)

        bitmap = qrEcode?.encodeAsBitmap()


        val mDialogView = LayoutInflater.from(activity).inflate(io.xels.xelsandroidapp.R.layout.show_qr_code, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.qrCode.setImageBitmap(bitmap)

        mDialogView.closeBtn.setOnClickListener(View.OnClickListener {
            mAlertDialog.dismiss()
        })
    }


    fun shareInformation(activity: FragmentActivity?, address: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, address)
        activity?.startActivity(Intent.createChooser(shareIntent, "Share address using"))
    }


}



