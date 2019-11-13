package com.example.transformtaller.Servicio.Api

import com.example.transformtaller.Modelo.ConsultaPago
import com.example.transformtaller.Modelo.PlantillaConvenio

interface IServicioPlantillaConvenio {

    /**
     * Metodo que obtiene la informacion del nro de referencia seleccionado
     */
    fun getReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago

    /**
     * Metodo que realiza el pago del nro de referencia
     */
    fun postReferenciaPagoTransform(plantillaConvenio: PlantillaConvenio): ConsultaPago
}