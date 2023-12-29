package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.trusted.sharing.ShareData.KEY_TEXT
import com.example.pillreminder.R
import com.example.pillreminder.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateDataActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateDataBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
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

        binding.btnSave.setOnClickListener {
            val uid = currentUser!!.uid
            val age = binding.editAgeUpdt.text.toString()
            val disease = binding.editDiseaseUpdt.text.toString()
            val medications = binding.editMedicationsUpdt.text.toString()
            val indications = binding.editIndicationsUpdt.text.toString()

            val user = mapOf<String,String>(
                "age" to age,
                "disease" to disease,
                "medications" to medications,
                "indications" to indications
            )

            database.child(uid).updateChildren(user).addOnSuccessListener {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MyDataActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, "Error al actualizar la información", Toast.LENGTH_SHORT).show()
            }

            if (savedInstanceState != null) {
                val savedText = savedInstanceState.getString("savedText")
                binding.editAgeUpdt.setText(savedText)
            }
        }

        binding.clBack.setOnClickListener {
            startActivity(Intent(this, MyDataActivity::class.java))
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Guardar el texto actual en el estado
        outState.putString("savedText", binding.editAgeUpdt.text.toString())
        super.onSaveInstanceState(outState)
    }
}