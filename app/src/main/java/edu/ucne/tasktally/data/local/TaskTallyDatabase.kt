package edu.ucne.tasktally.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.tasktally.data.local.DAOs.GemaDao
import edu.ucne.tasktally.data.local.DAOs.MentorDao
import edu.ucne.tasktally.data.local.DAOs.RecompensaDao
import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TareaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TareaMentorDao
import edu.ucne.tasktally.data.local.DAOs.TransaccionDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.entidades.GemaEntity
import edu.ucne.tasktally.data.local.entidades.MentorEntity
import edu.ucne.tasktally.data.local.entidades.RecompensaMentorEntity
import edu.ucne.tasktally.data.local.entidades.RecompensaGemaEntity
import edu.ucne.tasktally.data.local.entidades.TareaMentorEntity
import edu.ucne.tasktally.data.local.entidades.TareaGemaEntity
import edu.ucne.tasktally.data.local.entidades.TransaccionEntity
import edu.ucne.tasktally.data.local.entidades.ZonaEntity

@Database(
    entities = [
        MentorEntity::class,
        ZonaEntity::class,
        GemaEntity::class,
        TareaMentorEntity::class,
        TareaGemaEntity::class,
        RecompensaMentorEntity::class,
        RecompensaGemaEntity::class,
        TransaccionEntity::class,
    ],
    version = 9,
    exportSchema = false
)
abstract class TaskTallyDatabase : RoomDatabase() {
    abstract fun gemaDao(): GemaDao
    abstract fun mentorDao(): MentorDao
    abstract fun zonaDao(): ZonaDao
    abstract fun tareaDao(): TareaMentorDao
    abstract fun tareaGemaDao(): TareaGemaDao
    abstract fun recompensaDao(): RecompensaDao
    abstract fun recompensaGemaDao(): RecompensaGemaDao
    abstract fun transaccionDao(): TransaccionDao
}
