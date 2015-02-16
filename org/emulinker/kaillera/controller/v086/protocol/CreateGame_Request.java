package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame;

public class CreateGame_Request extends CreateGame {
   public static final String DESC = "Create Game Request";

   public CreateGame_Request(int messageNumber, String romName) throws MessageFormatException {
      super(messageNumber, "", romName, "", '\uffff', '\uffff');
   }

   public byte getID() {
      return (byte)10;
   }

   public String getDescription() {
      return "Create Game Request";
   }

   public String toString() {
      return this.getInfoString() + "[romName=" + this.getRomName() + "]";
   }
}
