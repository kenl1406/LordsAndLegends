# Lords & Legends — Crew (Android, Jetpack Compose)

A demo of the staff app, ported from the web prototype.

## Project layout

```
app/src/main/
  AndroidManifest.xml
  java/com/lordsandlegends/crew/
    MainActivity.kt              # host + screen state + bottom bar
    ui/theme/Theme.kt            # palette + typography (Cormorant fallback = serif)
    ui/components/
      Common.kt                  # TopBar, BackBar, Cards, Buttons, Pills, Eyebrow
      BottomTabBar.kt
      Charts.kt                  # Donut + ProgressRing (Canvas-based)
      Video.kt                   # video card row + bottom-sheet player
    ui/screens/
      LoginScreen.kt
      OverviewScreen.kt          # hero card + Academy / Performance tiles + news
      AcademyScreen.kt           # progress ring + cocktail/Pilot videos + modules
      PerformanceScreen.kt       # KPI grid + two donut charts + leaderboard
      ProfileScreen.kt
  res/
    drawable/
      logo.xml                   # vector approximation; drop logo.png to override
      ic_launcher_foreground.xml
      ic_launcher_background.xml
    mipmap-anydpi-v26/ic_launcher.xml
    values/{colors,strings,themes}.xml
    xml/backup_rules.xml
```

## Replacing the logo with the real PNG

Drop your image at `app/src/main/res/drawable/logo.png` (lowercase, no spaces).
Then in `LoginScreen.kt` it'll already be picked up — `painterResource(R.drawable.logo)` resolves PNG over the bundled vector when both exist.

## Notes

- The video player is a styled placeholder (matches the web demo, bottom-sheet modal). To play actual video, swap the `Box(...)` in `Video.kt :: VideoSheet` for an `AndroidView { VideoView(it).apply {...} }` or ExoPlayer (Media3) — kept stub-only here so there are no extra deps.
- Pie charts are drawn with `Canvas` + `drawArc` (no chart library dependency).
- Theme is locked to the light "parchment" scheme; dark mode is intentionally not wired up.
- Demo navigation is plain state (`rememberSaveable<Screen>`). No Navigation Compose dependency.
