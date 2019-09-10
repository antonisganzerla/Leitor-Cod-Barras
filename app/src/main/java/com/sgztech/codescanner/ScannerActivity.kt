package com.sgztech.codescanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.sgztech.codescanner.MainActivity.Companion.SCANNED_DATA
import com.sgztech.codescanner.MainActivity.Companion.SCANNED_ERROR
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        codeScanner = CodeScanner(this, scanner_view)

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            val intent = Intent()
            intent.putExtra(SCANNED_DATA, it.text)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            val intent = Intent()
            intent.putExtra(SCANNED_ERROR, it.message)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
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
}
