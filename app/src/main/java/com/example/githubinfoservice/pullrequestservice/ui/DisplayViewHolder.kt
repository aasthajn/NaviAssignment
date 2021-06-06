package com.example.githubinfoservice.pullrequestservice.ui

import android.view.ViewGroup
import com.example.githubinfoservice.R
import com.example.githubinfoservice.pullrequestservice.model.datamodels.PullRequestData
import kotlinx.android.synthetic.main.item_display.view.*

class DisplayViewHolder(parent: ViewGroup, var listener: DisplayListAdapter.DataClickListener) :
    BaseHolder(parent.inflate(R.layout.item_display)) {
    private val title = itemView.tv_title
    //private val image = itemView.image_movie

    override fun bind(data: PullRequestData) {
            title.text = data.title

            //image.loadImage(data)
            title.setOnClickListener {
                listener.onClick(data)
            }
    }
}
