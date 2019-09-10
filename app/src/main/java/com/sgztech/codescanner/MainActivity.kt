package com.sgztech.codescanner

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBtnScan()
        setupBtnCopy()
    }

    private fun setupBtnCopy() {
        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("bar_code", etData.text.toString())
            clipboard.primaryClip = clip

            Toast.makeText(
                applicationContext,
                "Copiado para Área de Transferência",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupBtnScan() {
        btnScan.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivityForResult(intent, RESULT_CODE)
            etData.text.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let{
            if(requestCode == RESULT_CODE){
                if(resultCode == Activity.RESULT_OK){
                    val scannedData = it.getStringExtra(SCANNED_DATA)
                    etData.setText(scannedData)
                    btnCopy.isEnabled = true
                }

                if(resultCode == Activity.RESULT_CANCELED){
                    val scannedError = it.getStringExtra(SCANNED_ERROR)
                    Toast.makeText(
                        applicationContext,
                        scannedError,
                        Toast.LENGTH_SHORT
                    ).show()
                    etData.text.clear()
                    btnCopy.isEnabled = false
                }
            }
        }
    }


    companion object{
        const val RESULT_CODE = 20
        const val SCANNED_DATA = "SCANNED_DATA"
        const val SCANNED_ERROR = "SCANNED_ERROR"
    }

}
