package io.xels.xelsandroidapp.view.activity

import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD
import io.xels.xelsandroidapp.R
import io.xels.xelsandroidapp.view.fragment.DashBoardFragment
import io.xels.xelsandroidapp.view.fragment.HistoryFragment
import io.xels.xelsandroidapp.view.fragment.ReceiveFragment
import io.xels.xelsandroidapp.view.fragment.SendFragment
import io.xels.xelsandroidapp.interfaces.ToolBarControll
import io.xels.xelsandroidapp.ulits.AppConstance
import io.xels.xelsandroidapp.ulits.PreferenceManager
import io.xels.xelsandroidapp.ulits.Utils
import io.xels.xelsandroidapp.ulits.Utils.isNetworkAvailable

class BaseActivity : FragmentActivity(), ToolBarControll, View.OnClickListener,

    NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val id = p0.itemId

        if (id == R.id.latestTransaction) {

            if (internetCheck(this@BaseActivity)) {

                fragment = HistoryFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment as HistoryFragment, "HistoryFragment")
                    .addToBackStack("HistoryFragment").commit()
            }


        } else if (id == R.id.receive) {

            if (internetCheck(this@BaseActivity)) {
                fragment = ReceiveFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment as ReceiveFragment, "ReceiveFragment")
                    .addToBackStack("ReceiveFragment").commit()
            }


        } else if (id == R.id.send) {

            if (internetCheck(this@BaseActivity)) {
                fragment = SendFragment()
                bundle = Bundle()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment as SendFragment, "SendFragment")
                    .addToBackStack("SendFragment").commit()
            }


        } else if (id == R.id.logout) {

            Utils.logout(this@BaseActivity)
            finish()
        } else if (id == R.id.dashBoard) {

            if (internetCheck(this@BaseActivity)) {

                fragment = DashBoardFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragment as DashBoardFragment, "DashBoardFragment")
                    .addToBackStack("DashBoardFragment").commit()
            }


        }

        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.drawerBtn ->
                drawer!!.openDrawer(Gravity.START)
        }


    }

    override fun showDialog(showDialog: Boolean) {
        if (showDialog) {

            progress?.show()

        } else {
            progress?.dismiss()

        }
    }


    var typeNetwork: IntArray = intArrayOf(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI)
    var toolBar: Toolbar? = null
    var titleTxt: TextView? = null
    var drawerBtn: Button? = null
    var drawer: DrawerLayout? = null
    var bundle: Bundle? = null
    private var progress: KProgressHUD? = null
    override fun setTitle(title: String) {
        titleTxt?.text = title
    }


    override fun internetCheck(context: FragmentActivity?): Boolean {

        var isActive = isNetworkAvailable(this@BaseActivity, typeNetwork)


        if (!isActive) {
            Toast.makeText(context, "Internet not available", Toast.LENGTH_SHORT).show()
        }


        return isActive

    }


    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        progress = KProgressHUD(this@BaseActivity)
        Utils.showDialog(this@BaseActivity)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        var headerView: View = navigationView.inflateHeaderView(R.layout.nav_header_base)

        var headeerTitle: TextView = headerView.findViewById(R.id.headerTitle)

        headeerTitle.text = PreferenceManager.getString(AppConstance.walletName)

        toolBar = findViewById(R.id.toolbar)
        titleTxt = toolBar?.findViewById(R.id.text_screen_title)
        drawerBtn = toolBar?.findViewById(R.id.drawerBtn)
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawerBtn?.setOnClickListener(this)
        titleTxt?.text = "DashBoard"

        Log.d("wallet name:", PreferenceManager.getString(AppConstance.walletName))
        fragment = DashBoardFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment as DashBoardFragment, "DashBoardFragment")
            .addToBackStack("DashBoardFragment").commit()

        navigationView.setItemIconTintList(null)

    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        }

        if (!drawer!!.isDrawerOpen(GravityCompat.START)) {
            if (supportFragmentManager.backStackEntryCount == 1 || supportFragmentManager.backStackEntryCount == 0) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }


    }


}