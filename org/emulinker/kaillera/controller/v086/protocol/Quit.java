package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.Quit_Notification;
import org.emulinker.kaillera.controller.v086.protocol.Quit_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public abstract class Quit extends V086Message {
   public static final byte ID = 1;
   private String userName;
   private int userID;
   private String message;

   public Quit(int messageNumber, String userName, int userID, String message) throws MessageFormatException {
      super(messageNumber);
      if(userID >= 0 && userID <= '\uffff') {
         if(message == null) {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: message == null!");
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
      return (byte)1;
   }

   public abstract String getDescription();

   public String getUserName() {
      return this.userName;
   }

   public int getUserID() {
      return this.userID;
   }

   public String getMessage() {
      return this.message;
   }

   public abstract String toString();

   public int getBodyLength() {
      return this.userName.length() + this.message.length() + 4;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      UnsignedUtil.putUnsignedShort(buffer, this.userID);
      EmuUtil.writeString(buffer, this.message, 0, charset);
   }

   public static Quit parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 5) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 3) {
            throw new ParseException("Failed byte count validation!");
         } else {
            int userID = UnsignedUtil.getUnsignedShort(buffer);
            String message = EmuUtil.readString(buffer, 0, charset);
            return (Quit)(userName.length() == 0 && userID == '\uffff'?new Quit_Request(messageNumber, message):new Quit_Notification(messageNumber, userName, userID, message));
         }
      }
   }
}
