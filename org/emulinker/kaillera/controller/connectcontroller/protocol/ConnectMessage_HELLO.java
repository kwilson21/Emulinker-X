package org.emulinker.kaillera.controller.connectcontroller.protocol;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.util.EmuUtil;

public class ConnectMessage_HELLO extends ConnectMessage {
   public static final String ID = "HELLO";
   public static final String DESC = "Client Connection Request";
   private String protocol;
   private InetSocketAddress clientSocketAddress;

   public ConnectMessage_HELLO(String protocol) {
      this.protocol = protocol;
   }

   public String getID() {
      return "HELLO";
   }

   public String getDescription() {
      return "Client Connection Request";
   }

   public String getProtocol() {
      return this.protocol;
   }

   public void setClientSocketAddress(InetSocketAddress clientSocketAddress) {
      this.clientSocketAddress = clientSocketAddress;
   }

   public InetSocketAddress getClientSocketAddress() {
      return this.clientSocketAddress;
   }

   public String toString() {
      return "Client Connection Request: protocol: " + this.protocol;
   }

   public int getLength() {
      return "HELLO".length() + this.protocol.length() + 1;
   }

   public void writeTo(ByteBuffer buffer) {
      buffer.put(charset.encode("HELLO"));
      EmuUtil.writeString(buffer, this.protocol, 0, charset);
   }

   public static ConnectMessage parse(String msg) throws MessageFormatException {
      if(msg.length() < "HELLO".length() + 2) {
         throw new MessageFormatException("Invalid message length!");
      } else if(!msg.startsWith("HELLO")) {
         throw new MessageFormatException("Invalid message identifier!");
      } else if(msg.charAt(msg.length() - 1) != 0) {
         throw new MessageFormatException("Invalid message stop byte!");
      } else {
         return new ConnectMessage_HELLO(msg.substring("HELLO".length(), msg.length() - 1));
      }
   }
}
