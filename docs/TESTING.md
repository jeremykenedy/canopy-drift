# Testing

Run the host tests and local checks:

```bash
bash test.sh
bash scripts/check-style.sh
python3 scripts/check-docs.py
python3 scripts/check-privacy.py
```

The Java tests cover settings resolution and validation. The Python tests cover installer URL trust, checksum checks, device selection, install/update behavior, confirmations, and failure cleanup. Android framework rendering and DreamService lifecycle require emulator testing.

On an emulator, verify screensaver discovery, visible animation, remote focus in settings, preview, Back exit, provider reads and writes, and unchanged system screensaver state. Physical Fire TV, Android TV, and Google TV results remain untested until volunteers report their device model, OS/API, resolution, and results.
