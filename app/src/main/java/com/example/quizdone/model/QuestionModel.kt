package com.example.quizdone.model

data class QuestionModel(
    val subject: String,
    val title : String,
    val options : HashMap<String , Boolean>
)
