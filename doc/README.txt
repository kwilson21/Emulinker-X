EmuLinker Kaillera Server - http://www.emulinker.org
====================================================

  *** Please see doc/quickstart.txt for install and setup instructions. ***

EmuLinker is a high-performance, fully featured, secure, open-source network 
server that supports the Kaillera network protocol.

Kaillera is a client DLL and network server that emulators can implement to 
enable networked play.

The latest documentation and details can be found on the official EmuLinker 
website at http://www.emulinker.org

This software is licensed under the terms you may find in the file named 
"LICENSE.txt" in the doc directory.

0.99.24 (04/03/2006)
====================

New Features

- Support for domain names in access.cfg; allows easier configuration for people 
  without a static ip address
- New admin command /clear to remove temp bans
- New Autofire Detector feature can alert players about possible cheating

Bug Fixes

- Server startup .bat file in last release was mistakenly pre-configured for JRockit
- Language file was not discovered properly for non-english locales
- Fixed minor player state bug

0.99.17 (02/17/2006)
====================

New Features

- Support for international languages and customized prompts
- New online admin commands including /closegame and /version
- Improved game desynch handling

Bug Fixes

- Fixed thread deadlock issues that caused previous version to lockup occasionally
- Server status is now sent in multiple packets, fixing the inherent Kaillera 
  protocol bug that caused login problems for some users.
- Fixed vulnerability where a specially constructed packet would leave a ghost 
  game on the server, eventually resulting in a denial of service
- Fixed bug causing packets with less than 3 messages to be marked as 3
- Various other minor bug fixes and tweaks

0.98.5 (10/30/2005)
===================

Bug Fixes Only

- Doubled default V086Controller buffer size to 8192; 4096 was too small for
  servers with more than approximately 75 users online.
- Fixed multiple places where thread deadlock could occur and cause server
  lockup, although the possibility was very rare.

0.98.2 (10/22/2005)
===================

New Features

- Noticably reduced game play delay and lagginess as a result of better network 
  packet handling code.
  
Bug Fixes

- Fixed occasional lockup in 0.97.6 during heavy load caused by thread deadlock.
- Fixed message count bug in 0.97.6.  Packets were always marked as size 18.

0.97.6 (10/14/2005)
===================

New Features

- Reduced game play delay and lagginess as a result of new more efficient 
  game data passing algorithms.
- Eliminated emulator lockup issues when the game starter closes the game 
  abruptly and others are still playing.
- Improved multiplayer handling for 3 or more players.  Games no longer 
  desynch when one player leaves but others are still playing.
- Games now remember users that were kicked and will not allow rejoining.
- Added MaxGames parameter to help control bandwidth usage.
- Changed allowed Connection Types so server owners can disable 1 or more 
  specific connection types instead of just setting a max.
- Server owners can now run servers that allow only specific games or emulators 
  using the new access manager's game and emulator filters.
- New access manager allows server owners to assign 3 separate user access 
  levels: normal, elevated, and admin.  Elevated level users have fewer 
  restrictions and the ability to join a full server.  Admin users 
  additionally have access to special server control commands such as: /kick, 
  /ban, /silence, /announce, etc.
- Server uses a configurable limited range of ports instead of any available 
  port on the machine, making firewall configuration easier and more secure.
- Configuration files are now standard name=value properties files instead of 
  XML, to make configuration less error-prone.

Bug Fixes

- Fixed "Too Many Open Files" bug in version 0.96 caused by the master server 
  list updater leaving sockets open.
 
0.96 (05/07/2005)
=================

First release

- All user inputs are bounds and type checked at a low level for high security.
- Server, game, and user state is strongly held; no user action may violate a 
  state to stop all known hacks from the original Kaillera server.
- Aggressive spam control
- Chat message and game name filtering to prevent hacks
- Does not allow interruption of games in progress
- Duplicate login protection
- Game desynch detection and notification
- Inactivity timeout to prevent extended user idling
- Built-in access manager for kicking/banning users by name and address
- Privileged login access for server owner and designated adminstrators
- Master server list updater
- Extensive logging of all server activity with automatic log file rotation
