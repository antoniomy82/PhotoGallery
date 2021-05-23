package com.antoniomy82.photogallery

import com.antoniomy82.photogallery.model.database.LocalDbRepository
import com.antoniomy82.photogallery.model.network.NetworkRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RepositoryTest {

    private var networkMockRepository: NetworkRepository? = null
    private var localMockRepository : LocalDbRepository?=null


    @BeforeEach
    fun setUp() {
        networkMockRepository = NetworkRepository()
        localMockRepository = LocalDbRepository()
        println("@BeforeEach -> setUp()")
    }


    @Test
    fun getAllPhotosTest() {
        assertEquals(5000, networkMockRepository?.getAllPhoto())
        println("@Test -> getPopularMoviesTest()")
    }

    @Test
    fun insertPhotoTest() {
        assertEquals(true, localMockRepository?.insertPhoto())
        println("@Test -> insertMovieTest()")
    }

    @Test
    fun deletePhotoTest() {
        assertEquals(true, localMockRepository?.deletePhoto())
        println("@Test -> deleteMovieTest()")
    }

    @Test
    fun getAllPhotosDBTest() {
        assertEquals(5000, localMockRepository?.getAllPhotos())
        println("@Test -> getPopularMoviesTest()")
    }

}