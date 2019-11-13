package com.example.transformtaller.Controlador

import com.example.transformtaller.Modelo.ConsultaPago
import com.example.transformtaller.Modelo.PlantillaConvenio
import com.example.transformtaller.Servicio.Proxy.ServicioPlantillaConvenioProxy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.bind.annotation.*

@RestController
class PlantillaConvenioController {
    var log = LoggerFactory.getLogger(this::class.java)
    var restTemplate = RestTemplateBuilder().build()

    lateinit var servicioPlantillaConvenioProxy: ServicioPlantillaConvenioProxy

    @PostMapping("/referenciaPago")
    fun getReferenciaPago(@RequestBody plantillaConvenio: PlantillaConvenio): ConsultaPago{
        servicioPlantillaConvenioProxy = ServicioPlantillaConvenioProxy()
        return servicioPlantillaConvenioProxy.getReferenciaPagoTransform(plantillaConvenio)
    }

    @PostMapping("/realizarPago")
    fun postReferenciaPago(@RequestBody plantillaConvenio: PlantillaConvenio): ConsultaPago{
        servicioPlantillaConvenioProxy = ServicioPlantillaConvenioProxy()
        return servicioPlantillaConvenioProxy.postReferenciaPagoTransform(plantillaConvenio)
    }



}