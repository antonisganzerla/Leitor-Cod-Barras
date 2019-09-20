package com.sgztech.codescanner.view

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.sgztech.codescanner.R
import com.sgztech.codescanner.util.PermissionUtil.checkResultPermission
import com.sgztech.codescanner.util.PermissionUtil.havePermissions
import com.sgztech.codescanner.util.SnackBarUtil.show
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBtnScan()
        setupBtnCopy()
        setupAds()
    }

    private fun setupBtnCopy() {
        btnCopy.setOnClickListener {
            val result = etResult.text.toString()

            if (result.isNotEmpty()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip =
                    ClipData.newPlainText("bar_code", result)
                clipboard.primaryClip = clip
                show(it, R.string.msg_copied)
            } else {
                show(it, R.string.msg_dont_data_for_copy)
            }
        }
    }

    private fun setupBtnScan() {
        btnScan.setOnClickListener {
            if (havePermissions(this, REQUEST_CODE_PERMISSION)) {
                openScannerActivity()
            }
        }
    }

    private fun setupAds() {
        MobileAds.initialize(this)
        setupBannerAd()
        setupInterstitialAd()
    }

    private fun setupBannerAd() {
        adView.loadAd(AdRequest.Builder().addTestDevice(getString(R.string.device_test_id)).build())
    }

    private fun setupInterstitialAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-9764822217711668/6092884455"
        loadInterstitialAd()
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                Log.d("Ads", "loaded new ads")
                loadInterstitialAd()
            }
        }
    }

    private fun loadInterstitialAd() {
        mInterstitialAd.loadAd(AdRequest.Builder().addTestDevice(getString(R.string.device_test_id)).build())
    }

    private fun openScannerActivity() {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivityForResult(
            intent,
            RESULT_CODE
        )
        etResult.text = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showInterstitialAd()
        data?.let {
            if (requestCode == RESULT_CODE) {

                if (resultCode == Activity.RESULT_OK) {
                    val scannedData = it.getStringExtra(SCANNED_DATA)
                    etResult.text = scannedData
                }

                if (resultCode == Activity.RESULT_CANCELED) {
                    val scannedError = it.getStringExtra(SCANNED_ERROR)
                    show(btnCopy, scannedError)
                    etResult.text = ""
                }
            }
        }
    }

    private fun showInterstitialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.d("Ads", "The interstitial wasn't loaded yet.")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (checkResultPermission(this, grantResults, permissions)) {
                    openScannerActivity()
                } else {
                    show(btnCopy, R.string.msg_without_access_to_camera)
                }
                return
            }
        }
    }


    companion object {
        const val RESULT_CODE = 20
        const val REQUEST_CODE_PERMISSION = 10
        const val SCANNED_DATA = "SCANNED_DATA"
        const val SCANNED_ERROR = "SCANNED_ERROR"
    }

}
