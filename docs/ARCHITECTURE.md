# Architecture

Canopy Drift is a dependency-free Android app with a native `DreamService`, a remote-operated settings activity, a preview activity, and a small exported settings provider. The scene is drawn with Android Canvas and a hardware-accelerated view. Procedural tree silhouettes and falling leaf shapes are generated at runtime; the app contains no third-party image, video, or audio assets.

Leaves use fixed-size arrays with seeded starting positions and independent drift, spin, and fall rates. The renderer schedules about 30 redraws per second while active and removes callbacks when the dream stops. It resolves persisted selections at scene start. Optional scene rotation moves between forest, riverside, and clearing compositions every 90 seconds.

The app has no internet permission, runtime network client, telemetry, crash reporting, background updater, or third-party runtime dependency. The standalone installer makes a user-initiated request to GitHub to fetch the release APK and checksum.
