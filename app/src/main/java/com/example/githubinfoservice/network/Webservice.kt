package com.example.githubinfoservice.network

import com.example.githubinfoservice.pullrequestservice.model.datamodels.RepoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Webservice {

    @GET(".")
    suspend fun searchNews(
        @Query("owner") owner: String,
        @Query("repo") repo: String,
        @Query("state") state: String,
        @Query("per_page") pageSize: Int,
        @Query("page_no") pageNo:Int
    ): RepoResponse
}