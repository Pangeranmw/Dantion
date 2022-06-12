<img src="https://raw.githubusercontent.com/Pangeranmw/Dantion/master/assets_readme/app-launcher.png" width="100" height="100" align="right" />

# Dantion Android App
Capstone project Bangkit 2022
![Dantion App](https://raw.githubusercontent.com/Pangeranmw/Dantion/master/assets_readme/bg-readme.png)

## About Our App

Dantion is a fast response application that can detect emergency situations (crimes, accidents, or fires) through voice recordings and report the location of the incident to the authorities automatically.

## How To Make This Android App Project

### How To Build This Project

If you build this application an error will occur. Because this application requires an API Key to display the Google Map. Follow this tutorial to generate Google map api key
>[Set up in Cloud Console](https://developers.google.com/maps/documentation/android-sdk/start#set_up_in_cloud_console) (Note: ignore the other steps from this link)

Once you have the api key, follow these steps:
<br>
![local-properties](https://raw.githubusercontent.com/Pangeranmw/Dantion/master/assets_readme/local-properties.png)

* Open local.properties file
* Inside your local properties, set MAPS_KEY variable
```
MAPS_KEY = your-api-key
```
* Set BASE_URL variable
```
BASE_URL = https://dangerdetection.et.r.appspot.com/
```

### Libraries We Use

| Library name  | Usages        | Dependency    |
| ------------- | ------------- | ------------- |
| [Retrofit2](https://square.github.io/retrofit/) | Request API and convert json response into an object | implementation "com.squareup.retrofit2:retrofit:2.9.0" <br> implementation "com.squareup.retrofit2:converter-gson:2.9.0" |
| [Tflite](https://www.tensorflow.org/lite) | Machine learning process | implementation "org.tensorflow:tensorflow-lite-task-audio:0.3.0" |
| [Maps](https://developers.google.com/maps) | Google maps service | implementation "com.google.android.gms:play-services-maps:18.0.2" <br> implementation "com.google.android.gms:play-services-location:19.0.1" |
| [OkHttp](https://square.github.io/okhttp/) | Make a data request to the server | implementation "com.squareup.okhttp3:logging-interceptor:4.9.3" |
| [Datastore](https://developer.android.com/topic/libraries/architecture/datastore?gclid=CjwKCAjwnZaVBhA6EiwAVVyv9JJDrHZ0zpyjRp2mCoKIKH2ijLF49ZQpVqUuvUv9E7FziCj7pSo6jRoCkfAQAvD_BwE&gclsrc=aw.ds)| Save data in local storage | implementation("androidx.datastore:datastore-preferences:1.0.0") |
| [Navigation component](https://developer.android.com/guide/navigation)| Navigation between pages | implementation "androidx.navigation:navigation-fragment-ktx:2.4.2" <br> implementation "androidx.navigation:navigation-ui-ktx:2.4.2" |
| [Room](https://developer.android.com/jetpack/androidx/releases/room?gclid=CjwKCAjwnZaVBhA6EiwAVVyv9N5Jvs6cSYCGlBiY0NPil7uduzHbZ6cCt3wLu5zziuXBaENV6_JYORoC-FEQAvD_BwE&gclsrc=aw.ds) | Local database | implementation "androidx.room:room-ktx:2.4.2" <br> implementation "androidx.room:room-runtime:2.4.2" |
| [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle?hl=id) | Connecting frontend and backend | implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1" <br> implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1" |