package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public class UserJoined extends V086Message {
   public static final byte ID = 2;
   public static final String DESC = "User Joined";
   private String userName;
   private int userID;
   private long ping;
   private byte connectionType;

   public UserJoined(int messageNumber, String userName, int userID, long ping, byte connectionType) throws MessageFormatException {
      super(messageNumber);
      if(userName.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userName.length == 0, (userID = " + userID + ")");
      } else if(userID >= 0 && userID <= '\uffff') {
         if(ping >= 0L && ping <= 2048L) {
            if(connectionType >= 1 && connectionType <= 6) {
               this.userName = userName;
               this.userID = userID;
               this.ping = ping;
               this.connectionType = connectionType;
            } else {
               throw new MessageFormatException("Invalid " + this.getDescription() + " format: connectionType out of acceptable range: " + connectionType);
            }
         } else {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: ping out of acceptable range: " + ping);
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userID out of acceptable range: " + userID);
      }
   }

   public byte getID() {
      return (byte)2;
   }

   public String getDescription() {
      return "User Joined";
   }

   public String getUserName() {
      return this.userName;
   }

   public int getUserID() {
      return this.userID;
   }

   public long getPing() {
      return this.ping;
   }

   public byte getConnectionType() {
      return this.connectionType;
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.userName + " userID=" + this.userID + " ping=" + this.ping + " connectionType=" + KailleraUser.CONNECTION_TYPE_NAMES[this.connectionType] + "]";
   }

   public int getBodyLength() {
      return this.userName.length() + 8;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      UnsignedUtil.putUnsignedShort(buffer, this.userID);
      UnsignedUtil.putUnsignedInt(buffer, this.ping);
      buffer.put(this.connectionType);
   }

   public static UserJoined parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 9) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 7) {
            throw new ParseException("Failed byte count validation!");
         } else {
            int userID = UnsignedUtil.getUnsignedShort(buffer);
            long ping = UnsignedUtil.getUnsignedInt(buffer);
            byte connectionType = buffer.get();
            return new UserJoined(messageNumber, userName, userID, ping, connectionType);
         }
      }
   }
}
