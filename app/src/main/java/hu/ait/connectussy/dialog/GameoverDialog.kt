package hu.ait.connectussy.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.ait.connectussy.databinding.GameoverDialogBinding

class GameoverDialog : DialogFragment() {
    interface GameoverHandler {
        fun gameoverCreated()
    }

    lateinit var gameoverHandler: GameoverHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is GameoverHandler) {
            gameoverHandler = context
        } else {
            throw RuntimeException(
                "The Activity is not implementing the ItemHandler interface."
            )
        }
    }

    lateinit var binding: GameoverDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        binding = GameoverDialogBinding.inflate(requireActivity().layoutInflater)
        dialogBuilder.setView(binding.root)

        dialogBuilder.setPositiveButton("Ok") { dialog, which ->
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
        }
        return dialogBuilder.create()
    }

}