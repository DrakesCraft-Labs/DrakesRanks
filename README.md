# DrakesRanks

## Qué es
Sistema ligero de rangos, permisos y formato de chat.

## Arquitectura
- `domain/` modelo `Rank`.
- `manager/` lectura/escritura en `ranks.yml` y PermissionAttachment.
- `listeners/` login/chat.
- `integration/papi/` placeholders.

## Hecho
- `/rank set|create|permission add`.
- Inyección de permisos al login/join.
- Formato de chat con MiniMessage.
- Placeholders PAPI: `%drakesranks_rank%`, `%drakesranks_prefix%`, `%drakesranks_suffix%`, `%drakesranks_color%`, `%drakesranks_weight%`.

## Falta
- Soporte de herencia entre rangos.
- Almacenamiento en base de datos (opcional).
- Editor GUI (opcional).

## Configuración
- `ranks.yml` con comentarios in-line.

## Dependencias
- Paper 1.20.6
- Java 21
- PlaceholderAPI (opcional)
