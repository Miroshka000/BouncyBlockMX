# BouncyBlocksMX

Multi-platform Minecraft plugin with shared bounce logic and separate adapters for Nukkit and AllayMC.

## Modules

- `bouncy-core` - shared bounce rules, config parsing and fall-damage protection
- `bouncy-platform-nukkit` - Nukkit plugin on Java 17
- `bouncy-platform-allay` - AllayMC plugin on Java 21

## Build

```bash
./gradlew build
```

Windows:

```powershell
.\gradlew.bat build
```

## Artifacts

- `bouncy-platform-nukkit/build/libs/bouncy-platform-nukkit-1.5.0.jar`
- `bouncy-platform-allay/build/libs/bouncy-platform-allay-1.5.0.jar`

## Runtime

- Nukkit module targets Java 17
- AllayMC module targets Java 21

## Configuration

Example config:

```yaml
TriggerBlocks:
  - "70"
  - "72"

BouncyBlocks:
  "57": 3.0
  "41": 2.0
  "42": 1.0
```

`TriggerBlocks` defines the blocks a player must stand on.
`BouncyBlocks` maps the support block under the trigger block to jump power.
