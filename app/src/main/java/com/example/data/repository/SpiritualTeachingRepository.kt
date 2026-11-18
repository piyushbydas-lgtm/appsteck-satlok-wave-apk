package com.example.data.repository

import com.example.data.model.SpiritualTeaching
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object SpiritualTeachingRepository {
    private val teachingsCollection by lazy {
        FirebaseFirestore.getInstance().collection("spiritual_teachings")
    }

    fun observeTeachings(): Flow<List<SpiritualTeaching>> = callbackFlow {
        val listener = teachingsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SpiritualTeaching::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addTeaching(teaching: SpiritualTeaching): Result<String> {
        return try {
            val docRef = teachingsCollection.add(teaching).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTeaching(teaching: SpiritualTeaching): Result<Unit> {
        return try {
            if (teaching.id.isBlank()) {
                return Result.failure(IllegalArgumentException("Teaching ID cannot be blank"))
            }
            teachingsCollection.document(teaching.id).set(teaching).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTeaching(id: String): Result<Unit> {
        return try {
            teachingsCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
