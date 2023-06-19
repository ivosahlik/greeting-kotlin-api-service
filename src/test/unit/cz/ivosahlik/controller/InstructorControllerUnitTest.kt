package cz.ivosahlik.controller

import com.ninjasquad.springmockk.MockkBean
import cz.ivosahlik.dto.InstructorDTO
import cz.ivosahlik.service.InstructorService
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(InstructorController::class)
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMock: InstructorService


    @Test
    fun addInstructor() {
        //given
        val instructorDTO = InstructorDTO(null, "Ivo Vošahlík")

        every { instructorServiceMock.addNewInstructor(any()) } returns InstructorDTO(
            1,
            "Ivo Vošahlík"
        )

        val savedInstructorDTO = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        //then
        Assertions.assertTrue {
            savedInstructorDTO!!.id != null
        }
    }

    @Test
    fun addInstructor_Validation() {
        //given
        val instructorDTO = InstructorDTO(null, "")

        every { instructorServiceMock.addNewInstructor(any()) } returns InstructorDTO(
            1,
            "Ivo Vošahlík"
        )

        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        //then
        assertEquals("instructorDTO.name must not be blank",response)
    }
}