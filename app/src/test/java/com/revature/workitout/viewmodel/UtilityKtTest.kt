package com.revature.workitout.viewmodel

import com.revature.workitout.viewmodel.utility.makeUppercase
import junit.framework.TestCase
import org.junit.Test

class UtilityKtTest : TestCase() {

    @Test
    fun testMakeUppercase() {
        var str = "test to uppercase"

        str = makeUppercase(str)

        assertEquals("Test To Uppercase", str)

    }
}