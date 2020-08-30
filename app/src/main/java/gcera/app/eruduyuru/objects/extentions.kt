package gcera.app.eruduyuru.objects

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

inline fun materialAlertDialog(
    context: Context,
    builder: MaterialAlertDialogBuilder.() -> Unit
)=MaterialAlertDialogBuilder(context).apply(builder).create().show()

fun MaterialAlertDialogBuilder.positiveButton(
    text:String,
    onClickListener:(dialog: DialogInterface,item:Int)->Unit
):MaterialAlertDialogBuilder{
    return setPositiveButton(text){dialog,item->
        onClickListener(dialog,item)
    }
}

inline fun MaterialAlertDialogBuilder.negativeButton(
    text:String,
    crossinline onClickListener:(dialog: DialogInterface, item:Int)->Unit
):MaterialAlertDialogBuilder{
    return setNegativeButton(text){dialog,item->
        onClickListener(dialog,item)
    }
}