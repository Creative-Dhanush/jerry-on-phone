package com.jerrydrill.wallpaper
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
class PositionPickerActivity:Activity(){
 private lateinit var prefs:SharedPreferences
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);prefs=getSharedPreferences(WALLPAPER_PREFS,MODE_PRIVATE)
  val root=FrameLayout(this).apply{setBackgroundColor(Color.parseColor("#101319"))}
  val hint=TextView(this).apply{text="Tap anywhere on the screen.\nThat's where Jerry will drill after every unlock.";setTextColor(Color.WHITE);textSize=16f;gravity=android.view.Gravity.CENTER}
  root.addView(hint,FrameLayout.LayoutParams(-1,-1))
  val bottomBar=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL}
  val setBtn=Button(this).apply{text="Set as Live Wallpaper";isEnabled=false};bottomBar.addView(setBtn);root.addView(bottomBar,FrameLayout.LayoutParams(-1,-2,android.view.Gravity.BOTTOM))
  root.setOnTouchListener{v:View,event:MotionEvent->if(event.action==MotionEvent.ACTION_DOWN){val x=event.x/v.width;val y=event.y/v.height;hint.text="Spot saved ✓\nTap again to change it, or press the button below.";setBtn.isEnabled=true;prefs.edit().putFloat(KEY_X,x).putFloat(KEY_Y,y).apply()};true}
  setBtn.setOnClickListener{val intent=Intent(WallpaperService.SERVICE_INTERFACE).apply{putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT",ComponentName(this@PositionPickerActivity,JerryDrillWallpaperService::class.java));action="android.service.wallpaper.CHANGE_LIVE_WALLPAPER"};try{startActivity(intent)}catch(e:Exception){startActivity(Intent(Intent.ACTION_SET_WALLPAPER))}}
  setContentView(root)
 }
 companion object{const val WALLPAPER_PREFS="jerry_drill_prefs";const val KEY_X="spot_x_frac";const val KEY_Y="spot_y_frac"}
}
