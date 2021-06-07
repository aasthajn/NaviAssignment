package com.example.githubinfoservice.pullrequestservice.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubinfoservice.R
import com.example.githubinfoservice.pullrequestservice.viewmodel.HomeViewModel
import com.example.githubinfoservice.utils.ConnectionLiveData
import com.example.githubinfoservice.utils.PaginationListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.home_fragment) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: DisplayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DisplayListAdapter(DisplayListAdapter.DataClickListener { url ->
            Toast.makeText(context, "URL : $url", Toast.LENGTH_SHORT).show()
        })

        val paginationListener = object : PaginationListener() {
            override fun loadMoreItems() {
                viewModel.setScrolledDown()
                refreshData()
            }

            override fun isLoading(): Boolean {
                return viewModel.getLoading()
            }

            override fun reachedEnd(): Boolean {
                return viewModel.getReachedEnd()
            }
        }

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addOnScrollListener(paginationListener)

        subscribe()
    }

    private fun refreshData() {
        viewModel.refreshDataFromRepository()
    }

    private fun subscribe() {
        viewModel.listResponse.observe(viewLifecycleOwner, { list ->
            list?.let { it ->
                adapter.list = (it).toMutableList()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    handleBottomViewonDataLoad()
                } else {
                    lifecycleScope.launch {
                        handleBottomViewonDataFetched()
                        delay(1500)
                        hideLoader()
                    }
                }
            }
        })

        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner, { status ->
            viewModel.setNetworkAvailable(status)
            if (!status) {
                Snackbar.make(recycler, resources.getString(R.string.offline), Snackbar.LENGTH_LONG)
                    .show()
            } else {
                refreshData()
            }
        })
    }

    private fun handleBottomViewonDataFetched() {
        tv_message.text = resources.getString(R.string.fetched_data)
        progress_bottom.visibility = View.GONE
    }

    private fun handleBottomViewonDataLoad() {
        bottom_view.visibility = View.VISIBLE
        progress_bottom.visibility = View.VISIBLE
        tv_message.text = resources.getString(R.string.loading)
        bottom_view.animate().alpha(1.0f)
    }

    private fun hideLoader() {
        bottom_view.animate().alpha(0.0f)
    }

}