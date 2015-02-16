package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.StartGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.StartGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.UnsignedUtil;

public abstract class StartGame extends V086Message {
   public static final byte ID = 17;
   private int val1;
   private short playerNumber;
   private short numPlayers;

   public StartGame(int messageNumber, short playerNumber, short numPlayers) throws MessageFormatException {
      this(messageNumber, 1, playerNumber, numPlayers);
   }

   public StartGame(int messageNumber, int val1, short playerNumber, short numPlayers) throws MessageFormatException {
      super(messageNumber);
      if(val1 >= 0 && val1 <= '\uffff') {
         if(playerNumber >= 0 && playerNumber <= 255) {
            if(numPlayers >= 0 && numPlayers <= 255) {
               this.val1 = val1;
               this.playerNumber = playerNumber;
               this.numPlayers = numPlayers;
            } else {
               throw new MessageFormatException("Invalid " + this.getDescription() + " format: numPlayers out of acceptable range: " + numPlayers);
            }
         } else {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: playerNumber out of acceptable range: " + playerNumber);
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: val1 out of acceptable range: " + val1);
      }
   }

   public byte getID() {
      return (byte)17;
   }

   public abstract String getDescription();

   public int getVal1() {
      return this.val1;
   }

   public short getPlayerNumber() {
      return this.playerNumber;
   }

   public short getNumPlayers() {
      return this.numPlayers;
   }

   public abstract String toString();

   public int getBodyLength() {
      return 5;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
      UnsignedUtil.putUnsignedShort(buffer, this.val1);
      UnsignedUtil.putUnsignedByte(buffer, this.playerNumber);
      UnsignedUtil.putUnsignedByte(buffer, this.numPlayers);
   }

   public static StartGame parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 5) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new ParseException("Failed byte count validation!");
         } else {
            int val1 = UnsignedUtil.getUnsignedShort(buffer);
            short playerNumber = UnsignedUtil.getUnsignedByte(buffer);
            short numPlayers = UnsignedUtil.getUnsignedByte(buffer);
            return (StartGame)(val1 == '\uffff' && playerNumber == 255 && numPlayers == 255?new StartGame_Request(messageNumber):new StartGame_Notification(messageNumber, val1, playerNumber, numPlayers));
         }
      }
   }
}
