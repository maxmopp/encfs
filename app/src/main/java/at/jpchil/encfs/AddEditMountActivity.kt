package at.jpchil.encfs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jaredrummler.ktsh.Shell
import java.text.SimpleDateFormat
import java.util.*

class AddEditMountActivity : AppCompatActivity() {
    lateinit var vaultName: EditText
    lateinit var encDir: EditText
    lateinit var clearDir: EditText
    lateinit var passwort: EditText
    lateinit var saveBtn: Button
    lateinit var mountBtn: Button
    lateinit var umountBtn: Button

    lateinit var viewModal: MountsViewModal
    var mountID = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_mount)

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MountsViewModal::class.java)

        vaultName = findViewById(R.id.idEdtVaultName)
        encDir = findViewById(R.id.idEdtEncDir)
        clearDir = findViewById(R.id.idEdtClearDir)
        passwort = findViewById(R.id.edtPassword)
        saveBtn = findViewById(R.id.btnSave)
        mountBtn = findViewById(R.id.btnMount)
        umountBtn = findViewById(R.id.btnuMount)

        // on below line we are getting data passed via an intent.
        val mountType = intent.getStringExtra("mountType")
        if (mountType.equals("Edit")) {
            // on below line we are setting data to edit text.
            val vaultName = intent.getStringExtra("vaultName")
            val encDir = intent.getStringExtra("encDir")
            val clearDir = intent.getStringExtra("clearDir")
            mountID = intent.getIntExtra("mountId", -1)
            saveBtn.setText(R.string.btnSave)
            this.vaultName.setText(vaultName)
            this.encDir.setText(encDir)
            this.clearDir.setText(clearDir)
        } /* else {
            saveBtn.setText("Save Mount")
        } */

        var mountState = getMount(clearDir.text.toString())
        println(mountState)
        if (mountState == 0) {
            markButtonDisabled(mountBtn)
            markButtonEnabled(umountBtn)
        } else {
            markButtonDisabled(umountBtn)
            markButtonEnabled(mountBtn)
        }

        fun displayText(outtext: String) {
            val textView: TextView = findViewById(R.id.textOut)
            textView?.setText(null)
            textView?.text = outtext
        }
        saveBtn.setOnClickListener {
            val vaultName = vaultName.text.toString()
            val encDir = encDir.text.toString()
            val clearDir = clearDir.text.toString()
            if (mountType.equals("Edit")) {
                if (vaultName.isNotEmpty() && encDir.isNotEmpty() && clearDir.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    val updateMount = Mount(vaultName, encDir, clearDir, currentDateAndTime)
                    updateMount.id = mountID
                    viewModal.updateMount(updateMount)
                    Toast.makeText(this, "$vaultName Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (vaultName.isNotEmpty() && encDir.isNotEmpty() && clearDir.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    viewModal.addMount(Mount(vaultName, encDir, clearDir, currentDateAndTime))
                    Toast.makeText(this, "$vaultName Added", Toast.LENGTH_LONG).show()
                }
            }
            // opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
//            this.finish()
        }
        mountBtn.setOnClickListener {
            val encDir = encDir.text.toString()
            val clearDir = clearDir.text.toString()
            val passwort = passwort.text.toString()
            val result = mountENCfs(encDir, clearDir, passwort)
            displayText(result.stdout()+"\n"+result.stderr()+"\n"+result.details.toString())
            // opening the new activity on below line
         //   startActivity(Intent(applicationContext, MainActivity::class.java))
         //   this.finish()
        }
        umountBtn.setOnClickListener {
            val clearDir = clearDir.text.toString()
            umountENCfs(clearDir)
            // opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
//            this.finish()
        }
    }
    private fun markButtonDisabled(button: Button) {
        button.isEnabled = false
        button.setBackgroundColor(Color.GRAY)
    }
    private fun markButtonEnabled(button: Button) {
        button.isEnabled = true
        button.setBackgroundColor(Color.BLUE)
    }
 }
