package com.rsq.clinic.model.dto

interface PersonCreateRequest {
    val firstName: String
    val lastName: String

    fun checkRequestFields()
}