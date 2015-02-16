package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.PlayerDrop;

public class PlayerDrop_Notification extends PlayerDrop {
   public static final String DESC = "Player Drop Notification";

   public PlayerDrop_Notification(int messageNumber, String userName, byte playerNumber) throws MessageFormatException {
      super(messageNumber, userName, playerNumber);
      if(userName.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userName.length == 0");
      }
   }

   public String getDescription() {
      return "Player Drop Notification";
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.getUserName() + " playerNumber=" + this.getPlayerNumber() + "]";
   }
}
