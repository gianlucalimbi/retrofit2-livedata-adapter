# Retrofit2 LiveData Call Adapter

Get a LiveData responses from Retrofit.

```kotlin
@GET("posts/{id}")
fun getPost(@Path("id") postId: Int): LiveData<Resource<Post>>
```

A [Retrofit2 Call Adapter][call-adapter] for Android Architecture Components' [LiveData][live-data]. Works with [Blueprint][blueprint].

## Installation with Gradle

This library is distributed through [Jitpack][jitpack]. To install it:

Add this line in the `repositories` section of your project's `build.gradle` file:
```groovy
allprojects {
  repositories {
    maven { url "https://jitpack.io" } // this line
  }
}
```

And this line in the `dependencies` of your module's `build.gradle` file:
```groovy
dependencies {
  implementation "com.github.gianlucalimbi:retrofit2-livedata-adapter:1.0.0" // this line
}
```

## Usage

When creating a Retrofit instance, just add the adapter using the `addCallAdapterFactory()` function.

```kotlin
Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(converterFactory)
    .addCallAdapterFactory(LiveDataCallAdapterFactory()) // this line
    .client(okHttpClient)
    .build()
```

You are now good to go, you can use `LiveData<Resource<AnyClass>>` in your retrofit service interface, like this:

```kotlin
interface ApiService {

  @GET("posts")
  fun getPosts(): LiveData<Resource<List<Post>>>

  @GET("posts/{id}")
  fun getPost(@Path("id") postId: Int): LiveData<Resource<Post>>

}
```

## Blueprint

This call adapter uses Blueprint's [Resource][resource] class. You can check the [Blueprint][blueprint] GitHub page for more documentation.

Blueprint also comes with a handy [ResourceObserver][resource-observer] class that you can use with this adapter.

```kotlin
val liveData = api.getPost(postId)
liveData.observe(lifecycleOwner, resourceObserver {

  onSuccess { data ->
    // data: Post -> the data returned from retrofit
  }

  onError { error ->
    // error: Exception -> an instance of HttpException if the API call failed (status >= 400), another Exception otherwise
  }

  onLoading { data ->
    // data: Post? -> always null
  }

  onChanged { resource ->
    // resource: Resource<Post>? -> the raw resource contained in the LiveData
  }

})
```

[call-adapter]: https://github.com/square/retrofit/wiki/Call-Adapters
[live-data]: https://developer.android.com/topic/libraries/architecture/livedata
[blueprint]: https://github.com/gianlucalimbi/blueprint
[resource]: https://github.com/gianlucalimbi/blueprint#resource
[resource-observer]: https://github.com/gianlucalimbi/blueprint#resource-observer
[jitpack]: https://jitpack.io/#gianlucalimbi/retrofit2-livedata-adapter
