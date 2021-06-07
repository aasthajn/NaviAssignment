package com.example.githubinfoservice.pullrequestservice.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            Toast.makeText(context, "$url", Toast.LENGTH_SHORT).show()
        })

        val paginationListener = object : PaginationListener() {
            override fun loadMoreItems() {
                viewModel.setScrolledDown()
                viewModel.refreshDataFromRepository()
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

    private fun subscribe() {

        viewModel.listResponse.observe(viewLifecycleOwner, { list ->
            list?.let { it ->
                adapter.list = (it).toMutableList()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    lifecycleScope.launch {
                        progress_bottom.visibility = View.VISIBLE
                        bottom_view.visibility = View.VISIBLE
                        bottom_view.animate().alpha(1.0f)
                        tv_message.text = "Loading..."
                    }
                } else {
                    lifecycleScope.launch {
                        delay(500)
                        progress_bottom.visibility = View.GONE
                        tv_message.text = "Fetched data"
                        bottom_view.animate().alpha(0.0f)
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
                Snackbar.make(recycler, resources.getString(R.string.online), Snackbar.LENGTH_SHORT).show()
                viewModel.refreshDataFromRepository()
            }
        })
    }

}