package com.nairdnah.skrabol

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView

class DialogLoading(val mActivity: Activity) {
    private lateinit var dialog: AlertDialog

    fun startLoading() {
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_dialog, null)

        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun isDismiss() {
        dialog.dismiss()
    }
}