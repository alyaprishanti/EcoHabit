package ap.mobile.ecohabit.data

import com.google.gson.JsonObject
import ap.mobile.ecohabit.ui.eco_target.EcoTargetRepository.WeeklyHistory
import ap.mobile.ecohabit.data.remote.FirestoreRestClient
import ap.mobile.ecohabit.ui.eco_target.EcoTargetRepository
import ap.mobile.ecohabit.ui.eco_target.EcoTargetRepository.carbonWeeklyTarget
import ap.mobile.ecohabit.ui.eco_target.EcoTargetRepository.quizWeeklyTarget
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EcoTargetRestRepository {
    private const val PROJECT_ID = "ecohabit-39d48"
    private const val COLLECTION = "weeklyHistory"
    // Use your API key (for development). For production, use OAuth bearer from backend or Google Sign-In.
    private const val API_KEY = "AIzaSyCQ-XSY-SnLtMR-BAkHkx4XV3NhE_s_2Pw"

    private val api = FirestoreRestClient.api

    private fun getStringField(fields: JsonObject, name: String): String {
        val f = fields.getAsJsonObject(name) ?: return ""
        return f.entrySet().firstOrNull()?.value?.asString ?: ""
    }

    private fun getIntField(fields: JsonObject, name: String): Int {
        val f = fields.getAsJsonObject(name) ?: return 0
        val e = f.entrySet().firstOrNull()?.value ?: return 0

        return try {
            e.asInt
        } catch (t: Throwable) {
            try {
                e.asDouble.toInt()
            } catch (t2: Throwable) {
                try {
                    e.asString.toDouble().toInt()
                } catch (_: Throwable) {
                    0
                }
            }
        }
    }

    private fun getDoubleField(fields: JsonObject, name: String): Double {
        val f = fields.getAsJsonObject(name) ?: return 0.0
        val e = f.entrySet().firstOrNull()?.value ?: return 0.0

        return try {
            e.asDouble
        } catch (t: Throwable) {
            try {
                e.asString.toDouble()
            } catch (t2: Throwable) {
                0.0
            }
        }
    }

    private fun getBooleanField(fields: JsonObject, name: String): Boolean {
        val f = fields.getAsJsonObject(name) ?: return false
        val e = f.entrySet().firstOrNull()?.value ?: return false
        return try { e.asBoolean } catch (_: Throwable) { e.asString.toBoolean() }
    }

    suspend fun fetchWeeklyHistoryList(): List<WeeklyHistory> = withContext(Dispatchers.IO) {
        val json = api.listDocuments(PROJECT_ID, COLLECTION, API_KEY)
        val docs = json.getAsJsonArray("documents") ?: return@withContext emptyList<WeeklyHistory>()
        val list = mutableListOf<WeeklyHistory>()
        for (elem in docs) {
            val doc = elem.asJsonObject
            val name = doc.get("name")?.asString ?: continue
            val id = name.substringAfterLast("/")
            val fields = doc.getAsJsonObject("fields") ?: JsonObject()
            val wh = WeeklyHistory(
                id = id,
                weekLabel = getStringField(fields, "weekLabel"),
                carbonTarget = getIntField(fields, "carbonTarget"),
                quizTarget = getIntField(fields, "quizTarget"),
                carbonTotal = getDoubleField(fields, "carbonTotal").toFloat(),
                quizTotal = getDoubleField(fields, "quizTotal").toFloat(),
                carbonAchieved = getBooleanField(fields, "carbonAchieved"),
                quizAchieved = getBooleanField(fields, "quizAchieved"),
                improvement = getIntField(fields, "improvement"),
                motivation = getStringField(fields, "motivation")
            )
            list.add(wh)
        }
        list
    }

    suspend fun fetchWeeklyHistoryById(id: String): WeeklyHistory? = withContext(Dispatchers.IO) {
        try {
            val doc = api.getDocument(PROJECT_ID, COLLECTION, id, API_KEY)
            val fields = doc.getAsJsonObject("fields") ?: JsonObject()
            WeeklyHistory(
                id = id,
                weekLabel = getStringField(fields, "weekLabel"),
                carbonTarget = getIntField(fields, "carbonTarget"),
                quizTarget = getIntField(fields, "quizTarget"),
                carbonTotal = getDoubleField(fields, "carbonTotal").toFloat(),
                quizTotal = getDoubleField(fields, "quizTotal").toFloat(),
                carbonAchieved = getBooleanField(fields, "carbonAchieved"),
                quizAchieved = getBooleanField(fields, "quizAchieved"),
                improvement = getIntField(fields, "improvement"),
                motivation = getStringField(fields, "motivation")
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateMotivation(id: String, note: String) = withContext(Dispatchers.IO) {
        val fields = JsonObject().apply {
            add("motivation", JsonObject().apply {
                addProperty("stringValue", note)
            })
        }

        val body = JsonObject().apply {
            add("fields", fields)
        }
        val response = api.patchDocument(
            PROJECT_ID,
            COLLECTION,
            id,
            API_KEY,
            listOf("carbonTotal", "quizTotal", "carbonAchieved", "quizAchieved", "motivation"),   // <-- BENAR
            body
        )
        response.toEntity()
    }

    suspend fun createOrUpdateDaily(weekId: String, dateId: String, carbon: Float, quiz: Int) = withContext(Dispatchers.IO) {
        val fields = JsonObject().apply {
            add("carbon", JsonObject().apply { addProperty("doubleValue", carbon) })
            add("quiz", JsonObject().apply { addProperty("integerValue", quiz.toString()) })
        }
        val body = JsonObject().apply { add("fields", fields) }

        // use create with custom id:
        return@withContext api.createDailyWithId(PROJECT_ID, COLLECTION, weekId, dateId, API_KEY, body)
    }

    suspend fun createDaily(dateId: String, carbon: Float, quiz: Int, weekId: String) =
        withContext(Dispatchers.IO) {

            val fields = JsonObject().apply {
                add("dateId", JsonObject().apply { addProperty("stringValue", dateId) })
                add("weekId", JsonObject().apply { addProperty("stringValue", weekId) })
                add("carbon", JsonObject().apply { addProperty("doubleValue", carbon) })
                add("quiz", JsonObject().apply { addProperty("integerValue", quiz) })
            }

            val body = JsonObject().apply { add("fields", fields) }

            api.createDocument(
                projectId = PROJECT_ID,
                collection = "dailyRecords",
                apiKey = API_KEY,
                body = body
            )
        }


    suspend fun updateWeeklyTotalsFromDaily(weekId: String) = withContext(Dispatchers.IO) {

        val daily = listDailyByWeek(weekId)

        val carbonTotal = daily.sumOf { it.carbon.toDouble() }
        val quizTotal = daily.sumOf { it.quiz.toDouble() }

        val fields = JsonObject().apply {
            add("carbonTotal", JsonObject().apply { addProperty("doubleValue", carbonTotal) })
            add("quizTotal", JsonObject().apply { addProperty("doubleValue", quizTotal) })
            add("carbonAchieved", JsonObject().apply { addProperty("booleanValue", carbonTotal <= carbonWeeklyTarget) })
            add("quizAchieved", JsonObject().apply { addProperty("booleanValue", quizTotal >= quizWeeklyTarget) })
        }

        val body = JsonObject().apply { add("fields", fields) }

        api.patchDocument(
            PROJECT_ID,
            "weeklyHistory",
            weekId,
            API_KEY,
            listOf("carbonTotal","quizTotal","carbonAchieved","quizAchieved"),
            body
        )
    }

    // EcoTargetRestRepository.kt
    suspend fun fetchDailyForWeek(weekId: String): List<EcoTargetRepository.DailyData> =
        withContext(Dispatchers.IO) {

            val json = api.listDocuments(
                projectId = PROJECT_ID,
                collection = "dailyRecords",     // <--- COLLECTION BARU
                apiKey = API_KEY
            )

            val docs = json.getAsJsonArray("documents") ?: return@withContext emptyList()
            val result = mutableListOf<EcoTargetRepository.DailyData>()

            for (elem in docs) {
                val obj = elem.asJsonObject
                val fields = obj.getAsJsonObject("fields") ?: JsonObject()

                val dateId = fields["dateId"]?.asJsonObject?.get("stringValue")?.asString ?: ""
                val carbon = fields["carbon"]?.asJsonObject?.get("doubleValue")?.asDouble?.toFloat() ?: 0f
                val quiz = fields["quiz"]?.asJsonObject?.get("integerValue")?.asString?.toIntOrNull() ?: 0
                val week = fields["weekId"]?.asJsonObject?.get("stringValue")?.asString ?: ""

                if (week == weekId) {
                    result.add(
                        EcoTargetRepository.DailyData(
                            dateId = dateId,
                            carbon = carbon,
                            quiz = quiz,
                            weekId = week
                        )
                    )
                }
            }
            result.sortedBy { it.dateId }
        }



    fun JsonObject.toEntity(): WeeklyHistory {
        val fields = this.getAsJsonObject("fields")

        return WeeklyHistory(
            id = this.get("name").asString.substringAfterLast("/"),
            weekLabel = fields["weekLabel"]?.asJsonObject?.get("stringValue")?.asString ?: "",
            carbonTarget = fields["carbonTarget"]?.asJsonObject?.get("integerValue")?.asInt ?: 0,
            quizTarget = fields["quizTarget"]?.asJsonObject?.get("integerValue")?.asInt ?: 0,
            carbonTotal = fields["carbonTotal"]?.asJsonObject?.get("doubleValue")?.asFloat ?: 0f,
            quizTotal = fields["quizTotal"]?.asJsonObject?.get("doubleValue")?.asFloat ?: 0f,
            carbonAchieved = fields["carbonAchieved"]?.asJsonObject?.get("booleanValue")?.asBoolean ?: false,
            quizAchieved = fields["quizAchieved"]?.asJsonObject?.get("booleanValue")?.asBoolean ?: false,
            improvement = fields["improvement"]?.asJsonObject?.get("integerValue")?.asInt ?: 0,
            motivation = fields["motivation"]
                ?.asJsonObject
                ?.entrySet()
                ?.firstOrNull()
                ?.value
                ?.asString
                ?: ""
        )
    }

    suspend fun listDailyByWeek(weekId: String): List<EcoTargetRepository.DailyData> =
        withContext(Dispatchers.IO) {

            val structuredQuery = JsonObject().apply {
                add("structuredQuery", JsonObject().apply {
                    add("from", JsonArray().apply {
                        add(JsonObject().apply {
                            addProperty("collectionId", "dailyRecords")
                        })
                    })
                    add("where", JsonObject().apply {
                        add("fieldFilter", JsonObject().apply {
                            add("field", JsonObject().apply {
                                addProperty("fieldPath", "weekId")
                            })
                            addProperty("op", "EQUAL")   // <-- FIX
                            add("value", JsonObject().apply {
                                addProperty("stringValue", weekId)
                            })
                        })
                    })
                })
            }

            val response = api.runQuery(PROJECT_ID, API_KEY, structuredQuery)

            val result = mutableListOf<EcoTargetRepository.DailyData>()

            for (item in response) {
                val doc = item.asJsonObject["document"]?.asJsonObject ?: continue
                val fields = doc["fields"].asJsonObject

                val dateId = fields["dateId"].asJsonObject["stringValue"].asString
                val carbon = fields["carbon"].asJsonObject["doubleValue"].asDouble.toFloat()
                val quiz = fields["quiz"].asJsonObject["integerValue"].asString.toInt()

                result.add(EcoTargetRepository.DailyData(dateId, carbon, quiz))
            }

            result.sortedBy { it.dateId }
        }



    suspend fun fetchAllDaily(): List<EcoTargetRepository.DailyData> =
        withContext(Dispatchers.IO) {

            val json = api.listDailyRecords(PROJECT_ID, "dailyRecords", API_KEY)
            val docs = json.getAsJsonArray("documents") ?: return@withContext emptyList()

            val list = mutableListOf<EcoTargetRepository.DailyData>()

            for (item in docs) {
                val doc = item.asJsonObject
                val fields = doc.getAsJsonObject("fields") ?: continue

                val dateId = fields["dateId"]?.asJsonObject?.get("stringValue")?.asString ?: ""
                val weekId = fields["weekId"]?.asJsonObject?.get("stringValue")?.asString ?: ""
                val carbon = fields["carbon"]?.asJsonObject?.get("doubleValue")?.asDouble?.toFloat() ?: 0f
                val quiz = fields["quiz"]?.asJsonObject?.get("integerValue")?.asString?.toInt() ?: 0

                list.add(
                    EcoTargetRepository.DailyData(
                        dateId = dateId,
                        carbon = carbon,
                        quiz = quiz,
                        weekId = weekId
                    )
                )
            }

            list
        }


}
