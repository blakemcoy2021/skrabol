package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.flexbox.FlexboxLayout

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

    val DEFAULT_WORD : String = "- - - - - - -"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_word)

        initView()
        txtvwGameWord.text = DEFAULT_WORD
        generateButtonTiles()

        btnGameClear.setOnClickListener {
            txtvwGameWord.setText(DEFAULT_WORD)
        }
        btnGameReRoll.setOnClickListener {
            layoutLetterPool.removeAllViews()
            generateButtonTiles()
        }
    }

    fun generateButtonTiles() {
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
                if (currWord == DEFAULT_WORD) {
                    currWord = ""
                }
                currWord += key
                txtvwGameWord.setText(currWord)
            })

            layoutLetterPool.addView(tilebtn)
        }
    }

    fun initView() {
        layoutLetterPool = findViewById(R.id.layout_letterpool)
        txtvwGameWord = findViewById(R.id.txtvwGameWord)
        btnGameClear = findViewById(R.id.btn_gameclear)
        btnGameReRoll = findViewById(R.id.btn_gamereroll)
    }
}