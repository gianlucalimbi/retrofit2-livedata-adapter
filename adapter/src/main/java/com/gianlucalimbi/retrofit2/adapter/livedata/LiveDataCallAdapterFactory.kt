package com.gianlucalimbi.retrofit2.adapter.livedata

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : CallAdapter.Factory() {

  override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
    if (getRawType(returnType) != LiveData::class.java) {
      return null
    }

    if (returnType !is ParameterizedType) {
      throw IllegalStateException("LiveData return type must be parameterized as LiveData<Foo> or LiveData<out Foo>")
    }

    val responseType = getParameterUpperBound(0, returnType)
    return LiveDataCallAdapter<Any>(responseType)
  }

}
