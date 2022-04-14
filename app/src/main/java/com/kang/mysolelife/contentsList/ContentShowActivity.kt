package com.kang.mysolelife.contentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import com.kang.mysolelife.R

class ContentShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)

        val getUrl = intent.getStringExtra("url")

//        Toast.makeText(this, getUrl, Toast.LENGTH_SHORT).show()

        val webView : WebView = findViewById(R.id.webView)
        webView.loadUrl(getUrl.toString())

    }
}