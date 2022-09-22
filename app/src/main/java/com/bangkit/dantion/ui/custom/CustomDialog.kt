package com.bangkit.dantion.ui.custom

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import com.bangkit.dantion.R

class CustomDialog(
    private val mainButtonText: String? = null,
    private val secondaryButtonText: String? = null,
    private val title: String? = null,
    private val subtitle: String? = null,
    private val image: Drawable? = null,
    private val blockMainButton : () -> Unit,
    private val blockSecondaryButton : () -> Unit,
): DialogFragment() {
    private lateinit var mainButton: Button
    private lateinit var secondaryButton: Button
    private lateinit var ivDialog: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        val view = inflater.inflate(R.layout.dialaogue_two_button, container, false)
        mainButton = view.findViewById(R.id.main_button_dialog)
        secondaryButton = view.findViewById(R.id.secondary_button_dialog)
        tvTitle = view.findViewById(R.id.tv_title_dialog)
        tvSubtitle = view.findViewById(R.id.tv_subtitle_dialog)
        ivDialog = view.findViewById(R.id.iv_dialog)

        if(title==null) tvTitle.isGone = true
        else tvTitle.text = title

        if(subtitle==null) tvSubtitle.isGone = true
        else tvSubtitle.text = subtitle

        if(image==null) ivDialog.isGone = true
        else ivDialog.setImageDrawable(image)

        if(secondaryButtonText==null) secondaryButton.isGone = true
        else {
            secondaryButton.text = secondaryButtonText
            secondaryButton.setOnClickListener {
                blockSecondaryButton()
                dismiss()
            }
        }

        if(mainButtonText==null) mainButton.isGone = true
        else {
            mainButton.text = mainButtonText
            mainButton.setOnClickListener {
                blockMainButton()
                dismiss()
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}