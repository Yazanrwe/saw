# Zero Ping (Fabric, Minecraft 1.20.1)

Client-side cosmetic mod: shows a fake 0–5 ms ping in the server list and the tab list.
It only changes what you see on your own screen; it does not change your real connection.

## Build

Requires JDK 17 and internet access to `maven.fabricmc.net`.

    gradle build          # or ./gradlew build if you add the Gradle wrapper

The mod jar is `build/libs/zero-ping-1.0.0.jar` (ignore the `-sources` jar).
Drop it in `.minecraft/mods` together with Fabric API 0.91.0+1.20.1.

## Config

`.minecraft/config/zero-ping.properties`

    enabled=true
    min-ping=0
    max-ping=5
