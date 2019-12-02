package com.example.demomvvm.util

import android.annotation.SuppressLint
import android.content.Context
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import id.zelory.compressor.Compressor
import io.reactivex.schedulers.Schedulers
import java.io.File


class AmazoneUploadUtil(var context: Context, var filelist: ArrayList<File>, var awsAmazoneUploadUtil: AwsUploadCompleteListner) {

    private var currentUploadIndex = 0
    private var multipleImageName: String = ""
    private var imagename: String = ""
    private lateinit var transferUtility: TransferUtility
    private var storebucketfolderPath: String = ""

    fun initialize(storebucketfolderPath: String) {
        transferUtility = TransferUtility.builder()
                .context(context)
                .awsConfiguration(AWSMobileClient.getInstance().configuration)
                .s3Client(AmazonS3Client(AWSMobileClient.getInstance().credentialsProvider))
                .build()
        multipleImageName = ""
        this.storebucketfolderPath = storebucketfolderPath
        uploadFile()

    }

    private fun singleFileUpload(file: File) {

        val uploadObserver = transferUtility.upload(BUCKET_NAME + storebucketfolderPath, imagename, file, CannedAccessControlList.PublicRead)

        uploadObserver.setTransferListener(object : TransferListener {

            override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED == uploadObserver.state) {
                    currentUploadIndex++
                    uploadFile()
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentDonef = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                percentDonef.toInt()


            }

            override fun onError(id: Int, ex: Exception) {
                if(currentUploadIndex!=0)
                currentUploadIndex--
                uploadFile()
                ex.printStackTrace()
            }
        })


    }

    private fun uploadFile() {
        if (filelist.size > currentUploadIndex) {
            compressImage()
        } else {
            awsAmazoneUploadUtil.onComplete(multipleImageName)
        }
    }

    @SuppressLint("CheckResult")
    private fun compressImage() {

        Compressor(context)
                .compressToFileAsFlowable(filelist.get(currentUploadIndex))
                .subscribeOn(Schedulers.io())
                .subscribe({ file ->
                    imagename = file.name

                    if (filelist.size - 1 != currentUploadIndex && filelist.size != 1)
                        multipleImageName = multipleImageName + file.name + ","
                    else
                        multipleImageName += file.name

                    singleFileUpload(file)

                }, { throwable ->
                    throwable.printStackTrace()

                })
    }

    interface AwsUploadCompleteListner {
        fun onComplete(imageName: String = "")
    }
}