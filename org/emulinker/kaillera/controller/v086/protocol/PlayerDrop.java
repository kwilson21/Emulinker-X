package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.PlayerDrop_Notification;
import org.emulinker.kaillera.controller.v086.protocol.PlayerDrop_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;

public abstract class PlayerDrop extends V086Message {
   public static final byte ID = 20;
   private String userName;
   private byte playerNumber;

   public PlayerDrop(int messageNumber, String userName, byte playerNumber) throws MessageFormatException {
      super(messageNumber);
      if(playerNumber >= 0 && playerNumber <= 255) {
         this.userName = userName;
         this.playerNumber = playerNumber;
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: playerNumber out of acceptable range: " + playerNumber);
      }
   }

   public byte getID() {
      return (byte)20;
   }

   public abstract String getDescription();

   public String getUserName() {
      return this.userName;
   }

   public byte getPlayerNumber() {
      return this.playerNumber;
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.userName + " playerNumber=" + this.playerNumber + "]";
   }

   public int getBodyLength() {
      return this.userName.length() + 2;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      buffer.put(this.playerNumber);
   }

   public static PlayerDrop parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 2) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         byte playerNumber = buffer.get();
         return (PlayerDrop)(userName.length() == 0 && playerNumber == 0?new PlayerDrop_Request(messageNumber):new PlayerDrop_Notification(messageNumber, userName, playerNumber));
      }
   }
}
