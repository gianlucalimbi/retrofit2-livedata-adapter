package com.gianlucalimbi.retrofit2.adapter.livedata

import androidx.lifecycle.LiveData
import com.gianlucalimbi.blueprint.Resource
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
      throw IllegalArgumentException("LiveData return type must be parameterized as LiveData<Resource<Foo>>")
    }

    val resourceType = getParameterUpperBound(0, returnType)

    if (getRawType(resourceType) != Resource::class.java) {
      throw IllegalArgumentException("LiveData return type must be parameterized with Resource")
    }

    if (resourceType !is ParameterizedType) {
      throw IllegalArgumentException("Resource return type must be parameterized as Resource<Foo>")
    }

    val responseType = getParameterUpperBound(0, resourceType)
    return LiveDataCallAdapter<Any>(responseType)
  }

}
