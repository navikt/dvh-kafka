package no.nav.dvh.kafka.consumer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
class NaisController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("isAlive")
    void isAlive() {
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("isReady")
    void isReady() {
    }
}