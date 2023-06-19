package cz.ivosahlik.controller

import cz.ivosahlik.repository.CourseRepository
import cz.ivosahlik.repository.InstructorRepository
import cz.ivosahlik.util.PostgreSQLContainerInitializer
import cz.ivosahlik.util.courseEntityList
import cz.ivosahlik.util.instructorEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
//@Testcontainers
internal class CourseControllerIntgTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

//    companion object {
//      @Container
//      val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
//          withDatabaseName("testdb")
//          withUsername("postgres")
//          withPassword("secret")
//      }
//
//      @JvmStatic
//      @DynamicPropertySource
//      fun properties(registry: DynamicPropertyRegistry) {
//          registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
//          registry.add("spring.datasource.username", postgresDB::getUsername)
//          registry.add("spring.datasource.password", postgresDB::getPassword)
//      }
//    }

    @BeforeEach
    fun setUp(){
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addInstructor() {

        val instructor = instructorRepository.findInstructorByName("Ivo Vošahlík")

        //given
        val courseDTO = cz.ivosahlik.dto.CourseDTO(
            null,
            "Build RestFul APis using Spring Boot and Kotlin", "Ivo Vošahlík",
            instructor.id
        )

        //when
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(cz.ivosahlik.dto.CourseDTO::class.java)
            .returnResult()
            .responseBody

        //then
        assertTrue {
            savedCourseDTO!!.id!=null
        }
    }


    @Test
    fun addCourse_InvlaidOInstructorId() {

        //given
        val courseDTO = cz.ivosahlik.dto.CourseDTO(
            null,
            "Build RestFul APis using Spring Boot and Kotlin", "Ivo Vošahlík",
            999
        )

        //when
        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        //then
        assertEquals("Instructor Id is not Valid!", response)
    }

    @Test
     fun retrieveAllCourses() {


        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(cz.ivosahlik.dto.CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs : $courseDTOs")

        assertEquals(3, courseDTOs!!.size)

    }

    @Test
    fun retrieveAllCourses_ByName() {

        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name", "SpringBoot")
            .toUriString()

        val courseDTOs = webTestClient
            .get()
            .uri(uri)
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

        val instructor = instructorRepository.findInstructorByName("Ivo Vošahlík")
        val courseEntity = cz.ivosahlik.entity.Course(
            null,
            "Apache Kafka for Developers using Spring Boot", "Development",
            instructor
        )
        courseRepository.save(courseEntity)
        val updatedCourseEntity = cz.ivosahlik.entity.Course(
            null,
            "Apache Kafka for Developers using Spring Boot1", "Development"
        )

        val updatedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", courseEntity.id)
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

        val instructor = instructorRepository.findInstructorByName("Ivo Vošahlík")
        val courseEntity = cz.ivosahlik.entity.Course(
            null,
            "Apache Kafka for Developers using Spring Boot", "Development",
            instructor
        )

        courseRepository.save(courseEntity)
        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", courseEntity.id)
            .exchange()
            .expectStatus().isNoContent

    }
}