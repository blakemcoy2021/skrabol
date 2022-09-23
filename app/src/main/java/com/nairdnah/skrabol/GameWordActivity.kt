package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.flexbox.FlexboxLayout
import java.util.*
import kotlin.collections.ArrayList

class GameWordActivity : AppCompatActivity() {

    val letter_pt1 = arrayOf("A", "E", "I", "L", "N", "O", "R", "S", "T", "U")
    val letter_ctr1 = arrayOf("9", "12", "9", "4", "6", "8", "6", "4", "6", "4")

    val letter_pt2 = arrayOf("D", "G")
    val letter_ctr2 = arrayOf("4", "3")

    val letter_pt3 = arrayOf("B", "C", "M", "P")
    val letter_ctr3 = arrayOf("2", "2", "2", "2")

    val letter_pt4 = arrayOf("F", "H", "V", "W", "Y")
    val letter_ctr4 = arrayOf("2", "2", "2", "2", "2")

    val letter_pt5 = arrayOf("K")
    val letter_ctr5 = arrayOf("1")

    val letter_pt8 = arrayOf("J", "X")
    val letter_ctr8 = arrayOf("1", "1")

    val letter_pt10 = arrayOf("Q", "Z")
    val letter_ctr10 = arrayOf("1", "1")


    private lateinit var layoutLetterPool : FlexboxLayout

    private lateinit var txtvwGameWord : TextView

    private lateinit var btnGameClear : Button
    private lateinit var btnGameReRoll : Button

    private lateinit var btnGameRun : ImageView
    private lateinit var edxGameWord : EditText
    private lateinit var txtvwGameStatus : TextView
    private lateinit var txtvwGamePoints : TextView

    val DEFAULT_WORD : Int = R.string.game_word
    val DEFAULT_STATUS : Int = R.string.game_status
    val DEFAULT_POINTS : Int = R.string.game_points

    private lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_word)

        sqLiteHelper = SQLiteHelper(this)

        initView()
        txtvwGameWord.text = getString(DEFAULT_WORD)
        txtvwGameStatus.text = getString(DEFAULT_STATUS)
        txtvwGamePoints.text = getString(DEFAULT_POINTS)
        generateButtonTiles()

        btnGameClear.setOnClickListener {
            txtvwGameWord.setText(getString(DEFAULT_WORD))
            txtvwGameStatus.setText(getString(DEFAULT_STATUS))
            txtvwGamePoints.setText(getString(DEFAULT_POINTS))
        }
        btnGameReRoll.setOnClickListener {
            layoutLetterPool.removeAllViews()
            generateButtonTiles()
        }
        btnGameRun.setOnClickListener {
            val inputword = edxGameWord.text.toString()
            var currword = txtvwGameWord.text.toString()
            if (inputword.trim().isEmpty() && currword == getString(DEFAULT_WORD)) {
                Toast.makeText(this, "Enter a word to post.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (inputword.trim().isNotEmpty() || inputword != "") {
                txtvwGameWord.text = inputword
                checkWordExist(inputword)
            }
            else if (currword != getString(DEFAULT_WORD)) {
                currword = currword[0].toUpperCase() + currword.substring(1).toLowerCase(Locale.getDefault())
                checkWordExist(currword)
            }

        }
    }

    fun checkWordExist(word: String) {
        val dictlist = sqLiteHelper.onGetDataSpecific(word, true)
        if (dictlist.size != 0) {
            Toast.makeText(this, "Word is Existing in the Records", Toast.LENGTH_SHORT).show()
            txtvwGameStatus.text = dictlist[0].details

//            if (word == "Jamvee") {
//                txtvwGamePoints.text = "100"
//            }
//            else if (word == "Jamvee Joyce") {
//                txtvwGamePoints.text = "101%"
//            }
//            else if (word == "Mhel Handrian") {
//                txtvwGamePoints.text = "-101"
//            }
//            else if (word == "GG") {
//                txtvwGamePoints.text = "-100 sa langit"
//            }
        }
        else {
            Toast.makeText(this, "Your word not found!", Toast.LENGTH_SHORT).show()
            txtvwGameStatus.text = getString(DEFAULT_STATUS)
            txtvwGamePoints.text = getString(DEFAULT_POINTS)
        }

    }

    fun generateButtonTiles() {

//        val tempLetterPool : ArrayList<Char> = arrayListOf('X','J','A','G','M','E','V','E','J','Y','C','O')
//        for (i in 0 until tempLetterPool.size) {
//            val letter = tempLetterPool[i]

        for (i in 0..11) {
            val letter = ('A'..'Z').random()

            val tilebtn = Button(this)

            tilebtn.apply {
                setAllCaps(false)
                layoutParams = LinearLayout.LayoutParams(200, 200)
                text = letter.toString()
                textSize = 20f
            }
            tilebtn.setOnClickListener(View.OnClickListener {
                val key = tilebtn.text.toString()
                var currWord = txtvwGameWord.text.toString()
                if (currWord == getString(DEFAULT_WORD)) {
                    currWord = ""
                }
                currWord += key
                txtvwGameWord.setText(currWord)
                layoutLetterPool.removeView(tilebtn)
            })

            layoutLetterPool.addView(tilebtn)
        }
    }

    fun initView() {
        layoutLetterPool = findViewById(R.id.layout_letterpool)
        txtvwGameWord = findViewById(R.id.txtvwGameWord)
        btnGameClear = findViewById(R.id.btn_gameclear)
        btnGameReRoll = findViewById(R.id.btn_gamereroll)
        btnGameRun = findViewById(R.id.btn_gamerun)
        edxGameWord = findViewById(R.id.edx_gameword)
        txtvwGameStatus = findViewById(R.id.txtvwGameStatus)
        txtvwGamePoints = findViewById(R.id.txtvwGamePoints)
    }
}