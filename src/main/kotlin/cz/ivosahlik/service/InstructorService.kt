package cz.ivosahlik.service

import cz.ivosahlik.dto.InstructorDTO
import cz.ivosahlik.entity.Instructor
import cz.ivosahlik.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {

    fun addNewInstructor(instructorDTO: InstructorDTO): InstructorDTO {

        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        instructorRepository.save(instructorEntity)

        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun findInstructorById(instructorId: Int): Optional<Instructor> = instructorRepository.findById(instructorId)
}