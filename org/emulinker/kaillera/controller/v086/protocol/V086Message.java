package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.ByteBufferMessage;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.AllReady;
import org.emulinker.kaillera.controller.v086.protocol.CachedGameData;
import org.emulinker.kaillera.controller.v086.protocol.Chat;
import org.emulinker.kaillera.controller.v086.protocol.ClientACK;
import org.emulinker.kaillera.controller.v086.protocol.CloseGame;
import org.emulinker.kaillera.controller.v086.protocol.ConnectionRejected;
import org.emulinker.kaillera.controller.v086.protocol.CreateGame;
import org.emulinker.kaillera.controller.v086.protocol.GameChat;
import org.emulinker.kaillera.controller.v086.protocol.GameData;
import org.emulinker.kaillera.controller.v086.protocol.GameKick;
import org.emulinker.kaillera.controller.v086.protocol.GameStatus;
import org.emulinker.kaillera.controller.v086.protocol.InformationMessage;
import org.emulinker.kaillera.controller.v086.protocol.JoinGame;
import org.emulinker.kaillera.controller.v086.protocol.KeepAlive;
import org.emulinker.kaillera.controller.v086.protocol.PlayerDrop;
import org.emulinker.kaillera.controller.v086.protocol.PlayerInformation;
import org.emulinker.kaillera.controller.v086.protocol.Quit;
import org.emulinker.kaillera.controller.v086.protocol.QuitGame;
import org.emulinker.kaillera.controller.v086.protocol.ServerACK;
import org.emulinker.kaillera.controller.v086.protocol.ServerStatus;
import org.emulinker.kaillera.controller.v086.protocol.StartGame;
import org.emulinker.kaillera.controller.v086.protocol.UserInformation;
import org.emulinker.kaillera.controller.v086.protocol.UserJoined;
import org.emulinker.util.EmuUtil;
import org.emulinker.util.UnsignedUtil;

public abstract class V086Message extends ByteBufferMessage {
   protected int number;
   protected byte messageType;

   protected V086Message(int number) throws MessageFormatException {
      if(number >= 0 && number <= '\uffff') {
         if(this.messageType >= 0 && this.messageType <= 23) {
            this.number = number;
         } else {
            throw new MessageFormatException("Invalid " + this.getDescription() + " format: Invalid message type: " + this.messageType);
         }
      } else {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: Invalid message number: " + number);
      }
   }

   public int getNumber() {
      return this.number;
   }

   public abstract byte getID();

   public abstract String getDescription();

   public int getLength() {
      return this.getBodyLength() + 1;
   }

   public abstract int getBodyLength();

   protected String getInfoString() {
      return this.getNumber() + ":" + EmuUtil.byteToHex(this.getID()) + "/" + this.getDescription();
   }

   public void writeTo(ByteBuffer buffer) {
      int len = this.getLength();
      if(len > buffer.remaining()) {
         log.warn("Ran out of output buffer space, consider increasing the controllers.v086.bufferSize setting!");
      } else {
         UnsignedUtil.putUnsignedShort(buffer, this.getNumber());
         buffer.mark();
         UnsignedUtil.putUnsignedShort(buffer, len);
         buffer.put(this.getID());
         this.writeBodyTo(buffer);
      }

   }

   protected abstract void writeBodyTo(ByteBuffer var1);

   public static V086Message parse(int messageNumber, int messageLength, ByteBuffer buffer) throws ParseException, MessageFormatException {
      byte messageType = buffer.get();
      Object message = null;
      switch(messageType) {
      case 1:
         message = Quit.parse(messageNumber, buffer);
         break;
      case 2:
         message = UserJoined.parse(messageNumber, buffer);
         break;
      case 3:
         message = UserInformation.parse(messageNumber, buffer);
         break;
      case 4:
         message = ServerStatus.parse(messageNumber, buffer);
         break;
      case 5:
         message = ServerACK.parse(messageNumber, buffer);
         break;
      case 6:
         message = ClientACK.parse(messageNumber, buffer);
         break;
      case 7:
         message = Chat.parse(messageNumber, buffer);
         break;
      case 8:
         message = GameChat.parse(messageNumber, buffer);
         break;
      case 9:
         message = KeepAlive.parse(messageNumber, buffer);
         break;
      case 10:
         message = CreateGame.parse(messageNumber, buffer);
         break;
      case 11:
         message = QuitGame.parse(messageNumber, buffer);
         break;
      case 12:
         message = JoinGame.parse(messageNumber, buffer);
         break;
      case 13:
         message = PlayerInformation.parse(messageNumber, buffer);
         break;
      case 14:
         message = GameStatus.parse(messageNumber, buffer);
         break;
      case 15:
         message = GameKick.parse(messageNumber, buffer);
         break;
      case 16:
         message = CloseGame.parse(messageNumber, buffer);
         break;
      case 17:
         message = StartGame.parse(messageNumber, buffer);
         break;
      case 18:
         message = GameData.parse(messageNumber, buffer);
         break;
      case 19:
         message = CachedGameData.parse(messageNumber, buffer);
         break;
      case 20:
         message = PlayerDrop.parse(messageNumber, buffer);
         break;
      case 21:
         message = AllReady.parse(messageNumber, buffer);
         break;
      case 22:
         message = ConnectionRejected.parse(messageNumber, buffer);
         break;
      case 23:
         message = InformationMessage.parse(messageNumber, buffer);
         break;
      default:
         throw new MessageFormatException("Invalid message type: " + messageType);
      }

      if(((V086Message)message).getLength() != messageLength) {
         log.debug("Bundle contained length " + messageLength + " !=  parsed length " + ((V086Message)message).getLength());
      }

      return (V086Message)message;
   }
}
