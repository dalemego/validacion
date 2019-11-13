package com.example.transformtaller.Servicio.Impl

import com.example.transformtaller.Modelo.ConsultaPago
import com.example.transformtaller.Modelo.PlantillaConvenio
import com.example.transformtaller.Modelo.RespuestaBasica
import com.example.transformtaller.Servicio.Api.IServicioPlantillaConvenio
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.HttpStatusCodeException
import com.schibsted.spt.data.jslt.Parser
import com.fasterxml.jackson.databind.ObjectMapper

class ServicioPlantillaConvenioRest(var URL: String) : IServicioPlantillaConvenio {
    var log = LoggerFactory.getLogger(this::class.java)
    var restTemplate = RestTemplateBuilder().build()

    /**
     * Metodo que obtiene la informacion del nro de referencia seleccionado
     */
    override fun getReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago {
            val response = requestPeticion(plantillaConvenio)
            return when {
                response == null -> ConsultaPago(message = "Hubo un error, contacta al administrador",
                        result = false,
                        totalPago = "",
                        referenciaFactura = plantillaConvenio.nroReferencia)
                !response.result -> return ConsultaPago()
                else -> {
                    val resultJson = getResultFromJslt(response.response, plantillaConvenio.plantillaRespuesta)
                    val consultaPago = getObjectFromJson(resultJson)
                    consultaPago.referenciaFactura = plantillaConvenio.nroReferencia
                    consultaPago.result = true
                    consultaPago.message = String()
                    return consultaPago
                }
            }
    }

    /**
     * Metodo que realiza la peticion de pago
     */
    override fun postReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago {
        val response = requestPeticion(plantillaConvenio)
        return when {
            response == null -> ConsultaPago(message = "Hubo un error, contacta al administrador",
                    result = false,
                    totalPago = "",
                    referenciaFactura = plantillaConvenio.nroReferencia)
            !response.result -> ConsultaPago()
            else -> {
                val resultJson = getResultFromJslt(response.response, plantillaConvenio.plantillaRespuesta)
                val consultaPago = getObjectFromJson(resultJson)
                consultaPago.result = true
                consultaPago
            }
        }
    }

    /**
     * Metodo que realiza la peticion
     */
    fun requestPeticion(plantillaConvenio: PlantillaConvenio): RespuestaBasica?{
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity<Any>(replaceDataPeticin(plantillaConvenio), header)
        try {
            val response = restTemplate.postForObject(URL, request, RespuestaBasica::class.java)
            return response
        } catch (e: HttpStatusCodeException) {
            return null
        }
    }

    /**
     * Metodo que cambia o remplaza una parte de la plantilla por el valor real
     */
    fun replaceDataPeticin(plantillaConvenio: PlantillaConvenio): PlantillaConvenio{
        plantillaConvenio.url = plantillaConvenio.url.replace("{nroReferencia}", plantillaConvenio.nroReferencia)
        return plantillaConvenio
    }

    /**
     * Metodo que transforma la respuesta por medio de xlst
     */
    fun getResultFromJslt(response: String, jslt: String): String {
        val objectMapper = ObjectMapper()
        val input = objectMapper.readTree(response)
        val jsltData = Parser.compileString(jslt)
        val output = jsltData.apply(input)
        log.error(output.toPrettyString())
        log.error(response)
        return output.toPrettyString()
    }

    /**
     * Metodo que trasnforma el xml en un objeto
     */
    fun getObjectFromJson(json: String): ConsultaPago {
        val objectMapper = ObjectMapper()
        var consultaPago = objectMapper.readValue(json, ConsultaPago::class.java)
        return consultaPago
    }

}