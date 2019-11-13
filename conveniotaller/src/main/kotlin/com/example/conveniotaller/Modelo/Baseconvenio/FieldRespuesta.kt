package com.example.conveniotaller.Modelo.Baseconvenio

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class FieldRespuesta<T>{
    lateinit var name: String
    var fields: T? = null
}