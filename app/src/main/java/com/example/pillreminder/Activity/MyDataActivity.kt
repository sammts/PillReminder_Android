package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pillreminder.R
import com.example.pillreminder.databinding.ActivityMyDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyDataActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyDataBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDataBinding.inflate(layoutInflater)
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
                    val mail = it.child("email").value
                    val age = it.child("age").value
                    val disease = it.child("disease").value
                    val medications = it.child("medications").value
                    val indications = it.child("indications").value
                    binding.tvNameUser.text = "$firstName $lastName"
                    binding.tvEmailUser.text = "$mail"
                    binding.editAgeMD.text = "Edad: $age"
                    binding.editDiseaseMD.text = "Enfermedades: $disease"
                    binding.editMedicationsMD.text = "Medicamentos: $medications"
                    binding.editIndicationsMD.text = "Indicaciones: $indications"
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
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnUpdate.setOnClickListener {
            startActivity(Intent(this, UpdateDataActivity::class.java))
        }


    }
}