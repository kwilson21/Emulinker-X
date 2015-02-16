package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;

public class AllReady extends V086Message {
   public static final byte ID = 21;
   public static final String DESC = "All Ready Signal";

   public AllReady(int messageNumber) throws MessageFormatException {
      super(messageNumber);
   }

   public byte getID() {
      return (byte)21;
   }

   public String getDescription() {
      return "All Ready Signal";
   }

   public String toString() {
      return this.getInfoString();
   }

   public int getBodyLength() {
      return 1;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
   }

   public static AllReady parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 1) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new MessageFormatException("Invalid All Ready Signal format: byte 0 = " + EmuUtil.byteToHex(b));
         } else {
            return new AllReady(messageNumber);
         }
      }
   }
}
