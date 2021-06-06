package com.example.githubinfoservice.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubinfoservice.Constants.PER_PAGE

abstract class PaginationListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
            && firstVisibleItemPosition >= 0
            && totalItemCount >= PER_PAGE && !isLoading()
        ) {
            loadMoreItems()
        }
    }

    protected abstract fun loadMoreItems()
    protected abstract fun isLoading(): Boolean
    protected abstract fun reachedEnd(): Boolean
}