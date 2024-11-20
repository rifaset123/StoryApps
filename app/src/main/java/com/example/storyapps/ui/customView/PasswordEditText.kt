package com.example.storyapps.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import com.example.storyapps.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {
    private var showButtonImage: Drawable
    private val startIconImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_24) as Drawable
    private val customMarginStart = resources.getDimensionPixelSize(R.dimen.margin_start)


    // menentukan kondisi awal dari custom view yang dibuat dan menambahkan listener untuk menangani aksi tertentu
    init {
        showButtonImage = ContextCompat.getDrawable(context, R.drawable.baseline_remove_red_eye_24) as Drawable
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        setButtonDrawables(startOfTheText = startIconImage)
        setOnTouchListener(this)

        // kode untuk menampilkan clear button ketika ada perubahan teks
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if ( s.isNotEmpty() && s.length < 8) {
                    error = context.getString(R.string.password_must_be_at_least_8_characters)
                } else {
                    error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.enter_your_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        textSize = 16f
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = showButtonImage, endPadding = 16) // memasukkan gambar pada parameter ketiga (right)
        setButtonDrawables(startOfTheText = startIconImage)
    }

    private fun hideClearButton() {
        setButtonDrawables(endPadding = 16)
        setButtonDrawables(startOfTheText = startIconImage)
    }


    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
        endPadding: Int = 0
    ){
        // menampilkan gambar pada EditText dengan parameter sebagai berikut (left, top, right, bottom)
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
        compoundDrawablePadding = endPadding
    }

    // menentukan behaviour dari komponen Custom View ketika komponen ditekan
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            //  pengecekan apakah area yang ditekan adalah area tempat tombol silang berada
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) { // pengecekan jenis handphone apakah menggunakan format RTL (Right-to-left)
                clearButtonEnd = (showButtonImage.intrinsicWidth + marginStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - showButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { // action_down = ketika tombol ditekan
                        showButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> { // ketika tombol dilepas setelah ditekan
                        showButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}