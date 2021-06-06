package com.example.githubinfoservice.pullrequestservice.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubinfoservice.pullrequestservice.model.datamodels.PullRequestData

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: PullRequestData)
}

fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}


