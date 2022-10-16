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
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handlePatientNotFoundException(ex: PatientNotFoundException) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.NOT_FOUND.value(), ex.message, Instant.now()),
            HttpStatus.NOT_FOUND
        )

    @ExceptionHandler
    fun handlePatientNotCreatedException(ex: PatientNotCreatedException) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleWrongPatientDataException(ex: WrongPatientDataException) =
        ResponseEntity(ErrorMultipleMessagesModel(HttpStatus.BAD_REQUEST.value(), ex.messages, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleDoctorNotFoundException(ex: DoctorNotFoundException) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.NOT_FOUND.value(), ex.message, Instant.now()),
            HttpStatus.NOT_FOUND
        )

    @ExceptionHandler
    fun handleDoctorNotCreatedException(ex: DoctorNotCreatedException) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleWrongDoctorDataException(ex: WrongDoctorDataException) =
        ResponseEntity(ErrorMultipleMessagesModel(HttpStatus.BAD_REQUEST.value(), ex.messages, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleDoctorUnavailableAtThisTime(ex: DoctorUnavailableAtThisTime) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleVisitNotCreatedException(ex: VisitNotCreatedException) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handleVisitNotUpdatedException(ex: VisitNotUpdatedException) =
        ResponseEntity(ErrorSingleMessageModel(HttpStatus.BAD_REQUEST.value(), ex.message, Instant.now()),
            HttpStatus.BAD_REQUEST
        )
}

class PatientNotFoundException(message: String) : RuntimeException(message)
class PatientNotCreatedException(message: String) : RuntimeException(message)
class WrongPatientDataException(val messages: List<String>) : RuntimeException()

class DoctorNotFoundException(message: String) : RuntimeException(message)
class DoctorNotCreatedException(message: String) : RuntimeException(message)
class WrongDoctorDataException(val messages: List<String>) : RuntimeException()
class DoctorUnavailableAtThisTime(message: String) : RuntimeException(message)

class VisitNotCreatedException(message: String) : RuntimeException(message)
class VisitNotUpdatedException(message: String) : RuntimeException(message)



data class ErrorSingleMessageModel(
    val status: Int? = null,
    val message: String? = null,
    val time: Instant? = null
)

data class ErrorMultipleMessagesModel(
    val status: Int? = null,
    val messages: List<String?>? = null,
    val time: Instant? = null
)