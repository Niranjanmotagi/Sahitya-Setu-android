package com.example.app.model

data class Poem(
    val title: String,
    val titleEn: String,
    val author: String,
    val authorEn: String,
    val content: String,
    val contentEn: String,
    val meaning: String,
    val meaningEn: String,
    val audio: String? = null
)