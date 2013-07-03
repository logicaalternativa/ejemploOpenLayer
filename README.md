/*
 *      README.md
 *      
 *      Copyright 2013 Miguel Rafael Esteban Martín (www.logicaalternativa.com) <miguel.esteban@logicaalternativa.com>
 *
 **/



==============
= index.html = 
==============

El archivo 

 index.html

contiene la presentación y las llamadas a las funciones JavaScript para mostrar el mapa

En index.jsp se codifican dos funciones

init => Se llama desde el evento onLoad de la página. Carga las variables 	  del proyy y del logo y llama a la función 'dibujarMapa' pasándole
        como argumento el id del div en donde se mostrará el mapa en la
        página.

enviar => Se llamará cuando se envía el formulario. Obtiene la dirección 
        tecleada y llama a la función 'enviarPetición()' que es la que se
        encarga de realizar la geolocalización y de mostrar la marca.

Se importan dos archivos js

	./js/OpenLayers.js
      ./js/PosicionarMapa.js

PosicionarMapa.js contiene todas las funciones y código desarrollado para este ejemplo

OpenLayers.js importa la librería de OpenLayers. Necesita las carpetas img y theme. Es necesario que mantengan esta posición relativa al archivo OpenLayers.js

	+ OpenLayers.js
      |
      + img
      | |
      | + ...
      | 
      + theme
        |
        + ... 


=====================
= PosicionarMapa.js = 
=====================

Toda la funcionalidad desarrollada para el código está en el archivo

   js/PosicionarMapa.js


Todas las llamadas a las funciones son internas excepto las funciones:

'dibujarMapa( div )'
--------------------

Dibuja el mapa en el div indicado. Se pasa como argumento el id del div donde se quiere mostrar el mapa


'enviarPeticion( valor )'
-------------------------
Se pasa como argumento la dirección que se quiere marcar en el mapa. Hará la petición al servicio de Openrouteservice.org para obtener las coordenadas y añadirá la marca al mapa


'borrarMarca( idMarca )'
------------------------
Se utiliza para borrar la marca. Se monta su llamada en el popup de cada marca. Se pasa por argumento el id de marca que se ha generado en su creación.

Os remito a los comentarios de cada función para explicar funcionalidad más específica de lo que el código hace y como lo hace.


==========
= PROXYS = 
==========

Para salvar las limitaciones de Ajax y poder hacer las peticiones al servicio Openrouteservice.org, he tenido que codificar un proxy que permita redireccionar la petición y devolver el resultado del servicio.

  1) JavaScript =>  Petición POST a nuestro Proxy  También se le pasa URL
                    del servicio por GET

  2) Proxy      =>  Se abre la conexión HTTP a la URL que se le pasa por
                    GET y se hace al servicio la misma petición POST que le
                    llega. Devuelve el resultado del servicio a la petición
                    que se le ha hecho desde JavaScript.

Existen dos implementaciones.

Proxy PHP
=========

 proxy/php/proxy.php

Que utiliza la librería Snoopy para hacer la petición HTTP al servicio


Proxy Java
==========

He implementado un servlet que extiende HttpServlet. Está todo en un proyecto MAVEN en el directorio

 proxy/java/openlayers

El servlet es 

proxy/java/openlayers/src/main/java/com/logicaalternativa/ejemplos/openlayers/Proxy.java

y se ha configurado en el web.xml

proxy/java/openlayers/src/main/webapp/WEB-INF/web.xml

