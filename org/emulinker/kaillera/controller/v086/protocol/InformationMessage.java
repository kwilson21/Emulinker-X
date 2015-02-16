package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.messaging.ParseException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.EmuUtil;

public class InformationMessage extends V086Message {
   public static final byte ID = 23;
   public static final String DESC = "Information Message";
   private String source;
   private String message;

   public InformationMessage(int messageNumber, String source, String message) throws MessageFormatException {
      super(messageNumber);
      if(source.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: source.length == 0");
      } else if(message.length() == 0) {
         throw new MessageFormatException("Invalid " + this.getDescription() + " format: message.length == 0");
      } else {
         this.source = source;
         this.message = message;
      }
   }

   public byte getID() {
      return (byte)23;
   }

   public String getDescription() {
      return "Information Message";
   }

   public String getSource() {
      return this.source;
   }

   public String getMessage() {
      return this.message;
   }

   public String toString() {
      return this.getInfoString() + "[source: " + this.source + " message: " + this.message + "]";
   }

   public int getBodyLength() {
      return this.source.length() + this.message.length() + 2;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      EmuUtil.writeString(buffer, this.source, 0, charset);
      EmuUtil.writeString(buffer, this.message, 0, charset);
   }

   public static InformationMessage parse(int messageNumber, ByteBuffer buffer) throws ParseException, MessageFormatException {
      if(buffer.remaining() < 4) {
         throw new ParseException("Failed byte count validation!");
      } else {
         String source = EmuUtil.readString(buffer, 0, charset);
         if(buffer.remaining() < 2) {
            throw new ParseException("Failed byte count validation!");
         } else {
            String message = EmuUtil.readString(buffer, 0, charset);
            return new InformationMessage(messageNumber, source, message);
         }
      }
   }
}
