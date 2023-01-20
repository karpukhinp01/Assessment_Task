package com.example.nousassesment.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nousassesment.FirstFragment
import com.example.nousassesment.RecyclerViewAdapter
import com.example.nousassesment.data.Item
import com.example.nousassesment.network.NousApi
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    private val _programmeList = MutableLiveData<List<Item>>()
    val programmeList: LiveData<List<Item>> get() = _programmeList

    init {
        getItemsList()
    }

    private fun getItemsList() {
        viewModelScope.launch {
            val result = NousApi.retrofitService.getItems()
            _programmeList.value = result.items
        }
    }

    fun filter(query: String): ArrayList<Item> {
        val filteredList = ArrayList<Item>()
        if (programmeList.value != null) {
            for (i in programmeList.value!!) {
                if (i.title.lowercase().contains(query.lowercase())
                    || i.description.lowercase().contains(query.lowercase()))
                    filteredList.add(i)
            }
        }
        return filteredList
    }

    fun sendEmail(item:Item): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, item.title)
        intent.putExtra(Intent.EXTRA_TEXT, item.description)
        intent.putExtra("image_url", item.imageUrl)
        return intent
    }
}