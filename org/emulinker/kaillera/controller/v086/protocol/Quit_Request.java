package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.Quit;

public class Quit_Request extends Quit {
   public static final String DESC = "User Quit Request";

   public Quit_Request(int messageNumber, String message) throws MessageFormatException {
      super(messageNumber, "", '\uffff', message);
   }

   public String getDescription() {
      return "User Quit Request";
   }

   public String toString() {
      return this.getInfoString() + "[message=" + this.getMessage() + "]";
   }
}
