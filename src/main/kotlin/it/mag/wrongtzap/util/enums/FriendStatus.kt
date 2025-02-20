package it.mag.wrongtzap.util.enums

enum class FriendStatus(status: String){
    PENDING(status = "PENDING"),
    ACCEPTED(status = "ACCEPTED"),
    SENT(status = "SENT"),
    RECEIVED(status = "RECEIVED")
}