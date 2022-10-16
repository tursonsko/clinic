package com.rsq.clinic.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rsq.clinic.model.dto.VisitCreateRequest
import com.rsq.clinic.model.dto.VisitResponse
import com.rsq.clinic.model.dto.VisitUpdateRequest
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.repository.DoctorRepository
import com.rsq.clinic.repository.PatientRepository
import com.rsq.clinic.repository.VisitRepository
import com.rsq.clinic.service.VisitService
import com.rsq.clinic.testutils.RestResponsePage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.beBlank
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class VisitControllerTest(
    mvc: MockMvc,
    mapper: ObjectMapper,
    patientRepository: PatientRepository,
    doctorRepository: DoctorRepository,
    visitRepository: VisitRepository,
    visitService: VisitService
) : FunSpec({

    afterTest {
        patientRepository.deleteAll()
        doctorRepository.deleteAll()
        visitRepository.deleteAll()
    }

    test("should create new visit related to patient and doctor ids") {
        //given
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatientData = patientRepository.save(patient).toPatientData()
        val savedPatientId = savedPatientData.id

        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctor = doctorRepository.save(doctor).toDoctorData()
        val savedDoctorId = savedDoctor.id

        val visitCreateRequest = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 20),
            visitTime = LocalTime.of(17, 30),
            spot = "Clinic Luxmed"
        )
        val visitBody = mapper.writeValueAsString(visitCreateRequest)

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.post("/api/visit")
                .param("patientId", "$savedPatientId")
                .param("doctorId", "$savedDoctorId")
                .content(visitBody)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        //then
        val responseBody = mapper.readValue(result.response.contentAsString, VisitResponse::class.java)
        responseBody.spot shouldBe visitCreateRequest.spot
        responseBody.visitDate shouldBe visitCreateRequest.visitDate
        responseBody.visitTime shouldBe visitCreateRequest.visitTime

        val patientResponseBody = responseBody.patient
        patientResponseBody?.id shouldBe savedPatientId

        val doctorResponseBody = responseBody.doctor
        doctorResponseBody?.id shouldBe savedDoctorId

    }

    test("should change visit time") {

        //given
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatientData = patientRepository.save(patient).toPatientData()
        val savedPatientId = savedPatientData.id

        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctorData = doctorRepository.save(doctor).toDoctorData()
        val savedDoctorId = savedDoctorData.id

        val visitCreateRequest = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 20),
            visitTime = LocalTime.of(17, 30),
            spot = "Clinic Luxmed"
        )
        val visitToUpdate = visitService.createVisit(visitCreateRequest, savedPatientId!!, savedDoctorId!!)

        val visitUpdateRequest = VisitUpdateRequest(
            visitId = visitToUpdate.id!!,
            visitDate = LocalDate.of(2023, Month.NOVEMBER, 30),
            visitTime = LocalTime.of(15, 0,0),
        )
        val visitBody = mapper.writeValueAsString(visitUpdateRequest)


        //when
        mvc.perform(
            MockMvcRequestBuilders.put("/api/visit/changeTime")
                .content(visitBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)

        //then
        val updatedVisit = visitRepository.findAll().first()
        updatedVisit.visitTime shouldBe visitUpdateRequest.visitTime
    }

    test("should get all visits with PatientId parameter") {

        //given
        //patients
        val patient1 = Patient(
            firstName = "John",
            lastName = "Zoe",
            address = "Gdansk"
        )
        val savedPatientData1 = patientRepository.save(patient1).toPatientData()
        val savedPatientId1 = savedPatientData1.id

        val patient2 = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatientData2 = patientRepository.save(patient2).toPatientData()
        val savedPatientId2 = savedPatientData2.id

        //doctor
        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctorData = doctorRepository.save(doctor).toDoctorData()
        val savedDoctorId = savedDoctorData.id

        //visits
        val visitCreateRequest1 = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 25),
            visitTime = LocalTime.of(14, 30),
            spot = "Clinic Luxmed"
        )
        val visit1 = visitService.createVisit(visitCreateRequest1, savedPatientId1!!, savedDoctorId!!)

        val visitCreateRequest2 = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 23),
            visitTime = LocalTime.of(17, 30),
            spot = "Clinic Luxmed"
        )
        val visit2 = visitService.createVisit(visitCreateRequest2, savedPatientId2!!, savedDoctorId)

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/api/visit")
                .param("patientId", "$savedPatientId1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseBody = mapper.readValue(result.response.contentAsString, RestResponsePage::class.java)
        responseBody.content.size shouldBeExactly 1
        val firstElementFromContentList = responseBody.content.elementAt(0) as LinkedHashMap<*, *>
        firstElementFromContentList["visitDate"] shouldBe LocalDate.of(2022, Month.OCTOBER, 25).toString()

    }

    test("should get all visits without PatientId parameter") {
        //given
        //patients
        val patient1 = Patient(
            firstName = "John",
            lastName = "Zoe",
            address = "Gdansk"
        )
        val savedPatientData1 = patientRepository.save(patient1).toPatientData()
        val savedPatientId1 = savedPatientData1.id

        val patient2 = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatientData2 = patientRepository.save(patient2).toPatientData()
        val savedPatientId2 = savedPatientData2.id

        //doctor
        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctorData = doctorRepository.save(doctor).toDoctorData()
        val savedDoctorId = savedDoctorData.id

        //visits
        val visitCreateRequest1 = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 25),
            visitTime = LocalTime.of(14, 30),
            spot = "Clinic Luxmed"
        )
        val visit1 = visitService.createVisit(visitCreateRequest1, savedPatientId1!!, savedDoctorId!!)

        val visitCreateRequest2 = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 23),
            visitTime = LocalTime.of(17, 30),
            spot = "Clinic Luxmed"
        )
        val visit2 = visitService.createVisit(visitCreateRequest2, savedPatientId2!!, savedDoctorId)

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/api/visit")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val responseBody = mapper.readValue(result.response.contentAsString, RestResponsePage::class.java)
        responseBody.content.size shouldBeExactly 2
        val firstElementFromContentList = responseBody.content.elementAt(0) as LinkedHashMap<*, *>
        firstElementFromContentList["visitDate"] shouldBe LocalDate.of(2022, Month.OCTOBER, 23).toString()
    }

    test("should remove single visit") {
        //given
        val patient = Patient(
            firstName = "John",
            lastName = "Doe",
            address = "Gdansk"
        )
        val savedPatientData = patientRepository.save(patient).toPatientData()
        val savedPatientId = savedPatientData.id

        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctorData = doctorRepository.save(doctor).toDoctorData()
        val savedDoctorId = savedDoctorData.id

        val visitCreateRequest = VisitCreateRequest(
            visitDate = LocalDate.of(2022, Month.OCTOBER, 20),
            visitTime = LocalTime.of(17, 30),
            spot = "Clinic Luxmed"
        )
        val visitToRemove = visitService.createVisit(visitCreateRequest, savedPatientId!!, savedDoctorId!!)
        val visitToRemoveId = visitToRemove.id

        val result = mvc.perform(
            MockMvcRequestBuilders.delete("/api/visit/$visitToRemoveId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()

        result.response.contentAsString should beBlank()
    }

})
