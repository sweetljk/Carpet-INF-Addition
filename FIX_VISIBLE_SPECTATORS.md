# Fix: visibleSpectators on Minecraft 26.1.2

The ported mixin cleared the spectator invisibility flag but did not change the
server-side tracking filter. In Minecraft 26.1.2, `ServerPlayer.broadcastToPlayer()`
rejects spectator entities for non-spectator viewers.

This patch adds a `broadcastToPlayer` injection. When the Carpet rule
`visibleSpectators` is enabled, spectator entities can be tracked by normal players.
The existing invisibility-state hook is preserved, and vanilla spectator-camera
filtering for spectator viewers remains unchanged.

## Build

Use JDK 25 (the project targets Java 25) and run from this directory:

- Windows: `gradlew.bat build`
- Linux: `./gradlew build`

The output jar is under `build/libs/`. This source patch was not compiled in the
current environment because only JDK 21 is available here; verify the build with JDK 25.

## Test

1. Put the built jar on the server and start Minecraft 26.1.2.
2. Run `/carpet visibleSpectators true`.
3. Switch one player to spectator and have a non-spectator player observe them.
4. Move the spectator across chunk/section boundaries to verify that the entity remains
   visible instead of being removed from tracking.
