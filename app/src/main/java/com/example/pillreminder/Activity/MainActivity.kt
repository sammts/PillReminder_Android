package com.example.pillreminder.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pillreminder.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        screenSplash.setKeepOnScreenCondition{ false }

        // Inicializa la instancia de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inicializa la instancia de Database
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Verifica si hay un usuario autenticado
        val currentUser = firebaseAuth.currentUser

        if(currentUser != null) {
            val uid = currentUser.uid
            //Toast.makeText(this, "Usuario: $uid", Toast.LENGTH_LONG).show()
            database.child(uid).get().addOnSuccessListener {
                if(it.exists()){
                    val firstName = it.child("firstName").value
                    val lastName = it.child("lastName").value
                    val disease = it.child("disease").value
                    val medications = it.child("medications").value
                    val indications = it.child("indications").value
                    binding.tvHelloUser.text = "Hola, $firstName!"
                    binding.editDiseaseMD.text = "Enfermedad: $disease"
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

        /*database.child("BB4h1gilr5Nwnp3jseHQC6c8zpf2").get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value
                //val disease = it.child("disease").value
                //val medications = it.child("medications").value
                binding.tvUser1.text = "$firstName"
                //binding.editDiseaseMD.text = "Enfermedad: $disease"
                //binding.editMedicationsMD.text = "Medicamentos: $medications"
            }else{
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error, no se pudo encontrar ningun usuario", Toast.LENGTH_SHORT).show()
        }

        database.child("NGpl1Ii0PveVRxcEFfgNHygRWSt2").get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value
                //val disease = it.child("disease").value
                //val medications = it.child("medications").value
                binding.tvUser2.text = "$firstName"
                //binding.editDiseaseMD.text = "Enfermedad: $disease"
                //binding.editMedicationsMD.text = "Medicamentos: $medications"
            }else{
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error, no se pudo encontrar ningun usuario", Toast.LENGTH_SHORT).show()
        }

        database.child("QrUX8Pq93Na0Xtm7WTsrcG0Z3vN2").get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value
                //val disease = it.child("disease").value
                //val medications = it.child("medications").value
                binding.tvUser3.text = "$firstName"
                //binding.editDiseaseMD.text = "Enfermedad: $disease"
                //binding.editMedicationsMD.text = "Medicamentos: $medications"
            }else{
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error, no se pudo encontrar ningun usuario", Toast.LENGTH_SHORT).show()
        }*/

        binding.btnSearch.setOnClickListener {
            val busqueda = binding.etSearch.text.toString()
            if (busqueda.isEmpty()){
                Toast.makeText(this, "Por favor, introduce un ID", Toast.LENGTH_SHORT).show()
            }else{
                val intent1 = Intent(this, InfoActivity::class.java)
                intent1.putExtra("busqueda", busqueda)
                startActivity(intent1)
            }
        }

        /*binding.cvUser1.setOnClickListener{
            val uid = "BB4h1gilr5Nwnp3jseHQC6c8zpf2"
            val intent2 = Intent(this, InfoActivity::class.java)
            intent2.putExtra("card1", uid)
            startActivity(intent2)
        }

        binding.cvUser2.setOnClickListener{
            val uid = "NGpl1Ii0PveVRxcEFfgNHygRWSt2"
            val intent3 = Intent(this, InfoActivity::class.java)
            intent3.putExtra("card2", uid)
            startActivity(intent3)
        }

        binding.cvUser3.setOnClickListener{
            val uid = "QrUX8Pq93Na0Xtm7WTsrcG0Z3vN2"
            val intent4 = Intent(this, InfoActivity::class.java)
            intent4.putExtra("card3", uid)
            startActivity(intent4)
        }*/

        binding.ivUserMain.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.buttomProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.tvLookAllInfoUser.setOnClickListener {
            startActivity(Intent(this, MyDataActivity::class.java))
        }

        binding.buttomHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}