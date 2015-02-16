package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.GameChat;

public class GameChat_Request extends GameChat {
   public static final String DESC = "In-Game Chat Request";

   public GameChat_Request(int messageNumber, String message) throws MessageFormatException {
      super(messageNumber, "", message);
   }

   public String getDescription() {
      return "In-Game Chat Request";
   }

   public String toString() {
      return this.getInfoString() + "[message=" + this.getMessage() + "]";
   }
}
