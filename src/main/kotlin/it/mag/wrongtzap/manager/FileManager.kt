package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.file.FileRequest
import it.mag.wrongtzap.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.NullPointerException

@Service
class FileManager @Autowired constructor(
    private val userService: UserService,
    private val groupService: GroupService,
    private val messageService: MessageService,
    private val fileService: FileService,
) {
    fun uploadFile(request: FileRequest, file:MultipartFile){
        if (checkEntityExistence(request.entityId, request.entityType)){
            val filePath = "/${request.entityType}/${request.entityId}/file"
            fileService.uploadFile("files", filePath,file)
        }
        else throw NullPointerException()
    }

    fun downloadFile(request: FileRequest): ByteArray{
        if(checkEntityExistence(request.entityId, request.entityType)){
            val filePath: String = "/${request.entityType}/${request.entityId}/file"
            val file = fileService.downloadFile("files", filePath)
            return file
        }
        else throw NullPointerException()
    }

    private fun checkEntityExistence(id: String, type: String): Boolean{
        when(type){
            "user" -> {
                val user = userService.retrieveById(id)
                return if(user!= null)
                    true else false
            }
            "group" -> {
                val group = groupService.retrieveChatById(id)
                return if(group!= null)
                    true else false
            }
            "message" -> {
                val message = messageService.retrieveById(id)
                return if (message!= null)
                    true else false
            }
        }
        return false
    }
}