package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.Quit;

public class Quit_Notification extends Quit {
   public static final String DESC = "User Quit Notification";

   public Quit_Notification(int messageNumber, String userName, int userID, String message) throws MessageFormatException {
      super(messageNumber, userName, userID, message);
      if(userName.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userName.length == 0, (userID = " + userID + ")");
      }
   }

   public String getDescription() {
      return "User Quit Notification";
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.getUserName() + " userID=" + this.getUserID() + " message=" + this.getMessage() + "]";
   }
}
