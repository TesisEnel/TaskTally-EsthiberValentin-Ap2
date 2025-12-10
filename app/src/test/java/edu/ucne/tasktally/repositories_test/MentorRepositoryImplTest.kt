package edu.ucne.tasktally.repositories_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.DAOs.mentor.MentorDao
import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import edu.ucne.tasktally.data.local.entidades.mentor.RecompensaMentorEntity
import edu.ucne.tasktally.data.local.entidades.mentor.TareaMentorEntity
import edu.ucne.tasktally.data.remote.DTOs.mentor.MentorTareasRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.RecompensaDto
import edu.ucne.tasktally.data.remote.DTOs.mentor.TareaDto
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.RecompensaOperationResult
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.BulkTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.TareaOperationResult
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneCodeResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.data.repositories.MentorRepositoryImpl
import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.models.TareaMentor
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
class MentorRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: MentorRepositoryImpl
    private lateinit var mentorDao: MentorDao
    private lateinit var zonaDao: ZonaDao
    private lateinit var api: TaskTallyApi

    @Before
    fun setup() {
        mentorDao = mockk(relaxed = true)
        zonaDao = mockk(relaxed = true)
        api = mockk(relaxed = true)
        repository = MentorRepositoryImpl(mentorDao, zonaDao, api)
    }

    // region Tareas CRUD Tests
    @Test
    fun `createTareaLocal guarda tarea con isPendingCreate true cuando no tiene remoteId`() = runTest {
        // Given
        val tarea = TareaMentor(
            tareaId = "tarea-123",
            remoteId = null,
            titulo = "Nueva tarea",
            descripcion = "Descripcion",
            puntos = 10,
            dias = "L,M,X",
            repetir = 1,
            nombreImgVector = "icon",
            isPendingCreate = true,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val tareaSlot = slot<TareaMentorEntity>()
        coEvery { mentorDao.upsertTarea(capture(tareaSlot)) } just Runs

        // When
        repository.createTareaLocal(tarea)

        // Then
        coVerify { mentorDao.upsertTarea(any()) }
        assertTrue(tareaSlot.captured.isPendingCreate)
        assertFalse(tareaSlot.captured.isPendingUpdate)
    }

    @Test
    fun `createTareaLocal guarda tarea con isPendingUpdate true cuando tiene remoteId`() = runTest {
        // Given
        val tarea = TareaMentor(
            tareaId = "tarea-123",
            remoteId = 1,
            titulo = "Tarea existente",
            descripcion = "Descripcion",
            puntos = 10,
            dias = "L,M,X",
            repetir = 1,
            nombreImgVector = "icon",
            isPendingCreate = true,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val tareaSlot = slot<TareaMentorEntity>()
        coEvery { mentorDao.upsertTarea(capture(tareaSlot)) } just Runs

        // When
        repository.createTareaLocal(tarea)

        // Then
        assertFalse(tareaSlot.captured.isPendingCreate)
        assertTrue(tareaSlot.captured.isPendingUpdate)
    }

    @Test
    fun `updateTareaLocal marca tarea como pendiente de actualizar`() = runTest {
        // Given
        val tarea = TareaMentor(
            tareaId = "tarea-123",
            remoteId = 1,
            titulo = "Tarea actualizada",
            descripcion = "Nueva descripcion",
            puntos = 20,
            dias = "J,V",
            repetir = 2,
            nombreImgVector = "icon2",
            isPendingCreate = false,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val tareaSlot = slot<TareaMentorEntity>()
        coEvery { mentorDao.upsertTarea(capture(tareaSlot)) } just Runs

        // When
        repository.updateTareaLocal(tarea)

        // Then
        assertTrue(tareaSlot.captured.isPendingUpdate)
    }

    @Test
    fun `deleteTareaLocal marca tarea como pendiente de eliminar`() = runTest {
        // Given
        val tarea = TareaMentor(
            tareaId = "tarea-123",
            remoteId = 1,
            titulo = "Tarea a eliminar",
            descripcion = "Descripcion",
            puntos = 10,
            dias = "L",
            repetir = 1,
            nombreImgVector = "icon",
            isPendingCreate = false,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val tareaSlot = slot<TareaMentorEntity>()
        coEvery { mentorDao.upsertTarea(capture(tareaSlot)) } just Runs

        // When
        repository.deleteTareaLocal(tarea)

        // Then
        assertTrue(tareaSlot.captured.isPendingDelete)
    }

    @Test
    fun `getTareaById retorna tarea cuando existe`() = runTest {
        // Given
        val tareaEntity = TareaMentorEntity(
            tareaId = "tarea-123",
            remoteId = 1,
            titulo = "Tarea test",
            descripcion = "Descripcion",
            puntos = 10,
            dias = "L,M",
            repetir = 1,
            nombreImgVector = "icon",
            isPendingCreate = false,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        coEvery { mentorDao.obtenerTareaMentorPorId("tarea-123") } returns tareaEntity

        // When
        val result = repository.getTareaById("tarea-123")

        // Then
        assertNotNull(result)
        assertEquals("Tarea test", result?.titulo)
    }

    @Test
    fun `getTareaById retorna null cuando no existe`() = runTest {
        // Given
        coEvery { mentorDao.obtenerTareaMentorPorId("inexistente") } returns null

        // When
        val result = repository.getTareaById("inexistente")

        // Then
        assertNull(result)
    }

    @Test
    fun `deleteAllTareasLocal llama al dao correctamente`() = runTest {
        // Given
        coEvery { mentorDao.eliminarTodasLasTareas() } just Runs

        // When
        repository.deleteAllTareasLocal(1)

        // Then
        coVerify { mentorDao.eliminarTodasLasTareas() }
    }
    // endregion

    // region Recompensas CRUD Tests
    @Test
    fun `createRecompensaLocal guarda recompensa con isPendingCreate true cuando no tiene remoteId`() = runTest {
        // Given
        val recompensa = RecompensaMentor(
            recompensaId = "rec-123",
            remoteId = null,
            titulo = "Nueva recompensa",
            descripcion = "Descripcion",
            precio = 100,
            isDisponible = true,
            nombreImgVector = "icon",
            isPendingCreate = true,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val recompensaSlot = slot<RecompensaMentorEntity>()
        coEvery { mentorDao.upsertRecompensa(capture(recompensaSlot)) } just Runs

        // When
        repository.createRecompensaLocal(recompensa)

        // Then
        assertTrue(recompensaSlot.captured.isPendingCreate)
        assertFalse(recompensaSlot.captured.isPendingUpdate)
    }

    @Test
    fun `createRecompensaLocal guarda recompensa con isPendingUpdate true cuando tiene remoteId`() = runTest {
        // Given
        val recompensa = RecompensaMentor(
            recompensaId = "rec-123",
            remoteId = 1,
            titulo = "Recompensa existente",
            descripcion = "Descripcion",
            precio = 100,
            isDisponible = true,
            nombreImgVector = "icon",
            isPendingCreate = true,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val recompensaSlot = slot<RecompensaMentorEntity>()
        coEvery { mentorDao.upsertRecompensa(capture(recompensaSlot)) } just Runs

        // When
        repository.createRecompensaLocal(recompensa)

        // Then
        assertFalse(recompensaSlot.captured.isPendingCreate)
        assertTrue(recompensaSlot.captured.isPendingUpdate)
    }

    @Test
    fun `updateRecompensaLocal marca recompensa como pendiente de actualizar`() = runTest {
        // Given
        val recompensa = RecompensaMentor(
            recompensaId = "rec-123",
            remoteId = 1,
            titulo = "Recompensa actualizada",
            descripcion = "Nueva descripcion",
            precio = 150,
            isDisponible = true,
            nombreImgVector = "icon2",
            isPendingCreate = false,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val recompensaSlot = slot<RecompensaMentorEntity>()
        coEvery { mentorDao.upsertRecompensa(capture(recompensaSlot)) } just Runs

        // When
        repository.updateRecompensaLocal(recompensa)

        // Then
        assertTrue(recompensaSlot.captured.isPendingUpdate)
    }

    @Test
    fun `deleteRecompensaLocal marca recompensa como pendiente de eliminar`() = runTest {
        // Given
        val recompensa = RecompensaMentor(
            recompensaId = "rec-123",
            remoteId = 1,
            titulo = "Recompensa a eliminar",
            descripcion = "Descripcion",
            precio = 100,
            isDisponible = true,
            nombreImgVector = "icon",
            isPendingCreate = false,
            isPendingUpdate = false,
            isPendingDelete = false
        )

        val recompensaSlot = slot<RecompensaMentorEntity>()
        coEvery { mentorDao.upsertRecompensa(capture(recompensaSlot)) } just Runs

        // When
        repository.deleteRecompensaLocal(recompensa)

        // Then
        assertTrue(recompensaSlot.captured.isPendingDelete)
    }

    @Test
    fun `getRecompensaById retorna recompensa cuando existe`() = runTest {
        // Given
        val recompensaEntity = RecompensaMentorEntity(
            recompensaId = "rec-123",
            remoteId = 1,
            titulo = "Recompensa test",
            descripcion = "Descripcion",
            precio = 100,
            isDisponible = true,
            nombreImgVector = "icon"
        )

        coEvery { mentorDao.obtenerRecompensaMentorPorId("rec-123") } returns recompensaEntity

        // When
        val result = repository.getRecompensaById("rec-123")

        // Then
        assertNotNull(result)
        assertEquals("Recompensa test", result?.titulo)
    }

    @Test
    fun `getRecompensaById retorna null cuando no existe`() = runTest {
        // Given
        coEvery { mentorDao.obtenerRecompensaMentorPorId("inexistente") } returns null

        // When
        val result = repository.getRecompensaById("inexistente")

        // Then
        assertNull(result)
    }
    // endregion

    // region Zone Tests
    @Test
    fun `getZoneInfo retorna datos de API cuando responde correctamente`() = runTest {
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
        coEvery { zonaDao.upsert(any()) } just Runs

        // When
        val result = repository.getZoneInfo(mentorId, zoneId)

        // Then
        assertEquals("Zona Mentor", result.nombre)
        assertEquals("MENTOR123", result.joinCode)
        coVerify { zonaDao.upsert(any()) }
    }

    @Test
    fun `getZoneInfo retorna datos locales cuando API falla`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1
        val localZona = ZonaEntity(
            zonaId = zoneId,
            nombre = "Zona Local",
            joinCode = "LOCAL123",
            mentorId = "1"
        )

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } throws IOException("Network error")
        coEvery { zonaDao.getById(zoneId) } returns localZona

        // When
        val result = repository.getZoneInfo(mentorId, zoneId)

        // Then
        assertEquals("Zona Local", result.nombre)
        assertEquals("LOCAL123", result.joinCode)
    }

    @Test
    fun `getZoneInfo retorna zona vacia cuando API falla y no hay datos locales`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } throws IOException("Network error")
        coEvery { zonaDao.getById(zoneId) } returns null

        // When
        val result = repository.getZoneInfo(mentorId, zoneId)

        // Then
        assertEquals(zoneId, result.zonaId)
        assertEquals("", result.nombre)
        assertEquals("", result.joinCode)
    }

    @Test
    fun `getZoneInfo retorna datos locales cuando API response body es null`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1
        val localZona = ZonaEntity(
            zonaId = zoneId,
            nombre = "Zona Local",
            joinCode = "LOCAL123",
            mentorId = "1"
        )

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } returns Response.success(null)
        coEvery { zonaDao.getById(zoneId) } returns localZona

        // When
        val result = repository.getZoneInfo(mentorId, zoneId)

        // Then
        assertEquals("Zona Local", result.nombre)
    }

    @Test
    fun `getZoneInfo retorna datos locales cuando API retorna error`() = runTest {
        // Given
        val mentorId = 1
        val zoneId = 1
        val localZona = ZonaEntity(
            zonaId = zoneId,
            nombre = "Zona Local",
            joinCode = "LOCAL123",
            mentorId = "1"
        )

        coEvery { api.obtenerMentorInfoZona(mentorId, zoneId) } returns Response.error(
            404,
            "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        )
        coEvery { zonaDao.getById(zoneId) } returns localZona

        // When
        val result = repository.getZoneInfo(mentorId, zoneId)

        // Then
        assertEquals("Zona Local", result.nombre)
    }

    @Test
    fun `updateZoneCode retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val mentorId = 1
        val response = UpdateZoneCodeResponse(
            zoneCode = "NEWCODE123"
        )

        coEvery { api.updateZoneCode(mentorId) } returns Response.success(response)

        // When
        val result = repository.updateZoneCode(mentorId)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("NEWCODE123", (result as Resource.Success).data?.zoneCode)
    }

    @Test
    fun `updateZoneCode retorna Error cuando API falla`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.updateZoneCode(mentorId) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.updateZoneCode(mentorId)

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `updateZoneCode retorna Error cuando body es null`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.updateZoneCode(mentorId) } returns Response.success(null)

        // When
        val result = repository.updateZoneCode(mentorId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Response body is null", (result as Resource.Error).message)
    }

    @Test
    fun `updateZoneCode retorna Error cuando hay excepcion`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.updateZoneCode(mentorId) } throws IOException("Network error")

        // When
        val result = repository.updateZoneCode(mentorId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Exception occurred"))
    }

    @Test
    fun `updateZoneName retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val mentorId = 1
        val newName = "Nueva Zona"
        val response = UpdateZoneResponse(
            zoneName = newName
        )

        coEvery { api.updateZoneName(mentorId, any()) } returns Response.success(response)

        // When
        val result = repository.updateZoneName(mentorId, newName)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(newName, (result as Resource.Success).data?.zoneName)
    }

    @Test
    fun `updateZoneName retorna Error cuando API falla`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.updateZoneName(mentorId, any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.updateZoneName(mentorId, "Test")

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `updateZoneName retorna Error cuando body es null`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.updateZoneName(mentorId, any()) } returns Response.success(null)

        // When
        val result = repository.updateZoneName(mentorId, "Test")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Response body is null", (result as Resource.Error).message)
    }
    // endregion

    // region GetTareasRecompensasMentor Tests
    @Test
    fun `getTareasRecompensasMentor retorna Error cuando mentorId es 0`() = runTest {
        // When
        val result = repository.getTareasRecompensasMentor(0)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No mentorId found", (result as Resource.Error).message)
    }

    @Test
    fun `getTareasRecompensasMentor guarda datos correctamente`() = runTest {
        // Given
        val mentorId = 1
        val mentorDataResponse = MentorTareasRecompensasResponse(
            tareas = listOf(
                TareaDto(
                    tareasGroupId = 1,
                    mentorId = mentorId,
                    titulo = "Tarea 1",
                    descripcion = "Desc 1",
                    puntos = 10,
                    dias = "L,M",
                    repetir = 1,
                    nombreImgVector = "icon1"
                )
            ),
            recompensas = listOf(
                RecompensaDto(
                    recompensaId = 1,
                    titulo = "Recompensa 1",
                    descripcion = "Desc 1",
                    precio = 100,
                    isDisponible = true,
                    nombreImgVector = "icon1",
                    gemas = emptyList()
                )
            )
        )

        coEvery { api.getTareasRecompensasMentor(mentorId) } returns Response.success(mentorDataResponse)
        coEvery { mentorDao.eliminarTodasLasTareas() } just Runs
        coEvery { mentorDao.eliminarTodasLasRecompensas() } just Runs
        coEvery { mentorDao.upsertTarea(any()) } just Runs
        coEvery { mentorDao.upsertRecompensa(any()) } just Runs

        // When
        val result = repository.getTareasRecompensasMentor(mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { mentorDao.eliminarTodasLasTareas() }
        coVerify { mentorDao.eliminarTodasLasRecompensas() }
        coVerify(exactly = 1) { mentorDao.upsertTarea(any()) }
        coVerify(exactly = 1) { mentorDao.upsertRecompensa(any()) }
    }

    @Test
    fun `getTareasRecompensasMentor retorna Error cuando API falla`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.getTareasRecompensasMentor(mentorId) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getTareasRecompensasMentor(mentorId)

        // Then
        assertTrue(result is Resource.Error)
        coVerify(exactly = 0) { mentorDao.eliminarTodasLasTareas() }
    }

    @Test
    fun `getTareasRecompensasMentor retorna Error cuando body es null`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.getTareasRecompensasMentor(mentorId) } returns Response.success(null)

        // When
        val result = repository.getTareasRecompensasMentor(mentorId)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Response body is null", (result as Resource.Error).message)
    }

    @Test
    fun `getTareasRecompensasMentor retorna Error cuando hay excepcion`() = runTest {
        // Given
        val mentorId = 1
        coEvery { api.getTareasRecompensasMentor(mentorId) } throws IOException("Network error")

        // When
        val result = repository.getTareasRecompensasMentor(mentorId)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Exception occurred"))
    }

    @Test
    fun `getTareasRecompensasMentor maneja valores null en DTOs correctamente`() = runTest {
        // Given
        val mentorId = 1
        val mentorDataResponse = MentorTareasRecompensasResponse(
            tareas = listOf(
                TareaDto(
                    tareasGroupId = null,
                    mentorId = null,
                    titulo = null,
                    descripcion = null,
                    puntos = null,
                    dias = null,
                    repetir = null,
                    nombreImgVector = null
                )
            ),
            recompensas = listOf(
                RecompensaDto(
                    recompensaId = null,
                    titulo = null,
                    descripcion = null,
                    precio = null,
                    isDisponible = null,
                    nombreImgVector = null,
                    gemas = null
                )
            )
        )

        coEvery { api.getTareasRecompensasMentor(mentorId) } returns Response.success(mentorDataResponse)
        coEvery { mentorDao.eliminarTodasLasTareas() } just Runs
        coEvery { mentorDao.eliminarTodasLasRecompensas() } just Runs
        coEvery { mentorDao.upsertTarea(any()) } just Runs
        coEvery { mentorDao.upsertRecompensa(any()) } just Runs

        // When
        val result = repository.getTareasRecompensasMentor(mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { mentorDao.upsertTarea(match { it.titulo == "" && it.puntos == 0 }) }
        coVerify { mentorDao.upsertRecompensa(match { it.titulo == "" && it.precio == 0 }) }
    }
    // endregion

    // region PostPendingTareas Tests
    @Test
    fun `postPendingTareas retorna Error cuando mentorId es 0`() = runTest {
        // When
        val result = repository.postPendingTareas(1, 0)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No mentorId found", (result as Resource.Error).message)
    }

    @Test
    fun `postPendingTareas retorna Success cuando no hay tareas pendientes`() = runTest {
        // Given
        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeEliminar() } returns emptyList()

        // When
        val result = repository.postPendingTareas(1, 1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(0, (result as Resource.Success).data?.successful)
        assertEquals(0, result.data?.failed)
    }

    @Test
    fun `postPendingTareas sincroniza create correctamente`() = runTest {
        // Given
        val zoneId = 1
        val mentorId = 1
        val pendingCreate = listOf(
            TareaMentorEntity(
                tareaId = "tarea-1",
                remoteId = null,
                titulo = "Nueva Tarea",
                descripcion = "Desc",
                puntos = 10,
                dias = "L",
                repetir = 1,
                nombreImgVector = "icon",
                isPendingCreate = true,
                isPendingUpdate = false,
                isPendingDelete = false
            )
        )
        val bulkResponse = BulkTareasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                TareaOperationResult(
                    accion = "create",
                    tareasGroupId = 100,
                    zoneId = zoneId,
                    titulo = "Nueva Tarea",
                    success = true,
                    mensaje = "Created"
                )
            )
        )

        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } returns pendingCreate
        coEvery { mentorDao.obtenerTareasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeEliminar() } returns emptyList()
        coEvery { api.bulkTareas(any()) } returns Response.success(bulkResponse)
        coEvery { mentorDao.upsertTarea(any()) } just Runs

        // When
        val result = repository.postPendingTareas(zoneId, mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            mentorDao.upsertTarea(match {
                it.remoteId == 100 && !it.isPendingCreate
            })
        }
    }

    @Test
    fun `postPendingTareas sincroniza update correctamente`() = runTest {
        // Given
        val zoneId = 1
        val mentorId = 1
        val pendingUpdate = listOf(
            TareaMentorEntity(
                tareaId = "tarea-1",
                remoteId = 100,
                titulo = "Tarea Actualizada",
                descripcion = "Nueva Desc",
                puntos = 20,
                dias = "L,M",
                repetir = 2,
                nombreImgVector = "icon2",
                isPendingCreate = false,
                isPendingUpdate = true,
                isPendingDelete = false
            )
        )
        val bulkResponse = BulkTareasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                TareaOperationResult(
                    accion = "update",
                    tareasGroupId = 100,
                    zoneId = zoneId,
                    titulo = "Tarea Actualizada",
                    success = true,
                    mensaje = "Updated"
                )
            )
        )

        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeActualizar() } returns pendingUpdate
        coEvery { mentorDao.obtenerTareasPendientesDeEliminar() } returns emptyList()
        coEvery { api.bulkTareas(any()) } returns Response.success(bulkResponse)
        coEvery { mentorDao.upsertTarea(any()) } just Runs

        // When
        val result = repository.postPendingTareas(zoneId, mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            mentorDao.upsertTarea(match { !it.isPendingUpdate })
        }
    }

    @Test
    fun `postPendingTareas sincroniza delete correctamente`() = runTest {
        // Given
        val zoneId = 1
        val mentorId = 1
        val pendingDelete = listOf(
            TareaMentorEntity(
                tareaId = "tarea-1",
                remoteId = 100,
                titulo = "Tarea a eliminar",
                descripcion = "Desc",
                puntos = 10,
                dias = "L",
                repetir = 1,
                nombreImgVector = "icon",
                isPendingCreate = false,
                isPendingUpdate = false,
                isPendingDelete = true
            )
        )
        val bulkResponse = BulkTareasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                TareaOperationResult(
                    accion = "delete",
                    tareasGroupId = 100,
                    zoneId = zoneId,
                    titulo = null,
                    success = true,
                    mensaje = "Deleted"
                )
            )
        )

        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeEliminar() } returns pendingDelete
        coEvery { api.bulkTareas(any()) } returns Response.success(bulkResponse)
        coEvery { mentorDao.eliminarTarea(any()) } just Runs

        // When
        val result = repository.postPendingTareas(zoneId, mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { mentorDao.eliminarTarea(any()) }
    }

    @Test
    fun `postPendingTareas retorna Error cuando API falla`() = runTest {
        // Given
        val pendingCreate = listOf(
            TareaMentorEntity(
                tareaId = "tarea-1",
                remoteId = null,
                titulo = "Nueva Tarea",
                descripcion = "Desc",
                puntos = 10,
                dias = "L",
                repetir = 1,
                nombreImgVector = "icon",
                isPendingCreate = true
            )
        )

        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } returns pendingCreate
        coEvery { mentorDao.obtenerTareasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeEliminar() } returns emptyList()
        coEvery { api.bulkTareas(any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.postPendingTareas(1, 1)

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `postPendingTareas retorna Error cuando hay excepcion`() = runTest {
        // Given
        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } throws IOException("Database error")

        // When
        val result = repository.postPendingTareas(1, 1)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Exception occurred"))
    }

    @Test
    fun `postPendingTareas ignora tareas sin remoteId en update`() = runTest {
        // Given
        val pendingUpdate = listOf(
            TareaMentorEntity(
                tareaId = "tarea-1",
                remoteId = null, // Sin remoteId
                titulo = "Tarea sin sync",
                descripcion = "Desc",
                puntos = 10,
                dias = "L",
                repetir = 1,
                nombreImgVector = "icon",
                isPendingUpdate = true
            )
        )

        coEvery { mentorDao.obtenerTareasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerTareasPendientesDeActualizar() } returns pendingUpdate
        coEvery { mentorDao.obtenerTareasPendientesDeEliminar() } returns emptyList()

        // When
        val result = repository.postPendingTareas(1, 1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(0, (result as Resource.Success).data?.successful)
        coVerify(exactly = 0) { api.bulkTareas(any()) }
    }
    // endregion

    // region PostPendingRecompensas Tests
    @Test
    fun `postPendingRecompensas retorna Error cuando mentorId es 0`() = runTest {
        // When
        val result = repository.postPendingRecompensas(1, 0)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No mentorId found", (result as Resource.Error).message)
    }

    @Test
    fun `postPendingRecompensas retorna Success cuando no hay recompensas pendientes`() = runTest {
        // Given
        coEvery { mentorDao.obtenerRecompensasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeEliminar() } returns emptyList()

        // When
        val result = repository.postPendingRecompensas(1, 1)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(0, (result as Resource.Success).data?.successful)
        assertEquals(0, result.data?.failed)
    }

    @Test
    fun `postPendingRecompensas sincroniza create correctamente`() = runTest {
        // Given
        val zoneId = 1
        val mentorId = 1
        val pendingCreate = listOf(
            RecompensaMentorEntity(
                recompensaId = "rec-1",
                remoteId = null,
                titulo = "Nueva Recompensa",
                descripcion = "Desc",
                precio = 100,
                isDisponible = true,
                nombreImgVector = "icon",
                isPendingCreate = true,
                isPendingUpdate = false,
                isPendingDelete = false
            )
        )
        val bulkResponse = BulkRecompensasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                RecompensaOperationResult(
                    accion = "create",
                    recompensaId = 100,
                    titulo = "Nueva Recompensa",
                    success = true,
                    mensaje = "Created"
                )
            )
        )

        coEvery { mentorDao.obtenerRecompensasPendientesDeCrear() } returns pendingCreate
        coEvery { mentorDao.obtenerRecompensasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeEliminar() } returns emptyList()
        coEvery { api.bulkRecompensas(any()) } returns Response.success(bulkResponse)
        coEvery { mentorDao.upsertRecompensa(any()) } just Runs

        // When
        val result = repository.postPendingRecompensas(zoneId, mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            mentorDao.upsertRecompensa(match {
                it.remoteId == 100 && !it.isPendingCreate
            })
        }
    }

    @Test
    fun `postPendingRecompensas sincroniza update correctamente`() = runTest {
        // Given
        val zoneId = 1
        val mentorId = 1
        val pendingUpdate = listOf(
            RecompensaMentorEntity(
                recompensaId = "rec-1",
                remoteId = 100,
                titulo = "Recompensa Actualizada",
                descripcion = "Nueva Desc",
                precio = 150,
                isDisponible = false,
                nombreImgVector = "icon2",
                isPendingCreate = false,
                isPendingUpdate = true,
                isPendingDelete = false
            )
        )
        val bulkResponse = BulkRecompensasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                RecompensaOperationResult(
                    accion = "update",
                    recompensaId = 100,
                    titulo = "Recompensa Actualizada",
                    success = true,
                    mensaje = "Updated"
                )
            )
        )

        coEvery { mentorDao.obtenerRecompensasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeActualizar() } returns pendingUpdate
        coEvery { mentorDao.obtenerRecompensasPendientesDeEliminar() } returns emptyList()
        coEvery { api.bulkRecompensas(any()) } returns Response.success(bulkResponse)
        coEvery { mentorDao.upsertRecompensa(any()) } just Runs

        // When
        val result = repository.postPendingRecompensas(zoneId, mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            mentorDao.upsertRecompensa(match { !it.isPendingUpdate })
        }
    }

    @Test
    fun `postPendingRecompensas sincroniza delete correctamente`() = runTest {
        // Given
        val zoneId = 1
        val mentorId = 1
        val pendingDelete = listOf(
            RecompensaMentorEntity(
                recompensaId = "rec-1",
                remoteId = 100,
                titulo = "Recompensa a eliminar",
                descripcion = "Desc",
                precio = 100,
                isDisponible = true,
                nombreImgVector = "icon",
                isPendingCreate = false,
                isPendingUpdate = false,
                isPendingDelete = true
            )
        )
        val bulkResponse = BulkRecompensasResponse(
            successful = 1,
            failed = 0,
            results = listOf(
                RecompensaOperationResult(
                    accion = "delete",
                    recompensaId = 100,
                    titulo = null,
                    success = true,
                    mensaje = "Deleted"
                )
            )
        )

        coEvery { mentorDao.obtenerRecompensasPendientesDeCrear() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeEliminar() } returns pendingDelete
        coEvery { api.bulkRecompensas(any()) } returns Response.success(bulkResponse)
        coEvery { mentorDao.eliminarRecompensaMentor(any()) } just Runs

        // When
        val result = repository.postPendingRecompensas(zoneId, mentorId)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { mentorDao.eliminarRecompensaMentor(any()) }
    }

    @Test
    fun `postPendingRecompensas retorna Error cuando API falla`() = runTest {
        // Given
        val pendingCreate = listOf(
            RecompensaMentorEntity(
                recompensaId = "rec-1",
                remoteId = null,
                titulo = "Nueva Recompensa",
                descripcion = "Desc",
                precio = 100,
                isDisponible = true,
                nombreImgVector = "icon",
                isPendingCreate = true
            )
        )

        coEvery { mentorDao.obtenerRecompensasPendientesDeCrear() } returns pendingCreate
        coEvery { mentorDao.obtenerRecompensasPendientesDeActualizar() } returns emptyList()
        coEvery { mentorDao.obtenerRecompensasPendientesDeEliminar() } returns emptyList()
        coEvery { api.bulkRecompensas(any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.postPendingRecompensas(1, 1)

        // Then
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `postPendingRecompensas retorna Error cuando hay excepcion`() = runTest {
        // Given
        coEvery { mentorDao.obtenerRecompensasPendientesDeCrear() } throws IOException("Database error")

        // When
        val result = repository.postPendingRecompensas(1, 1)

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Exception occurred"))
    }
    // endregion

    // region Observe Tests
    @Test
    fun `observeTareas retorna flow de tareas mapeadas`() = runTest {
        // Given
        val tareasEntities = listOf(
            TareaMentorEntity(
                tareaId = "1",
                remoteId = 1,
                titulo = "Tarea 1",
                descripcion = "Desc 1",
                puntos = 10,
                dias = "L",
                repetir = 1,
                nombreImgVector = "icon1"
            ),
            TareaMentorEntity(
                tareaId = "2",
                remoteId = 2,
                titulo = "Tarea 2",
                descripcion = "Desc 2",
                puntos = 20,
                dias = "M",
                repetir = 2,
                nombreImgVector = "icon2"
            )
        )

        coEvery { mentorDao.observeTodasLasTareasMentor() } returns flowOf(tareasEntities)

        // When
        val result = repository.observeTareas().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Tarea 1", result[0].titulo)
        assertEquals("Tarea 2", result[1].titulo)
    }

    @Test
    fun `observeRecompensas retorna flow de recompensas mapeadas`() = runTest {
        // Given
        val recompensasEntities = listOf(
            RecompensaMentorEntity(
                recompensaId = "1",
                remoteId = 1,
                titulo = "Recompensa 1",
                descripcion = "Desc 1",
                precio = 100,
                isDisponible = true,
                nombreImgVector = "icon1"
            )
        )

        coEvery { mentorDao.observeTodasLasRecompensasMentor() } returns flowOf(recompensasEntities)

        // When
        val result = repository.observeRecompensas().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Recompensa 1", result[0].titulo)
        assertEquals(100, result[0].precio)
    }
    // endregion
}