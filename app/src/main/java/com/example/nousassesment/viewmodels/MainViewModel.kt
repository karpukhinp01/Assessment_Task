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
import com.example.nousassesment.repositories.MainApi
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.collections.ArrayList

class MainViewModel(private val repository: MainApi): ViewModel() {

    private val _programmeList = MutableLiveData<List<Item>>()
    val programmeList: LiveData<List<Item>> get() = _programmeList

    private val _loadStatus = MutableLiveData<LoadStatus>()
    val loadStatus: LiveData<LoadStatus> get()= _loadStatus

    init {
        getItemsList()
    }

    // This function is responsible for fetching a list of items from the repository and updating the
    // programmeList LiveData with the received items.
    fun getItemsList() {
        viewModelScope.launch {
            _loadStatus.value = LoadStatus.LOADING
            try {
                val result = repository.getItems()
                _programmeList.value = result.items
                _loadStatus.value = LoadStatus.SUCCESS
            } catch (e: Exception) {
                _loadStatus.value = LoadStatus.ERROR
                _programmeList.value = listOf()
            }
        }
    }

    // This function filters the items based on a given query string.
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

    // This function retrieves a bitmap image from a given URL.
    private fun getBitMap(urlS: String): Bitmap? {
        return try {
            val url = URL(urlS)
            BitmapFactory.decodeStream(url.openStream())
        } catch (e: Exception) {
            null
        }
    }

    // This function saves a given bitmap image to a specific location in the device's external cache directory.
    private fun saveBitMap(image: Bitmap, context: Context): Uri {

        // We first create a new directory called "my_images" in the external cache directory.
        // This is to ensure that the images are stored in a dedicated folder of the external cache.
        val cachePath = File(context.externalCacheDir, "my_images/")
        cachePath.mkdirs()

        // Next, we create a new file with the name "shared_media.png" in the newly created directory.
        // This file will be used to save the image.
        val file = File(cachePath, "shared_media.png")
        val fileOutputStream: FileOutputStream

        try {

            // We open a file output stream for the file and compress
            // the image with PNG format and 100% quality.
            fileOutputStream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            Log.d("saveBitMap", e.message.toString())
        }

        // Finally, we return the Uri of the saved file using the FileProvider.getUriForFile method.
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }

    // This function is responsible for creating an intent to send an email with an attachment.
    // It takes in two parameters, the item that needs to be shared and the context of the application.
    fun sendEmail(item:Item, context: Context): Intent {

        // First, it retrieves the image associated with the
        // item by calling the getBitMap function passing the item's imageUrl.
        val image = getBitMap(item.imageUrl)
        var uri: Uri? = null
        // Next, it saves the image to the external
        // cache directory by calling the saveBitMap function, passing the image and the context, if
        // the image is not null.
        if (image != null) {
            uri = saveBitMap(image, context)
        }

        // Then, it creates an Intent with the action Intent.ACTION_SEND
        // and sets the type to "message/rfc822" to indicate that the intent is for sending an email.
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"


        // The intent's extras are set with the item's title as the subject,
        // item's description as the text body, and the Uri of the saved image as the attachment.
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_SUBJECT, item.title)
        intent.putExtra(Intent.EXTRA_TEXT, item.description)
        return intent
    }
}