package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout

class GameBoardActivity : AppCompatActivity() {

    private lateinit var layoutCheckerBoard : FlexboxLayout
    private lateinit var scrollCheckerBoard : HorizontalScrollView

    private val tiles_src = GameBoardResources()
    private var isFirstTurn : Boolean = true

    private lateinit var txtvwBoardPass : TextView
    private lateinit var txtvwBoardDraw : TextView
    private lateinit var txtvwBoardSwap : TextView

    private lateinit var txtvwBoardStatus : TextView
    private lateinit var txtvwBoardTurn : TextView
    private lateinit var txtvwBoardPoints1 : TextView

    private lateinit var imgvwBoardSurrender : ImageView
    private lateinit var imgvwBoardClear : ImageView
    private lateinit var imgvwBoardPlay : ImageView

    private lateinit var layoutBoardLetterPool : FlexboxLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        initView()
        generateBoardTiles()
        generateButtonTiles()
    }

    private fun generateBoardTiles() {

        for (i in 0..224) {
            val tilebtn = ImageView(this)

            val params = LinearLayout.LayoutParams(100, 100)
            val tileval = (i + 1).toString()

            tilebtn.apply {
                layoutParams = params
                // text = tileval
                // textSize = 8f
            }

            /** check on all gameboardsources array if loop counter exist among those array */
            var beenTileValued = false
            if (!beenTileValued) {
                for (j in tiles_src.getTileTripleWord()) {
                    if ((i+1) == j) {
                        tilebtn.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_tripleword))
                        beenTileValued = true
                        break
                    }
                }
            }
            if (!beenTileValued) {
                for (j in tiles_src.getTileTripleLetter()) {
                    if ((i+1) == j) {
                        tilebtn.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_tripleletter))
                        beenTileValued = true
                        break
                    }
                }
            }
            if (!beenTileValued) {
                for (j in tiles_src.getTileDoubleWord()) {
                    if ((i+1) == j) {
                        tilebtn.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_doubleword))
                        beenTileValued = true
                        break
                    }
                }
            }
            if (!beenTileValued) {
                for (j in tiles_src.getTileDoubleLetter()) {
                    if ((i+1) == j) {
                        tilebtn.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_doubleletter))
                        beenTileValued = true
                        break
                    }
                }
            }
            if (!beenTileValued) {
                if ((i+1) == 113) {
                    tilebtn.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_startcenter))
                }
                else {
                    tilebtn.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_plain))
                }
            }



            tilebtn.setOnClickListener(View.OnClickListener {
                // val key = tilebtn.text.toString()
                Toast.makeText(this, tileval, Toast.LENGTH_SHORT).show()
            })

            layoutCheckerBoard.addView(tilebtn)
        }


    }

    private fun generateButtonTiles() {
        for (i in 0..6) { // 11
            // val letter = ('A'..'Z').random()

            val letter = tiles_src.getLetterPoint1().random()
            val tilebtn = Button(this)

            tilebtn.apply {
                setAllCaps(false)
                layoutParams = LinearLayout.LayoutParams(150, 150)
                text = letter.toString()
                textSize = 15f
            }
            tilebtn.setOnClickListener(View.OnClickListener {
                val key = tilebtn.text.toString()
//                var currWord = txtvwGameWord.text.toString()
//                if (currWord == getString(DEFAULT_WORD)) {
//                    currWord = ""
//                }
//                currWord += key
//                txtvwGameWord.text = currWord
//                layoutLetterPool.removeView(tilebtn)
            })

            layoutBoardLetterPool.addView(tilebtn)
        }

        val highPointTiles = tiles_src.getHighLetterPoints()
        for (i in 0..4) { // 11
            val batchLetters = highPointTiles.random()
            val letter = batchLetters.random()

            val tilebtn = Button(this)

            tilebtn.apply {
                setAllCaps(false)
                layoutParams = LinearLayout.LayoutParams(150, 150)
                text = letter.toString()
                textSize = 15f
            }
            tilebtn.setOnClickListener(View.OnClickListener {
                val key = tilebtn.text.toString()
//                var currWord = txtvwGameWord.text.toString()
//                if (currWord == getString(DEFAULT_WORD)) {
//                    currWord = ""
//                }
//                currWord += key
//                txtvwGameWord.text = currWord
//                layoutLetterPool.removeView(tilebtn)
            })

            layoutBoardLetterPool.addView(tilebtn)
        }
    }

    private fun initView() {
        layoutCheckerBoard = findViewById(R.id.layout_checkerboard)
        scrollCheckerBoard = findViewById(R.id.scroll_board)

        txtvwBoardPass = findViewById(R.id.txt_pass)
        txtvwBoardDraw = findViewById(R.id.txt_draw)
        txtvwBoardSwap = findViewById(R.id.txt_swap)

        txtvwBoardStatus = findViewById(R.id.txt_status)
        txtvwBoardTurn = findViewById(R.id.txt_turn)
        txtvwBoardPoints1 = findViewById(R.id.txt_player1pts)

        imgvwBoardSurrender = findViewById(R.id.imgvw_surrender)
        imgvwBoardClear = findViewById(R.id.imgvw_clear)
        imgvwBoardPlay = findViewById(R.id.imgvw_play)

        layoutBoardLetterPool = findViewById(R.id.layout_boardletterpool)
    }
}