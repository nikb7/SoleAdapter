package com.nikb7.soleadapterexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.nikb7.soleadapter.OnRecyclerItemClickListener
import com.nikb7.soleadapter.SoleAdapter
import com.nikb7.soleadapter.StableId
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnRecyclerItemClickListener {

    override fun onRecyclerInnerItemClick(view: View, obj: StableId) {
        when {
            view.id == R.id.deleteBtn && obj is TextModel -> {
                Toast.makeText(this, "List inner item: ${obj.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRecyclerItemClick(obj: StableId) {
        when {
            obj is TextModel -> Toast.makeText(this, obj.text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRecyclerItemLongPressed(obj: StableId) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listAdapter.apply {
            recyclerView.adapter = this
            submitList(getRandomStringList())
        }
    }

    private fun getRandomStringList() = Locale.getAvailableLocales().map {
        TextModel(it.displayName)
    }

    private val listAdapter by lazy {
        SoleAdapter(
            viewMap = mapOf(TextModel::class to R.layout.text_cell),
            listener = this
        )
    }
}

data class TextModel(val text: String) : StableId {
    override fun getStableId(): String = text
}
