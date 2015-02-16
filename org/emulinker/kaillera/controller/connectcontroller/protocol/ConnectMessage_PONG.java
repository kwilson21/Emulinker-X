package org.emulinker.kaillera.controller.connectcontroller.protocol;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;

public class ConnectMessage_PONG extends ConnectMessage {
   public static final String ID = "PONG";
   public static final String DESC = "Server Pong";
   private InetSocketAddress clientSocketAddress;

   public String getID() {
      return "PONG";
   }

   public String getDescription() {
      return "Server Pong";
   }

   public void setClientSocketAddress(InetSocketAddress clientSocketAddress) {
      this.clientSocketAddress = clientSocketAddress;
   }

   public InetSocketAddress getClientSocketAddress() {
      return this.clientSocketAddress;
   }

   public String toString() {
      return "Server Pong";
   }

   public int getLength() {
      return "PONG".length() + 1;
   }

   public void writeTo(ByteBuffer buffer) {
      buffer.put(charset.encode("PONG"));
      buffer.put((byte)0);
   }

   public static ConnectMessage parse(String msg) throws MessageFormatException {
      if(msg.length() != 5) {
         throw new MessageFormatException("Invalid message length!");
      } else if(!msg.startsWith("PONG")) {
         throw new MessageFormatException("Invalid message identifier!");
      } else if(msg.charAt(msg.length() - 1) != 0) {
         throw new MessageFormatException("Invalid message stop byte!");
      } else {
         return new ConnectMessage_PONG();
      }
   }
}
