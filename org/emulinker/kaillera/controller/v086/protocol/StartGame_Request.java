package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.StartGame;

public class StartGame_Request extends StartGame {
   public static final String DESC = "Start Game Request";

   public StartGame_Request(int messageNumber) throws MessageFormatException {
      super(messageNumber, '\uffff', (short)255, (short)255);
   }

   public String getDescription() {
      return "Start Game Request";
   }

   public String toString() {
      return this.getInfoString();
   }
}
