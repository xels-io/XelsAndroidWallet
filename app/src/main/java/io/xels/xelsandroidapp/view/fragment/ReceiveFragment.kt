package io.xels.xelsandroidapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.response_model.GetUnUsedAddressResponseModel
import io.xels.xelsandroidapp.retrofit.ApiClient
import io.xels.xelsandroidapp.retrofit.ApiInterface
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.PreferenceManager
import io.xels.xelsandroidapp.ulits.Utils
import kotlinx.android.synthetic.main.fragment_recieve.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Point
import android.view.WindowManager
import io.xels.xelsandroidapp.interfaces.ToolBarControll


class ReceiveFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.copyToClipBoardBtn -> Utils.copyToClipBoard(activity, addressTxtView.text.toString(), "hello")


            R.id.showAllAddressTxtView ->{
                fragment=ShowAllAddressFragment()
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, fragment as ShowAllAddressFragment,
                    "ShowAllAddressFragment")?.addToBackStack("ShowAllAddressFragment")?.commit()
            }

        }


    }


    var apiInterface: ApiInterface? = null;
    var qrEcode: QRGEncoder? = null
    var bitmap: Bitmap? = null
    var fragment: Fragment? = null


    var toolBarControll: ToolBarControll? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolBarControll?.setTitle("Receive")
        toolBarControll?.showDialog(true)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recieve, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        copyToClipBoardBtn.setOnClickListener(this)
        showAllAddressTxtView.setOnClickListener(this)
        apiInterface = ApiClient.getClient()?.create(ApiInterface::class.java)

        apiInterface?.getUnUsedAddress(
            AppConstance.unAddress,
            PreferenceManager.getString(AppConstance.walletName),
            AppConstance.ACCOUNT_NAME
        )
            ?.enqueue(
                object : Callback<GetUnUsedAddressResponseModel?> {
                    override fun onFailure(call: Call<GetUnUsedAddressResponseModel?>, t: Throwable) {

                        Utils.showError(activity)

                        println(t.printStackTrace())
                        toolBarControll?.showDialog(false)


                    }

                    override fun onResponse(
                        call: Call<GetUnUsedAddressResponseModel?>,
                        response: Response<GetUnUsedAddressResponseModel?>
                    ) {

                        if (response.isSuccessful) {
                            addressLayout.visibility = View.VISIBLE
                            addressTxtView.text = response.body()?.innerMsg

                            showQrCode(response)
                            toolBarControll?.showDialog(false)


                        } else {
                            println("dadasdasd")
                            toolBarControll?.showDialog(false)

                        }

                    }

                })


    }

    private fun showQrCode(response: Response<GetUnUsedAddressResponseModel?>) {
        val manager = activity?.getSystemService(WINDOW_SERVICE) as WindowManager?
        val display = manager!!.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = if (width < height) width else height
        smallerDimension = smallerDimension * 3 / 4

        qrEcode =
            QRGEncoder(response.body()?.innerMsg, null, QRGContents.Type.TEXT, smallerDimension)

        bitmap = qrEcode?.encodeAsBitmap()

        qrCode.setImageBitmap(bitmap)
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