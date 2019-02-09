package com.gianlucalimbi.retrofit2.adapter.livedata

import com.google.gson.reflect.TypeToken
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.reflect.Type

internal fun getType(type: Type, vararg typeArguments: Type) = if (typeArguments.isEmpty()) {
  type
} else {
  TypeToken.getParameterized(type, *typeArguments).type
}

internal fun <T> successResponse(body: T) = Response.success(body)

internal fun <T> errorResponse(code: Int, message: String) = Response.error<T>(
    ResponseBody.create(null, message),
    okhttp3.Response.Builder()
        .code(code)
        .message(message)
        .protocol(Protocol.HTTP_1_1)
        .request(Request.Builder().url("http://localhost/").build())
        .build())
