# DrakesRanks

Plugin de ranks y chat, extraido del modulo `drakesranks` del antiguo `DrakesCore`.

## Objetivo
Proveer un sistema ligero de ranks con permissions inyectados y formato de chat.

## Que hace hoy
- Comandos:
  - `/rank set <player> <rank>`
  - `/rank create <name>`
  - `/rank permission add <rank> <node>`
  - `/rank list`
  - `/rank info <rank>`
  - `/rank reload`
- Carga/guarda ranks en `ranks.yml`.
- Fallback deterministico por `default-rank` en config.
- Aplica permissions via `PermissionAttachment` en login/join.
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
- Herencia de ranks.
- Permisos temporales y expiracion.
- Backend SQL para sincronizacion entre servidores.
