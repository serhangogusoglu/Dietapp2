package com.example.diet_app.data.repository

import com.example.diet_app.data.database.dao.UserDao
import com.example.diet_app.data.database.entity.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(email: String, passwordHash: String): Long {
        val user = UserEntity(email = email, passwordHash = passwordHash)
        return userDao.insertUser(user)
    }

    suspend fun findUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun authenticateUser(email: String, passwordAttempt: String): UserEntity? {
        val user = findUserByEmail(email)
        return if (user != null && user.passwordHash == passwordAttempt) {
            user
        } else {
            null
        }
    }
}