package com.sgztech.codescanner.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.sgztech.codescanner.R
import com.sgztech.codescanner.view.MainActivity.Companion.CAMERA_BACK
import com.sgztech.codescanner.view.MainActivity.Companion.CAMERA_FRONT
import com.sgztech.codescanner.view.MainActivity.Companion.SCANNED_DATA
import com.sgztech.codescanner.view.MainActivity.Companion.SCANNED_ERROR
import com.sgztech.codescanner.view.MainActivity.Companion.SELECTED_CAM
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        codeScanner = CodeScanner(this, scanner_view)

        setupTypeCam()
        setupSuccessCallback()
        setupErrorCallback()

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }


    private fun setupSuccessCallback() {
        codeScanner.decodeCallback = DecodeCallback {
            val intent = Intent()
            intent.putExtra(SCANNED_DATA, it.text)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun setupErrorCallback() {
        codeScanner.errorCallback = ErrorCallback {
            val intent = Intent()
            intent.putExtra(SCANNED_ERROR, it.message)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun setupTypeCam() {
        val selectedCam = intent.getIntExtra(SELECTED_CAM, CAMERA_BACK)
        codeScanner.camera = when (selectedCam) {
            CAMERA_BACK -> CodeScanner.CAMERA_BACK
            CAMERA_FRONT -> CodeScanner.CAMERA_FRONT
            else -> CodeScanner.CAMERA_BACK
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    companion object {

        fun setupIntent(context: Context, selectedCam: Int): Intent {
            val intent = Intent(context, ScannerActivity::class.java)
            intent.putExtra(SELECTED_CAM, selectedCam)
            return intent
        }
    }
}
