package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public class GameStatus extends V086Message {
   public static final byte ID = 14;
   public static final String DESC = "Game Status";
   private int gameID;
   private int val1;
   private byte gameStatus;
   private byte numPlayers;
   private byte maxPlayers;

   public GameStatus(int messageNumber, int gameID, int val1, byte gameStatus, byte numPlayers, byte maxPlayers) throws MessageFormatException {
      super(messageNumber);
      if(gameID >= 0 && gameID <= '\uffff') {
         if(val1 >= 0 && val1 <= '\uffff') {
            if(gameStatus >= 0 && gameStatus <= 2) {
               if(numPlayers >= 0 && numPlayers <= 255) {
                  if(maxPlayers >= 0 && maxPlayers <= 255) {
                     this.gameID = gameID;
                     this.val1 = val1;
                     this.gameStatus = gameStatus;
                     this.numPlayers = numPlayers;
                     this.maxPlayers = maxPlayers;
                  } else {
                     throw new MessageFormatException("Invalid " + this.getDescription() + " format: maxPlayers out of acceptable range: " + maxPlayers);
                  }
               } else {
                  throw new MessageFormatException("Invalid " + this.getDescription() + " format: numPlayers out of acceptable range: " + numPlayers);
               }
            } else {
               throw new MessageFormatException("Invalid " + this.getDescription() + " format: gameStatus out of acceptable range: " + gameStatus);
            }
         } else {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: val1 out of acceptable range: " + val1);
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: gameID out of acceptable range: " + gameID);
      }
   }

   public byte getID() {
      return (byte)14;
   }

   public String getDescription() {
      return "Game Status";
   }

   public int getGameID() {
      return this.gameID;
   }

   public int getVal1() {
      return this.val1;
   }

   public byte getGameStatus() {
      return this.gameStatus;
   }

   public byte getNumPlayers() {
      return this.numPlayers;
   }

   public byte getMaxPlayers() {
      return this.maxPlayers;
   }

   public String toString() {
      return this.getInfoString() + "[gameID=" + this.gameID + " gameStatus=" + KailleraGame.STATUS_NAMES[this.gameStatus] + " numPlayers=" + this.numPlayers + " maxPlayers=" + this.maxPlayers + "]";
   }

   public int getBodyLength() {
      return 8;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
      UnsignedUtil.putUnsignedShort(buffer, this.gameID);
      UnsignedUtil.putUnsignedShort(buffer, this.val1);
      buffer.put(this.gameStatus);
      buffer.put(this.numPlayers);
      buffer.put(this.maxPlayers);
   }

   public static GameStatus parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 8) {
         throw new ParseException("Failed byte count validation!");
      } else {
         byte b = buffer.get();
         if(b != 0) {
            throw new MessageFormatException("Invalid Game Status format: byte 0 = " + EmuUtil.byteToHex(b));
         } else {
            int gameID = UnsignedUtil.getUnsignedShort(buffer);
            int val1 = UnsignedUtil.getUnsignedShort(buffer);
            byte gameStatus = buffer.get();
            byte numPlayers = buffer.get();
            byte maxPlayers = buffer.get();
            return new GameStatus(messageNumber, gameID, val1, gameStatus, numPlayers, maxPlayers);
         }
      }
   }
}
