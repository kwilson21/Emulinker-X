package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public abstract class QuitGame extends V086Message {
   public static final byte ID = 11;
   private String userName;
   private int userID;

   public QuitGame(int messageNumber, String userName, int userID) throws MessageFormatException {
      super(messageNumber);
      if(userID >= 0 && userID <= '\uffff') {
         this.userName = userName;
         this.userID = userID;
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: userID out of acceptable range: " + userID);
      }
   }

   public byte getID() {
      return (byte)11;
   }

   public abstract String getDescription();

   public String getUserName() {
      return this.userName;
   }

   public int getUserID() {
      return this.userID;
   }

   public int getBodyLength() {
      return this.userName.length() + 3;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      UnsignedUtil.putUnsignedShort(buffer, this.userID);
   }

   public static QuitGame parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 3) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 2) {
            throw new ParseException("Failed byte count validation!");
         } else {
            int userID = UnsignedUtil.getUnsignedShort(buffer);
            return (QuitGame)(userName.length() == 0 && userID == '\uffff'?new QuitGame_Request(messageNumber):new QuitGame_Notification(messageNumber, userName, userID));
         }
      }
   }
}
