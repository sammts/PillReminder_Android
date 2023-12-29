package com.example.pillreminder.Activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pillreminder.databinding.ActivityLinkProfileActicityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LinkProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityLinkProfileActicityBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinkProfileActicityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Inicializa la instancia de Database
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Verifica si hay un usuario autenticado
        val currentUser = firebaseAuth.currentUser

        if(currentUser != null) {
            val uid = currentUser.uid
            database.child(uid).get().addOnSuccessListener {
                if(it.exists()){
                    val firstName = it.child("firstName").value
                    val lastName = it.child("lastName").value
                    binding.editIDCL.text = uid
                    binding.tvNameUser.text = "$firstName $lastName"
                }else{
                    Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error, no se pudo encontrar ningun usuario", Toast.LENGTH_SHORT).show()
            }
        }else {
            // No user is signed in
        }

        binding.clBack.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnCopy.setOnClickListener {
            copiarTextoAlPortapapeles()
        }
    }

    private fun copiarTextoAlPortapapeles() {
        val textoACopiar = binding.editIDCL.text.toString()

        // Obtener el ClipboardManager desde el sistema
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Crear un objeto ClipData para almacenar el texto a copiar
        val clipData = ClipData.newPlainText("TextoCopiado", textoACopiar)

        // Colocar el ClipData en el portapapeles
        clipboardManager.setPrimaryClip(clipData)

        // Mostrar un mensaje de éxito (esto es opcional)
        Toast.makeText(this, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }
}