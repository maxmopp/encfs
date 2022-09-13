package at.jpchil.encfs

import androidx.lifecycle.LiveData
import androidx.room.*

// annotation for dao class.
@Dao
interface MountsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mount :Mount)

    @Delete
    suspend fun delete(mount: Mount)

    @Query("Select * from mountsTable order by id ASC")
    fun getAllMounts(): LiveData<List<Mount>>

    @Update
    suspend fun update(mount: Mount)

}
