package cz.ivosahlik.controller

import com.ninjasquad.springmockk.MockkBean
import cz.ivosahlik.service.CourseService
import cz.ivosahlik.util.courseDTO
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(CourseController::class)
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        //given
        val courseDTO = courseDTO()

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id=1)

        //when
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(cz.ivosahlik.dto.CourseDTO::class.java)
            .returnResult()
            .responseBody

        //then
        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun addCourse_validation() {
        //given
        val courseDTO = courseDTO(name = "", category = "")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id=1)

        //when
        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        println("response : $response")
        assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank"
        , response)
    }

    @Test
    fun addCourse_runtime_exception() {
        //given
        val courseDTO = courseDTO()
        val errorMessage = "Unexpected Error Occurred!"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        //when
        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage
            , response)
    }



    @Test
    fun retrieveAllCourses() {

        every { courseServiceMock.retrieveAllCourses(any()) }.returnsMany(
            listOf(
                cz.ivosahlik.dto.CourseDTO(
                    1,
                    "Build RestFul APis using Spring Boot and Kotlin", "Development",
                    1
                ),
                cz.ivosahlik.dto.CourseDTO(
                    2,
                    "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development",
                    1
                )
            )
        )


        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(cz.ivosahlik.dto.CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs : $courseDTOs")

        assertEquals(2, courseDTOs!!.size)

    }

    @Test
    fun updateCourse() {

        val updatedCourseEntity = cz.ivosahlik.entity.Course(
            null,
            "Apache Kafka for Developers using Spring Boot1", "Development"
        )

        every { courseServiceMock.updateCourse(any(), any()) } returns cz.ivosahlik.dto.CourseDTO(
            100,
            "Apache Kafka for Developers using Spring Boot1", "Development",
            1
        )


        val updatedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseEntity)
            .exchange()
            .expectStatus().isOk
            .expectBody(cz.ivosahlik.dto.CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Apache Kafka for Developers using Spring Boot1", updatedCourseDTO?.name)

    }

    @Test
    fun deleteCourse() {

        every { courseServiceMock.deleteCourse(any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent

        verify(exactly = 1) { courseServiceMock.deleteCourse(any()) }

    }

}