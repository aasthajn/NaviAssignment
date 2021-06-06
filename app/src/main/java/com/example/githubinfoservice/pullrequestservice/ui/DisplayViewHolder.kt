package com.example.githubinfoservice.pullrequestservice.ui

import android.view.ViewGroup
import com.example.githubinfoservice.R
import com.example.githubinfoservice.pullrequestservice.model.datamodels.PullRequestData
import com.example.githubinfoservice.utils.loadImage
import kotlinx.android.synthetic.main.item_display.view.*

class DisplayViewHolder(parent: ViewGroup, var listener: DisplayListAdapter.DataClickListener) :
    BaseHolder(parent.inflate(R.layout.item_display)) {

    override fun bind(data: PullRequestData) {

        itemView.tv_body.text = data.body
        itemView.apply {
            tv_title.text = data.title
            data.body.let {
                tv_body.text = it
            }
            tv_closed_date.text = data.created_at
            image_avatar.loadImage(data.user.avatar_url)
            tv_title.setOnClickListener {
                listener.onClick(data)
            }
        }


    }
}
