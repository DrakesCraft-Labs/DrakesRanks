<p align="center">
  <img src="https://raw.githubusercontent.com/DrakesCraft-Labs/DrakesRanks/master/banner.svg" width="100%" alt="DRAKES RANKS animated banner" />
</p>

# DrakesRanks

> ### 🏰 ¡Únete a la Comunidad Oficial de DrakesCraft!
> 
> * 🎮 **IP del Servidor**: `mc.drakescraft.cl` *(Java 1.21.11 & Bedrock)*
> * 💬 **Discord Oficial**: [discord.gg/drakescraft](https://discord.gg/rv3vtXZTk7)
> * 🌐 **Web & Guía**: [web.drakescraft.cl](https://web.drakescraft.cl) — 🛒 **Tienda**: [web.drakescraft.cl/store](https://web.drakescraft.cl/store.html)
> 
> *¡Juega con este addon y más de 80 expansiones optimizadas en vivo en nuestra network de supervivencia técnica!*

---

Plugin de rangos y chat, extraido del modulo `drakesranks` del antiguo `DrakesCore`.

## Objetivo
Proveer un sistema ligero de rangos con permisos inyectados y formato de chat.

## Que hace hoy
- Comandos:
  - `/rank set <player> <rank>`
  - `/rank create <name>`
  - `/rank permission add <rank> <node>`
  - `/rank list`
  - `/rank info <rank>`
  - `/rank reload`
- Carga/guarda rangos en `ranks.yml`.
- Fallback deterministico por `default-rank` en config.
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

---

## 📄 License & Intellectual Property

Copyright © 2026 [**JackStar6677-1**](https://github.com/JackStar6677-1) · [**DrakesCraft Labs**](https://github.com/DrakesCraft-Labs). All Rights Reserved.

This software is **Source-Available** for public inspection and technical audit. Redistribution, commercial repackaging, or unauthorized derivative distribution without explicit written permission from the author is strictly prohibited.
