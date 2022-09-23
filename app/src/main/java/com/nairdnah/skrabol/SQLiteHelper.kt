package com.nairdnah.skrabol

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    companion object {
        private const val DB_VER = 1
        private const val DB_NAME = "skrabol_dict.db"
        private const val TBL_DICT = "tbl_dict"
        private const val col1 = "id"
        private const val col2 = "word"
        private const val col3 = "pronounce"
        private const val col4 = "details"
        private const val col5 = "category"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblTestDb = ("CREATE TABLE $TBL_DICT ($col1 LONG PRIMARY KEY, $col2 TEXT, $col3 TEXT, $col4 TEXT, $col5 TEXT) ")
        db?.execSQL(createTblTestDb)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS $TBL_DICT")
            onCreate(db)
        }
    }

    fun onDropTable() {
        val db = this.readableDatabase
        db?.execSQL("DROP TABLE IF EXISTS $TBL_DICT")
        onCreate(db)
        db.close()
    }

    fun onGetDataCount() : Long {
        val db = this.readableDatabase
        val count : Long = DatabaseUtils.queryNumEntries(db, TBL_DICT)
        db.close()
        return count
    }

    fun onGetDataSpecific(name: String, explicit: Boolean): ArrayList<DictionaryModel> {
        val dictlist : ArrayList<DictionaryModel> = ArrayList()
        var selectQuery = "SELECT * FROM $TBL_DICT WHERE "
        if (explicit) {
            selectQuery += "$col2='$name' "
        }
        else {
            selectQuery += "$col2 LIKE '%$name%' "
        }

        val db = this.readableDatabase

        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e : Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Long
        var word : String
        var pronounce : String
        var details : String
        var category : String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(cursor.getColumnIndex(col1))
                word = cursor.getString(cursor.getColumnIndex(col2))
                pronounce = cursor.getString(cursor.getColumnIndex(col3))
                details = cursor.getString(cursor.getColumnIndex(col4))
                category = cursor.getString(cursor.getColumnIndex(col5))

                val test = DictionaryModel(id, word, pronounce, details, category)
                dictlist.add(test)
            } while (cursor.moveToNext())
        }

        return dictlist
    }

    @SuppressLint("Recycle")
    fun onGetAllTestData() : ArrayList<DictionaryModel> {
        val dictlist : ArrayList<DictionaryModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_DICT"
        val db = this.readableDatabase

        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e : Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Long
        var word : String
        var pronounce : String
        var details : String
        var category : String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(cursor.getColumnIndex(col1))
                word = cursor.getString(cursor.getColumnIndex(col2))
                pronounce = cursor.getString(cursor.getColumnIndex(col3))
                details = cursor.getString(cursor.getColumnIndex(col4))
                category = cursor.getString(cursor.getColumnIndex(col5))

                val test = DictionaryModel(id, word, pronounce, details, category)
                dictlist.add(test)
            } while (cursor.moveToNext())
        }

        return dictlist
    }


    fun onInsertStatement(dict : DictionaryModel) : Long {
        val db = this.writableDatabase

        val obj = ContentValues()
        obj.put(col1, dict.id)
        obj.put(col2, dict.word)
        obj.put(col3, dict.pronounce)
        obj.put(col4, dict.details)
        obj.put(col5, dict.category)

        val success = db.insert(TBL_DICT, null, obj)
        db.close()
        return success
    }

    fun onUpdateStatement(test: DictionaryModel): Int {
        val db = this.writableDatabase

        val obj = ContentValues()
        obj.put(col1, test.id)
        obj.put(col2, test.word)
        obj.put(col3, test.pronounce)
        obj.put(col4, test.details)
        obj.put(col5, test.category)

        val success = db.update(TBL_DICT, obj, "id=" + test.id, null)
        db.close()
        return success
    }

    fun onDeleteStatement(id: Long) : Int {
        val db = this.writableDatabase

        val obj = ContentValues()
        obj.put(col1, id)

        val success = db.delete(TBL_DICT, "id=$id", null)
        db.close()
        return success
    }


}