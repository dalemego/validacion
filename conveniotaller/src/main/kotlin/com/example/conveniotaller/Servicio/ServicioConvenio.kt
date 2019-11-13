package com.example.conveniotaller.Servicio

import com.example.conveniotaller.Modelo.Baseconvenio.*
import com.example.conveniotaller.Modelo.Convenio
import com.example.conveniotaller.Modelo.RespuestaBasica
import com.fasterxml.jackson.databind.ObjectMapper
import jdk.internal.org.objectweb.asm.TypeReference
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.HttpStatusCodeException

class ServicioConvenio {
    var log = LoggerFactory.getLogger(this::class.java)
    var restTemplate = RestTemplateBuilder().build()

    companion object {
        val URL_DB = "https://firestore.googleapis.com/v1/projects/procesosapp-fd711/databases/(default)/documents"
        val COMVENIO = "/convenio"
        val KEY = "?key=hCVHU0HFeDcr7pYzMTM2qvmzJgocVD7zLeiM0iVf"
        var QUERY = ":runQuery"
    }

    /**
     * Metodo que registra un nuevo convenio
     */
    fun registraConvenio(convenio: Convenio): RespuestaBasica<Any> {
        val respuestaBasica = RespuestaBasica<Any>()
        val header = HttpHeaders()
        val convenioDb = ConvenioDb()
        convenioDb.url = makeStringValue(convenio.url)
        convenioDb.tipoServicio = makeStringValue(convenio.tipoServicio)
        convenioDb.nombreConvenio = makeStringValue(convenio.nombreConvenio)
        convenioDb.codigoReferencia = makeStringValue(convenio.codigoReferencia)
        convenioDb.plantillaPeticionConsulta = makeStringValue(convenio.plantillaPeticionConsulta)
        convenioDb.plantillaRespuestaConsulta = makeStringValue(convenio.plantillaRespuestaConsulta)
        convenioDb.plantillaPeticionPago = makeStringValue(convenio.plantillaPeticionPago)
        convenioDb.plantillaRespuestaPago = makeStringValue(convenio.plantillaRespuestaPago)

        val requestField = Fields<ConvenioDb>(convenioDb)
        header.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity<Any>(requestField, header)
        val result = consultaConvenio(convenio.codigoReferencia)

        when {
            result == null ->
                respuestaBasica.message = "Hubo un error al consultar el nro de referencia, contacte al administrador!!"
            result is DocumentDb<*> && result.document != null-> respuestaBasica.message = "El convenio ya existe"
            else -> {
                try {
                    val url = StringBuilder(URL_DB)
                    url.append(COMVENIO)
                    url.append(KEY)
                    val response = restTemplate.postForObject(url.toString(), request, String::class.java)
                    respuestaBasica.result = true
                    respuestaBasica.message = "Convenio creado exitosamente"
                } catch (e: HttpStatusCodeException) {
                    log.error(e.responseBodyAsString)
                    respuestaBasica.message = "Hubo un error al consultar el nro de referencia, contacte al administrador!!"
                }
            }
        }
        return respuestaBasica
    }

    /**
     * Metodo que consulta la información de un convenio
     */
    fun consultaConvenio(codigoReferencia: String): Any? {
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_JSON

        var listFrom = mutableListOf<CollectionId>()
        listFrom.add(CollectionId("convenio"))
        val stringValue = StringValue()
        stringValue.stringValue = codigoReferencia
        var filter = StructuredQuery(
                Where(
                        Field(
                                FieldFilter(
                                        FieldPath("codigoReferencia"),
                                        op = "EQUAL",
                                        value = stringValue)),
                        from = listFrom))
        val map = ObjectMapper()
        val json = map.writeValueAsString(filter)
        val request = HttpEntity<Any>(filter, header)
        try {
            val url = StringBuilder(URL_DB)
            url.append(QUERY)
            url.append(KEY)
            val response = restTemplate.postForObject(url.toString(), request, List::class.java)
            log.error(response!!.toString())
            return when {
                response!!.size > 0 -> {
                    val document: Map<*, *> = map.convertValue(response?.get(0), Map::class.java)
                    val jsonResult = map.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(document)
                    log.error(jsonResult)
                    val documentDb = map.readValue(jsonResult, object : com.fasterxml.jackson.core.type.TypeReference<DocumentDb<ConvenioDb>>() {})
                    return documentDb
                }
                else -> return true
            }
        } catch (e: HttpStatusCodeException) {
            log.error(e.responseBodyAsString)
            return null
        }
    }

    /**
     * Metodo que devuelve los datos del convenio
     */
    fun getConvenio(codigoReferencia: String): Any {
        val respuestaBasica = RespuestaBasica<Any>()
        val result = consultaConvenio(codigoReferencia)
        when {
            result == null ->
                respuestaBasica.message = "Hubo un error al consultar el nro de referencia, contacte al administrador!!"
            result is DocumentDb<*> && result.document == null -> respuestaBasica.message = "No hay convenio relacionado con el codigo de referencia, contacte al administrador!!"
            result is DocumentDb<*> -> {

                val documentDb = result as DocumentDb<ConvenioDb>
                val convenio = Convenio(url = documentDb.document!!.fields!!.url.stringValue,
                        codigoReferencia = documentDb.document!!.fields!!.codigoReferencia.stringValue,
                        nombreConvenio = documentDb.document!!.fields!!.nombreConvenio.stringValue,
                        plantillaPeticionConsulta = documentDb.document!!.fields!!.plantillaPeticionConsulta.stringValue,
                        plantillaPeticionPago = documentDb.document!!.fields!!.plantillaPeticionPago.stringValue,
                        plantillaRespuestaConsulta = documentDb.document!!.fields!!.plantillaRespuestaConsulta.stringValue,
                        plantillaRespuestaPago = documentDb.document!!.fields!!.plantillaRespuestaPago.stringValue,
                        tipoServicio = documentDb.document!!.fields!!.tipoServicio.stringValue)
                respuestaBasica.result = true
                respuestaBasica.data = convenio
            }
        }

        return respuestaBasica
    }

    /**
     * Metood para construir obtetos de tipo StringValue
     */
    fun makeStringValue(value: String): StringValue {
        val stringValue = StringValue()
        stringValue.stringValue = value
        return stringValue
    }

}