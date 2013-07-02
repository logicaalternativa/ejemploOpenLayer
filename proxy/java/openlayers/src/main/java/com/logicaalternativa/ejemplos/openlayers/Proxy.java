/*
 *      Proxy.java
 *      
 *      Copyright 2013 Miguel Rafael Esteban Martín (www.logicaalternativa.com) <miguel.esteban@logicaalternativa.com>
 *      
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 */

package com.logicaalternativa.ejemplos.openlayers;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

/**
 * Clase que realiza la funciones de Cross-Domain Proxy
 * para OpenLayers
 * @author miguel
 *
 */
public class Proxy extends HttpServlet{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 748261019258762565L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// Se realiza una copia del InputStream del request porque una vez que se lea el paráemtro
		// url vendrá vacio
		InputStream entrada = copiarInputStream(request.getInputStream());		
		
		// Se obtiene l URL del parámetro
		String uri = request.getParameter("url");
		
		// Se crea el objeto conexión. Aquí hacemos de cliente 
		HttpURLConnection httpCon =  crearConexion( uri, request.getMethod() );
		
		// Se escribe el cuerpo de request en el requeste de la conexión cliente
		escribirEntradaSalida( entrada, httpCon.getOutputStream() );
		
		// Se comprueba el código html => 200 = Correcto
		if ( httpCon.getResponseCode() != HttpURLConnection.HTTP_OK ) {
			
		    throw new IOException( "(" + httpCon.getResponseCode() + ") " +  httpCon.getResponseMessage() );
		    
		 }	
		
		// Se escribe en la respuesta, la respuesta que nos ha dado la conexión al servicio
		escribirEntradaSalida(httpCon.getInputStream(), response.getOutputStream() );
		
	}
	
	
	/**
	 * Realiza una copia del InputStream
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private InputStream copiarInputStream ( InputStream in ) throws IOException {
		
		ByteArrayOutputStream copia = new ByteArrayOutputStream();
		
		escribirEntradaSalida(in, copia );
		
		ByteArrayInputStream res = new ByteArrayInputStream( copia.toByteArray() );
		
		return res;
		
		
	}
	
	
	
	/**
	 * Escribe el {@link InputStream} de entrada en el {@link OutputStream} de salida
	 * @param entrada
	 * @param salida
	 * @throws IOException
	 */
	private void escribirEntradaSalida( InputStream entrada, OutputStream salida ) throws IOException   {
		try {
		
			byte[] buffer = new byte[1024];
			int len = entrada.read(buffer);
			while (len != -1) {
			    salida.write(buffer, 0, len);
			    len = entrada.read(buffer);
			}
		} finally {
			cerrar(entrada);
			cerrar(salida);
			
		}
		
	}
	
	
	
	
	/**
	 * Se crea el objeto conexión cliente a la URL
	 * @param uri
	 * @param metodo
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection crearConexion( String uri, String metodo ) throws IOException{
				
		URL url = new URL( uri );
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod( metodo.toUpperCase() );

		
		return httpCon;
		
	}
	
	/**
	 * Se cierra los Stream controladamente
	 * @param closeable
	 */
	private void cerrar (Closeable closeable ) {
		
		if ( closeable != null ) {
			
			try {
				
				closeable.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
}
