package org.emulinker.kaillera.controller.connectcontroller.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.util.EmuUtil;

public class ConnectMessage_HELLOD00D extends ConnectMessage {
   public static final String ID = "HELLOD00D";
   public static final String DESC = "Server Connection Response";
   private int port;

   public ConnectMessage_HELLOD00D(int port) {
      this.port = port;
   }

   public String getID() {
      return "HELLOD00D";
   }

   public String getDescription() {
      return "Server Connection Response";
   }

   public int getPort() {
      return this.port;
   }

   public String toString() {
      return "Server Connection Response: port: " + this.getPort();
   }

   public int getLength() {
      return "HELLOD00D".length() + Integer.toString(this.port).length() + 1;
   }

   public void writeTo(ByteBuffer buffer) {
      buffer.put(charset.encode("HELLOD00D"));
      EmuUtil.writeString(buffer, Integer.toString(this.port), 0, charset);
   }

   public static ConnectMessage parse(String msg) throws MessageFormatException {
      if(msg.length() < "HELLOD00D".length() + 2) {
         throw new MessageFormatException("Invalid message length!");
      } else if(!msg.startsWith("HELLOD00D")) {
         throw new MessageFormatException("Invalid message identifier!");
      } else if(msg.charAt(msg.length() - 1) != 0) {
         throw new MessageFormatException("Invalid message stop byte!");
      } else {
         try {
            int e = Integer.parseInt(msg.substring("HELLOD00D".length(), msg.length() - 1));
            return new ConnectMessage_HELLOD00D(e);
         } catch (NumberFormatException var2) {
            throw new MessageFormatException("Invalid port number!");
         }
      }
   }
}
