package com.balionis.aws13.service

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class AppLambdaHandlerTest : AnnotationSpec() {

    @Test
    fun testMe() {
        val msg = "myMsg"

        val subject = AppLambdaHandler()

        val actual = subject.handle(msg)

        actual shouldBe msg.toUpperCase()
    }
}
