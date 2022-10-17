package com.rsq.clinic.service

import com.rsq.clinic.advice.WrongDoctorDataException
import com.rsq.clinic.model.dto.DoctorCreateRequest
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.repository.DoctorRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class DoctorServiceTest : BehaviorSpec({

    val repository = mockk<DoctorRepository>(relaxed = true)
    var doctor = mockk<Doctor>(relaxed = true)
    var doctorCreateRequest = mockk<DoctorCreateRequest>(relaxed = true)
    val service = DoctorService(repository)

    beforeTest {
        doctor = Doctor("W", "T", "Add")
        doctorCreateRequest = DoctorCreateRequest(
            firstName = "Ryan",
            lastName = "Reynolds",
            specialization = "Spec"
        )
    }

    given("wrong DoctorCreateRequest data") {
        val wrongRequest = DoctorCreateRequest(
            firstName = "2Ryan",
            lastName = "Reynolds",
            specialization = "Spec"
        )
        `when`("trying to create new Doctor") {
            then("throw WrongDoctorDataException") {
                shouldThrow<WrongDoctorDataException> { service.createDoctor(wrongRequest) }

            }
        }
    }

    given("proper DoctorCreateRequest data") {
        every { repository.save(doctor) } returns doctor
        `when`("trying to create new Doctor") {
            service.createDoctor(doctorCreateRequest)
            then("throw WrongDoctorDataException") {
                verify(exactly = 1) {repository.save(doctor)}
            }
        }
    }

})
