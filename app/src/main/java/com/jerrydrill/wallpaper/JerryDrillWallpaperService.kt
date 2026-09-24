package com.jerrydrill.wallpaper
import android.content.SharedPreferences
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlin.math.sin
import kotlin.random.Random
class JerryDrillWallpaperService:WallpaperService(){
 override fun onCreateEngine():Engine=JerryEngine()
 inner class JerryEngine:Engine(){
  private val handler=Handler(Looper.getMainLooper());private var visible=false;private var frame=0;private var state=State.IDLE;private var start=0;private lateinit var prefs:SharedPreferences
  private val bg=Paint().apply{color=Color.parseColor("#141822")};private val mouse=Paint(Paint.ANTI_ALIAS_FLAG).apply{color=Color.parseColor("#A9663B")};private val crack=Paint(Paint.ANTI_ALIAS_FLAG).apply{color=Color.WHITE;style=Paint.Style.STROKE;strokeWidth=4f};private val drill=Paint(Paint.ANTI_ALIAS_FLAG).apply{color=Color.GRAY}
  private val runner=Runnable{drawFrame()}
  override fun onCreate(h:SurfaceHolder){super.onCreate(h);prefs=applicationContext.getSharedPreferences(PositionPickerActivity.WALLPAPER_PREFS,MODE_PRIVATE)}
  override fun onVisibilityChanged(v:Boolean){visible=v;if(v){state=State.WALK_IN;start=frame;handler.post(runner)}else handler.removeCallbacks(runner)}
  override fun onSurfaceDestroyed(h:SurfaceHolder){super.onSurfaceDestroyed(h);visible=false;handler.removeCallbacks(runner)}
  private fun drawFrame(){var c:Canvas?=null;try{c=surfaceHolder.lockCanvas();if(c!=null)draw(c)}finally{if(c!=null)surfaceHolder.unlockCanvasAndPost(c)};frame++;handler.removeCallbacks(runner);if(visible)handler.postDelayed(runner,33)}
  private fun draw(c:Canvas){val w=c.width.toFloat();val h=c.height.toFloat();c.drawRect(0f,0f,w,h,bg);val x=prefs.getFloat(PositionPickerActivity.KEY_X,.5f)*w;val y=prefs.getFloat(PositionPickerActivity.KEY_Y,.5f)*h;val t=frame-start
   when(state){State.IDLE->{}
    State.WALK_IN->{if(t>20){state=State.DRILL;start=frame};jerry(c,x,y,false,t)}
    State.DRILL->{crack(c,x,y,(t/90f).coerceIn(0f,1f));jerry(c,x,y,true,t);if(t>90){state=State.DONE;start=frame}}
    State.DONE->{crack(c,x,y,1f);jerry(c,x,y,false,t);if(t>10)handler.removeCallbacks(runner)}}}
  private fun crack(c:Canvas,x:Float,y:Float,p:Float){val r=Random(1234);for(i in 0 until (8*p).toInt().coerceAtLeast(1)){val a=(360f/8)*i+r.nextInt(-10,10);val len=90*p;val rad=Math.toRadians(a.toDouble());c.drawLine(x,y,x+(len*Math.cos(rad)).toFloat(),y+(len*Math.sin(rad)).toFloat(),crack)}}
  private fun jerry(c:Canvas,x:Float,y:Float,d:Boolean,t:Int){val j=if(d)sin(t*2.0).toFloat()*2 else 0f;val bx=x-40+j;val by=y-10;c.drawOval(RectF(bx,by,bx+30,by+24),mouse);c.drawCircle(bx-8,by-4,14f,mouse);c.drawCircle(bx-14,by-14,6f,mouse);c.drawCircle(bx-2,by-14,6f,mouse);c.drawRect(bx+30,by+1,bx+60,by+7,drill);c.drawCircle(bx+60,by+4,5f,drill)}
 }
 enum class State{IDLE,WALK_IN,DRILL,DONE}
}
