package edu.ucne.tasktally.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TransaccionDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.DAOs.gema.GemaDao
import edu.ucne.tasktally.data.local.DAOs.mentor.MentorDao
import edu.ucne.tasktally.data.local.entidades.mentor.RecompensaMentorEntity
import edu.ucne.tasktally.data.local.entidades.gemas.RecompensaGemaEntity
import edu.ucne.tasktally.data.local.entidades.mentor.TareaMentorEntity
import edu.ucne.tasktally.data.local.entidades.gemas.TareaGemaEntity
import edu.ucne.tasktally.data.local.entidades.TransaccionEntity
import edu.ucne.tasktally.data.local.entidades.ZonaEntity

@Database(
    entities = [
        ZonaEntity::class,
        TareaMentorEntity::class,
        TareaGemaEntity::class,
        RecompensaMentorEntity::class,
        RecompensaGemaEntity::class,
        TransaccionEntity::class,
    ],
    version = 16,
    exportSchema = false
)
abstract class TaskTallyDatabase : RoomDatabase() {
    abstract fun zonaDao(): ZonaDao
    abstract fun mentorDao(): MentorDao
    abstract fun recompensaGemaDao(): RecompensaGemaDao
    abstract fun transaccionDao(): TransaccionDao
    abstract fun gemaDao(): GemaDao
}
