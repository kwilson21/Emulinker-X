package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.event.ServerEvent;

public class GameStatusChangedEvent implements ServerEvent {
   private KailleraServer server;
   private KailleraGame game;

   public GameStatusChangedEvent(KailleraServer server, KailleraGame game) {
      this.server = server;
      this.game = game;
   }

   public String toString() {
      return "GameStatusChangedEvent";
   }

   public KailleraServer getServer() {
      return this.server;
   }

   public KailleraGame getGame() {
      return this.game;
   }
}
