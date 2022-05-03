package com.amary.sisosmed.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amary.sisosmed.core.source.remote.network.ApiResult
import com.amary.sisosmed.core.source.remote.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.IOException

open class BasePagingSource<T: Any>(
    val remoteSource: suspend (Int) -> Flow<ApiResult<ApiResponse<List<T>>>>
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            when(val result = remoteSource(page).first()){
                is ApiResult.Success -> {
                    LoadResult.Page(
                        data = result.data.data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = page + 1
                    )
                }
                is ApiResult.Error -> {
                    val responseBody = "$${result.codeError} - ${result.errorMessage}"
                    throw Exception(responseBody)
                }
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object{
        private const val STARTING_PAGE_INDEX = 1
        private const val DEFAULT_PAGE_SIZE = 30

        fun <T: Any> build(
            pageSize: Int = DEFAULT_PAGE_SIZE,
            enablePlaceholders: Boolean = false,
            remoteSource: suspend (Int) -> Flow<ApiResult<ApiResponse<List<T>>>>
        ): Pager<Int, T> = Pager(
            config = PagingConfig(enablePlaceholders = enablePlaceholders, pageSize = pageSize),
            pagingSourceFactory = { BasePagingSource(remoteSource) }
        )

    }

}