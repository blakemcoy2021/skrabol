package com.nairdnah.testmodules

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nairdnah.skrabol.DictionaryModel
import com.nairdnah.skrabol.R

class DictionaryAdapter : RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>() {

    private var dictlist: ArrayList<DictionaryModel> = ArrayList()
    private var onClickItem: ((DictionaryModel) -> Unit)? = null

    fun addItems(items: ArrayList<DictionaryModel>) {
        this.dictlist = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (DictionaryModel) -> Unit) {
        this.onClickItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DictionaryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_dictionary_adapter, parent, false)
    )

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        val test = dictlist[position]
        holder.bindView(test)
        holder.itemView.setOnClickListener { onClickItem?.invoke(test) }
    }

    override fun getItemCount(): Int {
        return dictlist.size
    }

    class DictionaryViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var word = view.findViewById<TextView>(R.id.txtvwDataWord)
        private var details = view.findViewById<TextView>(R.id.txtvwDataDetails)

        fun bindView(test: DictionaryModel) {
            word.text = test.word
            details.text = test.details

        }
    }


}