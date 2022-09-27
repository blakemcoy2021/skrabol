package com.nairdnah.skrabol

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class DialogLegends: DialogFragment() {

    val letter_pt1 = arrayOf('A', 'E', 'I', 'L', 'N', 'O', 'R', 'S', 'T', 'U')
    val letter_pt2 = arrayOf('D', 'G')
    val letter_pt3 = arrayOf('B', 'C', 'M', 'P')
    val letter_pt4 = arrayOf('F', 'H', 'V', 'W', 'Y')
    val letter_pt5 = arrayOf('K')
    val letter_pt8 = arrayOf('J', 'X')
    val letter_pt10 = arrayOf('Q', 'Z')

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = activity?.let {
            val customDialog = AlertDialog.Builder(it)
            val customView = requireActivity().layoutInflater.inflate(R.layout.dialog_legends, null)
            val tilepts : Array<Int> = arrayOf(
                    R.id.spn_tiles1,
                    R.id.spn_tiles2,
                    R.id.spn_tiles3,
                    R.id.spn_tiles4,
                    R.id.spn_tiles5,
                    R.id.spn_tiles8,
                    R.id.spn_tiles10
            )
            val tileArr = arrayOf(
                    letter_pt1, letter_pt2, letter_pt3, letter_pt4, letter_pt5, letter_pt8, letter_pt10
            )
            for (i in tilepts.indices) {
                val spn_widget : Spinner = customView.findViewById(tilepts[i])
                val spn_adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, tileArr[i])
                spn_widget.adapter = spn_adapter
            }

            customDialog.setView(customView)
            customDialog.setCancelable(false)
            customDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                println("close dialog")
            })
            customDialog.create()
        }?: throw IllegalStateException("Activity is null !!")
        return d
    }

}