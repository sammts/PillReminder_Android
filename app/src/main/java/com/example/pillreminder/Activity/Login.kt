package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pillreminder.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //Para las cajas de texto
    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenSplash.setKeepOnScreenCondition{ false }

        firebaseAuth = FirebaseAuth.getInstance()

        ///Iniciar sesión
        binding.btnLogin.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            //binding.progressBarLogin.visibility = View.VISIBLE

            //autenticando al usuario
            autenticaUsuario(email, contrasenia)
        }

        //Registrarse
        binding.tvRegistrate.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        //Restablecer contraseña
        binding.tvRestablecerPasswordLogin.setOnClickListener {
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            val passwordResetDialog = AlertDialog.Builder(it.context)
                .setTitle("Restablecer contraseña")
                .setMessage("Ingrese su correo para recibir el enlace y restablecer su contraseña")
                .setView(resetMail)
                .setPositiveButton("Enviar correo") { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "El enlace para restablecer la contraseña ha sido enviado a su correo",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "El enlace no se ha podido enviar: ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Favor de ingresar la dirección de correo",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    private fun validaCampos(): Boolean{
        email = binding.tietEmailLogin.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContraseniaLogin.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmailLogin.error = "Se requiere el correo"
            binding.tietEmailLogin.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContraseniaLogin.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.tietContraseniaLogin.requestFocus()
            return false
        }

        return true
    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(this, "Error: El correo electrónico no tiene un formato correcto", Toast.LENGTH_SHORT).show()
                binding.tietEmailLogin.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.tietEmailLogin.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.tietContraseniaLogin.error = "La contraseña no es válida"
                binding.tietContraseniaLogin.requestFocus()
                binding.tietContraseniaLogin.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, "Error: el correo electrónico ya está en uso con otra cuenta.", Toast.LENGTH_LONG).show()
                binding.tietEmailLogin.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmailLogin.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, "Error: La sesión ha expirado. Favor de ingresar nuevamente.", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, "Error: No existe el usuario con la información proporcionada.", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, "La contraseña porporcionada es inválida", Toast.LENGTH_LONG).show()
                binding.tietContraseniaLogin.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.tietContraseniaLogin.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, "Red no disponible o se interrumpió la conexión", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error. No se pudo autenticar exitosamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticaUsuario(usr: String, psw:String){
        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener {authResult->
            if(authResult.isSuccessful){
                Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("psw", psw)
                startActivity(intent)
                finish()
            }else{
                binding.progressBarLogin.visibility = View.GONE
                manejaErrores(authResult)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}