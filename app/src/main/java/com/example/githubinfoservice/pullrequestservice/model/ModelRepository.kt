package com.example.githubinfoservice.pullrequestservice.model

import android.app.Application
import com.example.githubinfoservice.Constants
import com.example.githubinfoservice.Constants.OWNER
import com.example.githubinfoservice.Constants.PAGE_NO
import com.example.githubinfoservice.Constants.PER_PAGE
import com.example.githubinfoservice.Constants.REPO_NAME
import com.example.githubinfoservice.Constants.STATE
import com.example.githubinfoservice.network.BaseRepository
import com.example.githubinfoservice.network.Resource
import com.example.githubinfoservice.network.Webservice
import com.example.githubinfoservice.network.webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ModelRepository(application: Application) : BaseRepository(application) {

    private val client: Webservice = webservice

    suspend fun getClosedPullRequests(): Resource<Any> {
        return enqueue(
            call = {
                withContext(Dispatchers.IO) {
                    client.getPullRequests(OWNER, REPO_NAME, STATE, PER_PAGE, PAGE_NO)
                }
            }
        )
    }

}