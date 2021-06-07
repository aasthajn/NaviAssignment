package com.example.githubinfoservice.pullrequestservice.model

import com.example.githubinfoservice.Constants.OWNER
import com.example.githubinfoservice.Constants.PER_PAGE
import com.example.githubinfoservice.Constants.REPO_NAME
import com.example.githubinfoservice.Constants.SORT
import com.example.githubinfoservice.Constants.STATE
import com.example.githubinfoservice.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ModelRepository: BaseRepository() {

    private val client: Webservice = RetrofitClient.webservice

    suspend fun getClosedPullRequests(page_No:Int): Resource<Any> {
        return enqueue(
            call = {
                withContext(Dispatchers.IO) {
                    client.getPullRequests(OWNER, REPO_NAME, STATE, PER_PAGE, page_No, SORT)
                    //client.getPullRequestsMock(OWNER, REPO_NAME, STATE, PER_PAGE, page_No, SORT)
                }
            }
        )
    }

}