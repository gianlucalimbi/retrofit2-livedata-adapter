package com.gianlucalimbi.retrofit2.adapter.livedata

import androidx.lifecycle.LiveData
import com.gianlucalimbi.blueprint.Resource
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit

@RunWith(MockitoJUnitRunner::class)
class LiveDataCallAdapterFactoryTest {

  @Mock private lateinit var retrofit: Retrofit
  private lateinit var annotations: Array<out Annotation>

  private lateinit var factory: LiveDataCallAdapterFactory

  @Before
  fun setUp() {
    annotations = arrayOf()
    factory = LiveDataCallAdapterFactory()
  }

  @Test
  fun `When a Type different from LiveData is passed, null is returned`() {
    val type = getType(String::class.java)

    assertThat(factory.get(type, annotations, retrofit), `is`(nullValue()))
  }

  @Test(expected = IllegalArgumentException::class)
  fun `When a Type of LiveData with no parameter is passed, an exception is thrown`() {
    val type = getType(LiveData::class.java)

    factory.get(type, annotations, retrofit)
  }

  @Test(expected = IllegalArgumentException::class)
  fun `When a Type of LiveData with a parameter different from Resource is passed, an exception is thrown`() {
    val type = getType(LiveData::class.java, String::class.java)

    factory.get(type, annotations, retrofit)
  }

  @Test(expected = IllegalArgumentException::class)
  fun `When a Type of LiveData Resource with no parameters is passed, an exception is thrown`() {
    val type = getType(LiveData::class.java, Resource::class.java)

    factory.get(type, annotations, retrofit)
  }

  @Test
  fun `When a Type of LiveData Resource with a parameter is passed, an instance of the adapter is returned`() {
    val type = getType(LiveData::class.java, getType(Resource::class.java, String::class.java))

    assertThat(factory.get(type, annotations, retrofit), `is`(instanceOf(LiveDataCallAdapter::class.java)))
  }

}
