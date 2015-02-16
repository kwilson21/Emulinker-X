package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame;

public class JoinGame_Notification extends JoinGame {
   public static final String DESC = "Join Game Notification";

   public JoinGame_Notification(int messageNumber, int gameID, int val1, String userName, long ping, int userID, byte connectionType) throws MessageFormatException {
      super(messageNumber, gameID, val1, userName, ping, userID, connectionType);
      if(userName.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userName.length() == 0");
      }
   }

   public byte getID() {
      return (byte)12;
   }

   public String getDescription() {
      return "Join Game Notification";
   }

   public String toString() {
      return this.getInfoString() + "[gameID=" + this.getGameID() + " val1=" + this.getVal1() + " userName=" + this.getUserName() + " ping=" + this.getPing() + " userID=" + this.getUserID() + " connectionType=" + this.getConnectionType() + "]";
   }
}
