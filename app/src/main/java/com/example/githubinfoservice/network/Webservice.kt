package com.example.githubinfoservice.network

import com.example.githubinfoservice.Constants.BASE_URL
import com.example.githubinfoservice.pullrequestservice.model.datamodels.PullRequestData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Webservice {

    @GET("$BASE_URL{owner}/{repo_name}/pulls")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo_name") repo_name: String,
        @Query("state") state: String,
        @Query("per_page") pageSize: Int,
        @Query("page") pageNo: Int
    ): List<PullRequestData>
}