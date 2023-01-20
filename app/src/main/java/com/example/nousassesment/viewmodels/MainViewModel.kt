package com.example.nousassesment.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nousassesment.data.Item
import com.example.nousassesment.data.LoadStatus
import com.example.nousassesment.network.NousApi
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    private val _programmeList = MutableLiveData<List<Item>>()
    val programmeList: LiveData<List<Item>> get() = _programmeList

    private val _loadStatus = MutableLiveData<LoadStatus>()
    val loadStatus: LiveData<LoadStatus> get()= _loadStatus

    init {
        getItemsList()
    }

    fun getItemsList() {
        viewModelScope.launch {
            _loadStatus.value = LoadStatus.LOADING
            try {
                val result = NousApi.retrofitService.getItems()
                _programmeList.value = result.items
                _loadStatus.value = LoadStatus.SUCCESS
            } catch (e: Exception) {
                _loadStatus.value = LoadStatus.ERROR
                _programmeList.value = listOf()
            }
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
    fun getBitMap(urlS: String): Bitmap? {
        try {
            val url = URL(urlS)
            return BitmapFactory.decodeStream(url.openStream())
        } catch (e: Exception) {
            Log.d("Bitmap", e.message.toString())
            return null
        }
    }

    private fun saveBitMap(image: Bitmap, context: Context): Uri {

        val cachePath = File(context.externalCacheDir, "my_images/")
        cachePath.mkdirs()

        val file = File(cachePath, "shared_media.png")
        val fileOutputStream: FileOutputStream

        try {
            fileOutputStream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            Log.d("Bitmap", e.message.toString())
        }
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }

    fun sendEmail(item:Item, context: Context): Intent {

        val image = getBitMap(item.imageUrl)
        var uri: Uri? = null
        if (image != null) {
            uri = saveBitMap(image, context)
        }

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_SUBJECT, item.title)
        intent.putExtra(Intent.EXTRA_TEXT, item.description)
        return intent
    }
}