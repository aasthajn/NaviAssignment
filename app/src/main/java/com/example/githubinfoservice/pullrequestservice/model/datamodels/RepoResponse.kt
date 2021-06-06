package com.example.githubinfoservice.pullrequestservice.model.datamodels

data class RepoResponse(val list: List<PullRequestData>)

data class PullRequestData(val title: String)
