package com.example.githubinfoservice.pullrequestservice.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubinfoservice.R
import com.example.githubinfoservice.pullrequestservice.viewmodel.HomeViewModel
import com.example.githubinfoservice.utils.ConnectionLiveData
import com.example.githubinfoservice.utils.PaginationListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(R.layout.home_fragment) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: DisplayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DisplayListAdapter(DisplayListAdapter.DataClickListener { url ->
            Toast.makeText(context, "url${url}", Toast.LENGTH_SHORT).show()
        })

        val paginationListener = object : PaginationListener() {
            override fun loadMoreItems() {
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

        viewModel.listResponse.observe(viewLifecycleOwner, {
            it?.let {
                adapter.list = (it).toMutableList()
            } ?: run { adapter.clearList() }
            // Snackbar.make(recycler, viewModel.PAGE_NO.toString(), Snackbar.LENGTH_LONG).show()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            it?.let {
                progress_bottom.visibility = when (it) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
                setRecyclePaddingBottom(it)
            }
        })

        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner, { status ->
            if (!status) {
                Snackbar.make(recycler, "You are Offline", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(recycler, "You are Online", Snackbar.LENGTH_LONG)
                    .setAction("Refresh") {
                        viewModel.resetList()
                        viewModel.refreshDataFromRepository()
                    }.setActionTextColor(Color.YELLOW).show()
                viewModel.refreshDataFromRepository()
            }
        })
    }

    private fun setRecyclePaddingBottom(isLoading: Boolean) {
        if (isLoading)
            recycler.setPadding(0, 0, 0, 160)
        else
            recycler.setPadding(0, 0, 0, 0)
    }

}