package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.ACK;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public class ServerACK extends ACK {
   public static final byte ID = 5;
   public static final String DESC = "Server to Client ACK";

   public ServerACK(int messageNumber) throws MessageFormatException {
      super(messageNumber, 0L, 1L, 2L, 3L);
   }

   public byte getID() {
      return (byte)5;
   }

   public String getDescription() {
      return "Server to Client ACK";
   }

   public static ServerACK parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 17) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new MessageFormatException("Invalid Server to Client ACK format: byte 0 = " + EmuUtil.byteToHex(b));
         } else {
            long val1 = UnsignedUtil.getUnsignedInt(buffer);
            long val2 = UnsignedUtil.getUnsignedInt(buffer);
            long val3 = UnsignedUtil.getUnsignedInt(buffer);
            long val4 = UnsignedUtil.getUnsignedInt(buffer);
            if(val1 == 0L && val2 == 1L && val3 == 2L && val4 == 3L) {
               return new ServerACK(messageNumber);
            } else {
               throw new MessageFormatException("Invalid Server to Client ACK format: bytes do not match acceptable format!");
            }
         }
      }
   }
}
