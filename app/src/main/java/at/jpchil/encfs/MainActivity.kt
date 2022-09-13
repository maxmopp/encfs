package at.jpchil.encfs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), ListClickInterface, ListClickEditInterface, ListClickDeleteInterface {

    lateinit var viewModal: MountsViewModal
    lateinit var mountsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton

    val mountsRVAdapter = mountsRVAdapter(this, this, this, this)

    // directory ... /mnt/runtime/write/emulated/0/jpchil/.jDocs to /mnt/runtime/write/emulated/0/Documents/jDocs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mountsRV = findViewById(R.id.mountsRV)
        addFAB = findViewById(R.id.idFAB)

        mountsRV.layoutManager = LinearLayoutManager(this)

        mountsRV.adapter = mountsRVAdapter
        viewModal = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                                     ).get(MountsViewModal::class.java)

        viewModal.allMounts.observe(this, Observer { list ->
            list?.let {
                mountsRVAdapter.updateList(it)
            }
        })
        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditMountActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onMountClick(mount: Mount, position: Int) {
        val recyclerMountView = findViewById<RecyclerView>(R.id.mountsRV)
        recyclerMountView.layoutManager = LinearLayoutManager(this)
        recyclerMountView.adapter = mountsRVAdapter
        if (getMount(mount.clearDir) == 0) {
            umountENCfs(mount.clearDir)
            refreshRV(recyclerMountView)
        } else {
            supportFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
                val passwort = bundle.getString("bundleKey")
                println("MainActivity 1: #"+passwort+"#")
                val result = mountENCfs(mount.encDir, mount.clearDir, passwort)
                refreshRV(recyclerMountView)
            }
        val pwdDialog=PasswordDialogFragment()
        pwdDialog.show(supportFragmentManager,"PasswordDialogFragment")
        }
    }

    fun refreshRV(rvmv:RecyclerView) {
        rvmv.adapter!!.notifyDataSetChanged()
    }

    override fun onEditIconClick(mount: Mount) {
        val intent = Intent(this@MainActivity, AddEditMountActivity::class.java)
        intent.putExtra("mountType", "Edit")
        intent.putExtra("vaultName", mount.vaultName)
        intent.putExtra("encDir", mount.encDir)
        intent.putExtra("clearDir", mount.clearDir)
        intent.putExtra("mountId", mount.id)
        startActivity(intent)
//        this.finish()
    }

    override fun onDeleteIconClick(mount: Mount) {
        YesNoDialog(this).show( getString(R.string.strYesNo)+" "+mount.vaultName, getString(R.string.strYesNoMsg)){
            if (it.toString() == "YES") {
                viewModal.deleteMount(mount)
            }
        }
        Toast.makeText(this, "${mount.vaultName} Deleted", Toast.LENGTH_LONG).show()
    }
}



