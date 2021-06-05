package com.app.androidproductstest.api.wrappers

import android.util.Log
import com.app.androidproductstest.api.ApiConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class ProcessCallResponse<T> {

    suspend fun processCall(response: Response<*>): BaseResource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val responseCode = response.code()
                val errorResponse = response.errorBody()

                if (response.isSuccessful) {
                    val body = response.body() as BaseResponseWrapper<T>
                    val data = body.items
                    if (body.status == ApiConstants.REQUEST_SUCCESS && data != null) {
                        Resource.Success(data = body)
                    } else {
                        Resource.DataError(errorCode = body.message ?: "")
                    }
                } else {
                    val jsonObj = JSONObject(errorResponse!!.charStream().readText())
                    Log.d("processCall", "$jsonObj")
                    Resource.DataError(errorCode = jsonObj.getString("message"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.DataError(errorCode = "Something went wrong")
            }
        }
    }
}