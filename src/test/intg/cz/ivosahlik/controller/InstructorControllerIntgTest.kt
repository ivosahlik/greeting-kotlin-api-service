package cz.ivosahlik.controller

import cz.ivosahlik.dto.InstructorDTO
import cz.ivosahlik.util.PostgreSQLContainerInitializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class InstructorControllerIntgTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient
    @Test
    fun addInstructor() {

        val instructorDTO = InstructorDTO(null, "Ivo Vošahlík")


        //when
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
}