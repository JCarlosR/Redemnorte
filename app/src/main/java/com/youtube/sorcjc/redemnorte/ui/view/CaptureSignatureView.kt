package com.youtube.sorcjc.redemnorte.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.ByteArrayOutputStream


class CaptureSignatureView(context: Context?, attr: AttributeSet?) : View(context, attr) {
    private var myBitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val path: Path = Path()
    private val bitmapPaint: Paint = Paint(Paint.DITHER_FLAG)
    private val paint: Paint = Paint()
    private var mX = 0f
    private var mY = 0f
    private val touchTolerance = 4f
    private val lineThickness = 4f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        myBitmap = Bitmap.createBitmap(
            w,
            if (h > 0) h else (this.parent as View).height,
            Bitmap.Config.ARGB_8888
        )

        myBitmap?.let {
            canvas = Canvas(myBitmap as Bitmap)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE)

        myBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, bitmapPaint)
        }

        canvas.drawPath(path, paint)
    }

    private fun touchStart(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        if (!path.isEmpty) {
            path.lineTo(mX, mY)
            canvas?.drawPath(path, paint)
        } else {
            canvas?.drawPoint(mX, mY, paint)
        }
        path.reset()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        super.onTouchEvent(e)
        val x = e.x
        val y = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    fun clearCanvas() {
        canvas?.drawColor(Color.WHITE)
        invalidate()
    }

    val bytes: ByteArray
        get() {
            val b = bitmap
            val outputStream = ByteArrayOutputStream()
            b.run {
                compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            return outputStream.toByteArray()
        }

    val bitmap: Bitmap
        get() {
            val v: View = this.parent as View
            val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)
            return b
        }

    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = Color.argb(255, 0, 0, 0)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = lineThickness
    }
}