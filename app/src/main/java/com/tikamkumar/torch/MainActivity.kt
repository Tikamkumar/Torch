package com.tikamkumar.torch

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private lateinit var binding: com.tikamkumar.torch.databinding.ActivityMainBinding
    private val cameraRequest = 123
    private var hasFlash = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = com.tikamkumar.torch.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            cameraRequest
        )
        hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        binding.torch.setOnClickListener {
            if (hasFlash) {
                if (binding.hello.text == "a") {
                    binding.torch.setImageResource(R.drawable.i)
                    binding.hello.text = "b"
                    binding.parent.setBackgroundResource(android.R.color.holo_orange_light)
                    flashlightOn()
                } else {
                    binding.torch.setImageResource(R.drawable.torch)
                    binding.hello.text = "a"
                    binding.parent.setBackgroundResource(R.color.black)
                    flashlightoff()
                }
            } else {
                Toast.makeText(this, "No Flashlight available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun flashlightoff() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: Exception) {
            Log.i("MainActivity", "Error")
        }
    }

    private fun flashlightOn() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: Exception) {
            Log.i("MainActivity", "Error")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            cameraRequest -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
                } else {
                    binding.torch.isEnabled = false
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

}