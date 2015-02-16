package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public class CloseGame extends V086Message {
   public static final byte ID = 16;
   public static final String DESC = "Close Game";
   private int gameID;
   private int val1;

   public CloseGame(int messageNumber, int gameID, int val1) throws MessageFormatException {
      super(messageNumber);
      if(gameID >= 0 && gameID <= '\uffff') {
         if(val1 >= 0 && val1 <= '\uffff') {
            this.gameID = gameID;
            this.val1 = val1;
         } else {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: val1 out of acceptable range: " + val1);
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: gameID out of acceptable range: " + gameID);
      }
   }

   public byte getID() {
      return (byte)16;
   }

   public String getDescription() {
      return "Close Game";
   }

   public int getGameID() {
      return this.gameID;
   }

   public int getVal1() {
      return this.val1;
   }

   public String toString() {
      return this.getInfoString() + "[gameID=" + this.gameID + " val1=" + this.val1 + "]";
   }

   public int getBodyLength() {
      return 5;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
      UnsignedUtil.putUnsignedShort(buffer, this.gameID);
      UnsignedUtil.putUnsignedShort(buffer, this.val1);
   }

   public static CloseGame parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 5) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new MessageFormatException("Invalid Close Game format: byte 0 = " + EmuUtil.byteToHex(b));
         } else {
            int gameID = UnsignedUtil.getUnsignedShort(buffer);
            int val1 = UnsignedUtil.getUnsignedShort(buffer);
            return new CloseGame(messageNumber, gameID, val1);
         }
      }
   }
}
