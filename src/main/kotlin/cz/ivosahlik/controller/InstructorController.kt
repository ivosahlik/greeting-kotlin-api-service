package cz.ivosahlik.controller

import cz.ivosahlik.dto.InstructorDTO
import cz.ivosahlik.service.InstructorService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(val instructorService: InstructorService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor( @Valid @RequestBody instructorDTO: InstructorDTO): InstructorDTO
    = instructorService.addNewInstructor(instructorDTO)

}