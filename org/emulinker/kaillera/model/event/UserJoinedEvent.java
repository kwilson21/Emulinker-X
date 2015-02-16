package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.ServerEvent;

public class UserJoinedEvent implements ServerEvent {
   private KailleraServer server;
   private KailleraUser user;

   public UserJoinedEvent(KailleraServer server, KailleraUser user) {
      this.server = server;
      this.user = user;
   }

   public String toString() {
      return "UserJoinedEvent";
   }

   public KailleraServer getServer() {
      return this.server;
   }

   public KailleraUser getUser() {
      return this.user;
   }
}
