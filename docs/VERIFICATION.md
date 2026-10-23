# Verification

## Device matrix

| Device | Result |
| --- | --- |
| Google TV emulator, `sdk_google_atv64_amati_arm64`, Android 14/API 34, 1920x1080 | Installed the release candidate and checked the animated preview, remote-operated settings, settings schema and values, accepted and rejected provider updates, and APK permissions. Two captures several seconds apart differed, confirming continuous motion. Installing did not change the selected screensaver (`com.google.android.apps.tv.dreamx/.service.Backdrop`) or enabled state. Android's dream shell command requires root on this image, so idle activation was not directly exercised. |
| Physical Fire TV | Not tested. We are looking for a Fire TV owner to install and test selection, idle activation, settings, and exit behavior, then report the model, Fire OS/API, resolution, and results in the issue tracker. |
| Physical Android TV | Not tested. We are looking for an Android TV owner to test those behaviors and report the device model, Android version/API, resolution, and results in the issue tracker. |
| Physical Google TV | Not tested. We are looking for a Google TV owner to test those behaviors and report the device model, Android version/API, resolution, and results in the issue tracker. |

Emulator results establish Android app behavior only. They do not establish physical device activation, sustained thermal behavior, or native 4K composition. No physical TV is used for this verification.

## Captures

`docs/screenshots/canopy-drift-preview.png` and `docs/screenshots/canopy-drift-settings.png` were captured from the running app on the Google TV emulator (`sdk_google_atv64_amati_arm64`, Android 14/API 34, 1920x1080). The animated scene was allowed to run before capture. The application screenshot contains no menus or status overlays. The settings capture shows the remote-friendly settings screen.

## Runtime checks

The release candidate showed continuous preview motion, accepted D-pad setting changes, exposed its settings schema, accepted an Oak selection, and rejected an unsupported wind value. The app requests no Android permissions. The emulator's selected screensaver was restored after confirming installation did not change it. Physical Fire TV, Android TV, and Google TV owners are invited to report idle activation and device-specific results through the repository issue tracker.

## Optional clock regression, 2026-10-22

An isolated Android TV emulator fixture compiled the production scene and provider classes from this change. The combined five-app run passed 123 actual Canvas/provider assertions. It checked the existing appearance default (OFF), explicit ON/OFF under scene randomization, portable boolean storage and schema, and invalid-write retention. OFF produced zero time/date/seconds text draws while the scene remained drawn.

This isolated regression does not establish production DreamService binding, physical-TV performance or acceptance of the existing artwork. Signed release and both-TV installation gates remain pending.
