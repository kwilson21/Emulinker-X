package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.ServerEvent;

public class ChatEvent implements ServerEvent {
   private KailleraServer server;
   private KailleraUser user;
   private String message;

   public ChatEvent(KailleraServer server, KailleraUser user, String message) {
      this.server = server;
      this.user = user;
      this.message = message;
   }

   public String toString() {
      return "ChatEvent";
   }

   public KailleraServer getServer() {
      return this.server;
   }

   public KailleraUser getUser() {
      return this.user;
   }

   public String getMessage() {
      return this.message;
   }
}
