package at.jpchil.encfs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Mount::class), version = 1, exportSchema = false)
abstract class MountsDatabase : RoomDatabase() {

    abstract fun getMountsDao(): MountsDao

    companion object {
        @Volatile
        private var INSTANCE: MountsDatabase? = null

        fun getDatabase(context: Context): MountsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MountsDatabase::class.java,
                    "mount_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
