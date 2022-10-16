package com.rsq.clinic

import com.rsq.clinic.utils.containsForbiddenCharactersForData
import com.rsq.clinic.utils.containsNoCharacters
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

//todo remove
//@SpringBootTest
//@ActiveProfiles("test")
class Sandbox: FunSpec({

//    val repository = mockk<NumberRepository>()
//    val service = ServiceBasic(repository)
//
//    afterTest {
//        patientRepository.deleteAll()
//    }
//
//    test("test1") {
//        service.addTwoIntegers(2, 5) shouldBe 7
//    }
//
//    test("Saves to repository") {
//        every { repository.addNumber(any()) } just Runs
//        service.saveNumber(1)
//        verify(exactly = 1) { repository.addNumber(NumberBasic(1)) }
//    }
//
//    test("test2") {
//        every { repository.getNumbers() } returns listOf(NumberBasic(360))
//        println(service.getNumbers())
//        service.getNumbers() shouldBe listOf(NumberBasic(360))
//    }

    // todo make BDD tests from this case
    test("test matcher only proper chars in string Name and LastName") {

        val nameOnlyNumbers = "44423"
        nameOnlyNumbers.containsForbiddenCharactersForData() shouldBe true

        val nameWithSpaces = "\t"
        nameWithSpaces.containsNoCharacters() shouldBe true

        val nameWithForbiddenChars = "#\\Wojtek%$"
        nameWithForbiddenChars.containsForbiddenCharactersForData() shouldBe true

        val nameWithForbiddenChars2 = "Wojtek1"
        nameWithForbiddenChars2.containsForbiddenCharactersForData() shouldBe true

        val nameWithForbiddenChars3 = "Wojtek."
        nameWithForbiddenChars3.containsForbiddenCharactersForData() shouldBe true

        val nameWithForbiddenChars4 = " "
        nameWithForbiddenChars4.containsForbiddenCharactersForData() shouldBe false
    }

/*    //sandbox

    val doctorData = doctorService.getAllDoctors(0, 10).content
        .flatMap { it.visits ?: listOf() }

    //sandbox*/

})