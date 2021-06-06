package com.example.githubinfoservice.pullrequestservice.model.datamodels

data class RepoResponse(val list: List<PullRequestData>)

data class PullRequestData(val url:String,val number:Int,val id:Double,val title: String,val state:String,val body:String?, val created_at:String, val updated_at:String, val user: User, val head:Head,val base:Base )

data class User (val login:String, val id: Double, val avatar_url:String, val url: String)

data class Head(val user: User, val label:String, val ref:String, val sha:String)

data class Base (val user: User, val label:String, val ref:String, val sha:String)
