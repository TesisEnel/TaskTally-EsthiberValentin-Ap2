package edu.ucne.tasktally.repositories_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.DAOs.gema.GemaDao
import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import edu.ucne.tasktally.data.local.entidades.gemas.RecompensaGemaEntity
import edu.ucne.tasktally.data.local.entidades.gemas.TareaGemaEntity
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.CanjearRecompensaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.RecompensasGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.BulkUpdateTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.TareaUpdateResult
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.TareasGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.data.repositories.GemaRepositoryImpl
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
@ExperimentalCoroutinesApi
class GemaRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: GemaRepositoryImpl
    private lateinit var gemaDao: GemaDao
    private lateinit var zonaDao: ZonaDao
    private lateinit var api: TaskTallyApi
    private lateinit var authPreferencesManager: AuthPreferencesManager

    @Before
    fun setup() {
        gemaDao = mockk(relaxed = true)
        zonaDao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        authPreferencesManager = mockk(relaxed = true)
        repository = GemaRepositoryImpl(gemaDao, zonaDao, api, authPreferencesManager)
    }

    // region GetTareasRemote Tests
    @Test
    fun `getTareasRemote retorna Success y guarda en local cuando API responde correctamente`() = runTest {
        // Given
        val gemaId = 1
        val tareasResponse = listOf(
            TareasGemaResponse(
                tareaId = 1,
                gemaId = 1,
                titulo = "Tarea 1",
                descripcion = "Descripcion 1",
                puntos = 10,
                estado = "pendiente",
                dia = "Lunes",
                nombreImgVector = "icon1",
                gemaCompletoNombre = null
            ),
            TareasGemaResponse(
                tareaId = 2,
                gemaId = 1,
                titulo = "Tarea 2",
                descripcion = "Descripcion 2",
                puntos = 20,
                estado = "pendiente",
                dia = "Martes",
                nombreImgVector = "icon2",
                gemaCompletoNombre = null
            )
        )

        coEvery { api.getTareasGema(gemaId) } returns Response.success(tareasResponse)
        coEvery { gemaDao.eliminarTodasLasTareas() } just Runs
        coEvery { gemaDao.upsertTarea(any()) } just Runs

        // When
        val result = repository.getTareasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data?.size)
        coVerify { gemaDao.eliminarTodasLasTareas() }
        coVerify(exactly = 2) { gemaDao.upsertTarea(any()) }
    }

    @Test
    fun `getTareasRemote retorna Error cuando API falla`() = runTest {
        // Given
        val gemaId = 1
        coEvery { api.getTareasGema(gemaId) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getTareasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Error)
        coVerify(exactly = 0) { gemaDao.eliminarTodasLasTareas() }
    }

    @Test
    fun `getTareasRemote retorna Error cuando hay excepcion`() = runTest {
        // Given
        val gemaId = 1
        coEvery { api.getTareasGema(gemaId) } throws IOException("Network error")

        // When
        val result = repository.getTareasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Exception occurred"))
    }

    @Test
    fun `getTareasRemote retorna Error cuando body es null`() = runTest {
        // Given
        val gemaId = 1
        coEvery { api.getTareasGema(gemaId) } returns Response.success(null)

        // When
        val result = repository.getTareasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Response body is null", (result as Resource.Error).message)
    }
    // endregion

    // region Tareas Gema Tests
    @Test
    fun `iniciarTareaGema llama al dao correctamente`() = runTest {
        // Given
        val gemaId = 1
        val tareaId = "tarea-123"
        coEvery { gemaDao.iniciarTarea(tareaId) } just Runs

        // When
        repository.iniciarTareaGema(gemaId, tareaId)

        // Then
        coVerify { gemaDao.iniciarTarea(tareaId) }
    }

    @Test
    fun `completarTareaGema actualiza puntos correctamente`() = runTest {
        // Given
        val gemaId = 1
        val tareaId = "tarea-123"
        val tareaEntity = TareaGemaEntity(
            tareaId = tareaId,
            remoteId = 1,
            titulo = "Tarea test",
            descripcion = "Descripcion",
            puntos = 50,
            estado = "en_progreso",
            dia = "Lunes",
            nombreImgVector = "icon",
            gemaCompletoNombre = null
        )

        coEvery { gemaDao.obtenerTareaPorId(tareaId) } returns tareaEntity
        coEvery { gemaDao.completarTarea(tareaId) } just Runs
        coEvery { authPreferencesManager.puntosDisponibles } returns flowOf(100)
        coEvery { authPreferencesManager.puntosGastados } returns flowOf(20)
        coEvery { authPreferencesManager.updatePuntos(any(), any()) } just Runs

        // When
        repository.completarTareaGema(gemaId, tareaId)

        // Then
        coVerify { gemaDao.completarTarea(tareaId) }
        coVerify { authPreferencesManager.updatePuntos(150, 20) } // 100 + 50 puntos de la tarea
    }

    @Test
    fun `completarTareaGema no hace nada si tarea no existe`() = runTest {
        // Given
        val gemaId = 1
        val tareaId = "tarea-inexistente"

        coEvery { gemaDao.obtenerTareaPorId(tareaId) } returns null

        // When
        repository.completarTareaGema(gemaId, tareaId)

        // Then
        coVerify(exactly = 0) { gemaDao.completarTarea(any()) }
        coVerify(exactly = 0) { authPreferencesManager.updatePuntos(any(), any()) }
    }

    @Test
    fun `completarTareaGema maneja puntos null correctamente`() = runTest {
        // Given
        val gemaId = 1
        val tareaId = "tarea-123"
        val tareaEntity = TareaGemaEntity(
            tareaId = tareaId,
            remoteId = 1,
            titulo = "Tarea test",
            descripcion = "Descripcion",
            puntos = 30,
            estado = "en_progreso",
            dia = "Martes",
            nombreImgVector = "icon"
        )

        coEvery { gemaDao.obtenerTareaPorId(tareaId) } returns tareaEntity
        coEvery { gemaDao.completarTarea(tareaId) } just Runs
        coEvery { authPreferencesManager.puntosDisponibles } returns flowOf(null)
        coEvery { authPreferencesManager.puntosGastados } returns flowOf(null)
        coEvery { authPreferencesManager.updatePuntos(any(), any()) } just Runs

        // When
        repository.completarTareaGema(gemaId, tareaId)

        // Then
        coVerify { gemaDao.completarTarea(tareaId) }
        coVerify { authPreferencesManager.updatePuntos(30, 0) } // 0 + 30 puntos
    }
    // endregion

    // region Recompensas Tests
    @Test
    fun `getRecompensasRemote retorna Success y guarda en local`() = runTest {
        // Given
        val gemaId = 1
        val recompensasResponse = listOf(
            RecompensasGemaResponse(
                recompensaId = 1,
                titulo = "Recompensa 1",
                descripcion = "Descripcion 1",
                precio = 100,
                canjeada = false,
                nombreImgVector = "icon1",
                transaccionId = null
            )
        )

        coEvery { api.getRecompensasGema(gemaId) } returns Response.success(recompensasResponse)
        coEvery { gemaDao.eliminarTodasLasRecompensas() } just Runs
        coEvery { gemaDao.upsertRecompensa(any()) } just Runs

        // When
        val result = repository.getRecompensasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data?.size)
        coVerify { gemaDao.eliminarTodasLasRecompensas() }
    }

    @Test
    fun `getRecompensasRemote retorna Error cuando API falla`() = runTest {
        // Given
        val gemaId = 1
        coEvery { api.getRecompensasGema(gemaId) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getRecompensasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Error)
        coVerify(exactly = 0) { gemaDao.eliminarTodasLasRecompensas() }
    }

    @Test
    fun `getRecompensasRemote retorna Error cuando body es null`() = runTest {
        // Given
        val gemaId = 1
        coEvery { api.getRecompensasGema(gemaId) } returns Response.success(null)

        // When
        val result = repository.getRecompensasRemote(gemaId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Response body is null", (result as Resource.Error).message)
    }

    @Test
    fun `canjearRecompensa actualiza recompensa y puntos correctamente`() = runTest {
        // Given
        val recompensaId = "recompensa-123"
        val gemaId = 1
        val recompensaEntity = RecompensaGemaEntity(
            recompensaId = recompensaId,
            remoteId = 1,
            titulo = "Recompensa test",
            descripcion = "Descripcion",
            precio = 50,
            canjeada = false,
            nombreImgVector = "icon",
            transaccionId = null,
            isPendingUpdate = false
        )

        val recompensaSlot = slot<RecompensaGemaEntity>()

        coEvery { gemaDao.obtenerRecompensaPorId(recompensaId) } returns recompensaEntity
        coEvery { gemaDao.upsertRecompensa(capture(recompensaSlot)) } just Runs
        coEvery { authPreferencesManager.puntosDisponibles } returns flowOf(100)
        coEvery { authPreferencesManager.puntosGastados } returns flowOf(20)
        coEvery { authPreferencesManager.updatePuntos(any(), any()) } just Runs

        // When
        repository.canjearRecompensa(recompensaId, gemaId)

        // Then
        assertTrue(recompensaSlot.captured.canjeada)
        assertTrue(recompensaSlot.captured.isPendingUpdate)
        coVerify { authPreferencesManager.updatePuntos(50, 70) } // 100-50, 20+50
    }

    @Test
    fun `canjearRecompensa no hace nada si recompensa no existe`() = runTest {
        // Given
        val recompensaId = "recompensa-inexistente"
        val gemaId = 1

        coEvery { gemaDao.obtenerRecompensaPorId(recompensaId) } returns null

        // When
        repository.canjearRecompensa(recompensaId, gemaId)

        // Then
        coVerify(exactly = 0) { gemaDao.upsertRecompensa(any()) }
        coVerify(exactly = 0) { authPreferencesManager.updatePuntos(any(), any()) }
    }

    @Test
    fun `canjearRecompensa maneja puntos null correctamente`() = runTest {
        // Given
        val recompensaId = "recompensa-123"
        val gemaId = 1
        val recompensaEntity = RecompensaGemaEntity(
            recompensaId = recompensaId,
            remoteId = 1,
            titulo = "Recompensa test",
            descripcion = "Descripcion",
            precio = 30,
            canjeada = false,
            nombreImgVector = "icon",
            transaccionId = null
        )

        coEvery { gemaDao.obtenerRecompensaPorId(recompensaId) } returns recompensaEntity
        coEvery { gemaDao.upsertRecompensa(any()) } just Runs
        coEvery { authPreferencesManager.puntosDisponibles } returns flowOf(null)
        coEvery { authPreferencesManager.puntosGastados } returns flowOf(null)
        coEvery { authPreferencesManager.updatePuntos(any(), any()) } just Runs

        // When
        repository.canjearRecompensa(recompensaId, gemaId)

        // Then
        coVerify { authPreferencesManager.updatePuntos(-30, 30) } // 0-30, 0+30
    }
    // endregion

    // region Zone Info Tests
    @Test
    fun `getZoneInfo retorna datos de API cuando responde correctamente`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1
        val zoneInfoResponse = ZoneInfoGemaResponse(
            zoneId = zoneId,
            zoneName = "Zona Test",
            gemas = emptyList()
        )

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } returns Response.success(zoneInfoResponse)
        coEvery { zonaDao.upsert(any()) } just Runs

        // When
        val result = repository.getZoneInfo(gemaId, zoneId)

        // Then
        assertEquals("Zona Test", result.nombre)
        coVerify { zonaDao.upsert(any()) }
    }

    @Test
    fun `getZoneInfo retorna datos locales cuando API falla`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1
        val localZona = ZonaEntity(
            zonaId = zoneId,
            nombre = "Zona Local",
            joinCode = "LOCAL123",
            mentorId = "1"
        )

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } throws IOException("Network error")
        coEvery { zonaDao.getById(zoneId) } returns localZona

        // When
        val result = repository.getZoneInfo(gemaId, zoneId)

        // Then
        assertEquals("Zona Local", result.nombre)
        assertEquals("LOCAL123", result.joinCode)
    }

    @Test
    fun `getZoneInfo retorna zona vacia cuando API falla y no hay datos locales`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } throws IOException("Network error")
        coEvery { zonaDao.getById(zoneId) } returns null

        // When
        val result = repository.getZoneInfo(gemaId, zoneId)

        // Then
        assertEquals(0, result.zonaId)
        assertEquals("", result.nombre)
        assertEquals("", result.joinCode)
    }

    @Test
    fun `getZoneInfo retorna datos locales cuando API response body es null`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1
        val localZona = ZonaEntity(
            zonaId = zoneId,
            nombre = "Zona Local",
            joinCode = "LOCAL123",
            mentorId = "1"
        )

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } returns Response.success(null)
        coEvery { zonaDao.getById(zoneId) } returns localZona

        // When
        val result = repository.getZoneInfo(gemaId, zoneId)

        // Then
        assertEquals("Zona Local", result.nombre)
    }

    @Test
    fun `getZoneInfo retorna datos locales cuando API retorna error`() = runTest {
        // Given
        val gemaId = 1
        val zoneId = 1
        val localZona = ZonaEntity(
            zonaId = zoneId,
            nombre = "Zona Local",
            joinCode = "LOCAL123",
            mentorId = "1"
        )

        coEvery { api.obtenerGemaInfoZona(gemaId, zoneId) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )
        coEvery { zonaDao.getById(zoneId) } returns localZona

        // When
        val result = repository.getZoneInfo(gemaId, zoneId)

        // Then
        assertEquals("Zona Local", result.nombre)
    }
    // endregion

    // region PostPendingEstadosTareas Tests
    @Test
    fun `postPendingEstadosTareas retorna Success cuando no hay tareas pendientes`() = runTest {
        // Given
        val gemaId = 1
        coEvery { gemaDao.getPendingUpdateTareas() } returns emptyList()

        // When
        val result = repository.postPendingEstadosTareas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(0, (result as Resource.Success).data?.successful)
        assertEquals(0, result.data?.failed)
    }

    @Test
    fun `postPendingEstadosTareas sincroniza tareas pendientes correctamente`() = runTest {
        // Given
        val gemaId = 1
        val pendingTareas = listOf(
            TareaGemaEntity(
                tareaId = "tarea-1",
                remoteId = 1,
                titulo = "Tarea 1",
                descripcion = "Desc",
                puntos = 10,
                estado = "completada",
                dia = "Lunes",
                nombreImgVector = "icon",
                isPendingUpdate = true
            )
        )
        val bulkResponse = BulkUpdateTareasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                TareaUpdateResult(
                    tareaId = 1,
                    success = true,
                    mensaje = "Actualizada",
                    nuevoEstado = "completada",
                    puntosGanados = 10,
                    puntosActuales = 110
                )
            )
        )

        coEvery { gemaDao.getPendingUpdateTareas() } returns pendingTareas
        coEvery { api.bulkUpdateEstadoTareas(gemaId, any()) } returns Response.success(bulkResponse)
        coEvery { gemaDao.upsertTarea(any()) } just Runs
        coEvery { authPreferencesManager.puntosGastados } returns flowOf(20)
        coEvery { authPreferencesManager.updatePuntos(any(), any()) } just Runs

        // When
        val result = repository.postPendingEstadosTareas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            gemaDao.upsertTarea(match { !it.isPendingUpdate })
        }
        coVerify { authPreferencesManager.updatePuntos(110, 20) }
    }

    @Test
    fun `postPendingEstadosTareas ignora tareas sin remoteId`() = runTest {
        // Given
        val gemaId = 1
        val pendingTareas = listOf(
            TareaGemaEntity(
                tareaId = "tarea-1",
                remoteId = null, // Sin remoteId
                titulo = "Tarea 1",
                descripcion = "Desc",
                puntos = 10,
                estado = "completada",
                dia = "Lunes",
                nombreImgVector = "icon",
                isPendingUpdate = true
            )
        )

        coEvery { gemaDao.getPendingUpdateTareas() } returns pendingTareas

        // When
        val result = repository.postPendingEstadosTareas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(0, (result as Resource.Success).data?.successful)
        coVerify(exactly = 0) { api.bulkUpdateEstadoTareas(any(), any()) }
    }

    @Test
    fun `postPendingEstadosTareas retorna Error cuando API falla`() = runTest {
        // Given
        val gemaId = 1
        val pendingTareas = listOf(
            TareaGemaEntity(
                tareaId = "tarea-1",
                remoteId = 1,
                titulo = "Tarea 1",
                descripcion = "Desc",
                puntos = 10,
                estado = "completada",
                dia = "Lunes",
                nombreImgVector = "icon",
                isPendingUpdate = true
            )
        )

        coEvery { gemaDao.getPendingUpdateTareas() } returns pendingTareas
        coEvery { api.bulkUpdateEstadoTareas(gemaId, any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.postPendingEstadosTareas(gemaId)

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `postPendingEstadosTareas no actualiza puntos cuando puntosActuales es null`() = runTest {
        // Given
        val gemaId = 1
        val pendingTareas = listOf(
            TareaGemaEntity(
                tareaId = "tarea-1",
                remoteId = 1,
                titulo = "Tarea 1",
                descripcion = "Desc",
                puntos = 10,
                estado = "completada",
                dia = "Lunes",
                nombreImgVector = "icon",
                isPendingUpdate = true
            )
        )
        val bulkResponse = BulkUpdateTareasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                TareaUpdateResult(
                    tareaId = 1,
                    success = true,
                    mensaje = "Actualizada",
                    nuevoEstado = "completada",
                    puntosGanados = null,
                    puntosActuales = null
                )
            )
        )

        coEvery { gemaDao.getPendingUpdateTareas() } returns pendingTareas
        coEvery { api.bulkUpdateEstadoTareas(gemaId, any()) } returns Response.success(bulkResponse)
        coEvery { gemaDao.upsertTarea(any()) } just Runs

        // When
        val result = repository.postPendingEstadosTareas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { authPreferencesManager.updatePuntos(any(), any()) }
    }
    // endregion

    // region PostPendingCanjearRecompensas Tests
    @Test
    fun `postPendingCanjearRecompensas retorna Success cuando no hay recompensas pendientes`() = runTest {
        // Given
        val gemaId = 1
        coEvery { gemaDao.getPendingUpdateRecompensas() } returns emptyList()

        // When
        val result = repository.postPendingCanjearRecompensas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `postPendingCanjearRecompensas sincroniza recompensas correctamente`() = runTest {
        // Given
        val gemaId = 1
        val pendingRecompensas = listOf(
            RecompensaGemaEntity(
                recompensaId = "rec-1",
                remoteId = 1,
                titulo = "Recompensa 1",
                descripcion = "Desc",
                precio = 50,
                canjeada = true,
                nombreImgVector = "icon",
                transaccionId = null,
                isPendingUpdate = true
            )
        )
        val canjearResponse = CanjearRecompensaResponse(
            success = true,
            message = "Canjeada exitosamente",
            transaccionId = 1,
            puntosRestantes = 50
        )

        coEvery { gemaDao.getPendingUpdateRecompensas() } returns pendingRecompensas
        coEvery { api.canjearRecompensa(any()) } returns Response.success(canjearResponse)
        coEvery { gemaDao.upsertRecompensa(any()) } just Runs
        coEvery { authPreferencesManager.puntosGastados } returns flowOf(70)
        coEvery { authPreferencesManager.updatePuntos(any(), any()) } just Runs

        // When
        val result = repository.postPendingCanjearRecompensas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            gemaDao.upsertRecompensa(match { !it.isPendingUpdate })
        }
        coVerify { authPreferencesManager.updatePuntos(50, 70) }
    }

    @Test
    fun `postPendingCanjearRecompensas ignora recompensas sin remoteId`() = runTest {
        // Given
        val gemaId = 1
        val pendingRecompensas = listOf(
            RecompensaGemaEntity(
                recompensaId = "rec-1",
                remoteId = null, // Sin remoteId
                titulo = "Recompensa 1",
                descripcion = "Desc",
                precio = 50,
                canjeada = true,
                nombreImgVector = "icon",
                transaccionId = null,
                isPendingUpdate = true
            )
        )

        coEvery { gemaDao.getPendingUpdateRecompensas() } returns pendingRecompensas

        // When
        val result = repository.postPendingCanjearRecompensas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { api.canjearRecompensa(any()) }
    }

    @Test
    fun `postPendingCanjearRecompensas retorna Error cuando API falla`() = runTest {
        // Given
        val gemaId = 1
        val pendingRecompensas = listOf(
            RecompensaGemaEntity(
                recompensaId = "rec-1",
                remoteId = 1,
                titulo = "Recompensa 1",
                descripcion = "Desc",
                precio = 50,
                canjeada = true,
                nombreImgVector = "icon",
                transaccionId = null,
                isPendingUpdate = true
            )
        )

        coEvery { gemaDao.getPendingUpdateRecompensas() } returns pendingRecompensas
        coEvery { api.canjearRecompensa(any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.postPendingCanjearRecompensas(gemaId)

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `postPendingCanjearRecompensas no actualiza puntos cuando puntosRestantes es null`() = runTest {
        // Given
        val gemaId = 1
        val pendingRecompensas = listOf(
            RecompensaGemaEntity(
                recompensaId = "rec-1",
                remoteId = 1,
                titulo = "Recompensa 1",
                descripcion = "Desc",
                precio = 50,
                canjeada = true,
                nombreImgVector = "icon",
                transaccionId = null,
                isPendingUpdate = true
            )
        )
        val canjearResponse = CanjearRecompensaResponse(
            success = true,
            message = "Canjeada",
            transaccionId = null,
            puntosRestantes = null
        )

        coEvery { gemaDao.getPendingUpdateRecompensas() } returns pendingRecompensas
        coEvery { api.canjearRecompensa(any()) } returns Response.success(canjearResponse)
        coEvery { gemaDao.upsertRecompensa(any()) } just Runs

        // When
        val result = repository.postPendingCanjearRecompensas(gemaId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { authPreferencesManager.updatePuntos(any(), any()) }
    }

    @Test
    fun `postPendingCanjearRecompensas retorna Error cuando hay excepcion`() = runTest {
        // Given
        val gemaId = 1
        coEvery { gemaDao.getPendingUpdateRecompensas() } throws IOException("Database error")

        // When
        val result = repository.postPendingCanjearRecompensas(gemaId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Exception occurred"))
    }
    // endregion

    // region ObserveTareas Tests
    @Test
    fun `observeTareas retorna flow de tareas mapeadas`() = runTest {
        // Given
        val tareasEntities = listOf(
            TareaGemaEntity(
                tareaId = "1",
                remoteId = 1,
                titulo = "Tarea 1",
                descripcion = "Desc 1",
                puntos = 10,
                estado = "pendiente",
                dia = "Lunes",
                nombreImgVector = "icon1"
            ),
            TareaGemaEntity(
                tareaId = "2",
                remoteId = 2,
                titulo = "Tarea 2",
                descripcion = "Desc 2",
                puntos = 20,
                estado = "completada",
                dia = "Martes",
                nombreImgVector = "icon2"
            )
        )

        coEvery { gemaDao.observeTodasLasTareas() } returns flowOf(tareasEntities)

        // When
        val result = repository.observeTareas().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Tarea 1", result[0].titulo)
        assertEquals("Tarea 2", result[1].titulo)
    }
    // endregion

    // region ObserveRecompensas Tests
    @Test
    fun `observeRecompensas retorna flow de recompensas mapeadas`() = runTest {
        // Given
        val recompensasEntities = listOf(
            RecompensaGemaEntity(
                recompensaId = "1",
                remoteId = 1,
                titulo = "Recompensa 1",
                descripcion = "Desc 1",
                precio = 100,
                canjeada = false,
                nombreImgVector = "icon1",
                transaccionId = null
            )
        )

        coEvery { gemaDao.observeTodasLasRecompensas() } returns flowOf(recompensasEntities)

        // When
        val result = repository.observeRecompensas().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Recompensa 1", result[0].titulo)
        assertEquals(100, result[0].precio)
    }
    // endregion
}