package com.example.quizdone.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizdone.R
import com.example.quizdone.main.ui.category.CategoryFragment

class MainActivity : AppCompatActivity() {

    //private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, CategoryFragment.newInstance())
                .commitNow()
        }
    }
}
