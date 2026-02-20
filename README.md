# DrakesRanks

Plugin de rangos y chat, extraido del modulo `drakesranks` del antiguo `DrakesCore`.

## Objetivo
Proveer un sistema ligero de rangos con permisos inyectados y formato de chat.

## Que hace hoy
- Comandos:
  - `/rank set <player> <rank>`
  - `/rank create <name>`
  - `/rank permission add <rank> <node>`
- Carga/guarda rangos en `ranks.yml`.
- Aplica permisos via `PermissionAttachment` en login/join.
- Formatea chat en `AsyncChatEvent` (Paper).
- Expone placeholders PAPI:
  - `%drakesranks_rank%`
  - `%drakesranks_prefix%`
  - `%drakesranks_suffix%`
  - `%drakesranks_color%`
  - `%drakesranks_weight%`

## Configuracion
- `src/main/resources/ranks.yml`
- Secciones: `ranks` y `players`.

## Dependencias
- Paper 1.20.6
- Java 21
- PlaceholderAPI (opcional)

## Pendiente real
- Herencia de rangos.
- Permisos temporales y expiracion.
- Backend SQL para sincronizacion entre servidores.
