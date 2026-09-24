# Jerry Drill Wallpaper — Android Project

This is a **live wallpaper** app: Jerry stands at whatever spot you tap during
setup, and every time your home screen becomes visible again (which includes
right after you unlock your phone), he plays a short drilling animation that
leaves a crack behind.

## What's inside
- `PositionPickerActivity.kt` — the setup screen. Tap anywhere on your
  screen; that spot (stored as a 0–1 fraction of width/height, so it works
  on any screen size) is saved, then the button opens Android's native
  "Set live wallpaper" picker pointed at this app.
- `JerryDrillWallpaperService.kt` — the actual wallpaper engine. It reads
  the saved spot, and on `onVisibilityChanged(true)` (fires when the home
  screen becomes visible, e.g. right after unlock) it replays: Jerry walks
  in → drills → crack appears → holds until next unlock.
- Everything is drawn with plain `Canvas`/`Paint` so there are no external
  asset dependencies — you can swap in real sprite/GIF artwork later for a
  more "real cartoon" look (see **Leveling it up** below).

## How to get a real, installable .apk
1. Install **Android Studio** (free, from developer.android.com).
2. `File → Open` and select this `JerryDrillWallpaper` folder.
3. Let Gradle sync (first time takes a few minutes, needs internet).
4. Click the green ▶ Run button with a device or emulator selected —
   or `Build → Build Bundle(s)/APK(s) → Build APK(s)` to get a `.apk`
   file you can copy to any Android phone and install directly (you'll
   need to allow "install unknown apps" for whichever app you transfer
   it with).
5. Open the app once → tap where you want Jerry → tap
   **"Set as Live Wallpaper"** → confirm in the system picker.

## Why this can't also be "one file for iPhone"
This isn't a limitation of the build — it's an Apple platform rule.
iOS does not allow **any** app, from anyone, to draw persistent animations
over the home screen or lock screen. Apple's sandboxing blocks exactly the
mechanism this whole idea depends on. There's no iOS equivalent of Android's
"live wallpaper" API, so a truly identical experience on iPhone isn't
buildable by anyone without jailbreaking (which voids warranty and isn't
something I'll help set up). If you want *something* iPhone-side, the
closest legitimate options are a custom Lock Screen widget (iOS 16+) or a
Shortcuts-based static wallpaper — both far more limited than this.

## Leveling it up (optional next steps)
- Replace the Canvas-drawn Jerry with a real sprite‑sheet or Lottie/GIF
  animation for the "real cartoon" feel you described (Lottie is the
  cleanest route — export the animation from After Effects/Rive as JSON).
- Add a "reset crack on next unlock" vs. "keep it permanent" toggle in
  the picker screen, stored as another SharedPreferences flag.
- Add sound (drill buzz) via `SoundPool`, muted by default so it doesn't
  surprise people in class/meetings.
