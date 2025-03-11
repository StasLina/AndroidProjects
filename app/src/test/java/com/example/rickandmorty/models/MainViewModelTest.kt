///
/// Тесты для класса MainViewModel
///
/// данные тесты тестируются в одном потоке

package com.example.rickandmorty.models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickandmorty.api.*
import com.example.rickandmorty.models.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

// Тестовые данные для Info
val testInfo = Info(
    count = 826,
    pages = 42,
    next = "https://rickandmortyapi.com/api/character?page=2",
    prev = null
)

// Тестовые данные для Origin
val testOrigin = Origin(
    name = "Earth",
    url = "https://rickandmortyapi.com/api/location/1"
)

// Тестовые данные для Location
val testLocation = Location(
    name = "Earth",
    url = "https://rickandmortyapi.com/api/location/1"
)

// Тестовые данные для Character
val testCharacter = Character(
    id = 1,
    name = "Rick Sanchez",
    status = "Alive",
    species = "Human",
    type = "",
    gender = "Male",
    origin = testOrigin,
    location = testLocation,
    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    episode = listOf("https://rickandmortyapi.com/api/episode/1"),
    url = "https://rickandmortyapi.com/api/character/1",
    created = "2017-11-04T18:48:46.250Z"
)

// Тестовые данные для CharacterResponse
val testCharacterResponse = CharacterResponse(
    info = testInfo,
    results = listOf(testCharacter) // Можно добавить больше персонажей в список
)

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var characterRepository: ICharacterRepository
    private val testDispatcher = Dispatchers.Unconfined//StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        characterRepository = mock(ICharacterRepository::class.java)
        viewModel = MainViewModel(characterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        Dispatchers.resetMain() // сбросить главный диспетчер
    }

    @Test
    fun `test check API response`() = runBlockingTest {
        val mockResponse = testCharacterResponse
        val response = Response.success(mockResponse)
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<CharacterResponse> {}
        viewModel.getSelectionData.observeForever(observer)
        assertNotNull(viewModel.getSelectionData.value)
        assertEquals(mockResponse, viewModel.getSelectionData.value)
        verify(characterRepository).getCharacter()
        viewModel.getSelectionData.removeObserver(observer)
    }

    @Test
    fun `test error handling on network failure`() = runBlockingTest {
        `when`(characterRepository.getCharacter()).thenThrow(RuntimeException("Network Error"))

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals("Исключение: Network Error", viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `test error handling on unsuccessful response`() = runBlockingTest {
        val errorMessage = "Lorem Ipsum"
        val response = Response.error<CharacterResponse>(404, ResponseBody.create(null, errorMessage))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals(MainViewModel.HTTP404, viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `test UI update on successful data retrieval`() = runBlockingTest {
        val mockResponse = testCharacterResponse
        val response = Response.success(mockResponse)
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<CharacterResponse> {}
        viewModel.getSelectionData.observeForever(observer)
        assertEquals(mockResponse, viewModel.getSelectionData.value)
        viewModel.getSelectionData.removeObserver(observer)
    }

    @Test
    fun `test coroutine cancellation on ViewModel destruction`() = runBlockingTest {
        val mockResponse = testCharacterResponse
        val response = Response.success(mockResponse)
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        viewModel.onCleared()
        verify(characterRepository).getCharacter()
    }
}