package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.google.android.flexbox.FlexboxLayout

class GameBoardActivity : AppCompatActivity() {

    private lateinit var layoutCheckerBoard : FlexboxLayout
    private lateinit var scrollCheckerBoard : HorizontalScrollView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        initView()
        generateButtonTiles()
    }

    private fun generateButtonTiles() {

        for (i in 0..224) {
            val tilebtn = Button(this)

            val params = LinearLayout.LayoutParams(100, 100)

            tilebtn.apply {
                layoutParams = params
                text = (i + 1).toString()
                textSize = 8f
            }

            layoutCheckerBoard.addView(tilebtn)
        }


    }

    private fun initView() {
        layoutCheckerBoard = findViewById(R.id.layout_checkerboard)
        scrollCheckerBoard = findViewById(R.id.scroll_board)

    }
}