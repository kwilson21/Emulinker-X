package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.event.ServerEvent;

public class GameCreatedEvent implements ServerEvent {
   private KailleraServer server;
   private KailleraGame game;

   public GameCreatedEvent(KailleraServer server, KailleraGame game) {
      this.server = server;
      this.game = game;
   }

   public String toString() {
      return "GameCreatedEvent";
   }

   public KailleraServer getServer() {
      return this.server;
   }

   public KailleraGame getGame() {
      return this.game;
   }
}
