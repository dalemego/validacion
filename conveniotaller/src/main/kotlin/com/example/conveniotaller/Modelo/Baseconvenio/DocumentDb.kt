package com.example.conveniotaller.Modelo.Baseconvenio

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonIgnoreProperties(ignoreUnknown = true)
class DocumentDb<T>{
    var document: FieldRespuesta<T>? = null
}