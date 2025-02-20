package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.chat.response.*
import it.mag.wrongtzap.controller.web.message.MessageResponse
import it.mag.wrongtzap.controller.web.page.response.PagedMessageResponse
import it.mag.wrongtzap.controller.web.user.response.FriendResponse
import it.mag.wrongtzap.controller.web.user.response.ProfileResponse
import it.mag.wrongtzap.controller.web.user.response.UserResponse
import it.mag.wrongtzap.model.*
import it.mag.wrongtzap.util.enums.FriendStatus
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class MapperService {

    fun groupToResponse(chat: GroupChat): GroupChatDTO {
        val response = GroupChatDTO(
            name = chat.name,
            chatId = chat.chatId,
            creationDate = chat.creationDate,
            messages = chat.messages.map { message -> messageToResponse(message) }.toMutableList(),
            users = chat.members.map { user -> userToProfile(user) }.toMutableSet(),
            joinDate = chat.userJoinEntry.map {
                date -> EntryDateDTO(
                    userId = date.key,
                    timestamp = date.value
                )
            }.toMutableSet(),
            archived = chat.archived
        )
        return response
    }


    fun chatToResponse(chat: Chat): ChatDTO {
        val response = ChatDTO(
            chatId = chat.chatId,
            creationDate = chat.creationDate,
            messages = chat.messages.map { message -> messageToResponse(message) }.toMutableList(),
            members = chat.members.map { user -> userToProfile(user) },
            archived = chat.archived
        )
        return response
    }

    fun userToResponse(user: User): UserResponse {

        val response = UserResponse(
            userId = user.userId,
            username = user.username,
            directChats = user.chats.map { chat -> chatToResponse(chat) }.toMutableSet(),
            groupChats = user.groups.map { chat -> groupToResponse(chat) }.toMutableSet(),
            friends = userToFriendResponse(user)
        )
        return response
    }

    fun userToProfile(user: User): ProfileResponse {
        val response = ProfileResponse(
            userId =  user.userId,
            username = user.username,
        )
        return response
    }

    fun userToFriendResponse(user: User): MutableSet<FriendResponse>{
        val friends: MutableSet<FriendResponse> = mutableSetOf()
        friends.addAll(user.inboundFriendships.map(this::receivedFriendToResponse))
        friends.addAll(user.outboundFriendships.map(this::sentFriendToResponse))

        return friends
    }

    fun sentFriendToResponse(friend: Friend): FriendResponse {
        return FriendResponse(
            friendshipId = friend.requestId,
            userId = friend.receiver.userId,
            username = friend.receiver.username,
            status = if(friend.status != FriendStatus.ACCEPTED) FriendStatus.SENT.toString() else friend.status.toString()
        )
    }

    fun receivedFriendToResponse(friend: Friend): FriendResponse {
        return FriendResponse(
            friendshipId = friend.requestId,
            userId = friend.sender.userId,
            username = friend.sender.username,
            status = if(friend.status != FriendStatus.ACCEPTED) FriendStatus.RECEIVED.toString() else friend.status.toString()
        )
    }
    //---END OF----//

    fun messageToResponse(message: Message): MessageResponse {
        val response = MessageResponse(
            content = message.content,
            timestamp = message.timestamp,
            chatId = message.associatedChat.chatId,
            username = message.sender.username,
            userId = message.sender.userId,
        )
        return response
    }

    fun groupToPagedGroup(group: GroupChat, page: Page<Message>): GroupAltDTO {
        return GroupAltDTO(
            messages = PagedMessageResponse(
                content = page.content.map(this::messageToResponse),
                pageSize = page.size,
                pageNumber = page.number,
                totalPages = page.totalPages,
                totalRecords = page.totalElements
            ),
            chatId = group.chatId,
            name = group.name ,
            creationDate = group.creationDate,
            users = group.members.map(this::userToProfile).toMutableSet(),
            joinDate = group.userJoinEntry.map {
                    date -> EntryDateDTO(
                userId = date.key,
                timestamp = date.value
            )
            }.toMutableSet(),
            archived = group.archived
        )
    }

    fun chatToPagedChat(chat: Chat, page: Page<Message>): ChatAltDTO {
        return ChatAltDTO(
            messages = PagedMessageResponse(
                content = page.content.map(this::messageToResponse),
                pageSize = page.size,
                pageNumber = page.number,
                totalPages = page.totalPages,
                totalRecords = page.totalElements
            ),
            chatId = chat.chatId,
            creationDate = chat.creationDate,
            members = chat.members.map(this::userToProfile),
            archived = chat.archived
        )
    }

    

}