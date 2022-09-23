package com.nairdnah.skrabol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var btnDict : Button
    private lateinit var btnGame : Button
    private lateinit var btnBoard : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        btnDict.setOnClickListener {
            val intent = Intent(this@MainActivity, DictionaryActivity::class.java)
            startActivity(intent)
        }
        btnGame.setOnClickListener {
            val intent = Intent(this@MainActivity, GameWordActivity::class.java)
            startActivity(intent)
        }
        btnBoard.setOnClickListener {
            val msg = "Board is still in-progress" // ""Hi Ms. Attorney Jamvee :)"
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        btnDict = findViewById(R.id.btn_dict)
        btnGame = findViewById(R.id.btn_game)
        btnBoard = findViewById(R.id.btn_board)
    }
}