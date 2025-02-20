package it.mag.wrongtzap.controller

import it.mag.wrongtzap.controller.web.file.FileRequest
import it.mag.wrongtzap.manager.FileManager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController()
class FileController @Autowired constructor(
    private val fileManager: FileManager
){
    @PostMapping("/files/upload", consumes = ["multipart/form-data"])
    fun postFiles(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("entityId") entityId: String,
        @RequestParam("entityType")entityType: String,
    ) = fileManager.uploadFile(FileRequest(entityId = entityId, entityType = entityType), file)

    @PostMapping("/files/download")
    fun getFiles(@RequestBody request: FileRequest): ResponseEntity<ByteArray>{
        val response = fileManager.downloadFile(request)
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(response)
    }
}