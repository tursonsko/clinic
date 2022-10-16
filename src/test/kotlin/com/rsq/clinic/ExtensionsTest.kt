package com.rsq.clinic

import com.rsq.clinic.utils.containsForbiddenCharactersForData
import com.rsq.clinic.utils.containsNoCharacters
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ExtensionsTest: FunSpec({

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

})