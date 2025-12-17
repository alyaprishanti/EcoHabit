package ap.mobile.ecohabit.ui.eco_calculator

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}
