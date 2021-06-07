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

    private val _isReachedEnd: MutableLiveData<Boolean> = MutableLiveData()
    val isReachedEnd: LiveData<Boolean>
        get() = _isReachedEnd

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isScrolledDown: MutableLiveData<Boolean> = MutableLiveData()
    val isScrolledDown: LiveData<Boolean>
        get() = _isScrolledDown

    private val githubRepo = ModelRepository

    var PAGE_NO: Int = 1

    fun refreshDataFromRepository() {
        if(isScrolledDown()|| listResponse.value ==null){
        viewModelScope.launch {
            _isLoading.value = true
            when (val resultResponse = githubRepo.getClosedPullRequests(PAGE_NO)) {
                is Resource.Success -> {
                    _isScrolledDown.value = false
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

        }}
    }

    fun resetList() {
        PAGE_NO = 1
        _listResponse.value = null
        _isReachedEnd.value = false
    }

    fun resetLoading(){
        _isLoading.value = false
    }

    fun resetReachedEnd() {
        _isReachedEnd.value = false
    }

    fun setScrolledDown(){
        _isScrolledDown.value = true
    }

    fun getLoading():Boolean{
       return _isLoading.value?:false
    }

    fun getReachedEnd():Boolean{
        return _isReachedEnd.value?:false
    }

    fun isScrolledDown():Boolean {
        return  _isScrolledDown.value?:false
    }
}