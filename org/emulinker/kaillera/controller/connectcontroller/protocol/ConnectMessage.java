package org.emulinker.kaillera.controller.connectcontroller.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage_HELLO;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage_HELLOD00D;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage_PING;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage_PONG;
import org.emulinker.kaillera.controller.connectcontroller.protocol.ConnectMessage_TOO;
import org.emulinker.kaillera.controller.messaging.ByteBufferMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.util.EmuUtil;

public abstract class ConnectMessage extends ByteBufferMessage {
   public static Charset charset = Charset.forName("ISO-8859-1");

   protected abstract String getID();

   public static ConnectMessage parse(ByteBuffer buffer) throws MessageFormatException {
      String messageStr = null;

      try {
         CharsetDecoder e = charset.newDecoder();
         messageStr = e.decode(buffer).toString();
      } catch (CharacterCodingException var3) {
         throw new MessageFormatException("Invalid bytes received: failed to decode to a string!", var3);
      }

      if(messageStr.startsWith("TOO")) {
         return ConnectMessage_TOO.parse(messageStr);
      } else if(messageStr.startsWith("HELLOD00D")) {
         return ConnectMessage_HELLOD00D.parse(messageStr);
      } else if(messageStr.startsWith("HELLO")) {
         return ConnectMessage_HELLO.parse(messageStr);
      } else if(messageStr.startsWith("PING")) {
         return ConnectMessage_PING.parse(messageStr);
      } else if(messageStr.startsWith("PONG")) {
         return ConnectMessage_PONG.parse(messageStr);
      } else {
         buffer.rewind();
         throw new MessageFormatException("Unrecognized connect message: " + EmuUtil.dumpBuffer(buffer));
      }
   }
}
