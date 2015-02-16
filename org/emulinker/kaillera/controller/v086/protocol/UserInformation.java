package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.util.EmuUtil;

public class UserInformation extends V086Message {
   public static final byte ID = 3;
   public static final String DESC = "User Information";
   private String userName;
   private String clientType;
   private byte connectionType;

   public UserInformation(int messageNumber, String userName, String clientType, byte connectionType) throws MessageFormatException {
      super(messageNumber);
      this.userName = userName;
      this.clientType = clientType;
      if(connectionType >= 1 && connectionType <= 6) {
         this.connectionType = connectionType;
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: connectionType out of acceptable range: " + connectionType);
      }
   }

   public byte getID() {
      return (byte)3;
   }

   public String getDescription() {
      return "User Information";
   }

   public int getBodyLength() {
      return this.userName.length() + this.clientType.length() + 3;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getClientType() {
      return this.clientType;
   }

   public byte getConnectionType() {
      return this.connectionType;
   }

   public String toString() {
      return this.getInfoString() + "[userName=" + this.userName + " clientType=" + this.clientType + " connectionType=" + KailleraUser.CONNECTION_TYPE_NAMES[this.connectionType] + "]";
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      EmuUtil.writeString(buffer, this.clientType, 0, charset);
      buffer.put(this.connectionType);
   }

   public static UserInformation parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 5) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 3) {
            throw new ParseException("Failed byte count validation!");
         } else {
            String clientType = EmuUtil.readString(buffer, 0, charset);
            if(buffer.remaining() < 1) {
               throw new ParseException("Failed byte count validation!");
            } else {
               byte connectionType = buffer.get();
               return new UserInformation(messageNumber, userName, clientType, connectionType);
            }
         }
      }
   }
}
