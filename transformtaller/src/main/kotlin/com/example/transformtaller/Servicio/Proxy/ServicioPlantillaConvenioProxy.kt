package com.example.transformtaller.Servicio.Proxy

import com.example.transformtaller.Modelo.ConsultaPago
import com.example.transformtaller.Modelo.PlantillaConvenio
import com.example.transformtaller.Servicio.Api.IServicioPlantillaConvenio
import com.example.transformtaller.Servicio.Impl.ServicioPlantillaConvenioRest
import com.example.transformtaller.Servicio.Impl.ServicioPlantillaConvenioSoap
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component


class ServicioPlantillaConvenioProxy(): IServicioPlantillaConvenio{

    var log = LoggerFactory.getLogger(this::class.java)

    companion object {
        val TIPO_SOAP = "SOAP"
        val TIPO_REST = "REST"
        val TIPO_TEXT = "TEXT"
        val POST = "POST"
        val GET = "GET"

       val URL_GET = "http://dispatchertaller:8082/referenciaPago"
       val URL_POST = "http://dispatchertaller:8082/pagarReferencia"
        //val URL_GET = "http://localhost:8082/referenciaPago"
       // val URL_POST = "http://localhost:8082/pagarReferencia"
    }

    /**
     * Metodo que obtiene la informacion del nro de referencia seleccionado
     */
    override fun getReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago {
        when {
            plantillaConvenio.tipoServicio.equals(TIPO_SOAP) -> {
                val servicio = ServicioPlantillaConvenioSoap(URL_GET, GET)
                return servicio.getReferenciaPagoTransform(plantillaConvenio)
            }
            plantillaConvenio.tipoServicio.equals(TIPO_REST) ->  {
                val servicio = ServicioPlantillaConvenioRest(URL_GET)
                return servicio.getReferenciaPagoTransform(plantillaConvenio)
            }
        }
        return ConsultaPago()
    }

    /**
     * Metodo que realiza la peticion de pago
     */
    override fun postReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago {
        when {
            plantillaConvenio.tipoServicio.equals(TIPO_SOAP) -> {
                val servicio = ServicioPlantillaConvenioSoap(URL_POST, POST)
                return servicio.postReferenciaPagoTransform(plantillaConvenio)
            }
            plantillaConvenio.tipoServicio.equals(TIPO_REST) ->  {
                val servicio = ServicioPlantillaConvenioRest(URL_POST)
                return servicio.postReferenciaPagoTransform(plantillaConvenio)
            }
        }
        return ConsultaPago()
    }


}