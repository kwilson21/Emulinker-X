package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.GameChat;

public class GameChat_Notification extends GameChat {
   public static final String DESC = "In-Game Chat Notification";

   public GameChat_Notification(int messageNumber, String userName, String message) throws MessageFormatException {
      super(messageNumber, userName, message);
   }

   public String getDescription() {
      return "In-Game Chat Notification";
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.getUserName() + " message: " + this.getMessage() + "]";
   }
}
