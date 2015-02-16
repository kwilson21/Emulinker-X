package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame;

public class JoinGame_Request extends JoinGame {
   public static final String DESC = "Join Game Request";

   public JoinGame_Request(int messageNumber, int gameID, byte connectionType) throws MessageFormatException {
      super(messageNumber, gameID, 0, "", 0L, '\uffff', connectionType);
   }

   public byte getID() {
      return (byte)12;
   }

   public String getDescription() {
      return "Join Game Request";
   }

   public String toString() {
      return this.getInfoString() + "[gameID=" + this.getGameID() + " connectionType=" + this.getConnectionType() + "]";
   }
}
