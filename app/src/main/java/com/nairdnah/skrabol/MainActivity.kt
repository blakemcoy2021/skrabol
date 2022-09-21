package com.nairdnah.skrabol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

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
    }

    private fun initView() {
        btnDict = findViewById(R.id.btn_dict)
        btnGame = findViewById(R.id.btn_game)
        btnBoard = findViewById(R.id.btn_board)
    }
}