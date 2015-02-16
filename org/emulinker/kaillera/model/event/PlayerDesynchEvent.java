package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;

public class PlayerDesynchEvent implements GameEvent {
   private KailleraGame game;
   private KailleraUser user;
   private String message;

   public PlayerDesynchEvent(KailleraGame game, KailleraUser user, String message) {
      this.game = game;
      this.user = user;
      this.message = message;
   }

   public String toString() {
      return "GameDesynchEvent";
   }

   public KailleraGame getGame() {
      return this.game;
   }

   public KailleraUser getUser() {
      return this.user;
   }

   public String getMessage() {
      return this.message;
   }
}
