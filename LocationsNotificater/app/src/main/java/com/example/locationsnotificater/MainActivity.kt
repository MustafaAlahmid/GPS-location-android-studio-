package com.example.locationsnotificater

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
        lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.btn)

        button.setOnClickListener {
            var intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)


        }

    }

}
