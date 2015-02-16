package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.Chat;

public class Chat_Request extends Chat {
   public static final String DESC = "Chat Request";

   public Chat_Request(int messageNumber, String message) throws MessageFormatException {
      super(messageNumber, "", message);
   }

   public String getDescription() {
      return "Chat Request";
   }

   public String toString() {
      return this.getInfoString() + "[message=" + this.getMessage() + "]";
   }
}
