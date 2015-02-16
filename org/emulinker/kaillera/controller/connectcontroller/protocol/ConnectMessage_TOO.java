package org.emulinker.kaillera.controller.connectcontroller.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;

public class ConnectMessage_TOO extends ConnectMessage {
   public static final String ID = "TOO";
   public static final String DESC = "Server Full Response";

   public String getID() {
      return "TOO";
   }

   public String getDescription() {
      return "Server Full Response";
   }

   public String toString() {
      return "Server Full Response";
   }

   public int getLength() {
      return "TOO".length() + 1;
   }

   public void writeTo(ByteBuffer buffer) {
      buffer.put(charset.encode("TOO"));
      buffer.put((byte)0);
   }

   public static ConnectMessage parse(String msg) throws MessageFormatException {
      if(msg.length() != "TOO".length() + 1) {
         throw new MessageFormatException("Invalid message length!");
      } else if(!msg.startsWith("TOO")) {
         throw new MessageFormatException("Invalid message identifier!");
      } else if(msg.charAt(msg.length() - 1) != 0) {
         throw new MessageFormatException("Invalid message stop byte!");
      } else {
         return new ConnectMessage_TOO();
      }
   }
}
