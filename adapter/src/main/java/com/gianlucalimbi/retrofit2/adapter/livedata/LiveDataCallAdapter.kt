package com.gianlucalimbi.retrofit2.adapter.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gianlucalimbi.blueprint.Resource
import retrofit2.*
import java.lang.reflect.Type

internal class LiveDataCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, LiveData<Resource<T>>> {

  override fun responseType(): Type = responseType

  override fun adapt(call: Call<T>): LiveData<Resource<T>> {
    val liveData = MutableLiveData<Resource<T>>()

    liveData.postValue(Resource.loading())

    call.enqueue(object : Callback<T> {

      override fun onResponse(call: Call<T>, response: Response<T>) {
        if (call.isCanceled) {
          return
        }

        if (response.isSuccessful) {
          liveData.postValue(Resource.success(response.body()))
        } else {
          liveData.postValue(Resource.error(HttpException(response)))
        }
      }

      override fun onFailure(call: Call<T>, throwable: Throwable) {
        if (call.isCanceled) {
          return
        }

        liveData.postValue(Resource.error(throwable))
      }

    })

    return liveData
  }

}
