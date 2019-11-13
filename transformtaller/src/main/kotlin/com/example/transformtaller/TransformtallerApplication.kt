package com.example.transformtaller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class TransformtallerApplication

fun main(args: Array<String>) {
    runApplication<TransformtallerApplication>(*args)
}
