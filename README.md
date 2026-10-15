# ScrollFix Wayland

A tiny client-side Fabric mod that stops the hotbar from **skipping every other slot** when you scroll the mouse wheel on Linux under Wayland.

## The problem

On Wayland desktops (KDE Plasma, GNOME, etc.), Minecraft running through XWayland often receives **two scroll events for every wheel notch**. The result: each click of the wheel jumps two hotbar slots instead of one. It happens in a completely vanilla instance, and the in-game *Discrete Scrolling* and *Scroll Sensitivity* options don't fix it. It's a long-standing issue — see Mojang's tracker, [MC-92401](https://bugs-legacy.mojang.com/browse/MC-92401).

## The fix

ScrollFix Wayland hooks Minecraft's mouse-scroll handler. When a second scroll event in the same direction arrives within a few milliseconds of the first, it's treated as a duplicate and dropped. Deliberate scrolling is much slower than that, so normal scrolling (hotbar, inventories, menus) behaves as expected.

- Client-side only — no server install needed
- No dependencies (doesn't need Fabric API)
- If a future Minecraft version renames the scroll method, the mod quietly does nothing instead of crashing

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 26.2.
2. Drop `scrollfix-wayland-<version>.jar` into your `mods` folder.

## Is it working?

Check your game log for:

```
[ScrollFix Wayland] Loaded, duplicate window = 25 ms
[ScrollFix Wayland] Dropped a duplicate scroll event (fix is working)
```

The second line appears the first time a duplicate is caught.

## Configuration

After the first launch, edit `config/scrollfix-wayland.properties`:

| Setting | Default | What it does |
|---|---|---|
| `thresholdMs` | `25` | Same-direction scroll events closer together than this are dropped. Raise it (40–50) if the hotbar still skips sometimes; lower it (~15) if fast scrolling feels like it loses notches. |

## Building from source

Requires JDK 25.

```
./gradlew build
```

The jar ends up in `build/libs/`. Every push is also built automatically by GitHub Actions.

## Compatibility

- Minecraft 26.2, Fabric
- Tested on Bazzite (KDE Plasma, Wayland) with Prism Launcher
- Should be harmless on X11, Windows, and macOS, but there's no reason to install it there

## License

MIT — see [LICENSE](LICENSE).
