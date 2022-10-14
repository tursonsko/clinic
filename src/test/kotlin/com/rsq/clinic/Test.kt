package com.rsq.clinic

import com.rsq.clinic.model.entity.NumberBasic
import com.rsq.clinic.repository.NumberRepository
import com.rsq.clinic.repository.PatientRepository
import com.rsq.clinic.service.ServiceBasic
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

//todo remove
@SpringBootTest
@ActiveProfiles("test")
class Test(
    patientRepository: PatientRepository
) : FunSpec({

    val repository = mockk<NumberRepository>()
    val service = ServiceBasic(repository)

    afterTest {
        patientRepository.deleteAll()
    }

    test("test1") {
        service.addTwoIntegers(2, 5) shouldBe 7
    }

    test("Saves to repository") {
        every { repository.addNumber(any()) } just Runs
        service.saveNumber(1)
        verify(exactly = 1) { repository.addNumber(NumberBasic(1)) }
    }

    test("test2") {
        every { repository.getNumbers() } returns listOf(NumberBasic(360))
        println(service.getNumbers())
        service.getNumbers() shouldBe listOf(NumberBasic(360))
    }

})