[![downloads](https://img.shields.io/badge/-⬇%20releases-brightgreen)](https://github.com/makamys/WorldTweaks/releases)
[![builds](https://img.shields.io/badge/-🛈%20builds-blue)](https://makamys.github.io/docs/CI-Downloads/CI-Downloads.html)

# WorldTweaks
This mod lets you tweak and disable various parts of vanilla and BoP worldgen. There are options that let you:
* Disable villages
* Change mineshaft generation probability
* Disable cave spider room generation in mineshafts
* Disable dungeon generation
* Use noise-based distribution for tree generation (similarly to Beta 1.7.3)
* Tweak some BoP parameters:
  * Disable mud, quicksand, and poison ivy generation
  * Multiply the amount of BoP foliage per chunk by a value

All options are disabled by default.

# License
This mod is released under the [Unlicense](UNLICENSE), excluding the `makamys.worldtweaks.lib.owg` package which contains code borrowed from ted80's [Old World Gen](https://github.com/Ted80-Minecraft-Mods/Old-World-Gen).

# Contributing

When running in an IDE, add these program arguments
```
--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin worldtweaks.mixin.json
```
