package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameEvent;

public class UserQuitGameEvent implements GameEvent {
   private KailleraGame game;
   private KailleraUser user;

   public UserQuitGameEvent(KailleraGame game, KailleraUser user) {
      this.game = game;
      this.user = user;
   }

   public String toString() {
      return "UserQuitGameEvent";
   }

   public KailleraGame getGame() {
      return this.game;
   }

   public KailleraUser getUser() {
      return this.user;
   }
}
