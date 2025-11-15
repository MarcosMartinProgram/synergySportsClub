package com.martinmarcos.synergysportclub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.martinmarcos.synergysportclub.R
import com.martinmarcos.synergysportclub.data.dao.PagoDAO // TU DAO DE PAGOS
import com.martinmarcos.synergysportclub.data.dao.ValorCuotaDAO // Para obtener el precio
import java.text.SimpleDateFormat
import java.util.*

class CobrarCuotaDatosActivity : AppCompatActivity() {

    // Vistas de la UI
    private lateinit var tvSocioDni: TextView
    private lateinit var tvSocioNombre: TextView
    private lateinit var tvFechaPago: TextView
    private lateinit var tvFechaVencimiento: TextView
    private lateinit var tvImporte: TextView
    private lateinit var spinnerMetodoPago: Spinner
    private lateinit var spinnerCuotas: Spinner
    private lateinit var btnCobrarCuota: Button

    // DAOs
    private lateinit var valorCuotaDAO: ValorCuotaDAO
    private lateinit var pagoDAO: PagoDAO

    // Datos del socio y del pago
    private var idPersona: Int = -1
    private var dniSocio: String? = null
    private var nombreSocio: String? = null
    private var valorCuotaMensual: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobrar_cuota_datos)

        // 1. Inicializar DAOs y vincular vistas con los IDs CORRECTOS
        inicializarComponentes()

        // 2. Recibir datos del socio desde la activity anterior
        recibirDatosPersona()

        // 3. Obtener el precio actual de la cuota desde la base de datos
        obtenerPrecioCuota()

        // 4. Configurar los Spinners y sus listeners
        configurarSpinners()

        // 5. Configurar el botón de cobro
        btnCobrarCuota.setOnClickListener {
            registrarPago()
        }
    }

    private fun inicializarComponentes() {
        pagoDAO = PagoDAO(this)
        valorCuotaDAO = ValorCuotaDAO(this)

        // Vinculamos usando los IDs que añadimos al XML
        tvSocioDni = findViewById(R.id.tvSocioDni)
        tvSocioNombre = findViewById(R.id.tvSocioNombre)
        tvFechaPago = findViewById(R.id.tvFechaPago)
        tvFechaVencimiento = findViewById(R.id.tvFechaVencimiento)
        tvImporte = findViewById(R.id.tvImporte)
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago)
        spinnerCuotas = findViewById(R.id.spinnerCuotas)
        btnCobrarCuota = findViewById(R.id.buttonCobroCuota)
    }

    private fun recibirDatosPersona() {
        // ¡CAMBIO IMPORTANTE! Recibimos los datos con las nuevas claves.
        idPersona = intent.getIntExtra("ID_PERSONA", -1)
        val dniPersona = intent.getStringExtra("DNI_PERSONA")
        val nombrePersona = intent.getStringExtra("NOMBRE_PERSONA")

        if (idPersona == -1) {
            // ... (código de error no cambia)
            return
        }

        // Actualizamos la UI
        tvSocioDni.text = "Socio (DNI): $dniPersona"
        tvSocioNombre.text = "Nombre: $nombrePersona"
    }

    private fun obtenerPrecioCuota() {
        val cuotaActual = valorCuotaDAO.getValorCuotaActual()
        if (cuotaActual != null) {
            valorCuotaMensual = cuotaActual.monto
        } else {
            Toast.makeText(this, "¡Atención! No se ha definido un precio para la cuota.", Toast.LENGTH_LONG).show()
            btnCobrarCuota.isEnabled = false // Deshabilitamos el cobro si no hay precio
        }
    }

    private fun configurarSpinners() {
        // Adaptadores para los spinners
        val metodosPago = arrayOf("Efectivo", "Tarjeta de Débito", "Tarjeta de Crédito", "Transferencia")
        spinnerMetodoPago.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, metodosPago)

        val mesesCuotas = arrayOf(1, 2, 3, 6, 12) // Cantidad de meses a pagar
        spinnerCuotas.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mesesCuotas)

        // Listener para que el importe se actualice cuando el usuario cambie la cantidad de cuotas
        spinnerCuotas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                actualizarFechasYMontos()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun actualizarFechasYMontos() {
        val mesesSeleccionados = spinnerCuotas.selectedItem.toString().toInt()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Fecha de Pago (Hoy)
        val fechaPago = Date()
        tvFechaPago.text = "Fecha de Pago: ${formatoFecha.format(fechaPago)}"

        // Fecha de Vencimiento
        val calendar = Calendar.getInstance()
        calendar.time = fechaPago
        calendar.add(Calendar.MONTH, mesesSeleccionados) // Añadimos los meses seleccionados
        tvFechaVencimiento.text = "Próx. Vencimiento: ${formatoFecha.format(calendar.time)}"

        // Monto Total
        val montoTotal = valorCuotaMensual * mesesSeleccionados
        tvImporte.text = "Importe: $${"%.2f".format(montoTotal)}"
    }

    private fun registrarPago() {
        val mesesPagados = spinnerCuotas.selectedItem.toString().toInt()
        val montoTotal = valorCuotaMensual * mesesPagados

        // Formato para guardar en la base de datos (YYYY-MM-DD es mejor para ordenar)
        val formatoDb = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaPagoDb = formatoDb.format(Date())

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, mesesPagados)
        val fechaVencimientoDb = formatoDb.format(calendar.time)

        val resultadoId = pagoDAO.registrarPagoCuotaSocio(
            idPersona = idPersona.toLong(), // Pasamos el ID de la persona
            fechaPago = fechaPagoDb,
            monto = montoTotal,
            fechaVencimiento = fechaVencimientoDb
        )

        if (resultadoId != -1L) {
            Toast.makeText(this, "Pago registrado con éxito.", Toast.LENGTH_LONG).show()
            // Volver al menú principal
            val intent = Intent(this, MenuPrincipalActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al registrar el pago.", Toast.LENGTH_LONG).show()
        }
    }
}