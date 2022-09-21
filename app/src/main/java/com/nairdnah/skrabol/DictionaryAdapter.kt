package com.nairdnah.testmodules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nairdnah.skrabol.DictionaryModel
import com.nairdnah.skrabol.R

class DictionaryAdapter : RecyclerView.Adapter<DictionaryAdapter.TestViewHolder>() {

    private var testlist: ArrayList<DictionaryModel> = ArrayList()

    fun addItems(items: ArrayList<DictionaryModel>) {
        this.testlist = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TestViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_dictionary_adapter, parent, false)
    )

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = testlist[position]
        holder.bindView(test)
    }

    override fun getItemCount(): Int {
        return testlist.size
    }

    class TestViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var word = view.findViewById<TextView>(R.id.txtvwDataWord)
        private var details = view.findViewById<TextView>(R.id.txtvwDataDetails)

        fun bindView(test: DictionaryModel) {
            word.text = test.word
            details.text = test.details

        }
    }


}