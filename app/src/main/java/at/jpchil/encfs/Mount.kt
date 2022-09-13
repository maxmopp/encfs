package at.jpchil.encfs

import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jaredrummler.ktsh.Shell
import java.util.concurrent.TimeUnit

@Entity(tableName = "mountsTable")

class Mount (@ColumnInfo(name = "vaultname")val vaultName :String, @ColumnInfo(name = "encdir")val encDir :String, @ColumnInfo(name = "cleardir")val clearDir :String, @ColumnInfo(name = "timestamp")val timeStamp :String) {
    @PrimaryKey(autoGenerate = true) var id = 0
}

val b= " "
val endApo = "\""
val suBin = "su --mount-master -c \""
val encBinm = "/system/xbin/encfs -o allow_other -S "
val encBinu = "/system/xbin/encfs -u "

fun getMount(directory: String): Int {
    val suBin = "su --mount-master -c "
    val mountpointBin = suBin + "mountpoint "
    val shell= Shell("sh")
    val cmd = mountpointBin + directory
    val result=shell.run(cmd)
    return result.exitCode
}

fun mountENCfs(encDir: String, clearDir: String, passwort: String?):Shell.Command.Result {
    val b= " "
    val endApo = "\""
    val suBin = "su --mount-master -c \""
    val encBinm = "/system/xbin/encfs -o allow_other -S "
    println("Will mount $encDir to $clearDir using password")
 //   Toast.makeText(this, "This will take a while, please WAIT", Toast.LENGTH_LONG).show()
    val shell= Shell("sh")
    val asswort= passwort?.replace("$","\\$") // did not find another way
    println("Will mount $encDir to $clearDir using passwort")
    val cmd = suBin + b + encBinm + b + encDir + b + clearDir + " <<< '" + asswort  + "'" + endApo
 //   displayText("attempting to mount ENCfs")
    val result=shell.run(cmd) {
        timeout = Shell.Timeout(1, TimeUnit.MINUTES)
    }
    return result
}

fun umountENCfs(clearDir: String) {
    val b= " "
    val endApo = "\""
    val suBin = "su --mount-master -c \""
    val encBinu = "/system/xbin/encfs -u "
    println("Will unmount $clearDir")
    val shell = Shell("sh")
 //   Toast.makeText(this, "umounting " + clearDir, Toast.LENGTH_LONG ).show()
    val cmd = suBin + b + encBinu + b + clearDir + endApo
    val result=shell.run(cmd) {timeout = Shell.Timeout(1, TimeUnit.MINUTES)}

}