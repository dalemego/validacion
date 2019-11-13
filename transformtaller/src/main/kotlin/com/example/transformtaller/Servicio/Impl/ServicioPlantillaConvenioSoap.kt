package com.example.transformtaller.Servicio.Impl

import com.example.transformtaller.Modelo.ConsultaPago
import com.example.transformtaller.Modelo.PlantillaConvenio
import com.example.transformtaller.Modelo.RespuestaBasica
import com.example.transformtaller.Servicio.Api.IServicioPlantillaConvenio
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.HttpStatusCodeException
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.transform.stream.StreamSource
import java.io.StringWriter
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult


class ServicioPlantillaConvenioSoap(var URL: String, var tipoRequest: String) : IServicioPlantillaConvenio {
    var log = LoggerFactory.getLogger(this::class.java)
    var restTemplate = RestTemplateBuilder().build()
    val POST = "POST"

    /**
     * Metodo que obtiene la informacion del nro de referencia seleccionado
     */
    override fun getReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago {
        log.error(tipoRequest)
            val response = requestPeticion(plantillaConvenio)
            return when {
                response == null -> ConsultaPago(message = "Hubo un error, contacta al administrador",
                        result = false,
                        totalPago = "",
                        referenciaFactura = plantillaConvenio.nroReferencia)
                !response.result -> ConsultaPago()
                else -> {
                    val resultXml = getResultFromXslt(response.response, plantillaConvenio.plantillaRespuesta)
                    val consultaPago = getObjectFromXml(resultXml)
                    consultaPago.referenciaFactura = plantillaConvenio.nroReferencia
                    consultaPago.result = true
                    consultaPago.message = String()
                    consultaPago
                }
            }

    }

    /**
     * Metodo que realiza la peticion de pago
     */
    override fun postReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago {
        log.error(tipoRequest)
        val response = requestPeticion(plantillaConvenio)
        return when {
            response == null -> ConsultaPago(message = "Hubo un error, contacta al administrador",
                    result = false,
                    totalPago = "",
                    referenciaFactura = plantillaConvenio.nroReferencia)
            !response.result -> return ConsultaPago()
            else -> {
                val resultJson = getResultFromXslt(response.response, plantillaConvenio.plantillaRespuesta)
                val consultaPago = getObjectFromXml(resultJson)
                consultaPago.result = true
                return consultaPago
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
            log.error(response!!.response)
            return response
        } catch (e: HttpStatusCodeException) {
            return null
        }
    }

    /**
     * Metodo que cambia o remplaza una parte de la plantilla por el valor real
     */
    fun replaceDataPeticin(plantillaConvenio: PlantillaConvenio): PlantillaConvenio {
        plantillaConvenio.plantillaPeticion = plantillaConvenio.plantillaPeticion
                .replace("{nroReferencia}", plantillaConvenio.nroReferencia, true)
        if (tipoRequest.equals(POST))
            plantillaConvenio.plantillaPeticion = plantillaConvenio.plantillaPeticion
                    .replace("{totalPago}", plantillaConvenio.totalPago, true)
        log.error(plantillaConvenio.plantillaPeticion)
        return plantillaConvenio
    }


    /**
     * Metodo que transforma la respuesta por medio de xlst
     */
    fun getResultFromXslt(response: String, xslt: String): String {
        val builder = TransformerFactory.newInstance()
        val streamWriter = StringWriter()
        val streamResult = StreamResult(streamWriter)
        val transformer = builder.newTransformer(StreamSource(StringReader(xslt)))
        var stringNoUrl = response.replace(Regex("\\s+xmlns\\s*(:\\w)?\\s*=\\s*\\\"(?<url>[^\\\"]*)\\\""), "")
        var stringNoDots = stringNoUrl.replace(":", "")
        log.error(stringNoDots)
        transformer.transform(StreamSource(StringReader(stringNoDots)), streamResult)
        return streamWriter.toString()
    }

    /**
     * Metodo que trasnforma el xml en un objeto
     */
    fun getObjectFromXml(xml: String): ConsultaPago {
        val context = JAXBContext.newInstance(ConsultaPago::class.java)
        val jaxbUnmarshaller = context.createUnmarshaller()
        val streamSource = StreamSource(StringReader(xml))
        val consultaPago = jaxbUnmarshaller.unmarshal(streamSource, ConsultaPago::class.java)
        return consultaPago.value
    }

}