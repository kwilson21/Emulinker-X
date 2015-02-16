package org.emulinker.kaillera.controller.messaging;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ByteBufferMessage {
   protected static Log log = LogFactory.getLog(ByteBufferMessage.class);
   public static Charset charset = Charset.defaultCharset();
   private ByteBuffer buffer;

   public abstract int getLength();

   public abstract String getDescription();

   public abstract String toString();

   protected void initBuffer() {
      this.initBuffer(this.getLength());
   }

   private void initBuffer(int size) {
      this.buffer = getBuffer(size);
   }

   public void releaseBuffer() {
   }

   public ByteBuffer toBuffer() {
      this.initBuffer();
      this.writeTo(this.buffer);
      this.buffer.flip();
      return this.buffer;
   }

   public abstract void writeTo(ByteBuffer var1);

   public static ByteBuffer getBuffer(int size) {
      return ByteBuffer.allocateDirect(size);
   }

   public static void releaseBuffer(ByteBuffer buffer) {
   }

   static {
      String charsetName = System.getProperty("emulinker.charset");
      if(charsetName != null) {
         try {
            if(Charset.isSupported(charsetName)) {
               charset = Charset.forName(charsetName);
            } else {
               log.fatal("Charset " + charsetName + " is not supported!");
            }
         } catch (Exception var2) {
            log.fatal("Failed to load charset " + charsetName + ": " + var2.getMessage(), var2);
         }
      }

      log.info("Using character set: " + charset.displayName());
   }
}
