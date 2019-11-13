package com.example.conveniotaller.Controlador

import com.example.conveniotaller.Modelo.Convenio
import com.example.conveniotaller.Modelo.RespuestaBasica
import com.example.conveniotaller.Servicio.ServicioConvenio
import org.springframework.web.bind.annotation.*

@RestController
class ConvenioController {

    @PostMapping("/convenio")
    fun postConvenio(@RequestBody convenio: Convenio): RespuestaBasica<Any>{
        val servicioConvenio = ServicioConvenio()
        return servicioConvenio.registraConvenio(convenio)
    }

    @GetMapping("/convenio/{codigoReferencia}")
    fun getConvenio(@PathVariable codigoReferencia: String): Any{
        val servicioConvenio = ServicioConvenio()
        return servicioConvenio.getConvenio(codigoReferencia)
    }

}