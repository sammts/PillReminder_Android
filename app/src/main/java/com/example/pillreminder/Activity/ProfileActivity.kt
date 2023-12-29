package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pillreminder.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
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
                    binding.tvNameUser.text = "$firstName $lastName"
                    binding.tvEmailUser.text = "$mail"
                }else{
                    Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error, no se pudo encontrar ningun usuario", Toast.LENGTH_SHORT).show()
            }
        }else {
            // No user is signed in
        }

        binding.buttomProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.buttomHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, Login::class.java))
            Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
        }

        binding.clShareProfile.setOnClickListener {
            startActivity(Intent(this, LinkProfileActivity::class.java))
        }

        binding.clEditProfile.setOnClickListener {
            startActivity(Intent(this, EditMyDataActivity::class.java))
        }

    }
}