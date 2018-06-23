package com.tagakov.userapi

data class User(val firstName: String, val lastName: String, val userpic: String)

class UserService {
    fun getUser(): User {
        return User("John", "Doe", "https://picsum.photos/300/?random")
    }
}