package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.event.ServerEvent;

public class GameClosedEvent implements ServerEvent {
   private KailleraServer server;
   private KailleraGame game;

   public GameClosedEvent(KailleraServer server, KailleraGame game) {
      this.server = server;
      this.game = game;
   }

   public String toString() {
      return "GameClosedEvent";
   }

   public KailleraServer getServer() {
      return this.server;
   }

   public KailleraGame getGame() {
      return this.game;
   }
}
