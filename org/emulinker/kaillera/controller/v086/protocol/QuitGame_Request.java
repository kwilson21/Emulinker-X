package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame;

public class QuitGame_Request extends QuitGame {
   public static final String DESC = "Quit Game Request";

   public QuitGame_Request(int messageNumber) throws MessageFormatException {
      super(messageNumber, "", '\uffff');
   }

   public String getDescription() {
      return "Quit Game Request";
   }

   public String toString() {
      return this.getInfoString();
   }
}
