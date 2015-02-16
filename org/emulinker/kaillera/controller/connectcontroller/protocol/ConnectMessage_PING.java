package org.emulinker.kaillera.controller.connectcontroller.protocol;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;

public class ConnectMessage_PING extends ConnectMessage {
   public static final String ID = "PING";
   public static final String DESC = "Client Ping";
   private InetSocketAddress clientSocketAddress;

   public String getID() {
      return "PING";
   }

   public String getDescription() {
      return "Client Ping";
   }

   public void setClientSocketAddress(InetSocketAddress clientSocketAddress) {
      this.clientSocketAddress = clientSocketAddress;
   }

   public InetSocketAddress getClientSocketAddress() {
      return this.clientSocketAddress;
   }

   public String toString() {
      return "Client Ping";
   }

   public int getLength() {
      return "PING".length() + 1;
   }

   public void writeTo(ByteBuffer buffer) {
      buffer.put(charset.encode("PING"));
      buffer.put((byte)0);
   }

   public static ConnectMessage parse(String msg) throws MessageFormatException {
      if(msg.length() != 5) {
         throw new MessageFormatException("Invalid message length!");
      } else if(!msg.startsWith("PING")) {
         throw new MessageFormatException("Invalid message identifier!");
      } else if(msg.charAt(msg.length() - 1) != 0) {
         throw new MessageFormatException("Invalid message stop byte!");
      } else {
         return new ConnectMessage_PING();
      }
   }
}
