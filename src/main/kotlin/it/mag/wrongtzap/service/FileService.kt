package it.mag.wrongtzap.service

import io.minio.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class FileService @Autowired constructor(
    private val minioClient: MinioClient)
{
    val bucketName = "files"
    fun bucketExists(bucketName: String): Boolean{
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
    }

    fun uploadFile(bucketName: String, filePath: String,  file: MultipartFile){

        val inputStream: InputStream = file.inputStream

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .`object`(filePath)
                .stream(inputStream, -1,10485760)
                .contentType(file.contentType)
                .build()
        )
    }

    fun downloadFile(bucketName: String, filePath: String): ByteArray{

        val response =  minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(filePath)
                .build()
        )

        return response.readAllBytes()
    }


    fun checkFileExistence(bucketName: String, filePath: String): Boolean{
        try {
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(filePath)
                    .build()
            )
            return true
        }
        catch (exception: Exception){
            return false
        }
    }

}
