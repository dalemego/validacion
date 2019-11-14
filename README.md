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

## Installation

Use el siguiente comando para crear los contenedores en [Docker](https://www.docker.com).

```bash
docker-compose up --build -d
```
Se requiere Docker para su compilación.

## Usage

```python
import foobar

foobar.pluralize('word') # returns 'words'
foobar.pluralize('goose') # returns 'geese'
foobar.singularize('phenomena') # returns 'phenomenon'
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
