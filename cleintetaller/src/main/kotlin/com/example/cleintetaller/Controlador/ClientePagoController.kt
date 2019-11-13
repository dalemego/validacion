package com.example.cleintetaller.Controlador

import com.example.cleintetaller.Modelo.Pago
import com.example.cleintetaller.Servicio.ServicioClientePago
import org.springframework.web.bind.annotation.*

@RestController
class ClientePagoController {

    @GetMapping("/referenciaPago/{referenciaPago}")
    fun getReferenciaPago(@PathVariable referenciaPago : String): Any? {
        val servicio = ServicioClientePago()
        return servicio.getInfoConvenio(referenciaPago)
    }

    @PostMapping("/referenciaPago")
    fun getReferenciaPago(@RequestBody pago: Pago): Any? {
        val servicio = ServicioClientePago()
        return servicio.postPago(pago)
    }

}