package at.jpchil.encfs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MountsViewModal (application: Application) :AndroidViewModel(application) {

    val allMounts : LiveData<List<Mount>>
    val repository : MountsRepository

    init {
        val dao = MountsDatabase.getDatabase(application).getMountsDao()
        repository = MountsRepository(dao)
        allMounts = repository.allMounts
    }

    fun deleteMount (mount: Mount) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(mount)
    }

    fun updateMount(mount: Mount) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(mount)
    }

    fun addMount(mount: Mount) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(mount)
    }
}
