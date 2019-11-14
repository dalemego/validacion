# Taller Modelamiento y Validación de Arquitectura

La presente documentación muestra los detalles de la implementación del sistema de pagos, herramientas, y patrones utilizados en el diseño de la arquitectura.

## Servicios disponibles

La solución del sistema de pagos contará con los siguientes servicios expuestos desde [Open API](https://app.swaggerhub.com/home?type=API):

- Pagar convenio: Envía petición de pago incluyendo el número de convenio y referencia de pago en la factura del cliente.
- Consultar convenio: Consulta el saldo del convenio a pagar a partir de un número de identificación de convenio que se encuentra almacenado en la base de datos local del banco.
- Agregar convenio: Inserta un convenio en la base de datos dado un identificador proporcionado por la entidad externa.
- Editar convenio: Modifica datos de convenio en la base de datos local del banco. 
- Eliminar convenio: Elimina registro del convenio en la base de datos.

## Justificación de la arquitectura

Se utiliza el patrón de microservicios como nuclear de la arquitectura contando con despliegues en contenedores para una mayor disponibilidad y simplicidad de los servicios necesarios para la gestión de los pagos y convenios. Adicional a esto, se utilizan los siguientes patrones y tecnologías

- Composición por Orquestación
- Layers
- API Proxy
- API Gateway 
- Spring Boot
- Open API

Con respecto al API Gateway, este se implementó en una arquitectura de microservicios, se habilitó su uso por medio del API management de Microsoft Azure el cual está disponible en el siguiente [link](http://testdev.azure-api.net)
 
## Servicio de transformación de datos
El servicio se desarrolla en la herramienta Spring Boot y tiene la responsabilidad de recibir la información del cliente, transformar los datos, y enrutar al servicio del convenio correspondiente dependiendo del tipo de servicio. La estructura implementada es la siguiente:

```bash
Servicio
 API
   IServicioImplementacionPlantilla
 Impl
   ServicioPlantillaConvenioRest
   ServicioPlantillaConvenioSoap
 Proxy
   ServicioPlantillaConvenioProxy
```

El servicio de transformación se utilizo por medio del patron Proxy el cual por medio de **ServicioPlantillaConvenioProxy** se controlara el acceso a cada una de las funciones de transformación ya sea para SOAP o REST.

Para realizar el proceso de transformación se implementaron los estilos de transformación, para facilitar la creación de peticiones y respuestas por medio de plantillas dependiendo del tipo de arquitecrua a nivel de servicios SOAP o REST. Los procesos implementados son:

- XSLT (eXtensible Stylesheet Language for Transformations) [link](https://docs.oracle.com/javase/tutorial/jaxp/xslt/transformingXML.html)
- JSLT (Json stylesheet language transformation) [link](https://github.com/schibsted/jslt)



## Installation

Use el siguiente comando para crear los contenedores en [Docker](https://www.docker.com).

```bash
docker-compose up --build -d
```
Se requiere Docker para su compilación.
