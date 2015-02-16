package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame;

public class CreateGame_Notification extends CreateGame {
   public static final String DESC = "Create Game Notification";

   public CreateGame_Notification(int messageNumber, String userName, String romName, String clientType, int gameID, int val1) throws MessageFormatException {
      super(messageNumber, userName, romName, clientType, gameID, val1);
   }

   public byte getID() {
      return (byte)10;
   }

   public String getDescription() {
      return "Create Game Notification";
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.getUserName() + " romName=" + this.getRomName() + " clientType=" + this.getClientType() + " gameID=" + this.getGameID() + " val1=" + this.getVal1() + "]";
   }
}
