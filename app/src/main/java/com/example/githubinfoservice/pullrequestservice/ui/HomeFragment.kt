package com.example.githubinfoservice.pullrequestservice.ui

import androidx.lifecycle.ViewModelProvider
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
import com.example.githubinfoservice.utils.PaginationListener
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = DisplayListAdapter(DisplayListAdapter.DataClickListener { url ->

            Toast.makeText(context,"url${url}", Toast.LENGTH_SHORT).show()
        })

        val layoutManager = LinearLayoutManager(context)

        val paginationListener = object : PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.refreshDataFromRepository()
            }

            override fun isLoading():Boolean {
               return viewModel._isLoading.value?:false
            }

            override fun reachedEnd():Boolean {
                return viewModel._isReachedEnd.value?:false
            }
        }

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addOnScrollListener(paginationListener)

        viewModel.listResponse.observe(viewLifecycleOwner, {
            it?.let {
                adapter.list = (it).toMutableList()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            it?.let {

               progress_circular.visibility = when(it) {
                   true-> View.VISIBLE
                   false->View.GONE
               }
            }
        })

        viewModel.refreshDataFromRepository()
    }

}