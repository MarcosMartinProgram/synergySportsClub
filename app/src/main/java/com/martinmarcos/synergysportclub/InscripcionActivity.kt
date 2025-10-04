package com.martinmarcos.synergysportclub

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class Socio(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val fechaNacimiento: String,
    val direccion: String,
    val telefono: String,
    val email: String,
    val tieneFichaMedica: Boolean
)

class InscripcionActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var inputNombre: EditText
    private lateinit var inputApellido: EditText
    private lateinit var inputDni: EditText
    private lateinit var inputFechaNacimiento: EditText
    private lateinit var inputDireccion: EditText
    private lateinit var inputTelefono: EditText
    private lateinit var inputEmail: EditText
    private lateinit var checkFichaMedica: CheckBox
    private lateinit var btnInscribir: Button

    private val cal: Calendar = Calendar.getInstance()
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        isLenient = false // fecha estricta
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscripcion)

        // --- Toolbar + botón atrás ---
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // --- Bind de vistas ---
        inputNombre = findViewById(R.id.inputNombre)
        inputApellido = findViewById(R.id.inputApellido)
        inputDni = findViewById(R.id.inputDni)
        inputFechaNacimiento = findViewById(R.id.inputFechaNacimiento)
        inputDireccion = findViewById(R.id.inputDireccion)
        inputTelefono = findViewById(R.id.inputTelefono)
        inputEmail = findViewById(R.id.inputEmail)
        checkFichaMedica = findViewById(R.id.checkFichaMedica)
        btnInscribir = findViewById(R.id.btnInscribir)

        // Asegurá que arranque deshabilitado (por si el XML no lo tiene)
        btnInscribir.isEnabled = false
        btnInscribir.alpha = 0.5f

        // --- DatePicker en el campo de fecha ---
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            cal.set(Calendar.YEAR, y)
            cal.set(Calendar.MONTH, m)
            cal.set(Calendar.DAY_OF_MONTH, d)
            inputFechaNacimiento.setText(sdf.format(cal.time))
            actualizarEstadoBoton()
        }
        fun abrirDatePicker() {
            val c = Calendar.getInstance()
            DatePickerDialog(
                this,
                dateSetListener,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        inputFechaNacimiento.setOnClickListener { abrirDatePicker() }
        inputFechaNacimiento.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) abrirDatePicker() }

        // --- Listeners para habilitar el botón solo si todo es válido ---
        inputNombre.addTextChangedListener { actualizarEstadoBoton() }
        inputApellido.addTextChangedListener { actualizarEstadoBoton() }
        inputDni.addTextChangedListener { actualizarEstadoBoton() }
        inputFechaNacimiento.addTextChangedListener { actualizarEstadoBoton() }
        inputDireccion.addTextChangedListener { actualizarEstadoBoton() }
        inputTelefono.addTextChangedListener { actualizarEstadoBoton() }
        inputEmail.addTextChangedListener { actualizarEstadoBoton() }
        checkFichaMedica.setOnCheckedChangeListener { _, _ -> actualizarEstadoBoton() }

        // --- Validación al perder foco (feedback rápido) ---
        inputNombre.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarNombre() }
        inputApellido.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarApellido() }
        inputDni.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarDni() }
        inputFechaNacimiento.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarFecha() }
        inputDireccion.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarDireccion() }
        inputTelefono.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarTelefono() }
        inputEmail.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validarEmail() }

        // --- Acción del botón INSCRIBIR ---
        btnInscribir.setOnClickListener {
            ocultarTeclado(it)
            if (!validarTodo()) return@setOnClickListener

            val socio = Socio(
                nombre = inputNombre.text.toString().trim(),
                apellido = inputApellido.text.toString().trim(),
                dni = inputDni.text.toString().trim(),
                fechaNacimiento = inputFechaNacimiento.text.toString().trim(),
                direccion = inputDireccion.text.toString().trim(),
                telefono = inputTelefono.text.toString().trim(),
                email = inputEmail.text.toString().trim(),
                tieneFichaMedica = checkFichaMedica.isChecked
            )

            // TODO: Guardar en Room/SQLite o enviar a API
            Toast.makeText(
                this,
                "Inscripción registrada para ${socio.nombre} ${socio.apellido}",
                Toast.LENGTH_LONG
            ).show()

            limpiarFormulario()
            actualizarEstadoBoton()
        }
    }

    // --- Soporte botón "Up" del Toolbar ---
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    // ---------- Validaciones individuales ----------
    private fun validarNombre(): Boolean {
        val v = inputNombre.text.toString().trim()
        return when {
            v.isEmpty() -> setErr(inputNombre, "Ingresá el nombre")
            !v.matches(Regex("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ\\s]{2,}$")) ->
                setErr(inputNombre, "Nombre inválido")
            else -> clearErr(inputNombre)
        }
    }

    private fun validarApellido(): Boolean {
        val v = inputApellido.text.toString().trim()
        return when {
            v.isEmpty() -> setErr(inputApellido, "Ingresá el apellido")
            !v.matches(Regex("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ\\s]{2,}$")) ->
                setErr(inputApellido, "Apellido inválido")
            else -> clearErr(inputApellido)
        }
    }

    // <-- CAMBIO pedido: DNI EXACTAMENTE 9 dígitos -->
    private fun validarDni(): Boolean {
        val v = inputDni.text.toString().trim()
        return when {
            v.isEmpty() -> setErr(inputDni, "Ingresá el DNI")
            !v.matches(Regex("^[0-9]{9}$")) -> setErr(inputDni, "DNI debe tener 9 dígitos")
            else -> clearErr(inputDni)
        }
    }

    private fun validarFecha(): Boolean {
        val v = inputFechaNacimiento.text.toString().trim()
        if (v.isEmpty()) return setErr(inputFechaNacimiento, "Elegí la fecha")

        return try {
            val fecha = sdf.parse(v)!!
            val hoy = Calendar.getInstance().time
            if (fecha.after(hoy)) {
                setErr(inputFechaNacimiento, "La fecha no puede ser futura")
            } else {
                clearErr(inputFechaNacimiento)
            }
        } catch (e: ParseException) {
            setErr(inputFechaNacimiento, "Formato inválido (dd/MM/aaaa)")
        }
    }

    private fun validarDireccion(): Boolean {
        val v = inputDireccion.text.toString().trim()
        return when {
            v.isEmpty() -> setErr(inputDireccion, "Ingresá la dirección")
            v.length < 5 -> setErr(inputDireccion, "Dirección demasiado corta")
            else -> clearErr(inputDireccion)
        }
    }

    private fun validarTelefono(): Boolean {
        val v = inputTelefono.text.toString().trim()
        val soloDigitos = v.filter { it.isDigit() } // permite +, espacios y guiones
        return when {
            v.isEmpty() -> setErr(inputTelefono, "Ingresá el teléfono")
            soloDigitos.length !in 6..15 -> setErr(inputTelefono, "Teléfono inválido")
            else -> clearErr(inputTelefono)
        }
    }

    private fun validarEmail(): Boolean {
        val v = inputEmail.text.toString().trim()
        return when {
            v.isEmpty() -> setErr(inputEmail, "Ingresá el email")
            !Patterns.EMAIL_ADDRESS.matcher(v).matches() -> setErr(inputEmail, "Email inválido")
            else -> clearErr(inputEmail)
        }
    }

    private fun validarTodo(): Boolean {
        val n = validarNombre()
        val a = validarApellido()
        val d = validarDni()
        val f = validarFecha()
        val dir = validarDireccion()
        val t = validarTelefono()
        val e = validarEmail()
        return n && a && d && f && dir && t && e
    }

    // ---------- Habilitar/Deshabilitar botón ----------
    private fun actualizarEstadoBoton() {
        val habilitar = validarTodo()
        btnInscribir.isEnabled = habilitar
        btnInscribir.alpha = if (habilitar) 1f else 0.5f
    }

    // ---------- Utilidades ----------
    private fun setErr(et: EditText, msg: String): Boolean {
        et.error = msg
        return false
    }

    private fun clearErr(et: EditText): Boolean {
        et.error = null
        return true
    }

    private fun limpiarFormulario() {
        inputNombre.text?.clear()
        inputApellido.text?.clear()
        inputDni.text?.clear()
        inputFechaNacimiento.text?.clear()
        inputDireccion.text?.clear()
        inputTelefono.text?.clear()
        inputEmail.text?.clear()
        checkFichaMedica.isChecked = false
        inputNombre.requestFocus()
    }

    private fun ocultarTeclado(view: View) {
        (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
