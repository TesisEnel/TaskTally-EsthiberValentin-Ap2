package edu.ucne.tasktally.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.tasktally.data.local.DAOs.GemaDao
import edu.ucne.tasktally.data.local.DAOs.MentorDao
import edu.ucne.tasktally.data.local.DAOs.RecompensaDao
import edu.ucne.tasktally.data.local.DAOs.TareaDao
import edu.ucne.tasktally.data.local.DAOs.UsuarioDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.entidades.GemaEntity
import edu.ucne.tasktally.data.local.entidades.MentorEntity
import edu.ucne.tasktally.data.local.entidades.RecompensaEntity
import edu.ucne.tasktally.data.local.entidades.TareaEntity
import edu.ucne.tasktally.data.local.entidades.UsuarioEntity
import edu.ucne.tasktally.data.local.entidades.ZonaEntity

@Database(
    entities = [
        UsuarioEntity::class,
        MentorEntity::class,
        ZonaEntity::class,
        GemaEntity::class,
        TareaEntity::class,
        RecompensaEntity::class,
    ],
    version = 7,
    exportSchema = false
)
abstract class TaskTallyDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun gemaDao(): GemaDao
    abstract fun mentorDao(): MentorDao
    abstract fun zonaDao(): ZonaDao
    abstract fun tareaDao(): TareaDao
    abstract fun recompensaDao(): RecompensaDao
}
