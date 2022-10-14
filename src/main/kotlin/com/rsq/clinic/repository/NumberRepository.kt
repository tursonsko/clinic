package com.rsq.clinic.repository

import com.rsq.clinic.model.entity.NumberBasic
import org.springframework.stereotype.Repository
//todo remove
@Repository
class NumberRepository {

    private val numbers = mutableListOf<NumberBasic>()

    fun getNumbers(): List<NumberBasic> = numbers

    fun addNumber(number: NumberBasic) {
        numbers.add(number)
    }
}