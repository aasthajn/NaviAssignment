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

    private val _isReachedEnd: MutableLiveData<Boolean> = MutableLiveData()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isScrolledDown: MutableLiveData<Boolean> = MutableLiveData()

    private val _isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData()

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    private val githubRepo = ModelRepository

    private var pageNo: Int = 1

    fun refreshDataFromRepository() {
        if ((isScrolledDown() || listResponse.value == null) && (_isNetworkAvailable.value == true)) {
            viewModelScope.launch {
                _isLoading.value = true
                when (val resultResponse = githubRepo.getClosedPullRequests(pageNo)) {
                    is Resource.Success -> {
                        _isScrolledDown.value = false
                        _isLoading.value = false
                        val list = resultResponse.output as List<PullRequestData>
                        if (list.size >= 0 && list.size >= Constants.PER_PAGE) {
                            pageNo += 1
                            _listResponse.postValue(list)
                        } else {
                            _listResponse.postValue(list)
                            _isReachedEnd.value = true
                        }
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        resultResponse.errorMessage?.message.let {
                            _errorLiveData.postValue(it)
                        }
                    }
                }

            }
        }
    }

    fun setScrolledDown() {
        _isScrolledDown.value = true
    }

    fun setNetworkAvailable(status: Boolean) {
        _isNetworkAvailable.value = status
    }

    fun getLoading(): Boolean {
        return _isLoading.value ?: false
    }

    fun getReachedEnd(): Boolean {
        return _isReachedEnd.value ?: false
    }

    private fun isScrolledDown(): Boolean {
        return _isScrolledDown.value ?: false
    }

    fun resetErrorLiveData() {
        _errorLiveData.value = null
    }
}