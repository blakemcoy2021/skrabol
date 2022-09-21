package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nairdnah.testmodules.DictionaryAdapter
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream

class DictionaryActivity : AppCompatActivity() {

    private lateinit var btnRun : ImageView
    private lateinit var btnDelete : ImageView
    private lateinit var btnSearch : Button
    private lateinit var btnUpdate : Button

    private lateinit var edxWord : EditText
    private lateinit var edxDetails : EditText
    private lateinit var spnCategory: Spinner

    private lateinit var sqLiteHelper: SQLiteHelper
    
    private lateinit var rcvwDict: RecyclerView
    private var dictAdapter: DictionaryAdapter? = null

    val categories = arrayOf("-Select Category-", "Food", "Action", "Kitchen", "House", "Outside")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        sqLiteHelper = SQLiteHelper(this)
        // sqLiteHelper.onDropTable()

        initView()
        initSpinner()
        initRecyclerView()

        getDataAllDictionary()

        println(sqLiteHelper.onGetDataCount())

        btnSearch.setOnClickListener {
            val word = edxWord.text.toString()
            if (word.isEmpty() || word == "") {
                Toast.makeText(this, "Input Word Required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //search db here

        }
        btnUpdate.setOnClickListener {
            val word = edxWord.text.toString()
            val details = edxDetails.text.toString()

        }
        btnRun.setOnClickListener {
            readJson()
        }
    }

    private fun getDataAllDictionary() {
        val dictlist = sqLiteHelper.onGetAllTestData()
        dictAdapter?.addItems(dictlist)
        Toast.makeText(this, "Record Count: " + dictlist.size, Toast.LENGTH_SHORT).show()

    }

    private fun readJson() {
        val jsonRaw: String?

        try {
            val istream : InputStream = assets.open("sampledict.json")
            jsonRaw = istream.bufferedReader().use {
                it.readText()
            }
            val jsonPreArr = JSONArray(jsonRaw)

            val jsonObj = jsonPreArr.getJSONObject(0)
            val jsonArr = jsonObj.getJSONArray("table_data")
            println("@#@#@#@#@#@# " + jsonArr.length())

                /** temp below block */
                // var printstr = ""
                var dictList = arrayListOf<DictionaryModel>()

            var isError = false
            var ctr = 0
            for (i in 0 until jsonArr.length() - 1) {
                val jsonData = jsonArr.getJSONObject(i)
                val jsonWord = jsonData.getString("key_0")
                val jsonDetails = jsonData.getString("key_1")

                val currId = sqLiteHelper.onGetDataCount() + 1
                    val dictmodel = DictionaryModel(id = currId, word = jsonWord, details = jsonDetails, category = "n/a")

                val status = sqLiteHelper.onInsertStatement(dictmodel)
                if (status > -1) {
                    ctr++
                }
                else {
                    isError = true
                    break
                }
                dictList.add(dictmodel)

                /** temp below block */
                // printstr += "$jsonWord "
            }
            if (isError) return

            /** temp below block */
            println("@#@#@#@#@#@#@#@# $ctr")
            // println(dictList)
            // Toast.makeText(this, printstr, Toast.LENGTH_SHORT).show()


            dictAdapter?.addItems(dictList)

        } catch (iox: IOException) {
            Toast.makeText(this, "Input/Output Problem", Toast.LENGTH_SHORT).show()
            iox.printStackTrace()
        } catch (jsonx: JSONException) {
            Toast.makeText(this, "JSON Parse Problem", Toast.LENGTH_SHORT).show()
            jsonx.printStackTrace()
        }
    }

    private fun initSpinner() {
        val spnAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spnCategory.adapter = spnAdapter
        spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext, "Selected Category is " + categories[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Nothing Selected", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initRecyclerView() {
        rcvwDict.layoutManager = LinearLayoutManager(this)
        rcvwDict.setHasFixedSize(true)
        dictAdapter = DictionaryAdapter()
        rcvwDict.adapter = dictAdapter
    }

    private fun initView() {
        btnRun = findViewById(R.id.btn_run)
        btnDelete = findViewById(R.id.btn_delete)
        btnSearch = findViewById(R.id.btn_search)
        btnUpdate = findViewById(R.id.btn_update)

        edxWord = findViewById(R.id.edx_word)
        edxDetails = findViewById(R.id.edx_details)

        rcvwDict = findViewById(R.id.rcvwDictionary)
        spnCategory = findViewById(R.id.spn_category)
    }
}