package com.example.transformtaller.Modelo

import javax.xml.bind.annotation.*

@XmlRootElement(name = "consultaPago")
@XmlAccessorType(XmlAccessType.FIELD)
class ConsultaPago(@XmlElement var referenciaFactura: String,
                   @XmlElement var totalPago: String,
                   var result: Boolean,
                   @XmlElement var message: String) {

    constructor() : this("",
            "",
            false,
            "No se encontro información del Nro. de referencia seleccionado")
}