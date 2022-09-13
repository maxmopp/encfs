package at.jpchil.encfs

import androidx.lifecycle.LiveData

class MountsRepository(private val mountsDao: MountsDao) {
    val allMounts: LiveData<List<Mount>> = mountsDao.getAllMounts()

    suspend fun insert(mount: Mount) {
        mountsDao.insert(mount)
    }

    suspend fun delete(mount: Mount){
        mountsDao.delete(mount)
    }

    suspend fun update(mount: Mount){
        mountsDao.update(mount)
    }
}
