package com.rsq.clinic.service

import com.rsq.clinic.advice.WrongPatientDataException
import com.rsq.clinic.model.dto.PatientCreateRequest
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.repository.PatientRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class PatientServiceTest : BehaviorSpec({

    val repository = mockk<PatientRepository>(relaxed = true)
    val service = PatientService(repository)

    given("wrong PatientCreateRequest data") {
        val request = PatientCreateRequest(
            firstName = "   ",
            lastName = "Turek",
            address = "Address 1/30 80-462"
        )
        `when`("trying to create new Patient") {
            then("throw WrongPatientDataException") {
                shouldThrow<WrongPatientDataException> { service.createPatient(request) }
            }
        }
    }

    given("proper patientId") {
        every { repository.findPatientById(any()) } returns Patient("Woj", "Tur", "Gdansk")
        `when`("try to find one patient") {
            val patientData = service.getPatient(UUID.randomUUID())
            then("founded Patient address") {
                patientData.address shouldBe "Gdansk"
            }
        }

    }

    given("no pageSize and no pageNumber") {
        val patient = Patient("W", "T", "Addr")
        val patients = PageImpl(mutableListOf(patient))
        every { repository.findAll(any<Pageable>()) } returns patients
        `when`("trying to get All Patients form database") {
            val result = service.getAllPatients(0, 10)
            then("result page content") {
                result.content.size shouldBeExactly 1
            }
        }
    }

//    given("todo1") {
//        `when`("") {
//            then("") {
//
//            }
//        }
//    }

})
