package com.dicoding.newsapp.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.newsapp.DataDummy
import com.dicoding.newsapp.LiveDataTestUtil.getOrAwaitValue
import com.dicoding.newsapp.data.NewsRepository
import com.dicoding.newsapp.data.local.entity.NewsEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.dicoding.newsapp.data.Result
import org.junit.Rule
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var newsRepository : NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private val dummyNews = DataDummy.generateDummyNewsEntity()

    @Before
    fun setUp() {
        newsViewModel = NewsViewModel(newsRepository)
    }

    @Test
    fun `when Get HeadlineNews Should Not Null and Return Success`() {
        val observer =  Observer<Result<List<NewsEntity>>> {}
        try {
            val expectedNews = MutableLiveData<Result<List<NewsEntity>>>()
            expectedNews.value = Result.Success(dummyNews)

            `when`(newsViewModel.getHeadlineNews()).thenReturn(expectedNews)

            val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()

            Mockito.verify(newsRepository).getHeadlineNews()

            Assert.assertNotNull(actualNews)
            Assert.assertTrue(actualNews is Result.Success)
            Assert.assertEquals(dummyNews.size, (actualNews as Result.Success).data.size)
        } finally {
            newsViewModel.getHeadlineNews().removeObserver(observer)
        }
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val headlineNews = MutableLiveData<Result<List<NewsEntity>>>()
        headlineNews.value = Result.Error("Error")

        `when`(newsViewModel.getHeadlineNews()).thenReturn(headlineNews)

        val actualNews = newsViewModel.getHeadlineNews().getOrAwaitValue()

        Mockito.verify(newsRepository).getHeadlineNews()

        Assert.assertNotNull(actualNews)
        Assert.assertTrue(actualNews is Result.Error)
    }

}