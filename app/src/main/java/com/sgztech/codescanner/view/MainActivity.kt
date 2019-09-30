package com.sgztech.codescanner.view

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.InterstitialAd
import com.sgztech.codescanner.R
import com.sgztech.codescanner.util.AdsUtil.ID_INTERSTICIAL_AD
import com.sgztech.codescanner.util.AdsUtil.buildIntersticialAd
import com.sgztech.codescanner.util.AdsUtil.init
import com.sgztech.codescanner.util.AdsUtil.setupBannerAd
import com.sgztech.codescanner.util.AdsUtil.showIntersticialAd
import com.sgztech.codescanner.util.AlertDialogUtil
import com.sgztech.codescanner.util.PermissionUtil.checkResultPermission
import com.sgztech.codescanner.util.PermissionUtil.havePermissions
import com.sgztech.codescanner.util.SnackBarUtil.show
import com.sgztech.codescanner.view.ScannerActivity.Companion.setupIntent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDefaultRbCam()
        setupBtnScan()
        setupBtnCopy()
        setupAds()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.nav_item_rate -> {
                rateApp()
                return true
            }
            R.id.nav_item_share -> {
                shareApp()
                return true
            }
            R.id.nav_item_about -> {
                AlertDialogUtil.showSimpleDialog(
                    this,
                    R.string.dialog_about_app_title,
                    R.string.dialog_about_app_message
                )
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun rateApp() {
        val uri = Uri.parse(getString(R.string.app_store_url))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            show(btnCopy, R.string.msg_store_app_not_found)
        }
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        val msg = getString(R.string.app_store_details).plus(getString(R.string.app_store_url))
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        intent.type = "text/plain"

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setupDefaultRbCam() {
        rb_cam_back.isChecked = true
    }

    private fun setupBtnCopy() {
        btnCopy.setOnClickListener {
            val result = etResult.text.toString()

            if (result.isNotEmpty()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip =
                    ClipData.newPlainText(LABEL_CLIP_DATA, result)
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
        init(applicationContext)
        setupBannerAd(adView)
        mInterstitialAd = buildIntersticialAd(applicationContext, ID_INTERSTICIAL_AD)
    }

    private fun openScannerActivity() {
        startActivityForResult(
            setupIntent(applicationContext, selectedCam()),
            RESULT_CODE
        )
        etResult.text = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showIntersticialAd(mInterstitialAd)
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

    private fun selectedCam(): Int{
        if(rb_cam_back.isChecked){
            return CAMERA_BACK
        }
        return CAMERA_FRONT
    }


    companion object {
        const val RESULT_CODE = 20
        const val REQUEST_CODE_PERMISSION = 10
        const val CAMERA_FRONT = 1
        const val CAMERA_BACK = 2
        const val SELECTED_CAM = "SELECTED_CAM"
        const val SCANNED_DATA = "SCANNED_DATA"
        const val SCANNED_ERROR = "SCANNED_ERROR"
        const val LABEL_CLIP_DATA = "bar_code"
    }

}
