package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public abstract class CreateGame extends V086Message {
   public static final byte ID = 10;
   private String userName;
   private String romName;
   private String clientType;
   private int gameID;
   private int val1;

   public CreateGame(int messageNumber, String userName, String romName, String clientType, int gameID, int val1) throws MessageFormatException {
      super(messageNumber);
      if(romName.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: romName.length == 0");
      } else if(gameID >= 0 && gameID <= '\uffff') {
         if(val1 != 0 && val1 != '\uffff') {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: val1 out of acceptable range: " + val1);
         } else {
            this.userName = userName;
            this.romName = romName;
            this.clientType = clientType;
            this.gameID = gameID;
            this.val1 = val1;
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: gameID out of acceptable range: " + gameID);
      }
   }

   public byte getID() {
      return (byte)10;
   }

   public abstract String getDescription();

   public String getUserName() {
      return this.userName;
   }

   public String getRomName() {
      return this.romName;
   }

   public String getClientType() {
      return this.clientType;
   }

   public int getGameID() {
      return this.gameID;
   }

   public int getVal1() {
      return this.val1;
   }

   public abstract String toString();

   public int getBodyLength() {
      return this.userName.length() + this.romName.length() + this.clientType.length() + 7;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      EmuUtil.writeString(buffer, this.romName, 0, charset);
      EmuUtil.writeString(buffer, this.clientType, 0, charset);
      UnsignedUtil.putUnsignedShort(buffer, this.gameID);
      UnsignedUtil.putUnsignedShort(buffer, this.val1);
   }

   public static CreateGame parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 8) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String userName = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 6) {
            throw new ParseException("Failed byte count validation!");
         } else {
            String romName = EmuUtil.readString(buffer, 0, charset);
            if(buffer.remaining() < 5) {
               throw new ParseException("Failed byte count validation!");
            } else {
               String clientType = EmuUtil.readString(buffer, 0, charset);
               if(buffer.remaining() < 4) {
                  throw new ParseException("Failed byte count validation!");
               } else {
                  int gameID = UnsignedUtil.getUnsignedShort(buffer);
                  int val1 = UnsignedUtil.getUnsignedShort(buffer);
                  return (CreateGame)(userName.length() == 0 && gameID == '\uffff' && val1 == '\uffff'?new CreateGame_Request(messageNumber, romName):new CreateGame_Notification(messageNumber, userName, romName, clientType, gameID, val1));
               }
            }
         }
      }
   }
}
