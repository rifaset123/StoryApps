package com.example.storyapps.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyapps.R

class ButtonEdit : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txtColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    // menentukan kondisi awal dari custom view yang dibuat dan menambahkan listener untuk menangani aksi tertentu
    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    // jika ada perubahan bentuk, taruhlah kodenya dalam metode onDraw()
    // Metode onDraw() digunakan untuk mengcustom button ketika enable dan disable
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Mengubah background dari Button
        background = if(isEnabled) enabledBackground else disabledBackground

        // Mengubah warna text pada button
        setTextColor(txtColor)

        // Mengubah ukuran text pada button
        textSize = 12f

        // Menjadikan object pada button menjadi center
        gravity = Gravity.CENTER

        // Mengubah text pada button pada kondisi enable dan disable
        text = if(isEnabled) "Submit" else context.getString(R.string.fill_the_form_first)
    }
}