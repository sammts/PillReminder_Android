package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pillreminder.R
import com.example.pillreminder.databinding.ActivityEditMyDataBinding
import com.example.pillreminder.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditMyDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMyDataBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMyDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Inicializa la instancia de Database
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Verifica si hay un usuario autenticado
        val currentUser = firebaseAuth.currentUser

        binding.btnSave.setOnClickListener {
            val uid = currentUser!!.uid
            val name = binding.editNameUpdt.text.toString()
            val lastName = binding.editLastNameUpdt.text.toString()
            val userName = binding.editUserNameUpdt.text.toString()

            val user = mapOf<String,String>(
                "firstName" to name,
                "lastName" to lastName,
                "userName" to userName,
            )

            database.child(uid).updateChildren(user).addOnSuccessListener {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfileActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar la información", Toast.LENGTH_SHORT).show()
            }
        }

        binding.clBack.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }


    }
}