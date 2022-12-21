package com.example.googlebooks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.googlebooks.data.local.entity.StarEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StarDao {
    @Insert
    fun insertStar(start: StarEntity)

    @Query("DELETE FROM StarEntity WHERE StarEntity.bookId = :bookId")
    fun deleteStar(bookId: String)

    @Query("SELECT *FROM StarEntity WHERE StarEntity.bookId = :bookId")
    fun isStar(bookId: String): Flow<List<StarEntity>>

    @Query("SELECT *FROM StarEntity")
    fun countStar(): Flow<List<StarEntity>>

    @Query("SELECT *FROM StarEntity ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun allStar(
        limit: Int,
        offset: Int
    ): List<StarEntity>
}