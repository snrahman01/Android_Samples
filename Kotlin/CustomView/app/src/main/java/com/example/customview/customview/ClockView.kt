package com.example.customview.customview

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customview.R
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import androidx.core.os.HandlerCompat.postDelayed
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class ClockView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var textColor:Int = 0
    private var textHeight:Float = 40.0f
    private var textWidth:Float = 40.0f

    private var paddingStart:Float = 0.0f
    private var paddingEnd:Float = 0.0f
    private var paddingTop:Float = 0.0f
    private var paddingBottom:Float = 0.0f

    private var clockRoundColor:Int = 0
    private var clockBackColor:Int  = 0
    private var clockDialerColor:Int = 0
    private var clockDialerCenterColor:Int = 0

    private val PI:Float = 3.1416f

    private  val baseSecAngle = PI/30
    private val baseMinAngle:Float = PI/1800
    private val baseHrAngle:Float = PI/21600

    private  var secondAngle:Float = baseSecAngle
    private var minuteAngle:Float = baseMinAngle*(5*60)
    private var hrAngle:Float = baseHrAngle*(10*60*60)

    private var clockRadius:Float = 0.0f

    private var paintWatchRound:Paint
    private var paintWatchRoundFill:Paint
    private var paintWatchDialer:Paint
    private lateinit var paintWatchDialerCenter:Paint
    private var paintLine:Paint
    private var paintText:Paint

    private var borderStrokeWidth:Float = 50.0f
    private val dialerStrokeWidth:Float = 4.0f
    private val textStrokeWidth:Float = 4.0f

    private var centerX:Float =0.0f
    private var centerY:Float =0.0f
    private var clockTextSize:Float=40.0f

    private val defaultStrockWidth = 50.0f


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ClockView,
            0, 0).apply {

            try {
                clockRadius = getFloat(R.styleable.ClockView_clockDiameter, 0.0f)

                clockRoundColor = getColor(R.styleable.ClockView_clockRoundColor,
                                            ContextCompat.getColor(context, R.color.colorPrimaryDark))

                clockBackColor = getColor(R.styleable.ClockView_clockRoundBackColor,
                                          ContextCompat.getColor(context, R.color.colorPrimary))

                clockDialerColor = getColor(R.styleable.ClockView_clockDialerColor,
                                            ContextCompat.getColor(context, R.color.colorAccent))

                clockDialerCenterColor = getColor(R.styleable.ClockView_clockDialerCenterColor,
                                                  ContextCompat.getColor(context, R.color.colorAccent))
                borderStrokeWidth = getFloat(R.styleable.ClockView_clockCircleStrokWidth,defaultStrockWidth)
            }finally {
                recycle()
            }
        }

        paintLine = Paint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context,R.color.colorAccent)
            strokeWidth = borderStrokeWidth
            textSize = clockTextSize
        }

        paintWatchRound = Paint(ANTI_ALIAS_FLAG).apply {
            color = clockRoundColor
            strokeWidth = borderStrokeWidth
            style = Paint.Style.STROKE
        }

        paintWatchRoundFill = Paint(ANTI_ALIAS_FLAG).apply {
            color= ContextCompat.getColor(context, R.color.colorSilverCenter)
            style=Paint.Style.FILL
        }

        paintWatchDialer = Paint(ANTI_ALIAS_FLAG).apply {
            color = clockDialerColor
            strokeWidth = dialerStrokeWidth
        }
        paintText = Paint(ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            textSize = 40.0f
            if (textHeight == 0f) {
                textHeight = textSize
            } else {
                textSize = textHeight
            }
        }

        startClock()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        var xpad = (paddingStart+paddingEnd)
        var ypad = (paddingTop+paddingBottom)

        val ww = w.toFloat()-xpad
        val hh = h.toFloat()-ypad

        //set clock diameter to view min value between width and height
        val diameter = Math.min(ww, hh)
        clockRadius = (diameter/2)-borderStrokeWidth
        centerX = (diameter/2)
        centerY = (diameter/2)

        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            //Clock Outer Circle
            drawCircle(centerX, centerY, clockRadius, paintWatchRound)

            //Clock Background
            drawCircle(
                centerX,
                centerY,
                clockRadius - (borderStrokeWidth / 2),
                paintWatchRoundFill
            )


            //Moiddle Point Indicator
            paintWatchDialer.style = Paint.Style.FILL_AND_STROKE
            drawCircle(centerX, centerY, 40.0f, paintWatchRound)
        }

        //Line of Minutes
        drawMinuteCircle(centerX, centerY,clockRadius-borderStrokeWidth/2, canvas)

        //Dialer Position On certain time point
        setDialer(centerX, centerY,clockRadius, canvas, secondAngle, minuteAngle, hrAngle)

    }



    private fun drawMinuteCircle(cx:Float, cy:Float, r:Float, canvas: Canvas?){

        var angle = 0.0f
        for(i in 1..60){

            var x1:Float = cx+(r*cos(angle))
            var y1:Float = cy+(r*sin(angle))
            var x2:Float = cx+((r-26)*cos(angle))
            var y2:Float = cy+((r-26)*sin(angle))

            paintLine.color = ContextCompat.getColor(context, R.color.colorAccent)
            paintLine.strokeWidth = textStrokeWidth
            when(i){
                1->{
                    canvas?.drawText("03", x1, y1+clockTextSize/2, paintText)
                }
                15->{
                    canvas?.drawText("06",
                        x1-clockTextSize/2,
                        y1+(borderStrokeWidth/2+clockTextSize/2),
                        paintText)
                }
                30->{
                    canvas?.drawText("09",
                        x1-borderStrokeWidth,
                        y1+clockTextSize/2,
                        paintText)
                }

                45->{
                    canvas?.drawText("12",
                        x1-clockTextSize/2,
                        y1-(borderStrokeWidth/2-clockTextSize/2),
                        paintText)
                }else->{
                    paintLine.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    paintLine.strokeWidth = 2.0f
                }
            }

            canvas?.drawLine(x1,y1,x2,y2, paintLine)
            angle += (3.1416f/30)
        }
    }

    //start timer
    private fun startClock(){
        var handler = Handler()
        val runbl = object : Runnable {
            override fun run() {
                minuteAngle += baseMinAngle
                hrAngle += (baseHrAngle)
                secondAngle+=baseSecAngle
                handler.postDelayed(this, 1000)
                invalidate()
            }
        }

        handler.postDelayed(runbl, 1000)
    }


    //Dialer Position change with second
    private fun setDialer(cx:Float, cy:Float,
                          r:Float, canvas: Canvas?,
                          angleSecond:Float,
                          angleMinute:Float,
                          angleHr:Float){

        paintLine.strokeWidth = 10.0f

        //Point of minute dialer
        var x1:Float = cx+((r-80)*cos(angleMinute))
        var y1:Float = cy+((r-80)*sin(angleMinute))
        canvas?.drawLine(cx,cy,x1,y1, paintLine)

        //Point of second dialer
        var x2:Float = cx+((r-150)*cos(angleHr))
        var y2:Float = cy+((r-150)*sin(angleHr))
        canvas?.drawLine(cx,cy, x2,y2, paintLine)

        //point of second dialer
        var x3:Float = cx+((r-50)*cos(angleSecond))
        var y3:Float = cy+((r-50)*sin(angleSecond))

        paintLine.strokeWidth = 2.0f
        canvas?.drawLine(cx,cy, x3,y3, paintLine)

    }


    public fun setClockCircleBorderColor(color:Int){
        this.clockRoundColor = ContextCompat.getColor(context,color)
    }
    public fun setClockCircleBackgrounColor(color:Int){
        this.clockBackColor = ContextCompat.getColor(context,color)
    }
    public fun setClockDialerColor(color:Int){
        this.clockDialerColor = ContextCompat.getColor(context,color)
    }
    public fun setClockCircleStrockWidth(strockWidth:Float){
        this.borderStrokeWidth = strockWidth
    }

}
