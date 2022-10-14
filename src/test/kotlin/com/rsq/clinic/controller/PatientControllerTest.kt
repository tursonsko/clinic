package com.rsq.clinic.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rsq.clinic.model.dto.PatientCreateRequest
import com.rsq.clinic.model.dto.PatientResponse
import com.rsq.clinic.model.dto.PatientUpdateRequest
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.repository.PatientRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.beBlank
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PatientControllerTest(
    mvc: MockMvc,
    mapper: ObjectMapper,
    patientRepository: PatientRepository,
) : FunSpec({

    test("should create single patient") {
        //given
        val patientCreateRequest = PatientCreateRequest(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val patientBody = mapper.writeValueAsString(patientCreateRequest)

        //when
        val result = mvc.perform(post("/api/patient")
            .content(patientBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andReturn()

        //then
        val responseBody = mapper.readValue(result.response.contentAsString, PatientResponse::class.java)
        responseBody.firstName.shouldBe(patientCreateRequest.firstName)
        responseBody.lastName.shouldBe(patientCreateRequest.lastName)
        responseBody.address.shouldBe(patientCreateRequest.address)
    }

    test("should find single patient") {
        //given
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatientData = patientRepository.save(patient).toPatientData()
        val savedPatientId = savedPatientData.id

        //when
        val result = mvc.perform(get("/api/patient/$savedPatientId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()

        //then
        val responseBody = mapper.readValue(
            result.response.contentAsString,
            PatientResponse::class.java
        )
        responseBody.shouldBe(savedPatientData)
    }

    test("should update single patient's address") {
        //given
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatient = patientRepository.save(patient)
        val savedPatientId = savedPatient.id

        val patientUpdateRequest = PatientUpdateRequest(address = "Poznan")
        val patientBody = mapper.writeValueAsString(patientUpdateRequest)

        //when
        val result = mvc.perform(put("/api/patient/$savedPatientId")
            .content(patientBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()

        //then
        val responseBody = mapper.readValue(
            result.response.contentAsString,
            PatientResponse::class.java
        )
        responseBody.address.shouldBe(patientUpdateRequest.address)
    }

    test("should remove single patient") {
        //given
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatient = patientRepository.save(patient)
        val savedPatientId = savedPatient.id

        //when & then
        val result = mvc.perform(delete("/api/patient/$savedPatientId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()

        result.response.contentAsString should beBlank()
    }

})
