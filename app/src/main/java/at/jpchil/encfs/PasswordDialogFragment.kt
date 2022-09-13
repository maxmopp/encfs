package at.jpchil.encfs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView

class PasswordDialogFragment() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val builder = AlertDialog.Builder(activity)
            // Get the layout inflater
            val inflater = activity?.layoutInflater;
            val pwdView = inflater?.inflate(R.layout.dialog_password, null)

            val txtPwd = pwdView?.findViewById<EditText>(R.id.passwort)
            builder.setPositiveButton("Ok"){ _, _ ->}
            builder.setNegativeButton("Cancel"){ _, _ ->}
            builder.setView(pwdView)

            val alertDialog = builder.create().apply {
                setCanceledOnTouchOutside(false)
            }
            alertDialog.show(); // or alertDialog.create();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val txtPwd = pwdView?.findViewById<EditText>(R.id.passwort)
                val passwort = txtPwd?.text.toString()
                setFragmentResult("requestKey", bundleOf("bundleKey" to passwort))
                alertDialog.dismiss()
            }
        return alertDialog
        }
}