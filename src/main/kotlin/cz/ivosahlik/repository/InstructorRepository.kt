package cz.ivosahlik.repository

import cz.ivosahlik.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {

    fun findInstructorByName(name : String) : Instructor
}