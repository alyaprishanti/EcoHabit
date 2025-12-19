package ap.mobile.ecohabit.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.*

interface FirestoreApiService {
    @GET("projects/{projectId}/databases/(default)/documents/{collection}")
    suspend fun listDocuments(
        @Path("projectId") projectId: String,
        @Path("collection") collection: String,
        @Query("key") apiKey: String
    ): JsonObject

    @GET("projects/{projectId}/databases/(default)/documents/{collection}/{id}")
    suspend fun getDocument(
        @Path("projectId") projectId: String,
        @Path("collection") collection: String,
        @Path("id") id: String,
        @Query("key") apiKey: String
    ): JsonObject

     @PATCH("projects/{projectId}/databases/(default)/documents/{collection}/{documentId}")
    suspend fun patchDocument(
        @Path("projectId") projectId: String,
        @Path("collection") collection: String,
        @Path("documentId") documentId: String,
        @Query("key") apiKey: String,
        @Query("updateMask.fieldPaths") updateMask: List<String>,
        @Body body: JsonObject
    ): JsonObject

    @POST("projects/{projectId}/databases/(default)/documents/{weeklyCollection}/{weekId}/daily")
    suspend fun createDailyWithId(
        @Path("projectId") projectId: String,
        @Path("weeklyCollection") weeklyCollection: String,
        @Path("weekId") weekId: String,
        @Query("documentId") dateId: String, // ✅ BENAR
        @Query("key") apiKey: String,
        @Body body: JsonObject
    ): JsonObject

    @POST("projects/{projectId}/databases/(default)/documents:runQuery")
    suspend fun runQuery(
        @Path("projectId") projectId: String,
        @Query("key") apiKey: String,
        @Body body: JsonObject
    ): JsonArray

    @GET("projects/{projectId}/databases/(default)/documents/{collection}")
    suspend fun listDailyRecords(
        @Path("projectId") projectId: String,
        @Path("collection") collection: String,  // dailyRecords
        @Query("key") apiKey: String
    ): JsonObject

    @POST("projects/{projectId}/databases/(default)/documents/{collection}")
    suspend fun createDocument(
        @Path("projectId") projectId: String,
        @Path("collection") collection: String,
        @Query("key") apiKey: String,
        @Body body: JsonObject
    ): JsonObject

    @PATCH("projects/{projectId}/databases/(default)/documents/{weeklyCollection}/{weekId}/daily/{dateId}")
    suspend fun patchDaily(
        @Path("projectId") projectId: String,
        @Path("weeklyCollection") weeklyCollection: String,
        @Path("weekId") weekId: String,
        @Path("dateId") dateId: String,
        @Query("key") apiKey: String,
        @Query("updateMask.fieldPaths") updateMask: List<String>,
        @Body body: JsonObject
    ): JsonObject

    @POST("projects/{projectId}/databases/(default)/documents/{weeklyCollection}/{weekId}/daily")
    suspend fun createDailyAuto(
        @Path("projectId") projectId: String,
        @Path("weeklyCollection") weeklyCollection: String,
        @Path("weekId") weekId: String,
        @Query("key") apiKey: String,
        @Body body: JsonObject
    ): JsonObject

    @GET("projects/{projectId}/databases/(default)/documents/{weeklyCollection}/{weekId}/daily")
    suspend fun listDaily(
        @Path("projectId") projectId: String,
        @Path("weeklyCollection") weeklyCollection: String, // weeklyHistory
        @Path("weekId") weekId: String,
        @Query("key") apiKey: String
    ): JsonObject

}
