///
/// Тесты для класса MainViewModel
///
/// данные тесты тестируются в одном потоке

package com.example.rickandmorty.models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.rickandmorty.api.*
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
import org.mockito.kotlin.mock
import retrofit2.Response

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
    fun `characterRepository_loadRickAndMortyItems_successfulResponse`() = runBlockingTest {
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
    fun `characterRepository_loadRickAndMortyItems_networkFailure`() = runBlockingTest {
        `when`(characterRepository.getCharacter()).thenThrow(RuntimeException("Network Error"))

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals("Исключение: Network Error", viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }


    @Test
    fun `characterRepository_loadRickAndMortyItems_error400`() = runBlockingTest {
        val errorMessage = "Неверный запрос"
        val response = Response.error<CharacterResponse>(400, ResponseBody.create(null, errorMessage))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals(MainViewModel.HTTP400, viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_error401`() = runBlockingTest {
        val errorMessage = "Неавторизован"
        val response = Response.error<CharacterResponse>(401, ResponseBody.create(null, errorMessage))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals(MainViewModel.HTTP401, viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_error403`() = runBlockingTest {
        val errorMessage = "Доступ запрещен"
        val response = Response.error<CharacterResponse>(403, ResponseBody.create(null, errorMessage))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals(MainViewModel.HTTP403, viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_error404`() = runBlockingTest {
        val errorMessage = "404 Lorem Ipsum"
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
    fun `characterRepository_loadRickAndMortyItems_error500`() = runBlockingTest {
        val errorMessage = "Внутренняя ошибка сервера"
        val response = Response.error<CharacterResponse>(500, ResponseBody.create(null, errorMessage))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals(MainViewModel.HTTP500, viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_error503`() = runBlockingTest {
        val errorMessage = "Сервис недоступен"
        val response = Response.error<CharacterResponse>(503, ResponseBody.create(null, errorMessage))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals(MainViewModel.HTTP503, viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_unknownError`() = runBlockingTest {
        val response = Response.error<CharacterResponse>(999, ResponseBody.create(null, "Unknown error"))
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        val observer = Observer<String> {}
        viewModel.getErrorMessage.observeForever(observer)
        assertEquals("Ошибка 999: Неизвестная ошибка", viewModel.getErrorMessage.value)
        verify(characterRepository).getCharacter()
        viewModel.getErrorMessage.removeObserver(observer)
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_uiUpdateOnSuccess`() = runBlockingTest {
        val mockResponse = testCharacterResponse
        val response = Response.success(mockResponse)
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        // для проверки что изменилось значения
        val observer = mock<Observer<CharacterResponse>>()

        viewModel.getSelectionData.observeForever(observer)
        verify(observer).onChanged(mockResponse) // Проверяем, что observer был вызван с правильными данными
        assertEquals(mockResponse, viewModel.getSelectionData.value) // Проверяем значение LiveData
        viewModel.getSelectionData.removeObserver(observer)
    }
}

@ExperimentalCoroutinesApi
class MainViewModelTestAsync {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var characterRepository: ICharacterRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        characterRepository = mock(ICharacterRepository::class.java)
        viewModel = MainViewModel(characterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // сбросить главный диспетчер
    }

    @Test
    fun `characterRepository_loadRickAndMortyItems_viewModelDestruction`() = runBlockingTest {
        val mockResponse = testCharacterResponse
        val response = Response.success(mockResponse)
        `when`(characterRepository.getCharacter()).thenReturn(response)

        viewModel.loadRickAndMortyItems()

        advanceUntilIdle()

        viewModel.onCleared()

        // проверяем что не вызвался ни разу
        verify(characterRepository, never()).getCharacter()

        assertNull(viewModel.getSelectionData.value)
    }
}