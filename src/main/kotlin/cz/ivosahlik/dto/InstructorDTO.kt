package cz.ivosahlik.dto

import javax.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "instructorDTO.name must not be blank")
    val name: String
)