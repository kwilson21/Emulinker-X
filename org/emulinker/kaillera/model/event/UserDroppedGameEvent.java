package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;

public class UserDroppedGameEvent implements GameEvent {
   private KailleraGame game;
   private KailleraUser user;
   private int playerNumber;

   public UserDroppedGameEvent(KailleraGame game, KailleraUser user, int playerNumber) {
      this.game = game;
      this.user = user;
      this.playerNumber = playerNumber;
   }

   public String toString() {
      return "UserDroppedGameEvent";
   }

   public KailleraGame getGame() {
      return this.game;
   }

   public KailleraUser getUser() {
      return this.user;
   }

   public int getPlayerNumber() {
      return this.playerNumber;
   }
}
