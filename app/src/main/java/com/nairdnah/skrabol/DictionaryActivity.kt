package com.nairdnah.skrabol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nairdnah.testmodules.DictionaryAdapter
import kotlinx.coroutines.*
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

    val categories = arrayOf("-Select Category-", "Food", "Action", "Kitchen", "House", "Outside") //, "In Love With")

    private var dict: DictionaryModel? = null
    private var isLoading: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        sqLiteHelper = SQLiteHelper(this)
        // sqLiteHelper.onDropTable()

        initView()
        initSpinner()
        initRecyclerView()

        val isThereData = sqLiteHelper.onGetDataCount()
        if (isThereData == 0L) {
            var data = ArrayList<DictionaryModel>()
            val loading = DialogLoading(this)
            loading.startLoading()

            GlobalScope.launch(Dispatchers.Default) {
                data = readJson()
                loading.isDismiss()
                withContext(Dispatchers.Main) {
                    dictAdapter?.addItems(data)
                }
            }

        }
        getDataAllDictionary()

        println(sqLiteHelper.onGetDataCount())

        btnSearch.setOnClickListener {
            val word = edxWord.text.toString()
            if (word.trim().isEmpty() || word == "") {
                getDataAllDictionary()
                Toast.makeText(this, "Input Word Required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            getDataSpecific(word)
        }
        btnUpdate.setOnClickListener {
            updateDataTest()
        }
        btnDelete.setOnClickListener {
            if (dict == null) {
                Toast.makeText(this, "No Record Selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                deleteDataTest(dict!!.id)
            }
        }
        btnRun.setOnClickListener {
            Toast.makeText(this, "Feature N/A", Toast.LENGTH_SHORT).show()
        }
        dictAdapter?.setOnClickItem {
            Toast.makeText(this, it.word, Toast.LENGTH_SHORT).show()
            edxWord.setText(it.word)
            edxDetails.setText(it.details)
            val cat = it.category
            if (cat != "n/a") {
                var pos = 0
                for (i in categories) {
                    if (i.equals(cat)) {
                        break
                    }
                    pos++
                }
                spnCategory.setSelection(pos)
            }
            else {
                spnCategory.setSelection(0)
            }

            dict = it
        }
    }

    private fun getDataAllDictionary() {
        val dictlist = sqLiteHelper.onGetAllTestData()
        dictAdapter?.addItems(dictlist)
        Toast.makeText(this, "Record Count: " + dictlist.size, Toast.LENGTH_SHORT).show()

    }

    private fun getDataSpecific(word: String) {
        val dictlist = sqLiteHelper.onGetDataSpecific(word, false)
        dictAdapter?.addItems(dictlist)
    }

    private fun updateDataTest() {
        if (dict == null) {
            Toast.makeText(this, "Select a record to be changed.", Toast.LENGTH_SHORT).show()
            return
        }

        val word = edxWord.text.toString()
        val details = edxDetails.text.toString()
        var category = spnCategory.selectedItem.toString()
        if (category == "-Select Category-") {
            category = "n/a"
        }

        if (word == dict?.word && details == dict?.details && category == dict?.category) {
            Toast.makeText(this, "Records not changed.", Toast.LENGTH_SHORT).show()
            return
        }

        val test = DictionaryModel(id = dict!!.id, word = word, details = details, category = category)
        val status = sqLiteHelper.onUpdateStatement(test)
        if (status > -1) {
            clearTextFields()
            getDataAllDictionary()
        }
        else {
            Toast.makeText(this, "Record Update Failed!", Toast.LENGTH_SHORT).show()
        }
        dict = null
    }

    private fun deleteDataTest(id: Long) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are You sure yu want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            sqLiteHelper.onDeleteStatement(id)
            clearTextFields()
            getDataAllDictionary()
            dict = null
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }



    private fun readJson(): ArrayList<DictionaryModel> {
            /** temp below block */
        // var printstr = ""

        var dictList = arrayListOf<DictionaryModel>()

        val jsonRaw: String?
        try {
            val istream : InputStream = assets.open("sampledict.json")
            jsonRaw = istream.bufferedReader().use {
                it.readText()
            }
            val jsonPreArr = JSONArray(jsonRaw)

            val jsonObj = jsonPreArr.getJSONObject(0)
            val jsonArr = jsonObj.getJSONArray("table_data")

            var isError = false
            var ctr = 0
            for (i in 0 until jsonArr.length() - 1) {
                val jsonData = jsonArr.getJSONObject(i)
                val jsonDetails = jsonData.getString("key_1")

                val jsonPronounce : String = jsonData.getString("key_0")
                var jsonWord = jsonPronounce

                    /** code block to clear special characters of the word */
                val specialCharArr = arrayListOf('’', '-', '/', '[', ']', ':', '?')
                for (i in specialCharArr) {
                    if (jsonWord.contains(i)) {
                        jsonWord = jsonWord.replace("$i", "")
                    }
                }

                val currId = sqLiteHelper.onGetDataCount() + 1
                val dictmodel = DictionaryModel(
                        id = currId, word = jsonWord, pronounce = jsonPronounce, details = jsonDetails, category = "n/a")

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
                    // val isInclude : Boolean = jsonWord.toString().contains('’')
                    // printstr += "$jsonWord "  // [$isInclude] "
            }
            if (isError) {
                dictList = arrayListOf<DictionaryModel>()
                return dictList
            }

                    /** temp below block */
                // println("@#@#@#@#@#@#@#@# $printstr")
                // println("@#@#@#@#@#@#@#@# $ctr")
                // println(dictList)
                // Toast.makeText(this, printstr, Toast.LENGTH_SHORT).show()
                // dictAdapter?.addItems(dictList)

        } catch (iox: IOException) {
            Toast.makeText(this, "Input/Output Problem", Toast.LENGTH_SHORT).show()
            iox.printStackTrace()
        } catch (jsonx: JSONException) {
            Toast.makeText(this, "JSON Parse Problem", Toast.LENGTH_SHORT).show()
            jsonx.printStackTrace()
        }

        return dictList
    }

    private fun clearTextFields() {
        edxWord.setText("")
        edxDetails.setText("")
        spnCategory.setSelection(0)
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
                if (position != 0) {
                    Toast.makeText(applicationContext, "Selected Category is " + categories[position], Toast.LENGTH_SHORT).show()
                }

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