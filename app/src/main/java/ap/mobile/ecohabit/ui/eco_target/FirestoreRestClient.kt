package ap.mobile.ecohabit.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object FirestoreRestClient {
    private const val BASE_URL = "https://firestore.googleapis.com/v1/"

    val gson = GsonBuilder().create()

    val api: FirestoreApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FirestoreApiService::class.java)
    }

}
