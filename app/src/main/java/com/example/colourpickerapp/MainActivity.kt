package com.example.colourpickerapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var txtHex : TextView
    private lateinit var colorPreview : View
    private lateinit var seekRed : SeekBar
    private lateinit var seekGreen : SeekBar
    private lateinit var seekBlue : SeekBar
    private lateinit var seekAlpha : SeekBar
    private lateinit var buttonApply : Button
    private lateinit var buttonCancel : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        txtHex = findViewById(R.id.colorText)
        colorPreview = findViewById(R.id.viewColorPreview)
        seekRed = findViewById(R.id.seekBarRed)
        seekGreen = findViewById(R.id.seekBarGreen)
        seekBlue = findViewById(R.id.seekBarBlue)
        seekAlpha = findViewById(R.id.seekBarWhite)
        buttonApply = findViewById(R.id.buttonApply)
        buttonCancel = findViewById(R.id.buttonCancel)

        val seekBars = listOf(seekRed, seekGreen, seekBlue, seekAlpha)

        seekBars.forEach { seekBar ->
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    updateColor()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        buttonApply.setOnClickListener {
            applyHexColor()
        }

        buttonCancel.setOnClickListener {
            updateColor()
        }
    }

    private fun updateColor() {
        val r = seekRed.progress
        val g = seekGreen.progress
        val b = seekBlue.progress
        val a = seekAlpha.progress

        val hexColor = String.format("#%02X%02X%02X%02X", a, r, g, b)

        txtHex.text = hexColor
        colorPreview.setBackgroundColor(Color.argb(a, r, g, b))
    }

    private fun applyHexColor() {
        val hex = findViewById<TextView>(R.id.colorText).text.toString().trim()

        val regex = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6})$".toRegex()
        if (!regex.matches(hex)) {
            Toast.makeText(this, "Invalid HEX format", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val color = Color.parseColor(hex)

            val a = Color.alpha(color)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            seekAlpha.progress = a
            seekRed.progress = r
            seekGreen.progress = g
            seekBlue.progress = b

            txtHex.text = hex
            colorPreview.setBackgroundColor(color)

        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "Invalid HEX code", Toast.LENGTH_SHORT).show()
        }
    }
}