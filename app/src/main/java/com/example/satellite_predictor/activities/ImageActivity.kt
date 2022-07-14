package com.example.satellite_predictor.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.satellite_predictor.databinding.ActivityImageBinding
import com.example.satellite_predictor.viewModels.ImageViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*


class ImageActivity : AppCompatActivity() {
    val REQUEST_CODE = 200
    var PERMISSION_ALL = 1
    var IMAGE_PICK_CODE = 2
    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var selectedImageUri: Uri? = null
    var cameraUri: Uri? = null

    private lateinit var binding: ActivityImageBinding
    private lateinit var viewModel: ImageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun fromCamera(view: View) {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Satellite Image")
        values.put(
            MediaStore.Images.Media.DESCRIPTION,
            "Photo taken on " + System.currentTimeMillis()
        )
        cameraUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    fun fromGallery(view: View) {
        val pickImageIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        pickImageIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(pickImageIntent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            selectedImageUri = cameraUri
            uploadImage(selectedImageUri)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {

            selectedImageUri = data?.data

            uploadImage(selectedImageUri)

        }
    }

    private fun uploadImage(Uri: Uri?) {
        Log.i("URI=", Uri.toString())
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri)
        binding.imageView2.setImageBitmap(bitmap)

        val root = Environment.getExternalStorageDirectory().toString()
        Log.i("root-path", root)
        val myDir = File("$root/satellite_img")
        if (!myDir.exists()) {
            myDir.mkdirs()
            Log.i("MyDir", "My dir created")
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewModel.predict(file)

        viewModel.result.observe(this, androidx.lifecycle.Observer { result ->
            val s = result.prediction
            var t = ""
            for (x in s)
                t = t + x
            Log.i("predicted-result", t)
        })
    }


}