package com.example.dispatchertaller.Controlador

import com.example.dispatchertaller.Modelo.ConsultaConvenio
import com.example.dispatchertaller.Modelo.RespuestaBasica
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpStatusCodeException

@RestController
class ConsultaConvenioController {
    var log = LoggerFactory.getLogger(this::class.java.simpleName)
    var restTemplate = RestTemplateBuilder().build()

    companion object {
        val TIPO_SOAP = "SOAP"
        val TIPO_REST = "REST"
        val TIPO_TEXT = "TEXT"
    }

    /**
     * Metodo de tipo endpoint para la peticion de obtención de información de un nro de referencia de pago
     */
    @PostMapping(path = ["/referenciaPago"], consumes = ["application/json"], produces = ["application/json"])
    fun getReferenciaPago(@RequestBody consultaConvenio: ConsultaConvenio): RespuestaBasica {
        return when {
            consultaConvenio.tipoServicio.equals(TIPO_SOAP) -> referenciaPago(consultaConvenio)
            consultaConvenio.tipoServicio.equals(TIPO_REST) -> restGetReferenciaPago(consultaConvenio)
            else -> RespuestaBasica(response = "Error de tipo de dato", result = false, tipoResponse = TIPO_TEXT)
        }

    }

    /**
     * Metodo de tipo endpoint para la peticion de registro de pago
     */
    @PostMapping(path = ["/pagarReferencia"], consumes = ["application/json"], produces = ["application/json"])
    fun postReferenciaPago(@RequestBody consultaConvenio: ConsultaConvenio): RespuestaBasica {
        return when {
            consultaConvenio.tipoServicio.equals(TIPO_SOAP)
                    .or(consultaConvenio.tipoServicio.equals(TIPO_REST)) -> referenciaPago(consultaConvenio, true)
            else -> RespuestaBasica(response = "Error de tipo de dato", result = false, tipoResponse = TIPO_TEXT)
        }
    }

    /**
     * Metodo que ejecuta un servicio de tipo SOAP o REST si es POST
     */
    fun referenciaPago(consultaConvenio: ConsultaConvenio, action: Boolean = false): RespuestaBasica {
        val request = HttpEntity<Any>(consultaConvenio.plantillaPeticion, retornaHeader(consultaConvenio.tipoServicio, action))
        log.error(consultaConvenio.plantillaPeticion)
        try {
            val response = restTemplate.postForObject(consultaConvenio.url, request, String::class.java)

            return RespuestaBasica(response = response!!, result = true, tipoResponse = consultaConvenio.tipoServicio)
        } catch (e: HttpStatusCodeException) {
            return RespuestaBasica(response = e.responseBodyAsString, result = false, tipoResponse = consultaConvenio.tipoServicio)
        }
    }

    /**
     * Metodo que ejecuta un servicio de tipo SOAP
     */
    fun restGetReferenciaPago(consultaConvenio: ConsultaConvenio): RespuestaBasica {
        try {
            val response = restTemplate.getForObject(consultaConvenio.url, String::class.java)
            return RespuestaBasica(response = response!!, result = true, tipoResponse = consultaConvenio.tipoServicio)
        } catch (e: HttpStatusCodeException) {
            return RespuestaBasica(response = e.responseBodyAsString, result = false, tipoResponse = consultaConvenio.tipoServicio)
        }
    }

    /**
     * Metodo que valida el tipo de header
     */
    fun retornaHeader(tipoWS: String, action: Boolean): HttpHeaders {
        val header = HttpHeaders()
        when {
            tipoWS.equals(TIPO_SOAP) -> {
                header.add("Content-Type", "text/xml")
                if(action) header.add("SOAPAction", "pagar")
            }

            tipoWS.equals(TIPO_REST) -> header.add("Content-Type", "application/json")
        }
        return header
    }
}