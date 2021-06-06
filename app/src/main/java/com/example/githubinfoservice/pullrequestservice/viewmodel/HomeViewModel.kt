package com.example.githubinfoservice.pullrequestservice.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.githubinfoservice.Constants
import com.example.githubinfoservice.network.ErrorResponse
import com.example.githubinfoservice.network.Resource
import com.example.githubinfoservice.pullrequestservice.model.ModelRepository
import com.example.githubinfoservice.pullrequestservice.model.datamodels.PullRequestData
import kotlinx.coroutines.launch

class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    private val _listResponse: MutableLiveData<List<PullRequestData>> = MutableLiveData()
    val listResponse: LiveData<List<PullRequestData>>
        get() = _listResponse

    private val _errorLiveData = MutableLiveData<ErrorResponse>()

    val _isReachedEnd: MutableLiveData<Boolean> = MutableLiveData()
    val isReachedEnd: LiveData<Boolean>
        get() = _isReachedEnd

    val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val githubRepo = ModelRepository

    var PAGE_NO: Int = 1

    fun refreshDataFromRepository() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val resultResponse = githubRepo.getClosedPullRequests(PAGE_NO)) {
                is Resource.Success -> {
                    _isLoading.value = false
                    val list = resultResponse.output as List<PullRequestData>
                    if (list.size >= 0 && list.size >= Constants.PER_PAGE) {
                        PAGE_NO += 1
                        _listResponse.postValue(list)
                    } else {
                        _listResponse.postValue(list)
                        _isReachedEnd.value = true
                    }
                }
                is Resource.Error -> {
                    _isLoading.value = false
                    _errorLiveData.postValue(resultResponse.errorMessage as ErrorResponse)
                }
            }

        }
    }

    fun resetList() {
        PAGE_NO = 1
        _listResponse.value = null
        _isReachedEnd.value = false
    }
}