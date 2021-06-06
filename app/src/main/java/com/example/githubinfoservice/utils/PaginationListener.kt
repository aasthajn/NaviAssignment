package com.example.githubinfoservice.utils

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubinfoservice.Constants.PER_PAGE

abstract class PaginationListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = recyclerView.childCount
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
        val totalItemCount = linearLayoutManager?.itemCount ?: 0
        val firstVisibleItemPosition = linearLayoutManager?.findFirstVisibleItemPosition() ?: 0

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && firstVisibleItemPosition >= 0
            && totalItemCount >= PER_PAGE && !isLoading() && !reachedEnd()
        ) {
       //     Log.d("Log",reachedEnd().toString())
            loadMoreItems()
        }
    }

    abstract fun loadMoreItems()
    abstract fun isLoading(): Boolean
    abstract fun reachedEnd(): Boolean
}