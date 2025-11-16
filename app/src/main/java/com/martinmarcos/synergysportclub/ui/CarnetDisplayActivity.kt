package com.martinmarcos.synergysportclub.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.martinmarcos.synergysportclub.R
import java.io.File
import java.io.FileOutputStream

class CarnetDisplayActivity : AppCompatActivity() {

    private lateinit var carnetView: View
    private lateinit var carnetContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet_display)

        carnetContainer = findViewById(R.id.carnet_preview_container)
        val btnCompartir: Button = findViewById(R.id.buttonCompartirPdf)

        // Inflar y rellenar el layout del carnet
        poblarCarnet()

        btnCompartir.setOnClickListener {
            val bitmap = viewToBitmap(carnetView)
            val pdfFile = bitmapToPdf(bitmap)
            if (pdfFile != null) {
                compartirPdf(pdfFile)
            } else {
                Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun poblarCarnet() {
        // 1. Obtener los datos pasados desde la activity anterior
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val apellido = intent.getStringExtra("APELLIDO") ?: ""
        val dni = intent.getStringExtra("DNI") ?: ""
        val idSocio = intent.getIntExtra("ID_SOCIO", 0)
        val vencimiento = intent.getStringExtra("FECHA_VENCIMIENTO") ?: "N/A"

        // 2. Inflar el layout del carnet
        val inflater = LayoutInflater.from(this)
        carnetView = inflater.inflate(R.layout.carnet_layout, carnetContainer, false)

        // 3. Vincular vistas y asignar datos
        carnetView.findViewById<TextView>(R.id.tvNombreSocioCarnet).text = "$nombre $apellido"
        carnetView.findViewById<TextView>(R.id.tvDniSocioCarnet).text = "DNI: $dni"
        carnetView.findViewById<TextView>(R.id.tvIdSocioCarnet).text = "Socio N°: $idSocio"
        carnetView.findViewById<TextView>(R.id.tvFechaVencimientoCarnet).text = "Válido hasta: $vencimiento"

        // 4. Añadir el carnet poblado al contenedor para que se vea en pantalla
        carnetContainer.addView(carnetView)
    }

    // Convierte una Vista (nuestro carnet) en un Bitmap (una imagen)
    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // Convierte el Bitmap en un archivo PDF
    private fun bitmapToPdf(bitmap: Bitmap): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        // Guardar el archivo en el directorio de cache de la app
        val pdfFile = File(cacheDir, "carnet_socio.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            pdfDocument.close()
            return pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
        }
        return null
    }

    // Lanza el intent para compartir el archivo PDF
    private fun compartirPdf(file: File) {
        val fileUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", file)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, fileUri)
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir carnet con..."))
    }
}
    