# Configuration

Open Canopy Drift from the device launcher and use the remote to change the scene. Values are stored locally and apply when the screensaver starts. Every selection supports Random. The randomize-all option chooses each scene selection at each start. If scene rotation is enabled, the selected setting moves through the three settings every 90 seconds.

| Setting | Choices | Default |
| --- | --- | --- |
| Tree species | Maple, Oak, Birch, Random | Maple |
| Season and leaf palette | Spring, Summer, Autumn, Winter, Random | Autumn |
| Falling leaf density | A few, A handful, Many, A ton, Schools, Random | A handful |
| Wind strength | Calm, Breeze, Windy, Strong gusts, Random | Breeze |
| Wind direction | Left, Variable, Right, Random | Variable |
| Setting | Forest, Riverside, Forest clearing, Random | Forest |
| Lighting | Day, Dusk, Night, Random | Day |
| Rotate forest settings | On, Off | On |
| Randomize all settings | On, Off | Off |

## Host settings provider

The exported provider authority is `com.jeremykenedy.canopydrift.settings`:

- `content://com.jeremykenedy.canopydrift.settings/schema` lists each setting, type, default, allowed values, and whether Random is supported.
- `content://com.jeremykenedy.canopydrift.settings/settings` returns the saved settings.
- Update a value by writing its `key` and `value` columns to the settings URI. Unknown keys and unsupported values are rejected.

The provider is a local Android content provider. It does not make a network request. Fire TV Toolkit can install and select the app. Fire TV UI currently does not provide a general editor for app-specific settings.
