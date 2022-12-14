package com.metalichesky.zvuk.audio.file

import com.metalichesky.zvuk.audio.AudioParams
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.*

class WavFileWriter {
    private var fileStream: BufferedOutputStream? = null
    private var tempFile: File? = null
    private var tempFileSize: Int = 0

    fun prepare() {
        tempFileSize = 0
        tempFile = createTempFile()
        fileStream = BufferedOutputStream(tempFile?.outputStream())
    }

    private fun getCacheDir(): File {
        return File("C:\\Users\\Dmitriy\\Desktop\\cache").apply {
            if (!exists()) {
                mkdirs()
            }
        }
//        return App.instance.externalCacheDir!!
    }

    private fun createTempFile(): File {
        val name = UUID.randomUUID().toString()
        return File(getCacheDir(), name)
    }

    fun write(byteArray: ByteArray, size: Int = byteArray.size) {
        if (tempFile == null) {
            prepare()
        }
        fileStream?.write(byteArray, 0, size)
        tempFileSize += size
    }

    fun complete(file: File, audioParams: AudioParams) {
        val tempFile = tempFile ?: return
        val sourceFileStream = BufferedInputStream(tempFile.inputStream())
        val destinationFileStream = BufferedOutputStream(file.outputStream())
        val byteArray = ByteArray(8 * 1024)
        var readed = sourceFileStream.read(byteArray)
        val fileSize = tempFile.length().toInt()
        println("complete() writed size ${tempFileSize} file size ${fileSize}")
        if (readed > 0) {
            destinationFileStream.write(
                WAVHeader(
                    audioParams,
                    fileSize
                ).header)
        }
        while(readed > 0) {
            destinationFileStream.write(byteArray, 0, readed)
            readed = sourceFileStream.read(byteArray)
        }
        destinationFileStream.flush()
        destinationFileStream.close()
        sourceFileStream.close()
    }

    fun release() {
        fileStream?.flush()
        fileStream?.close()
        tempFile?.delete()
        tempFile = null
        fileStream = null
        tempFileSize = 0
    }
}