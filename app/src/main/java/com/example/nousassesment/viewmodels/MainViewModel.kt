package com.example.nousassesment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nousassesment.data.Item
import com.example.nousassesment.network.NousApi
import kotlinx.coroutines.launch

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
}