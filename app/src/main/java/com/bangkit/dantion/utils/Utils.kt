package com.bangkit.dantion

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bangkit.dantion.ui.custom.CustomTextInputLayout
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun checkPassword(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> emptyData(text,customTextInputLayout,context)
        text.length in 1..5 -> customTextInputLayout.error = context.getString(R.string.password_less_6)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun checkEmail(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> emptyData(text,customTextInputLayout,context)
        !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches() -> customTextInputLayout.error = context.getString(R.string.email_incorrect)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun checkNumber(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> emptyData(text,customTextInputLayout,context)
        text.length in 1..9 -> customTextInputLayout.error = context.getString(R.string.number_less_10)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun emptyData(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> customTextInputLayout.error = context.getString(R.string.empty_form)
        else -> hideTextInputError(customTextInputLayout)
    }
}

fun hideTextInputError(customTextInputLayout: CustomTextInputLayout){
    customTextInputLayout.error = null
    customTextInputLayout.isErrorEnabled = false
}

fun getAddress(latitude: Double, longitude: Double, context: Context): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses.isNotEmpty()) {
        return addresses[0].getAddressLine(0)
    }
    return null
}
fun getCity(latitude: Double, longitude: Double, context: Context): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
    return addresses[0].subAdminArea
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        streamLength = getImageSize(bitmap,compressQuality)
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}
fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
fun getImageSize(bitmap: Bitmap, compressQuality: Int): Int{
    val bmpStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
    val bmpPicByteArray = bmpStream.toByteArray()
    return bmpPicByteArray.size
}

fun resizeImage(bitmap: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {
    val height = bitmap.height
    val width = bitmap.width

    val ratio: Float = width.toFloat() / height.toFloat()
    val maxRatio: Float = maxWidth.toFloat() / maxWidth.toFloat()

    maxWidth.apply {
        if (maxRatio > ratio) (maxHeight.toFloat() * ratio).toInt()
        else (maxWidth.toFloat() / ratio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true)
}

fun flipImage(bitmap: Bitmap): Bitmap {
    val matrix = Matrix()
    matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f) // flip gambar
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
fun cropImage(image: Bitmap): Bitmap {
    return when{
        image.width>image.height -> resizeImage(image, 768, 1024)
        image.width<image.height -> resizeImage(image, 1024, 1024)
        else -> resizeImage(image, 1024, 768)
    }
}

fun getTimeDifference(fromDate: String): String{
    val oldDate = if (fromDate.contains("Z")) fromDate.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").formatTo("yyyy-MM-dd HH:mm:ss").toDate("yyyy-MM-dd HH:mm:ss")
                    else fromDate.toDate("yyyy-MM-dd HH:mm:ss.SSSX").formatTo("yyyy-MM-dd HH:mm:ss").toDate("yyyy-MM-dd HH:mm:ss")
    val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()).toDate("yyyy-MM-dd HH:mm:ss")
    val diff = (currentDate.time + 50000) - oldDate.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return when{
        days>0 -> "${days.toInt()} hari yang lalu"
        hours>0 -> "${hours.toInt()} jam yang lalu"
        minutes>0 -> "${minutes.toInt()} menit yang lalu"
        seconds>0 -> "${seconds.toInt()} detik yang lalu"
        else -> "$seconds detik yang lalu"
    }
}

fun String.getDateFromTimeStamp(): String = substring(0, 10)

fun String.getTimeFromTimeStamp(): String = substring(11, 16)
fun String.getTimeWithSecondFromTimeStamp(): String = substring(11, 19)

fun String.getFirstName(): String = if (contains(" ")) {
    split(" ")[0].replaceFirstChar { it.uppercase() }
} else replaceFirstChar { it.uppercase() }

fun String.withDateFormat(): String {
    return this.toDate("yyyy-MM-dd").formatTo("EEEE, dd MMM yyyy")
}
fun String.withTimeFormat(): String {
    return this.toDate("k:mm").formatTo("kk:mm")
}
fun String.toDate(dateFormat: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale("id", "ID"))
    parser.timeZone = timeZone
    return parser.parse(this) as Date
}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale("id", "ID"))
    formatter.timeZone = timeZone
    return formatter.format(this)
}
fun setToastLong(message: String, context: Context){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
fun setToastShort(message: String, context: Context){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}