package com.example.githubinfoservice.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException

open class BaseRepository() {

    suspend fun <T : Any> enqueue(call: suspend () -> T): Resource<T> {
        return apiOutput(call)
    }

    private suspend fun <T : Any> apiOutput(call: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(call.invoke())
        } catch (throwable: Throwable) {

            when (throwable) {

                is HttpException -> {
                    try {
                        val response = throwable.response()
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorResponse: ErrorResponse =
                            Gson().fromJson(response?.errorBody()?.charStream(), type)
                        Resource.Error(errorResponse)
                    } catch (e: JsonSyntaxException) {
                        Resource.Error(
                            ErrorResponse(
                                throwable.response()?.message(),
                                throwable.code().toString()
                            )
                        )
                    } catch (e: Throwable) {
                        Resource.Error(
                            ErrorResponse(
                                throwable.response()?.message(),
                                throwable.code().toString()
                            )
                        )
                    }
                }
                else -> {
                    Resource.Error(ErrorResponse(throwable.message, ""))
                }
            }
        }
    }
}
