package com.example.webservices

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
    var wsConsultar: String = "http://172.16.251.133/servicios/MostrarAlumno.php"
    var wsInsertar: String = "http://172.16.251.133/servicios/InsertarAlumno.php"
    var wsActualizar: String = "http://172.16.251.133/servicios/ActualizarAlumno.php"
    var wsMostrar: String = "http://172.16.251.133/servicios/MostrarAlumnos.php"
    var wsElimnar: String = "http://172.16.251.133/servicios/BorrarAlumno.php"
    var hilo: ObtenerServicioWeb? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun consultaXNoControl(v: View) {
        if (etNoControl.text.isEmpty()) {
            Toast.makeText(this, "Falta ingresar NoControl", Toast.LENGTH_SHORT).show();
            etNoControl.requestFocus()
        } else {
            val no = etNoControl.text.toString()
            hilo = ObtenerServicioWeb()
            hilo?.execute("Consulta", no, "", "", "")
        }
    }

    fun consulta(v: View) {
        hilo = ObtenerServicioWeb()
        hilo?.execute("Mostrar", "", "", "", "")
    }

    fun eliminaAlumno(v: View) {
        if (etNoControl.text.isEmpty() || etCarrera.text.isEmpty() ||
            etNombre.text.isEmpty() || etTelefono.text.isEmpty()
        ) {
            Toast.makeText(this, "Falta ingresar todos los datos", Toast.LENGTH_SHORT).show();
            etNoControl.requestFocus()
        } else {
            val no = etNoControl.text.toString()
            val carr = etCarrera.text.toString()
            val nom = etNombre.text.toString()
            val tel = etTelefono.text.toString()
            hilo = ObtenerServicioWeb()
            hilo?.execute("Eliminar", no, carr, nom, tel)
        }
    }

    fun insertaAlumno(v: View) {
        if (etNoControl.text.isEmpty() || etCarrera.text.isEmpty() ||
            etNombre.text.isEmpty() || etTelefono.text.isEmpty()
        ) {
            Toast.makeText(this, "Falta ingresar todos los datos", Toast.LENGTH_SHORT).show();
            etNoControl.requestFocus()
        } else {
            val no = etNoControl.text.toString()
            val carr = etCarrera.text.toString()
            val nom = etNombre.text.toString()
            val tel = etTelefono.text.toString()
            hilo = ObtenerServicioWeb()
            hilo?.execute("Insertar", no, carr, nom, tel)
        }
    }

    fun actualizarAlumno(v: View) {
        if (etNoControl.text.isEmpty() || etCarrera.text.isEmpty() ||
            etNombre.text.isEmpty() || etTelefono.text.isEmpty()
        ) {
            Toast.makeText(this, "Falta ingresar todos los datos", Toast.LENGTH_SHORT).show();
            etNoControl.requestFocus()
        } else {
            val no = etNoControl.text.toString()
            val carr = etCarrera.text.toString()
            val nom = etNombre.text.toString()
            val tel = etTelefono.text.toString()
            hilo = ObtenerServicioWeb()
            hilo?.execute("Actualizar", no, carr, nom, tel)
        }
    }

    inner class ObtenerServicioWeb() : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String {
            var Url: URL? = null
            var sResultado = ""
            try {
                val urlConn: HttpURLConnection
                val input: DataInputStream
                when {
                    params[0] == "Actualizar" -> {
                        Url = URL(wsActualizar)
                    }
                    params[0] == "Insertar" -> {
                        Url = URL(wsInsertar)
                    }
                    params[0] == "Consulta" -> {
                        Url = URL(wsConsultar)
                    }
                    params[0] == "Mostrar" -> {
                        Url = URL(wsMostrar)
                    }
                    params[0] == "Eliminar" -> {
                        Url = URL(wsElimnar)
                    }
                }

                urlConn = Url!!.openConnection() as HttpURLConnection
                urlConn.doInput = true
                urlConn.doOutput = true
                urlConn.useCaches = false
                urlConn.setRequestProperty("Content-Type", "application/json")
                urlConn.setRequestProperty("Accept", "application/json")
                urlConn.connect()
                // Preparar los datos a enviar ws
                val jsonParam = JSONObject()
                jsonParam.put("nocontrol", params[1])
                jsonParam.put("carrera", params[2])
                jsonParam.put("nombre", params[3])
                jsonParam.put("telefono", params[4])
                val os = urlConn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(jsonParam.toString())
                writer.flush()
                writer.close()
                val respuesta = urlConn.responseCode
                val result = StringBuilder()
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    val inStream: InputStream = urlConn.inputStream
                    val isReader = InputStreamReader(inStream)
                    val bReader = BufferedReader(isReader)
                    var tempStr: String?
                    while (true) {
                        tempStr = bReader.readLine()
                        if (tempStr == null) {
                            break
                        }
                        result.append(tempStr)
                    }
                    urlConn.disconnect()
                    sResultado = result.toString()
                }
            } catch (e: MalformedURLException) {
                Log.d("Asher", e.message)
            } catch (e: IOException) {
                Log.d("Asher", e.message)
            } catch (e: JSONException) {
                Log.d("Asher", e.message)
            } catch (e: Exception) {
                Log.d("Asher", e.message)
            }
            return sResultado
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var no: String = ""
            var nom: String = ""
            var carr: String = ""
            var tel: String = ""
            try {
                val respuestaJSON = JSONObject(result)
                val resultJSON = respuestaJSON.getString("success")
                val MSG = respuestaJSON.getString("message")
                when {
                    resultJSON == "200" -> {
                        val alumnoJSON = respuestaJSON.getJSONArray("alumno")
                        if (alumnoJSON.length() >= 1) {
                            no = alumnoJSON.getJSONObject(0).getString("nocontrol")
                            nom = alumnoJSON.getJSONObject(0).getString("nombre")
                            carr = alumnoJSON.getJSONObject(0).getString("carrera")
                            tel = alumnoJSON.getJSONObject(0).getString("telefono")
                            etNoControl.setText(no)
                            etCarrera.setText(carr)
                            etNombre.setText(nom)
                            etTelefono.setText(tel)
                        }
                    }
                    resultJSON == "202" -> {
                        val alumnoJSON = respuestaJSON.getJSONArray("alumno")
                        if (alumnoJSON.length() >= 1) {
                            var admin = adminBD(baseContext)
                            for (i in 0 until alumnoJSON.length()) {
                                no = alumnoJSON.getJSONObject(i).getString("nocontrol")
                                nom = alumnoJSON.getJSONObject(i).getString("nombre")
                                carr = alumnoJSON.getJSONObject(i).getString("carrera")
                                tel = alumnoJSON.getJSONObject(i).getString("telefono")
                                val sentencia =
                                    "Insert into alumno(nocontrol,nombre,carrera,telefono) values('$no','$nom','$carr','$tel')"
                                admin.Ejecuta(sentencia)
                            }
                            Toast.makeText(baseContext, "Alummos almacenados BD local", Toast.LENGTH_SHORT).show();
                        }
                    }
                    resultJSON == "201" -> {
                        Toast.makeText(baseContext, MSG, Toast.LENGTH_SHORT).show();
                        etNoControl.setText("")
                        etCarrera.setText("")
                        etNombre.setText("")
                        etTelefono.setText("")
                        etNoControl.requestFocus()
                    }
                    resultJSON == "204" -> {
                        Toast.makeText(baseContext, "Alumno No Encontrado", Toast.LENGTH_SHORT).show();
                    }
                    resultJSON == "409" -> {
                        Toast.makeText(baseContext, "Error al agregar alumno", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (e: JSONException) {
                Log.d("Asher", e.message)
            } catch (e: Exception) {
                Log.d("Asher", e.message)
            }
        }
    }
} // Fin de la clase MainActivity
