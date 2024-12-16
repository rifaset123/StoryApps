package com.example.storyapps.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapps.data.response.ListStory
import com.example.storyapps.data.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStory>() {
    override fun getRefreshKey(state: PagingState<Int, ListStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStory> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(position, params.loadSize)
            val responseData = response.listStory?.filterNotNull() ?: emptyList()
            Log.d("StoryPagingSource", "Loaded ${responseData.size} stories at position $position")
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Error loading stories", exception)
            return LoadResult.Error(exception)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}