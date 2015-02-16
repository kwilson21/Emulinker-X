package org.emulinker.kaillera.controller.v086.protocol;

import java.nio.ByteBuffer;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.util.UnsignedUtil;

public abstract class ACK extends V086Message {
   private long val1;
   private long val2;
   private long val3;
   private long val4;

   public ACK(int messageNumber, long val1, long val2, long val3, long val4) throws MessageFormatException {
      super(messageNumber);
      this.val1 = val1;
      this.val2 = val2;
      this.val3 = val3;
      this.val4 = val4;
   }

   public long getVal1() {
      return this.val1;
   }

   public long getVal2() {
      return this.val2;
   }

   public long getVal3() {
      return this.val3;
   }

   public long getVal4() {
      return this.val4;
   }

   public String toString() {
      return this.getInfoString() + "[val1=" + this.getVal1() + " val2=" + this.getVal2() + " val3=" + this.getVal3() + " val4=" + this.getVal4() + "]";
   }

   public int getBodyLength() {
      return 17;
   }

   public void writeBodyTo(ByteBuffer buffer) {
      buffer.put((byte)0);
      UnsignedUtil.putUnsignedInt(buffer, this.val1);
      UnsignedUtil.putUnsignedInt(buffer, this.val2);
      UnsignedUtil.putUnsignedInt(buffer, this.val3);
      UnsignedUtil.putUnsignedInt(buffer, this.val4);
   }
}
