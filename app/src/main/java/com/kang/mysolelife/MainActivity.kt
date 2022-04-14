package com.kang.mysolelife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kang.mysolelife.auth.IntroActivity
import com.kang.mysolelife.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        findViewById<ImageView>(R.id.settingBtn).setOnClickListener {

            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

        }

//        findViewById<Button>(R.id.logoutBtn).setOnClickListener {
//
//            auth.signOut()
//
//            val intent = Intent(this, IntroActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//
//
//        }

    }
}