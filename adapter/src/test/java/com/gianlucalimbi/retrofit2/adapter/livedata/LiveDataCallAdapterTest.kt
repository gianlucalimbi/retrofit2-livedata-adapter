package com.gianlucalimbi.retrofit2.adapter.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gianlucalimbi.blueprint.Resource
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import java.lang.reflect.Type

@RunWith(MockitoJUnitRunner::class)
class LiveDataCallAdapterTest {

  @Mock private lateinit var call: Call<Any>
  private val responseType: Type = Any::class.java

  private lateinit var adapter: LiveDataCallAdapter<Any>

  @get:Rule val rule = InstantTaskExecutorRule()

  @Before
  fun setUp() {
    adapter = LiveDataCallAdapter(responseType)
  }

  @Test
  fun `When the response type is requested, the one passed in the constructor is returned`() {
    assertThat(adapter.responseType(), `is`(responseType))
  }

  @Test
  fun `When the data is adapted, the call in enqueued`() {
    adapter.adapt(call)

    verify(call).enqueue(any())
  }

  @Test
  fun `When the call is not completed, the result has a LOADING status`() {
    val liveData = adapter.adapt(call)

    assertThat(liveData.value?.status, `is`(Resource.Status.LOADING))
  }

  @Test
  fun `When the call is successfully completed, the result has a SUCCESS status and contains the result data`() {
    val result = Any()

    doAnswer { invocation ->
      val callback: Callback<Any> = invocation.getArgument(0)

      callback.onResponse(call, successResponse(result))
    }.`when`(call).enqueue(any())

    val liveData = adapter.adapt(call)

    val status = liveData.value?.status
    val data = liveData.value?.data

    assertThat(status, `is`(Resource.Status.SUCCESS))
    assertThat(data, `is`(result))
  }

  @Test
  fun `When the call is completed with an error, the result has an ERROR status and contains the raised HTTP error`() {
    val errorCode = 400
    val errorMessage = "error"

    doAnswer { invocation ->
      val callback: Callback<Any> = invocation.getArgument(0)

      callback.onResponse(call, errorResponse(errorCode, errorMessage))
    }.`when`(call).enqueue(any())

    val liveData = adapter.adapt(call)

    val status = liveData.value?.status
    val error = liveData.value?.error

    assertThat(status, `is`(Resource.Status.ERROR))
    assertThat(error, `is`(instanceOf(HttpException::class.java)))

    error as HttpException
    assertThat(error.code(), `is`(errorCode))
    assertThat(error.message(), `is`(errorMessage))
  }

  @Test
  fun `When the call fails, the result has an ERROR status and contains the raised error`() {
    val exception = Throwable("error")

    doAnswer { invocation ->
      val callback: Callback<Any> = invocation.getArgument(0)

      callback.onFailure(call, exception)
    }.`when`(call).enqueue(any())

    val liveData = adapter.adapt(call)

    val status = liveData.value?.status
    val error = liveData.value?.error

    assertThat(status, `is`(Resource.Status.ERROR))
    assertThat(error, `is`(exception))
  }

  @Test
  fun `When the call is canceled, the result is not updated`() {
    `when`(call.isCanceled).thenReturn(true)

    doAnswer { invocation ->
      val callback: Callback<Any> = invocation.getArgument(0)

      callback.onResponse(call, successResponse(Any()))
    }.`when`(call).enqueue(any())

    val liveData = adapter.adapt(call)

    val status = liveData.value?.status

    assertThat(status, `is`(Resource.Status.LOADING))
  }

}
