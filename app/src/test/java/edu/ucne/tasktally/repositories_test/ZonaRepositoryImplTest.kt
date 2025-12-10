package edu.ucne.tasktally.repositories_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.data.repositories.ZonaRepositoryImpl
import edu.ucne.tasktally.domain.models.Zona
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import retrofit2.Response
import kotlin.test.Test
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import java.io.IOException
import android.util.Log
import org.junit.After

@ExperimentalCoroutinesApi
class ZonaRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: ZonaRepositoryImpl
    private lateinit var dao: ZonaDao
    private lateinit var api: TaskTallyApi

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0

        dao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        repository = ZonaRepositoryImpl(dao, api)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    // region Basic CRUD Tests
    @Test
    fun `getZona retorna zona cuando existe`() = runTest {
        // Given
        val zonaEntity = ZonaEntity(
            zonaId = 1,
            nombre = "Zona Test",
            joinCode = "ABC123",
            mentorId = "1"
        )

        coEvery { dao.getById(1) } returns zonaEntity

        // When
        val result = repository.getZona(1)

        // Then
        assertNotNull(result)
        assertEquals("Zona Test", result?.nombre)
        assertEquals("ABC123", result?.joinCode)
    }

    @Test
    fun `getZona retorna null cuando no existe`() = runTest {
        // Given
        coEvery { dao.getById(999) } returns null

        // When
        val result = repository.getZona(999)

        // Then
        assertNull(result)
    }

    @Test
    fun `getZona retorna null cuando id es null`() = runTest {
        // Given
        coEvery { dao.getById(null) } returns null

        // When
        val result = repository.getZona(null)

        // Then
        assertNull(result)
    }

    @Test
    fun `getZonaByMentor retorna zona cuando existe`() = runTest {
        // Given
        val zonaEntity = ZonaEntity(
            zonaId = 1,
            nombre = "Zona Mentor",
            joinCode = "MENTOR123",
            mentorId = "5"
        )

        coEvery { dao.getByMentorId("5") } returns zonaEntity

        // When
        val result = repository.getZonaByMentor("5")

        // Then
        assertNotNull(result)
        assertEquals("5", result?.mentorId)
    }

    @Test
    fun `getZonaByMentor retorna null cuando no existe`() = runTest {
        // Given
        coEvery { dao.getByMentorId("999") } returns null

        // When
        val result = repository.getZonaByMentor("999")

        // Then
        assertNull(result)
    }

    @Test
    fun `getZonaByJoinCode retorna zona cuando existe`() = runTest {
        // Given
        val zonaEntity = ZonaEntity(
            zonaId = 1,
            nombre = "Zona Test",
            joinCode = "UNIQUE123",
            mentorId = "1"
        )

        coEvery { dao.getByJoinCode("UNIQUE123") } returns zonaEntity

        // When
        val result = repository.getZonaByJoinCode("UNIQUE123")

        // Then
        assertNotNull(result)
        assertEquals("UNIQUE123", result?.joinCode)
    }

    @Test
    fun `getZonaByJoinCode retorna null cuando no existe`() = runTest {
        // Given
        coEvery { dao.getByJoinCode("INVALID") } returns null

        // When
        val result = repository.getZonaByJoinCode("INVALID")

        // Then
        assertNull(result)
    }

    @Test
    fun `upsert guarda zona y retorna id`() = runTest {
        // Given
        val zona = Zona(
            zonaId = 1,
            nombre = "Nueva Zona",
            joinCode = "NEW123",
            mentorId = "1",
            gemas = emptyList()
        )

        coEvery { dao.upsert(any()) } just Runs

        // When
        val result = repository.upsert(zona)

        // Then
        assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete elimina zona correctamente`() = runTest {
        // Given
        val zona = Zona(
            zonaId = 1,
            nombre = "Zona a eliminar",
            joinCode = "DEL123",
            mentorId = "1",
            gemas = emptyList()
        )

        coEvery { dao.delete(any()) } just Runs

        // When
        repository.delete(zona)

        // Then
        coVerify { dao.delete(any()) }
    }

    @Test
    fun `deleteById elimina zona por id correctamente`() = runTest {
        // Given
        coEvery { dao.deleteById(1) } just Runs

        // When
        repository.deleteById(1)

        // Then
        coVerify { dao.deleteById(1) }
    }

    @Test
    fun `updateZoneCode actualiza codigo correctamente`() = runTest {
        // Given
        coEvery { dao.updateJoinCode(1, "NEWCODE") } just Runs

        // When
        repository.updateZoneCode(1, "NEWCODE")

        // Then
        coVerify { dao.updateJoinCode(1, "NEWCODE") }
    }

    @Test
    fun `updateZoneName actualiza nombre correctamente`() = runTest {
        // Given
        coEvery { dao.updateZoneName(1, "Nuevo Nombre") } just Runs

        // When
        repository.updateZoneName(1, "Nuevo Nombre")

        // Then
        coVerify { dao.updateZoneName(1, "Nuevo Nombre") }
    }

    @Test
    fun `observeZonas retorna flow de zonas mapeadas`() = runTest {
        // Given
        val zonasEntities = listOf(
            ZonaEntity(
                zonaId = 1,
                nombre = "Zona 1",
                joinCode = "CODE1",
                mentorId = "1"
            ),
            ZonaEntity(
                zonaId = 2,
                nombre = "Zona 2",
                joinCode = "CODE2",
                mentorId = "2"
            )
        )

        coEvery { dao.observeAll() } returns flowOf(zonasEntities)

        // When
        val result = repository.observeZonas().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Zona 1", result[0].nombre)
        assertEquals("Zona 2", result[1].nombre)
    }
    // endregion

    // region GetZoneInfoMentor Tests
    @Test
    fun `getZoneInfoMentor retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1
        val zoneInfoResponse = ZoneInfoMentorResponse(
            zoneId = zoneId,
            zoneName = "Zona Mentor",
            joinCode = "MENTOR123",
            gemas = emptyList()
        )

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } returns Response.success(zoneInfoResponse)

        // When
        val result = repository.getZoneInfoMentor(mentorId, zoneId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Zona Mentor", (result as Resource.Success).data?.zoneName)
        assertEquals("MENTOR123", result.data?.joinCode)
    }

    @Test
    fun `getZoneInfoMentor retorna Error cuando body es null`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } returns Response.success(null)

        // When
        val result = repository.getZoneInfoMentor(mentorId, zoneId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Respuesta vacía del servidor", (result as Resource.Error).message)
    }

    @Test
    fun `getZoneInfoMentor retorna Error cuando API falla`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getZoneInfoMentor(mentorId, zoneId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("HTTP 404"))
    }

    @Test
    fun `getZoneInfoMentor retorna Error cuando hay excepcion`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } throws IOException("Network error")

        // When
        val result = repository.getZoneInfoMentor(mentorId, zoneId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Error de red"))
    }
    // endregion

    // region GetZoneInfoGema Tests
    @Test
    fun `getZoneInfoGema retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1
        val zoneInfoResponse = ZoneInfoGemaResponse(
            zoneId = zoneId,
            zoneName = "Zona Gema",
            gemas = emptyList()
        )

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } returns Response.success(zoneInfoResponse)

        // When
        val result = repository.getZoneInfoGema(gemaId, zoneId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("Zona Gema", (result as Resource.Success).data?.zoneName)
    }

    @Test
    fun `getZoneInfoGema retorna Error cuando body es null`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } returns Response.success(null)

        // When
        val result = repository.getZoneInfoGema(gemaId, zoneId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Respuesta vacía del servidor", (result as Resource.Error).message)
    }

    @Test
    fun `getZoneInfoGema retorna Error cuando API falla`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getZoneInfoGema(gemaId, zoneId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("HTTP 500"))
    }

    @Test
    fun `getZoneInfoGema retorna Error cuando hay excepcion`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } throws IOException("Network error")

        // When
        val result = repository.getZoneInfoGema(gemaId, zoneId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Error de red"))
    }
    // endregion
}