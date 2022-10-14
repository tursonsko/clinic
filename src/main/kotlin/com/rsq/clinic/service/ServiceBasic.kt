package com.rsq.clinic.service

import com.rsq.clinic.model.entity.NumberBasic
import com.rsq.clinic.repository.NumberRepository
import org.springframework.stereotype.Service
//todo remove
@Service
class ServiceBasic(val repository: NumberRepository) {

    fun addTwoIntegers(num: Int, num2: Int): Int {
        return num + num2
    }

    fun saveNumber(number: Int) = repository.addNumber(NumberBasic(number))

    fun getNumbers() = repository.getNumbers()
}