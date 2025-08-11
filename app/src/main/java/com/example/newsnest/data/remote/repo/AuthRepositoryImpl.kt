package com.example.newsnest.data.remote.repo

import android.util.Log
import androidx.core.net.toUri
import com.example.newsnest.domain.repo.AuthRepository
import com.example.newsnest.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        imageUri: String,
        username: String
    ): Result<Unit> = suspendCancellableCoroutine { cont ->

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                Log.d(TAG, "registerUser: ${authResult.user?.email}")
                if (uid == null) {
                    cont.resume(Result.Error("User ID is null"))
                    return@addOnSuccessListener
                }

                val ref = storage.reference.child("profile_pictures/$uid.jpg")
                ref.putFile(imageUri.toUri())
                    .addOnSuccessListener {
                        ref.downloadUrl
                            .addOnSuccessListener { uri ->
                                Log.d(TAG, "registerUser: $uri")
                                val userMap = mapOf(
                                    "username" to username,
                                    "profilePictureUrl" to uri.toString(),
                                    "uid" to uid
                                )
                                firestore.collection("users").document(uid).set(userMap)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "registerUser: success")
                                        cont.resume(Result.Success(Unit))
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d(TAG, "registerUser: failure ${e.message}")
                                        cont.resume(Result.Error(e.message))
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.d(TAG, "registerUser: failure ${e.message}")
                                cont.resume(Result.Error(e.message))
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.d(TAG, "registerUser: failure ${e.message}")
                        cont.resume(Result.Error(e.message))
                    }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "registerUser: failure ${e.message}")
                cont.resume(Result.Error(e.message))
            }
            .addOnCanceledListener {
                cont.resume(Result.Error("Registration canceled"))
            }
    }

    companion object {
        const val TAG: String = "AuthRepositoryImpl"
    }

}