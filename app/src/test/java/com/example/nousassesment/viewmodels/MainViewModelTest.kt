package com.example.nousassesment.viewmodels

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import com.example.nousassesment.data.Item
import com.example.nousassesment.data.ItemList
import com.example.nousassesment.data.LoadStatus
import com.example.nousassesment.repositories.MainApi
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule

import org.junit.Test


class MainViewModelTest {

    val dispatcher = TestCoroutineDispatcher()

    val item1 = Item("id1", "title1", "description1", "https://example.com/test_item1.png")
    val item2 = Item("id2", "title2", "description2", "https://example.com/test_item2.png")
    val item3 = Item("id3", "title3", "description3", "https://example.com/test_item3.png")
    val itemList = listOf(item1, item2, item3)


    @Test
    fun `filter should return filtered items based on query`() {
        Dispatchers.setMain(dispatcher)
        val query = "1"


        val repository = mockk<MainApi>()
        coEvery { repository.getItems() } returns ItemList(itemList)

        val viewModel = spyk(MainViewModel(repository))
        every { viewModel.programmeList.value } returns listOf(item1, item2, item3)

        // Act
        val filteredList = viewModel.filter(query)

        // Assert
        assertEquals(1, filteredList.size)
        assertEquals("title1", filteredList[0].title)
        assertEquals("description1", filteredList[0].description)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `getItemsList should update programmeList LiveData`() {
        Dispatchers.setMain(dispatcher)

        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk()

        val repository = mockk<MainApi>()
        coEvery { repository.getItems() } returns ItemList(listOf(item1, item2, item3))

        val viewModel = MainViewModel(repository)

        //Act
        viewModel.getItemsList()
        viewModel.programmeList.observeForever {  }
        viewModel.loadStatus.observeForever {  }

        // Assert
        assertEquals(viewModel.programmeList.value, itemList)
        assertEquals(viewModel.loadStatus.value, LoadStatus.SUCCESS)

    }
}