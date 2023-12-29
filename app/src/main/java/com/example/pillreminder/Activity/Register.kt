package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pillreminder.UserRegister
import com.example.pillreminder.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    //Para las cajas de texto
    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegistrarse.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.progressBarRegister.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
                if(authResult.isSuccessful){

                    // Verifica si hay un usuario autenticado
                    val currentUser = firebaseAuth.currentUser
                    val userId = currentUser!!.uid
                    //val myRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
                    //Enviar correo para verificación de email
                    var user = firebaseAuth.currentUser
                    val firsName = binding.tietNameRegister.text.toString()
                    val lastName = binding.tietLNameRegister.text.toString()
                    val userName = binding.tietUserNameRegister.text.toString()
                    val mail = binding.tietEmailRegister.text.toString()
                    val pass = binding.tietContraseniaRegister.text.toString()
                    val id = userId

                    user?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        Toast.makeText(this, "No se pudo enviar el correo de verificación", Toast.LENGTH_SHORT).show()
                    }

                    database = FirebaseDatabase.getInstance().getReference("Users")
                    val User = UserRegister(id,firsName, lastName, userName, mail, pass, age = null,
                    disease = null, medications = null, indications = null)
                    database.child(id).setValue(User).addOnSuccessListener {
                        binding.tietNameRegister.text?.clear()
                        binding.tietLNameRegister.text?.clear()
                        binding.tietUserNameRegister.text?.clear()
                        binding.tietEmailRegister.text?.clear()
                        binding.tietContraseniaRegister.text?.clear()

                        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "No se pudo guardar", Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("psw", contrasenia)
                    startActivity(intent)
                    finish()


                }else{
                    binding.progressBarRegister.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
        }

        //Iniciar sesión
        binding.tvReturnLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun validaCampos(): Boolean{
        email = binding.tietEmailRegister.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContraseniaRegister.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmailRegister.error = "Se requiere el correo"
            binding.tietEmailRegister.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContraseniaRegister.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.tietContraseniaRegister.requestFocus()
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
                binding.tietEmailRegister.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.tietEmailRegister.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.tietContraseniaRegister.error = "La contraseña no es válida"
                binding.tietContraseniaRegister.requestFocus()
                binding.tietContraseniaRegister.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, "Error: el correo electrónico ya está en uso con otra cuenta.", Toast.LENGTH_LONG).show()
                binding.tietEmailRegister.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmailRegister.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, "Error: La sesión ha expirado. Favor de ingresar nuevamente.", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, "Error: No existe el usuario con la información proporcionada.", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, "La contraseña porporcionada es inválida", Toast.LENGTH_LONG).show()
                binding.tietContraseniaRegister.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.tietContraseniaRegister.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, "Red no disponible o se interrumpió la conexión", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error. No se pudo autenticar exitosamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}