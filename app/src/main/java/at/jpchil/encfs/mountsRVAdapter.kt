package at.jpchil.encfs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Color.YELLOW
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.jpchil.encfs.R.color.*

class mountsRVAdapter(
    val context: Context,
    val listClickEditInterface: ListClickEditInterface,
    val listClickDeleteInterface: ListClickDeleteInterface,
    val listClickInterface: ListClickInterface,
) :
    RecyclerView.Adapter<mountsRVAdapter.ViewHolder>() {

    private val allMounts = ArrayList<Mount>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mountTV = itemView.findViewById<TextView>(R.id.idencDir)
        val stateTV = itemView.findViewById<TextView>(R.id.mountState)
        val clearDirTV = itemView.findViewById<TextView>(R.id.idEdtClearDir)
        val dateTV = itemView.findViewById<TextView>(R.id.idTVDate)
        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
        val editIV = itemView.findViewById<ImageView>(R.id.idIVEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.mount_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    public override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mountTV.setText(allMounts.get(position).vaultName)

        if (getMount(allMounts.get(position).clearDir)==0) {
//            println(allMounts.get(position).clearDir + " is mounted")
            holder.itemView.setBackgroundColor(Color.parseColor("#0277bd"))
            holder.stateTV.setText("mounted")
        } else {
//            println(allMounts.get(position).clearDir + " is NOT mounted")
            holder.itemView.setBackgroundColor(R.color.gray)
            holder.stateTV.setText("not mounted")
        }


        holder.dateTV.setText("Last Updated : " + allMounts.get(position).timeStamp)
        holder.editIV.setOnClickListener {
            listClickEditInterface.onEditIconClick(allMounts.get(position))
        }
        holder.deleteIV.setOnClickListener {
            listClickDeleteInterface.onDeleteIconClick(allMounts.get(position))
        }

        holder.itemView.setOnClickListener {
            listClickInterface.onMountClick(allMounts.get(position), position)

        }
        var position = allMounts.get(position)
    }

    override fun getItemCount(): Int {
        return allMounts.size
    }

    fun updateList(newList: List<Mount>) {
        allMounts.clear()
        allMounts.addAll(newList)
        notifyDataSetChanged()
    }

}

interface ListClickEditInterface {
    fun onEditIconClick(mount: Mount)
}

interface ListClickDeleteInterface {
    fun onDeleteIconClick(mount: Mount)
}

interface ListClickInterface {
    fun onMountClick(mount: Mount, position: Int)
}
