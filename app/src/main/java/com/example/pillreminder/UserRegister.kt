package com.example.pillreminder

data class UserRegister(val id: String? = null, val firstName:String? = null, val lastName:String? = null,
                        val userName:String? = null, val email:String? = null,
                        val password:String?, val age:String? = null,
                        val disease:String? = null, val medications:String?,
                        val indications:String? = null)


