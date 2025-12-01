package edu.ucne.tasktally.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.tasktally.data.local.DAOs.EstadoDao
import edu.ucne.tasktally.data.local.DAOs.GemaDao
import edu.ucne.tasktally.data.local.DAOs.GemaZonaDao
import edu.ucne.tasktally.data.local.DAOs.MentorDao
import edu.ucne.tasktally.data.local.DAOs.ProgresoDao
import edu.ucne.tasktally.data.local.DAOs.RachaDao
import edu.ucne.tasktally.data.local.DAOs.RecompensaDao
import edu.ucne.tasktally.data.local.DAOs.TareaDao
import edu.ucne.tasktally.data.local.DAOs.TransaccionRecompensaDao
import edu.ucne.tasktally.data.local.DAOs.UserInfoDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.entidades.EstadoEntity
import edu.ucne.tasktally.data.local.entidades.GemaEntity
import edu.ucne.tasktally.data.local.entidades.GemaZonaEntity
import edu.ucne.tasktally.data.local.entidades.MentorEntity
import edu.ucne.tasktally.data.local.entidades.ProgresoEntity
import edu.ucne.tasktally.data.local.entidades.RachaEntity
import edu.ucne.tasktally.data.local.entidades.RecompensaEntity
import edu.ucne.tasktally.data.local.entidades.TareaEntity
import edu.ucne.tasktally.data.local.entidades.TransaccionRecompensaEntity
import edu.ucne.tasktally.data.local.entidades.UserInfoEntity
import edu.ucne.tasktally.data.local.entidades.UsuarioEntity
import edu.ucne.tasktally.data.local.entidades.ZonaEntity

@Database(
    entities = [
        UsuarioEntity::class,
        UserInfoEntity::class,
        MentorEntity::class,
        ZonaEntity::class,
        GemaEntity::class,
        RachaEntity::class,
        GemaZonaEntity::class,
        EstadoEntity::class,
        TareaEntity::class,
        RecompensaEntity::class,
        ProgresoEntity::class,
        TransaccionRecompensaEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class TaskTallyDatabase : RoomDatabase() {
    abstract fun estadoDao(): EstadoDao
    abstract fun gemaDao(): GemaDao
    abstract fun gemaZonaDao(): GemaZonaDao
    abstract fun mentorDao(): MentorDao
    abstract fun zonaDao(): ZonaDao
    abstract fun userInfoDao(): UserInfoDao
    abstract fun tareaDao(): TareaDao
    abstract fun recompensaDao(): RecompensaDao
    abstract fun rachaDao(): RachaDao
    abstract fun progresoDao(): ProgresoDao
    abstract fun transaccionRecompensaDao(): TransaccionRecompensaDao
}
