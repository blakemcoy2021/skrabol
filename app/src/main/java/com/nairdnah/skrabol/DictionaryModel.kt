package com.nairdnah.skrabol

import android.annotation.SuppressLint

data class DictionaryModel(
    var id: Long = 0,
    var word: String = "",
    var details: String = "",
    var category: String = "n/a"
) {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getAutoId() : Long {

            /** below block is for making millis as unique id but fail to processs speed */
//            val formatid = "yyyyMMddHHmmss"
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val current = LocalDateTime.now()
//                val formatter = DateTimeFormatter.ofPattern(formatid)
//                val answer: String =  current.format(formatter)
//                //Log.d("answer",answer)
//
//                return answer.toLong()
//            }
//            else {
//                val date = Date()
//                val formatter = SimpleDateFormat(formatid)
//                val answer: String = formatter.format(date)
//                //Log.d("answer",answer)
//
//                return answer.toLong()
//            }

            return 0
        }
    }
}