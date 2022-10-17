package com.nairdnah.skrabol

class GameBoardResources {

    /** triple word score */
    private val tile_tripleword = arrayOf(
            1,
            8,
            15,
            106,
            120,
            211,
            225
    )
    /** double letter score */
    private val tile_doubleletter = arrayOf(
            4,
            12,
            37,
            39,
            46,
            53,
            60,
            93,
            97,
            99,
            103,
            109,
            117,
            123,
            127,
            129,
            133,
            166,
            173,
            180,
            187,
            189,
            214,
            222
    )
    /** double word score */
    private val tile_doubleword = arrayOf(
            17,
            29,
            33,
            43,
            49,
            57,
            65,
            71,
            155,
            161,
            169,
            177,
            183,
            193,
            197,
            209
    )
    /** triple letter score */
    private val tile_tripleletter = arrayOf(
            21,
            25,
            77,
            81,
            85,
            89,
            137,
            141,
            145,
            149,
            201,
            205
    )

    fun getTileTripleWord(): Array<Int> {
        return tile_tripleword
    }
    fun getTileDoubleLetter(): Array<Int> {
        return tile_doubleletter
    }
    fun getTileDoubleWord(): Array<Int> {
        return tile_doubleword
    }
    fun getTileTripleLetter(): Array<Int> {
        return tile_tripleletter
    }

    private val letter_pt1 = arrayOf('A', 'E', 'I', 'L', 'N', 'O', 'R', 'S', 'T', 'U')
    private val letter_ctr1 = arrayOf(9, 12, 9, 4, 6, 8, 6, 4, 6, 4)

    private val letter_pt2 = arrayOf('D', 'G')
    private val letter_ctr2 = arrayOf(4, 3)

    private val letter_pt3 = arrayOf('B', 'C', 'M', 'P')
    private val letter_ctr3 = arrayOf(2, 2, 2, 2)

    private val letter_pt4 = arrayOf('F', 'H', 'V', 'W', 'Y')
    private val letter_ctr4 = arrayOf(2, 2, 2, 2, 2)

    private val letter_pt5 = arrayOf('K')
    private val letter_ctr5 = arrayOf(1)

    private val letter_pt8 = arrayOf('J', 'X')
    private val letter_ctr8 = arrayOf(1, 1)

    private val letter_pt10 = arrayOf('Q', 'Z')
    private val letter_ctr10 = arrayOf(1, 1)

    fun getLetterPoint1(): Array<Char> {
        return letter_pt1
    }

    fun getHighLetterPoints(): Array<Array<Char>> {
        return arrayOf(
                letter_pt2,
                letter_pt3,
                letter_pt4,
                letter_pt5,
                letter_pt8,
                letter_pt10
        )
    }

}