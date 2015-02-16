package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;

public class GameInfoEvent implements GameEvent {
   private KailleraGame game;
   private String message;
   private KailleraUser user;

   public GameInfoEvent(KailleraGame game, String message, KailleraUser user) {
      this.game = game;
      this.message = message;
      this.user = user;
   }

   public String toString() {
      return "GameInfoEvent";
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
