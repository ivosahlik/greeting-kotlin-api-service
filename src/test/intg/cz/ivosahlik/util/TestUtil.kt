package cz.ivosahlik.util

import cz.ivosahlik.dto.CourseDTO
import cz.ivosahlik.entity.Course
import cz.ivosahlik.entity.Instructor

fun courseEntityList() = listOf(
    Course(
        null,
        "Test", "Development"
    ),
    Course(
        null,
        "Kotlin", "Development",
    ),
    Course(
        null,
        "Kotlin", "Development",
    )
)

fun courseDTO(
    id: Int? = null,
    name: String = "Test",
    category: String = "Development",
    instructorId: Int? = 1
) = CourseDTO(
    id,
    name,
    category,
    instructorId
)

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(
        null,
        "Test", "Development",
        instructor
    ),
    Course(
        null,
        "Kotlin", "Development", instructor
    ),
    Course(
        null,
        "Kotlin", "Development",
        instructor
    )
)

fun instructorEntity(name : String = "Ivo Vošahlík")
= Instructor(null, name)





