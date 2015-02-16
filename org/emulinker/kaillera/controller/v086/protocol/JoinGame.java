package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame_Notification;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame_Request;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public abstract class JoinGame extends V086Message {
   public static final byte ID = 12;
   private int gameID;
   private int val1;
   private String userName;
   private long ping;
   private int userID;
   private byte connectionType;

   public JoinGame(int messageNumber, int gameID, int val1, String userName, long ping, int userID, byte connectionType) throws MessageFormatException {
      super(messageNumber);
      if(gameID >= 0 && gameID <= '\uffff') {
         if(ping >= 0L && ping <= 65535L) {
            if(userID >= 0 && userID <= '\uffff') {
               if(connectionType >= 1 && connectionType <= 6) {
                  this.gameID = gameID;
                  this.val1 = val1;
                  this.userName = userName;
                  this.ping = ping;
                  this.userID = userID;
                  this.connectionType = connectionType;
               } else {
                  throw new MessageFormatException("Invalid " + this.getDescription() + " format: connectionType out of acceptable range: " + connectionType);
               }
            } else {
               throw new MessageFormatException("Invalid " + this.getDescription() + " format: userID out of acceptable range: " + userID);
            }
         } else {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: ping out of acceptable range: " + ping);
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: gameID out of acceptable range: " + gameID);
      }
   }

   public byte getID() {
      return (byte)12;
   }

   public abstract String getDescription();

   public int getGameID() {
      return this.gameID;
   }

   public int getVal1() {
      return this.val1;
   }

   public String getUserName() {
      return this.userName;
   }

   public long getPing() {
      return this.ping;
   }

   public int getUserID() {
      return this.userID;
   }

   public byte getConnectionType() {
      return this.connectionType;
   }

   public abstract String toString();

   public int getBodyLength() {
      return this.userName.length() + 13;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
      UnsignedUtil.putUnsignedShort(buffer, this.gameID);
      UnsignedUtil.putUnsignedShort(buffer, this.val1);
      EmuUtil.writeString(buffer, this.userName, 0, charset);
      UnsignedUtil.putUnsignedInt(buffer, this.ping);
      UnsignedUtil.putUnsignedShort(buffer, this.userID);
      buffer.put(this.connectionType);
   }

   public static JoinGame parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 13) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new MessageFormatException("Invalid format: byte 0 = " + EmuUtil.byteToHex(b));
         } else {
            int gameID = UnsignedUtil.getUnsignedShort(buffer);
            int val1 = UnsignedUtil.getUnsignedShort(buffer);
            String userName = EmuUtil.readString(buffer, 0, charset);
            if(buffer.remaining() < 7) {
               throw new ParseException("Failed byte count validation!");
            } else {
               long ping = UnsignedUtil.getUnsignedInt(buffer);
               int userID = UnsignedUtil.getUnsignedShort(buffer);
               byte connectionType = buffer.get();
               return (JoinGame)(userName.length() == 0 && ping == 0L && userID == '\uffff'?new JoinGame_Request(messageNumber, gameID, connectionType):new JoinGame_Notification(messageNumber, gameID, val1, userName, ping, userID, connectionType));
            }
         }
      }
   }
}
