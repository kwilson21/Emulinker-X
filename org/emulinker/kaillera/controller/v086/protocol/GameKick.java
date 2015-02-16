package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.UnsignedUtil;

public class GameKick extends V086Message {
   public static final byte ID = 15;
   public static final String DESC = "Game Kick Request";
   private int userID;

   public GameKick(int messageNumber, int userID) throws MessageFormatException {
      super(messageNumber);
      if(userID >= 0 && userID <= '\uffff') {
         this.userID = userID;
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userID out of acceptable range: " + userID);
      }
   }

   public byte getID() {
      return (byte)15;
   }

   public String getDescription() {
      return "Game Kick Request";
   }

   public int getUserID() {
      return this.userID;
   }

   public String toString() {
      return this.getInfoString() + "[userID=" + this.userID + "]";
   }

   public int getBodyLength() {
      return 3;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
      UnsignedUtil.putUnsignedShort(buffer, this.userID);
   }

   public static GameKick parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 3) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         return new GameKick(messageNumber, UnsignedUtil.getUnsignedShort(buffer));
      }
   }
}
