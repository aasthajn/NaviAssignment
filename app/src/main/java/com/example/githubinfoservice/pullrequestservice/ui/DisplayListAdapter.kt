package com.example.githubinfoservice.pullrequestservice.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubinfoservice.pullrequestservice.model.datamodels.PullRequestData

class DisplayListAdapter(private val onClickListener: DataClickListener) :
    RecyclerView.Adapter<BaseHolder>() {

    private var dataList: ArrayList<PullRequestData> = arrayListOf()
    var list: MutableList<PullRequestData>
        get() = dataList
        set(value) {
            if (dataList.size == 0) {
                dataList.addAll(value)
                notifyDataSetChanged()
            } else {
                dataList.addAll(value)
                notifyItemRangeInserted(dataList.size, value.size)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return DisplayViewHolder(parent, onClickListener)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(dataList[position])
    }

    class DataClickListener(val clickListener: (url: String) -> Unit) {
        fun onClick(pullRequestData: PullRequestData) = clickListener(pullRequestData.url)
    }
}