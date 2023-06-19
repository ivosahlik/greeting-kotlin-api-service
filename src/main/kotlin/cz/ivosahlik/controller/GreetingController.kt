package cz.ivosahlik.controller

import cz.ivosahlik.service.GreetingService
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(val greetingService: GreetingService) {

    companion object : KLogging()
    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable("name") name : String): String = greetingService.retrieveGreeting(name)
}