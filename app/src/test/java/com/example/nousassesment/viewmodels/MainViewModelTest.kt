package com.example.nousassesment.viewmodels

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import org.junit.Assert.*

import com.example.nousassesment.data.Item
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain

import org.junit.Test
import java.io.File
import java.io.FileOutputStream

class MainViewModelTest {

    val dispatcher = TestCoroutineDispatcher()

    @Test
    fun `filter should return filtered items based on query`() {
        Dispatchers.setMain(dispatcher)
        val query = "1"

        val item1 = Item("id1", "title1", "description1", "https://example.com/test_item1.png")
        val item2 = Item("id2", "title2", "description2", "https://example.com/test_item2.png")
        val item3 = Item("id3", "title3", "description3", "https://example.com/test_item3.png")

        val viewModel = spyk(MainViewModel())
        every { viewModel.programmeList.value } returns listOf(item1, item2, item3)

        // Act
        val filteredList = viewModel.filter(query)

        // Assert
        assertEquals(1, filteredList.size)
        assertEquals("title1", filteredList[0].title)
        assertEquals("description1", filteredList[0].description)
    }
}