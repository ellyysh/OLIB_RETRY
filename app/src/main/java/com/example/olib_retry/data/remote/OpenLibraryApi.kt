package com.example.olib_retry.data.remote



import com.example.olib_retry.data.model.SearchResponse
import com.example.olib_retry.data.model.WorkDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {

    @GET("search.json")
    suspend fun searchWorks(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): SearchResponse

    @GET("works/{workId}.json")
    suspend fun getWorkDetail(
        @Path("workId") workId: String
    ): WorkDetailDto
}