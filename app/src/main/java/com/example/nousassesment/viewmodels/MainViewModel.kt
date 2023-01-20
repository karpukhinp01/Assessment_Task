package com.example.nousassesment.viewmodels

import android.app.Application
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
        for (i in programmeList.value!!) {
            if (i.title.lowercase().contains(query.lowercase())
                || i.description.lowercase().contains(query.lowercase()))
                filteredList.add(i)
        }
        return filteredList
    }
}