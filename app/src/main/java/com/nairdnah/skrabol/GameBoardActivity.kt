package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GameBoardActivity : AppCompatActivity() {

    private lateinit var layoutCheckerBoard : FlexboxLayout
    private lateinit var scrollCheckerBoard : HorizontalScrollView

    private val tiles_src = GameBoardResources()
    private var isFirstTurn : Boolean = true
        private var isBoardEnable : Boolean = false
    private var isTilesEnable : Boolean = true

    private var tileSelectionOrigin = 0
    private var tileSelectionEnd = 0
    private var wordToPlay = ""
        private var idsOfBoardTiles : ArrayList<Int> = ArrayList()
        private var validBoardTilePlacers : ArrayList<Int> = ArrayList()

    private val boardResources = GameBoardResources()

    private var turnCounter = 1

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

        private lateinit var sqLiteHelper: SQLiteHelper
        private var selectedTilesToPlay : ArrayList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

            sqLiteHelper = SQLiteHelper(this)

        initView()
        generateBoardTiles()
        generateButtonTiles()

        var initLabels = txtvwBoardTurn.text.toString() + " " + turnCounter.toString()
        txtvwBoardTurn.text = initLabels
        initLabels = txtvwBoardPoints1.text.toString() + " 0"
        txtvwBoardPoints1.text = initLabels


        imgvwBoardPlay.setOnClickListener {
            val currWord = txtvwBoardStatus.text.toString()
            if (currWord.contains(", create word", ignoreCase = true)) {
                Toast.makeText(this, "Create a word first to play!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            if (checkWordExist(currWord) == -1) {
//                Toast.makeText(this, "There is no such word found! Restored Tiles", Toast.LENGTH_SHORT).show()
//                for (btn in selectedTilesToPlay) {
//                    layoutBoardLetterPool.addView(btn)
//                }
//                inputTurnResetter()
//                return@setOnClickListener
//            }


            isTilesEnable = false
                // isBoardEnable = true
            wordToPlay = currWord
            if (isFirstTurn) {
                tileSelectionOrigin = 113
            }

            if (tileSelectionOrigin == 0) {
                txtvwBoardStatus.text = "$wordToPlay, tile select start of word"
                imgvwBoardPlay.isEnabled = false
            }
            else {
                imgvwBoardPlay.isEnabled = false
                txtvwBoardStatus.text = "$wordToPlay, tile select end of word"
                val valueWordAddMux = wordToPlay.length-1 /* because of index thing */
                val towardUp = tileSelectionOrigin - (15 * valueWordAddMux)
                val towardLeft = tileSelectionOrigin - valueWordAddMux
                val towardRight = tileSelectionOrigin + valueWordAddMux;
                val towardDown = tileSelectionOrigin + (15 * valueWordAddMux)
                validBoardTilePlacers.add(towardUp)
                validBoardTilePlacers.add(towardLeft)
                validBoardTilePlacers.add(towardRight)
                validBoardTilePlacers.add(towardDown)
                    // Toast.makeText(this, validBoardTilePlacers.toString(), Toast.LENGTH_SHORT).show()

                /** highlight those buttons */
                redrawTileSelectionEnd(true)
            }

        }

    }

    private fun inputTurnResetter() {
        val defaultText = getString(R.string.board_status)
       if (turnCounter == 1) {
            txtvwBoardStatus.text = defaultText
        }
        else {
            val idxTurn = defaultText.indexOf("turn", 0, ignoreCase = true)
            var newStatus = defaultText.substring(idxTurn)
            newStatus = "$turnCounter $newStatus"
            txtvwBoardStatus.text = newStatus
        }

    }

    private fun redrawTileSelectionEnd(isHighlight : Boolean) {
        val tripleWordArr = arrayOf(R.drawable.tile_tripleword, R.drawable.tile_tripleword_hl)
        val tripleLetterArr = arrayOf(R.drawable.tile_tripleletter, R.drawable.tile_tripleletter_hl)
        val doubleWordArr = arrayOf(R.drawable.tile_doubleword, R.drawable.tile_doubleword_hl)
        val doubleLetterArr = arrayOf(R.drawable.tile_doubleletter, R.drawable.tile_doubleletter_hl)
        val starArr = arrayOf(R.drawable.tile_startcenter, R.drawable.tile_startcenter_hl)
        val plainArr = arrayOf(R.drawable.tile_plain, R.drawable.tile_plain_hl)

        var mode = 0
        if (isHighlight) {
            mode = 1
        }

        for (i in validBoardTilePlacers) { // Log.d("@#@#@#@#@#@", idsOfBoardTiles[i-1].toString())
            var beenFound = false
            if (!beenFound) {
                for (j in tiles_src.getTileTripleWord()) {
                    if (i == j) {
                        findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, tripleWordArr[mode]))
                        beenFound = true
                    }
                }
            }
            if (!beenFound) {
                for (j in tiles_src.getTileTripleLetter()) {
                    if (i == j) {
                        findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, tripleLetterArr[mode]))
                        beenFound = true
                    }
                }
            }
            if (!beenFound) {
                for (j in tiles_src.getTileDoubleWord()) {
                    if (i == j) {
                        findViewById<ImageView>(idsOfBoardTiles[i]).setImageDrawable(ContextCompat.getDrawable(applicationContext, doubleWordArr[mode]))
                        beenFound = true
                    }
                }
            }
            if (!beenFound) {
                for (j in tiles_src.getTileDoubleLetter()) {
                    if (i == j) {
                        findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, doubleLetterArr[mode]))
                        beenFound = true
                    }
                }
            }
            if (!beenFound) {
                if (i == 113) {
                    findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, starArr[mode]))
                }
                else {
                    findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, plainArr[mode]))
                }
            }
        }
    }


    private fun generateBoardTiles() {

        for (i in 0..224) {
            val tilebtn = ImageView(this)

            val params = LinearLayout.LayoutParams(100, 100)
            val tileval = (i + 1).toString()


            var tag = "n/a"
            var id = -1
            id = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tag = "xsystem"
                View.generateViewId()
            }
            else {
                tag = "own"
                val ownGeneratedId = i.toString() + "ddMyyyyhhmmss" + i.toString()
                Integer.parseInt(SimpleDateFormat(ownGeneratedId, Locale.getDefault()).toString())
            }
            tilebtn.id = id

            idsOfBoardTiles.add(id) /** some_var_name_here = findViewById(id) :: could be in json in the future */
                Log.d("_#_#_#_$tag", tilebtn.id.toString())

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
                // val key = tilebtn.text.toString() /* only valid if it was not an ImageView as button */

                // if (isBoardEnable) {

                    if (tileSelectionOrigin == 0) {
                        tileSelectionOrigin = tileval.toInt()
                        txtvwBoardStatus.text = "$wordToPlay, tile select end of word"
                        Toast.makeText(this, "Start of word begins here at tile #$tileval", Toast.LENGTH_SHORT).show()

                        val valueWordAddMux = wordToPlay.length-1 /* because of index thing */
                        val towardUp = tileSelectionOrigin - (15 * valueWordAddMux)
                        val towardLeft = tileSelectionOrigin - valueWordAddMux
                        val towardRight = tileSelectionOrigin + valueWordAddMux;
                        val towardDown = tileSelectionOrigin + (15 * valueWordAddMux)
                        validBoardTilePlacers.add(towardUp)
                        validBoardTilePlacers.add(towardLeft)
                        validBoardTilePlacers.add(towardRight)
                        validBoardTilePlacers.add(towardDown)
                            // Toast.makeText(this, validBoardTilePlacers.toString(), Toast.LENGTH_SHORT).show()

                        /** highlight those buttons */
                        for (i in validBoardTilePlacers) {
                            var beenTileValued = false
                            if (!beenTileValued) {
                                for (j in tiles_src.getTileTripleWord()) {
                                    if ((i+1) == j) {
                                        findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_tripleword_hl))
                                        beenTileValued = true
                                        break
                                    }
                                }
                            }
                            if (!beenTileValued) {
                                for (j in tiles_src.getTileTripleLetter()) {
                                    if ((i+1) == j) {
                                        findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_tripleword_hl))
                                        beenTileValued = true
                                        break
                                    }
                                }
                            }
                            if (!beenTileValued) {
                                for (j in tiles_src.getTileDoubleWord()) {
                                    if ((i+1) == j) {
                                        findViewById<ImageView>(idsOfBoardTiles[i]).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_doubleword_hl))
                                        beenTileValued = true
                                        break
                                    }
                                }
                            }
                            if (!beenTileValued) {
                                for (j in tiles_src.getTileDoubleLetter()) {
                                    if ((i+1) == j) {
                                        findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_doubleletter_hl))
                                        beenTileValued = true
                                        break
                                    }
                                }
                            }
                            if (!beenTileValued) {
                                if ((i+1) == 113) {
                                    findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_startcenter_hl))
                                }
                                else {
                                    findViewById<ImageView>(idsOfBoardTiles[i-1]).setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.tile_plain_hl))
                                }
                            }


                        }

                    }
                    else {
                        tileSelectionEnd = tileval.toInt()
                        if (!validBoardTilePlacers.contains(tileSelectionEnd)) {
                            Toast.makeText(this, "Place on a valid tile (guided by yellow highlight).", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }

                        Toast.makeText(this, "End of word begins here at tile #$tileval", Toast.LENGTH_SHORT).show()

                            /** place wordToPlay on the board here */
                        val tilesPosToFill = ArrayList<Int>()

                        val pos = validBoardTilePlacers.indexOf(tileSelectionEnd)
                        /** [68,110,116,158] :: 0-up, 1-left, 2,-right, 3-down
                            pos = 68
                                UP: 68 + 15, 83...
                                LEFT: 110 + 1, 111...
                         */
                        when (pos) {
                            0 -> {
                                var incrementer = validBoardTilePlacers[pos] /** 68 */
                                tilesPosToFill.add(incrementer) /** [67,] */

                                val iteratorValue = wordToPlay.length - 1
                                for (i in 1..iteratorValue) {
                                    incrementer += 15 /** 67 + 15 = 82 */
                                    tilesPosToFill.add(incrementer) /** [67,82,] */
                                }
                            }
                            1 -> {
                                var incrementer = validBoardTilePlacers[pos] /** 110 */
                                tilesPosToFill.add(incrementer) /** [109,] */

                                val iteratorValue = wordToPlay.length - 1
                                for (i in 1..iteratorValue) {
                                    incrementer += 1 /** 109 + 1 = 110 */
                                    tilesPosToFill.add(incrementer) /** [109,110,] */
                                }
                            }
                            2 -> {
                                var incrementer = validBoardTilePlacers[pos] /** 116 */
                                tilesPosToFill.add(incrementer) /** [115,] */

                                val iteratorValue = wordToPlay.length - 1
                                for (i in 1..iteratorValue) {
                                    incrementer -= 1 /** 115 - 1 = 114 */
                                    tilesPosToFill.add(incrementer) /** [115,114,] */
                                }
                            }
                            3 -> {
                                var incrementer = validBoardTilePlacers[pos] /** 158 */
                                tilesPosToFill.add(incrementer) /** [157,] */

                                val iteratorValue = wordToPlay.length - 1
                                for (i in 1..iteratorValue) {
                                    incrementer -= 15 /** 157 - 15 = 142 */
                                    tilesPosToFill.add(incrementer) /** [157,142,] */
                                }
                            }
                        }
                            Log.d("@#@#@ tilesPosToFill", tilesPosToFill.toString())

                        val wordToPlayArr = wordToPlay.toCharArray()
                        val tilesImageToFill = ArrayList<Int>()
                        for (i in wordToPlayArr.indices) { /** BASU, B */
                            val idx = boardResources.getLetterEquivalent().indexOf(wordToPlayArr[i])
                            tilesImageToFill.add(boardResources.getLettersymbols()[idx])
                        }
                        /** pos 0,1 is good orientation already */
                        if (pos == 2 || pos == 3) {
                            tilesImageToFill.reverse()
                        }

                        /** unhighlight those buttons */
                        redrawTileSelectionEnd(false)

                        validBoardTilePlacers.clear()

                        /** place those letters on board */
                        for (i in tilesPosToFill.indices) {
                            val imgtilebtn = findViewById<ImageView>(tilesPosToFill[i])
                            val imgtilesrc = ContextCompat.getDrawable(applicationContext, tilesImageToFill[i])
                            imgtilebtn.setImageDrawable(imgtilesrc)
                        }


                        isFirstTurn = false
                            // isBoardEnable = false
                        isTilesEnable = true
                            imgvwBoardPlay.isEnabled = true
                            tileSelectionOrigin = 0
                        turnCounter++

                        inputTurnResetter()

                        // Toast.makeText(applicationContext, "index to substring $idxTurn", Toast.LENGTH_SHORT).show()

                        layoutBoardLetterPool.removeAllViews()
                        generateButtonTiles()

                        val turnUpdater = txtvwBoardTurn.text.toString().split(": ")[0] + ": " + turnCounter.toString()
                        txtvwBoardTurn.text = turnUpdater
                    }

                // }
                // else {

                    /** display tile number while disabled */
                    Toast.makeText(this, tileval, Toast.LENGTH_SHORT).show()

                // }

            })

            layoutCheckerBoard.addView(tilebtn)
        }


    }

    private fun generateButtonTiles() {
        for (i in 0..6) { // 11
            val letter = tiles_src.getLetterPoint1().random()
            val tilebtn = Button(this)

            tilebtn.apply {
                setAllCaps(false)
                layoutParams = LinearLayout.LayoutParams(150, 150)
                text = letter.toString()
                textSize = 15f
            }
            tilebtn.setOnClickListener(View.OnClickListener {
                if (isTilesEnable) {
                    val key = tilebtn.text.toString()
                    var currWord = txtvwBoardStatus.text.toString()
                    if (currWord.contains(", create word", ignoreCase = true)) {
                        currWord = ""
                    }
                    currWord += key
                    txtvwBoardStatus.text = currWord
                    selectedTilesToPlay.add(tilebtn)
                    layoutBoardLetterPool.removeView(tilebtn)
                }

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
                if (isTilesEnable) {
                    val key = tilebtn.text.toString()
                    var currWord = txtvwBoardStatus.text.toString()
                    if (currWord.contains(", create word", ignoreCase = true)) {
                        currWord = ""
                    }
                    currWord += key
                    txtvwBoardStatus.text = currWord
                    layoutBoardLetterPool.removeView(tilebtn)
                }

            })

            layoutBoardLetterPool.addView(tilebtn)
        }
    }





    private fun checkWordExist(word: String): Int {
        val dictlist = sqLiteHelper.onGetDataSpecific(word, true)
        if (dictlist.size != 0) {
            /**
             * Word is Existing in the Records,
             * dictlist[0].details
             * meaning of the `word`
             **/

            return wordPointer(word)

        }
        else {
            return -1
        }

    }
    private fun wordPointer(word: String): Int {
        val tilematrix = tiles_src.getLetterMatrix()
        val pointmatrix = tiles_src.getPointsMatrix()
        var points = 0
        for (i in word.indices) {

            for (j in tilematrix.indices) {
                var isBreakAtMatrix = false

                for (k in tilematrix[j].indices) {
                    val letterOfWord = word[i].toString().toLowerCase()
                    val fromMatrix = tilematrix[j][k].toString().toLowerCase()

                    if (letterOfWord == fromMatrix) {
                        isBreakAtMatrix = true
                        points += pointmatrix[j]
                        break;
                    }
                }
                if (isBreakAtMatrix) {
                    break
                }
            }

        }

        return points
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