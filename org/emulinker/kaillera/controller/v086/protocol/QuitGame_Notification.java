package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame;

public class QuitGame_Notification extends QuitGame {
   public static final String DESC = "Quit Game Notification";

   public QuitGame_Notification(int messageNumber, String userName, int userID) throws MessageFormatException {
      super(messageNumber, userName, userID);
   }

   public String getDescription() {
      return "Quit Game Notification";
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.getUserName() + " userID=" + this.getUserID() + "]";
   }
}
