package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pillreminder.R
import com.example.pillreminder.databinding.ActivityInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa la instancia de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inicializa la instancia de Database
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Verifica si hay un usuario autenticado
        val currentUser = firebaseAuth.currentUser

        val extras = intent.extras
        val extras1 = intent.extras
        val extras2 = intent.extras
        val extras3 = intent.extras
        if (extras != null) {
            val busqueda = extras.getString("busqueda")
            database.child(busqueda.toString()).get().addOnSuccessListener {
                if (it.exists()) {
                    val firstName = it.child("firstName").value
                    val lastName = it.child("lastName").value
                    val mail = it.child("email").value
                    val age = it.child("age").value
                    val disease = it.child("disease").value
                    val medications = it.child("medications").value
                    val indications = it.child("indications").value
                    binding.tvNameParent.text = "$firstName $lastName"
                    binding.tvEmailParent.text = "$mail"
                    binding.editAgeParent.text = "Edad: $age"
                    binding.editDiseaseParent.text = "Enfermedades: $disease"
                    binding.editMedicationsParent.text = "Medicamentos: $medications"
                    binding.editIndicationsParent.text = "Indicaciones: $indications"
                } else {
                    Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Error, no se pudo encontrar ningun usuario",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        else if (extras1 != null) {
            val cv1 = extras1.getString("card1")
            if (cv1 != null) {
                database.child(cv1).get().addOnSuccessListener {
                    if (it.exists()) {
                        val firstName = it.child("firstName").value
                        val lastName = it.child("lastName").value
                        val mail = it.child("email").value
                        val age = it.child("age").value
                        val disease = it.child("disease").value
                        val medications = it.child("medications").value
                        val indications = it.child("indications").value
                        binding.tvNameParent.text = "$firstName $lastName"
                        binding.tvEmailParent.text = "$mail"
                        binding.editAgeParent.text = "Edad: $age"
                        binding.editDiseaseParent.text = "Enfermedades: $disease"
                        binding.editMedicationsParent.text = "Medicamentos: $medications"
                        binding.editIndicationsParent.text = "Indicaciones: $indications"
                    } else {
                        Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error, no se pudo encontrar ningun usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        else if (extras2 != null) {
            val cv2 = extras2.getString("card2")
            if (cv2 != null) {
                database.child(cv2).get().addOnSuccessListener {
                    if (it.exists()) {
                        val firstName = it.child("firstName").value
                        val lastName = it.child("lastName").value
                        val mail = it.child("email").value
                        val age = it.child("age").value
                        val disease = it.child("disease").value
                        val medications = it.child("medications").value
                        val indications = it.child("indications").value
                        binding.tvNameParent.text = "$firstName $lastName"
                        binding.tvEmailParent.text = "$mail"
                        binding.editAgeParent.text = "Edad: $age"
                        binding.editDiseaseParent.text = "Enfermedades: $disease"
                        binding.editMedicationsParent.text = "Medicamentos: $medications"
                        binding.editIndicationsParent.text = "Indicaciones: $indications"
                    } else {
                        Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error, no se pudo encontrar ningun usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        else if (extras3 != null) {
            val cv3 = extras3.getString("card3")
            if (cv3 != null) {
                database.child(cv3).get().addOnSuccessListener {
                    if(it.exists()){
                        val firstName = it.child("firstName").value
                        val lastName = it.child("lastName").value
                        val mail = it.child("email").value
                        val age = it.child("age").value
                        val disease = it.child("disease").value
                        val medications = it.child("medications").value
                        val indications = it.child("indications").value
                        binding.tvNameParent.text = "$firstName $lastName"
                        binding.tvEmailParent.text = "$mail"
                        binding.editAgeParent.text = "Edad: $age"
                        binding.editDiseaseParent.text = "Enfermedades: $disease"
                        binding.editMedicationsParent.text = "Medicamentos: $medications"
                        binding.editIndicationsParent.text = "Indicaciones: $indications"
                    }else{
                        Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Error, no se pudo encontrar ningun usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.clBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}