package com.rsq.clinic.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.rsq.clinic.model.dto.DoctorCreateRequest
import com.rsq.clinic.model.dto.DoctorResponse
import com.rsq.clinic.model.dto.DoctorUpdateRequest
import com.rsq.clinic.model.entity.Doctor
import com.rsq.clinic.model.entity.Patient
import com.rsq.clinic.repository.DoctorRepository
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DoctorControllerTest(
    mvc: MockMvc,
    mapper: ObjectMapper,
    doctorRepository: DoctorRepository
) : FunSpec({

    afterTest {
        doctorRepository.deleteAll()
    }

    test("should create single doctor") {
        //given
        val doctorCreateRequest = DoctorCreateRequest(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val doctorBody = mapper.writeValueAsString(doctorCreateRequest)

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.post("/api/doctor")
            .content(doctorBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        //then
        val responseBody = mapper.readValue(result.response.contentAsString, DoctorResponse::class.java)
        responseBody.firstName.shouldBe(doctorCreateRequest.firstName)
        responseBody.lastName.shouldBe(doctorCreateRequest.lastName)
        responseBody.specialization.shouldBe(doctorCreateRequest.specialization)
    }

    test("should find single doctor") {
        //given
        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctorData = doctorRepository.save(doctor).toDoctorData()
        val savedDoctorId = savedDoctorData.id

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/api/doctor/$savedDoctorId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        //then
        val responseBody = mapper.readValue(
            result.response.contentAsString,
            DoctorResponse::class.java
        )
        responseBody.shouldBe(savedDoctorData)
    }

    test("should update single doctor's specialization") {
        //given
        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctor = doctorRepository.save(doctor)
        val savedDoctorId = savedDoctor.id

        val doctorUpdateRequest = DoctorUpdateRequest(specialization = "Gastrologist")
        val doctorBody = mapper.writeValueAsString(doctorUpdateRequest)

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.put("/api/doctor/$savedDoctorId")
            .content(doctorBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        //then
        val responseBody = mapper.readValue(
            result.response.contentAsString,
            DoctorResponse::class.java
        )
        responseBody.specialization.shouldBe(doctorUpdateRequest.specialization)
    }

    test("should remove single doctor") {
        //given
        val doctor = Doctor(
            firstName = "John",
            lastName = "Doe",
            specialization = "Surgeon"
        )
        val savedDoctor = doctorRepository.save(doctor)
        val savedDoctorId = savedDoctor.id

        //when & then
        val result = mvc.perform(
            MockMvcRequestBuilders.delete("/api/doctor/$savedDoctorId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andReturn()

        result.response.contentAsString should beBlank()
    }

    test("should find all doctors sorted and paginated nd return 4 of 5 caused by pageSize limit") {

        //given
        val doctorsList = listOf(
            Doctor(
                firstName = "John",
                lastName = "Doe",
                specialization = "Surgeon"
            ),
            Doctor(
                firstName = "Adam",
                lastName = "Zoe",
                specialization = "Surgeon"
            ),
            Doctor(
                firstName = "Zinedine",
                lastName = "Yoe",
                specialization = "Surgeon"
            ),
            Doctor(
                firstName = "Freddie",
                lastName = "Yoe",
                specialization = "Surgeon"
            ),
            Doctor(
                firstName = "John",
                lastName = "Soe",
                specialization = "Surgeon"
            )
        )
        doctorRepository.saveAll(doctorsList)

        //when
        val result = mvc.perform(
            MockMvcRequestBuilders.get("/api/doctor")
            .param("pageNumber", "0")
            .param("pageSize", "4")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        //then
        val responseBody = mapper.readValue(result.response.contentAsString, RestResponsePage::class.java)
        println(responseBody.content)
        val firstElementFromContentList = responseBody.content.elementAt(0) as LinkedHashMap<*, *>
        val lastElementFromContentList = responseBody.content.elementAt(3) as LinkedHashMap<*, *>

        responseBody.content.size shouldBeExactly 4
        firstElementFromContentList["lastName"] shouldBe "Doe"
        lastElementFromContentList["firstName"] shouldBe "Zinedine"
        lastElementFromContentList["lastName"] shouldBe "Yoe"

    }

})
