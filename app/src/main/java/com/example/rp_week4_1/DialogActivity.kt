package com.example.rp_week4_1

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rp_week4_1.databinding.DialogLoginBinding

class DialogActivity(context: Context, myCustomDialogInterface: MyCustomDialogInterface) :
    Dialog(context) {

    private var myCustomDialogInterface: MyCustomDialogInterface? = null

    init {
        this.myCustomDialogInterface = myCustomDialogInterface
    }


    lateinit var binding: DialogLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.d(TAG, "MyoncreateCall")

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnYes.setOnClickListener {
            this.myCustomDialogInterface?.onYes()
            dismiss()
        }


    }

}


