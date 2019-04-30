package io.xels.xelsandroidapp.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.PreferenceManager
import io.xels.xelsandroidapp.ulits.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.change_url.*

class UrlChangeActivity : FragmentActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.okBTn -> {
                PreferenceManager.updateValue(AppConstance.baseUrl, url?.text.toString())
                finish()
            }
            R.id.container -> {
                Utils.hideKeyBoard(this@UrlChangeActivity)
            }

        }
    }

    var url: EditText? = null
    var okBTn: Button? = null
    var container: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_url)
        url = findViewById(R.id.url) as EditText
        okBTn = findViewById(R.id.okBTn) as Button
        container = findViewById(R.id.container) as RelativeLayout

        container?.setOnClickListener(this)


        okBTn?.setOnClickListener(this)
        url?.setText(AppConstance.BASE_URL)

    }
}