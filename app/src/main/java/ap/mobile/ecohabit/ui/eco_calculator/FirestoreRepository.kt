package ap.mobile.ecohabit.ui.eco_calculator
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class FirestoreRepository(
    private val collectionName: String = "emissions"
) {
    private val col = FirebaseService.db.collection(collectionName)

    /**
     * Save emission result as a new document (auto-id).
     */
    fun saveResult(result: EmissionResult, onComplete: (Boolean, String?) -> Unit) {
        // Convert to map (Firestore-friendly)
        val map = mapOf(
            "date" to result.date,
            "transportType" to result.transportType,
            "transportEmiGrams" to result.transportEmiGrams,
            "transportDistanceKm" to result.transportDistanceKm,
            "electricityMap" to result.electricityMap,
            "foodMapGrams" to result.foodMapGrams,
            "totalKgCO2e" to result.totalKgCO2e,
            "category" to result.category,
            "diff" to result.diff
        )

        col.add(map)
            .addOnSuccessListener { docRef ->
                onComplete(true, docRef.id)
            }
            .addOnFailureListener { exc ->
                onComplete(false, exc.message)
            }
    }

    /**
     * Listen for latest document (ordered by date string desc).
     * Returns ListenerRegistration which should be removed when not needed.
     */
    fun listenLatest(onChange: (EmissionResult?) -> Unit): ListenerRegistration {
        return col.orderBy("date", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val doc = snapshot.documents.first()
                    val model = doc.toObject(EmissionResult::class.java)
                    onChange(model)
                } else {
                    onChange(null)
                }
            }
    }

    /**
     * Listen full history ordered by date desc.
     */
    fun listenHistory(onChange: (List<EmissionResult>) -> Unit): ListenerRegistration {
        return col.orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onChange(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { it.toObject(EmissionResult::class.java) }
                    onChange(list)
                } else {
                    onChange(emptyList())
                }
            }
    }
}
