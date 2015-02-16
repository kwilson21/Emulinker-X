package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.ACK;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public class ClientACK extends ACK {
   public static final byte ID = 6;
   public static final String DESC = "Client to Server ACK";

   public ClientACK(int messageNumber) throws MessageFormatException {
      super(messageNumber, 0L, 1L, 2L, 3L);
   }

   public byte getID() {
      return (byte)6;
   }

   public String getDescription() {
      return "Client to Server ACK";
   }

   public static ClientACK parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 17) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new MessageFormatException("Invalid Client to Server ACK format: byte 0 = " + EmuUtil.byteToHex(b));
         } else {
            long val1 = UnsignedUtil.getUnsignedInt(buffer);
            long val2 = UnsignedUtil.getUnsignedInt(buffer);
            long val3 = UnsignedUtil.getUnsignedInt(buffer);
            long val4 = UnsignedUtil.getUnsignedInt(buffer);
            return new ClientACK(messageNumber);
         }
      }
   }
}
