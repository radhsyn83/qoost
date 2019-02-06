package com.segamedev.qoost

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.WindowManager

abstract class BaseActivity : AppCompatActivity() {
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 23) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = resources.getColor(R.color.white)
        }

        setContentView(setLayoutResource())
        setToolBar(setToolBar(), setToolBarTitle())

        onViewReady(this, savedInstanceState)
    }

    protected abstract fun setLayoutResource(): Int
    protected abstract fun setToolBar(): Toolbar
    protected abstract fun setToolBarTitle(): String
    protected abstract fun onViewReady(context: Context, savedInstanceState: Bundle?)

    private fun setToolBar(toolbar: Toolbar?, title: String) {
        if (toolbar != null) {
            mToolbar = toolbar
            toolbar.setTitleTextColor(Color.WHITE)
            toolbar.title = ""
            setSupportActionBar(toolbar)
        }
    }

    fun toolbarBackDrawable() {
        val upArrow = ContextCompat.getDrawable(this, R.drawable.ic_left_arrow)
        upArrow!!.setColorFilter(
            ContextCompat.getColor(this, R.color.overlay_dark_70),
            PorterDuff.Mode.SRC_ATOP
        )
        supportActionBar!!.setHomeAsUpIndicator(upArrow)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun toolbarBackPressed(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}