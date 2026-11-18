package com.example.data.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class SpiritualTeaching(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val category: String = "Satsang",
    val author: String = "संत रामपाल जी महाराज",
    val mediaUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = "",
    val isPublished: Boolean = true
) : Serializable
