package com.example.cleintetaller.Servicio

import com.example.cleintetaller.Modelo.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.HttpStatusCodeException
import java.lang.StringBuilder

class ServicioClientePago {

    var log = LoggerFactory.getLogger(this::class.java)
    var restTemplate = RestTemplateBuilder().build()

    companion object {

        //val URL_GET_COVENIO = "http://localhost:8083/convenio"
        //val URL_POST_REFERENCIA = "http://localhost:8081/referenciaPago"
        //val URL_POST_PAGAR = "http://localhost:8081/realizarPago"
        val URL_GET_COVENIO = "http://conveniotaller:8083/convenio"
        val URL_POST_REFERENCIA = "http://transformtaller:8081/referenciaPago"
        val URL_POST_PAGAR = "http://transformtaller:8081/realizarPago"


    }

    /**
     * Metodo que busca la informacion del convenio
     */
    fun getInfoConvenio(referenciaPago: String): Any?{
        val header = HttpHeaders()
        val splitRefPago = referenciaPago.substring(0, 4)
        header.contentType = MediaType.APPLICATION_JSON
        //val request = HttpEntity<Any>(requestField, header)
        log.error(splitRefPago)
        var url = StringBuilder(URL_GET_COVENIO).append("/").append(splitRefPago)

        try {
            val response = restTemplate.getForObject(url.toString(), RespuestaBasica::class.java)
            if (!response!!.result) return response
            val map = ObjectMapper()
            val documentDb = map.convertValue(response?.data, Convenio::class.java)
            return getInfoPago(documentDb, referenciaPago)
        }catch (e: HttpStatusCodeException){
            return null
        }
    }

    /**
     * Metodo para realizar el pago
     */
    fun postPago(pago: Pago): Any?{
        val header = HttpHeaders()
        val splitRefPago = pago.referenciaPago.substring(0, 4)
        header.contentType = MediaType.APPLICATION_JSON
        //val request = HttpEntity<Any>(requestField, header)
        log.error(splitRefPago)
        var url = StringBuilder(URL_GET_COVENIO).append("/").append(splitRefPago)

        try {
            val response = restTemplate.getForObject(url.toString(), RespuestaBasica::class.java)
            if (!response!!.result) return response
            val map = ObjectMapper()
            val documentDb = map.convertValue(response?.data, Convenio::class.java)
            return postPago(documentDb, pago)
        }catch (e: HttpStatusCodeException){
            return null
        }
    }

    /**
     * Metodo que realiza la peticion de consulta de nro de referencia
     */
    fun getInfoPago(convenio: Convenio, referenciaPago:String): Any?{
        val referenciaPago = ReferenciaPago(url = convenio.url,
                tipoServicio = convenio.tipoServicio,
                codigoReferencia = convenio.codigoReferencia,
                totalPago = "",
                nroReferencia =  referenciaPago,
                plantillaPeticion = convenio.plantillaPeticionConsulta,
                plantillaRespuesta = convenio.plantillaRespuestaConsulta)
        val header = HttpHeaders()
        log.error(convenio.codigoReferencia)
        header.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity<Any>(referenciaPago, header)
        try {
            val response = restTemplate.postForObject(URL_POST_REFERENCIA, request, RespuestaReferenciaPago::class.java)
            return response
        }catch (e: HttpStatusCodeException){
            log.error(e.toString())
            return null
        }
    }

    /**
     * Metodo que para realizar el pago
     */
    fun postPago(convenio: Convenio, pago: Pago): Any?{
        val referenciaPago = ReferenciaPago(url = convenio.url,
                tipoServicio = convenio.tipoServicio,
                codigoReferencia = convenio.codigoReferencia,
                totalPago = pago.totalPago,
                nroReferencia =  pago.referenciaPago,
                plantillaPeticion = convenio.plantillaPeticionPago,
                plantillaRespuesta = convenio.plantillaRespuestaPago)
        val header = HttpHeaders()
        log.error(convenio.codigoReferencia)
        header.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity<Any>(referenciaPago, header)
        try {
            val response = restTemplate.postForObject(URL_POST_PAGAR, request, RespuestaReferenciaPago::class.java)
            return response
        }catch (e: HttpStatusCodeException){
            log.error(e.toString())
            return null
        }
    }



}