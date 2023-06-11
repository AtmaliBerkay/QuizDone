package com.example.quizdone.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.quizdone.R
import com.example.quizdone.main.ui.category.CategoryFragment
import com.example.quizdone.main.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    //private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        Handler().postDelayed({
            supportFragmentManager.beginTransaction()
                .replace(R.id.navContainer, LoginFragment()).commit()
        },2000)

        //LoginFragment as the first screen
            /*supportFragmentManager.beginTransaction()
                .replace(R.id.navContainer, LoginFragment()).commit()*/

    }
}
