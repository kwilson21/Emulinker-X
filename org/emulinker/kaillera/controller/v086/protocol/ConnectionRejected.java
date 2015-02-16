package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public class ConnectionRejected extends V086Message {
   public static final byte ID = 22;
   public static final String DESC = "Connection Rejected";
   private String userName;
   private int userID;
   private String message;

   public ConnectionRejected(int messageNumber, String userName, int userID, String message) throws MessageFormatException {
      super(messageNumber);
      if(userName.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userName.length == 0");
      } else if(userID >= 0 && userID <= '\uffff') {
         if(message.length() == 0) {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: message.length == 0");
         } else {
            this.userName = userName;
            this.userID = userID;
            this.message = message;
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userID out of acceptable range: " + userID);
      }
   }

   public byte getID() {
      return (byte)22;
   }

   public String getDescription() {
      return "Connection Rejected";
   }

   public String getUserName() {
      return this.userName;
   }

   public int getUserID() {
      return this.userID;
   }

   public String getMessage() {
      return this.message;
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.userName + " userID=" + this.userID + " message=" + this.message + "]";
   }

   public int getBodyLength() {
      return this.userName.length() + this.message.length() + 4;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      UnsignedUtil.putUnsignedShort(buffer, this.userID);
      EmuUtil.writeString(buffer, this.message, 0, charset);
   }

   public static ConnectionRejected parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 6) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 4) {
            throw new ParseException("Failed byte count validation!");
         } else {
            int userID = UnsignedUtil.getUnsignedShort(buffer);
            if(buffer.remaining() < 2) {
               throw new ParseException("Failed byte count validation!");
            } else {
               String message = EmuUtil.readString(buffer, 0, charset);
               return new ConnectionRejected(messageNumber, userName, userID, message);
            }
         }
      }
   }
}
