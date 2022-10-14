package com.rsq.clinic.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleIllegalStateException(ex: IllegalStateException) =
        ResponseEntity(ErrorMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleRuntimeException(ex: PatientNotFoundException) =
        ResponseEntity(ErrorMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )
}

class PatientNotFoundException(message: String) : RuntimeException(message)


data class ErrorMessageModel(
    val status: Int? = null,
    val message: String? = null,
    val time: Instant? = null
)