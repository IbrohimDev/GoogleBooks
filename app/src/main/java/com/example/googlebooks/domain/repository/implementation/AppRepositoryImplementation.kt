package com.example.googlebooks.domain.repository.implementation

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.googlebooks.domain.model.ManageUiSealed
import androidx.paging.cachedIn
import com.example.googlebooks.data.local.dao.StarDao
import kotlinx.coroutines.flow.catch

import com.example.googlebooks.data.local.entity.StarEntity
import com.example.googlebooks.data.local.preferences.SharedData
import com.example.googlebooks.data.remote.api.BooksApi
import com.example.googlebooks.data.remote.dataSource.BooksDataSource
import com.example.googlebooks.data.remote.dataSource.BooksDataSourceOffline
import com.example.googlebooks.data.remote.response.Item
import com.example.googlebooks.domain.repository.AppRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AppRepositoryImplementation @Inject constructor(
    private val booksApi: BooksApi,
    private val config: PagingConfig,
    private val auth: FirebaseAuth,
    private val starDao: StarDao,
    private val sharedData: SharedData,
    private val context: Context
) : AppRepository {

    override fun searchedItems(searchText: String, scope: CoroutineScope): Flow<PagingData<Item>> =
        Pager(config) {
            Timber.tag("HHH").d("Repository in searchedItems")
            BooksDataSource(context, booksApi, searchText, starDao)
        }.flow.cachedIn(scope)

    override fun itemsFromLocalDb(scope: CoroutineScope): Flow<PagingData<StarEntity>> =
        Pager(config) {
            BooksDataSourceOffline(starDao)
        }.flow.cachedIn(scope)

    override fun insertStarToDb(starEntity: StarEntity) {
        starDao.insertStar(starEntity)
    }

    override fun deleteStarFromDb(bookId: String) {
        starDao.deleteStar(bookId)
    }

    override fun starCount(): Flow<List<StarEntity>> = starDao.countStar()


    override fun createUser(email: String, password: String): Flow<ManageUiSealed> = flow {
        emit(ManageUiSealed.Loading)
        var authInner: FirebaseUser? = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    authInner = auth.currentUser
                }
            }.await()
        authInner?.let {
            emit(ManageUiSealed.Success)
        } ?: emit(ManageUiSealed.Failure)

    }.catch {
        emit(ManageUiSealed.Failure)
    }.flowOn(Dispatchers.IO)

    override fun signInUser(email: String, password: String): Flow<ManageUiSealed> = flow {
        emit(ManageUiSealed.Loading)
        var authInner: FirebaseUser? = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authInner = auth.currentUser
                }
            }.await()
        authInner?.let {
            emit(ManageUiSealed.Success)
        } ?: emit(ManageUiSealed.Failure)
    }.catch {
        emit(ManageUiSealed.Failure)
    }.flowOn(Dispatchers.IO)

    override fun checkUserValid(): Flow<Boolean> = flow {
        auth.currentUser?.let {
            emit(true)
        } ?: emit(false)
    }.flowOn(Dispatchers.IO)

    override fun setFirstName(text: String) {
        sharedData.firtName = text
    }

    override fun setLastName(text: String) {
        sharedData.lastName = text
    }

    override fun setAge(text: String) {
        sharedData.age = text
    }

    override fun getFirstName() = sharedData.firtName

    override fun getLastName() = sharedData.lastName

    override fun getAge() = sharedData.age
    override fun signOut(): Flow<Unit> = flow {
        auth.signOut()
        emit(Unit)
    }.flowOn(Dispatchers.IO)
}