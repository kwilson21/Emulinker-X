package org.emulinker.kaillera.controller.v086;

import org.emulinker.kaillera.controller.v086.protocol.V086Message;

public class LastMessageBuffer {
   private int max;
   private int next;
   private int size;
   private V086Message[] array;

   public LastMessageBuffer(int max) {
      this.array = new V086Message[max];
      this.max = max;
      this.next = max - 1;
   }

   public void add(V086Message o) {
      this.array[this.next] = o;
      if(--this.next < 0) {
         this.next = this.max - 1;
      }

      if(this.size < this.max) {
         ++this.size;
      }

   }

   public int fill(V086Message[] o, int num) {
      if(this.size < num) {
         System.arraycopy(this.array, this.next + 1, o, 0, this.size);
         return this.size;
      } else {
         if(this.next + 1 + num <= this.max) {
            System.arraycopy(this.array, this.next + 1, o, 0, num);
         } else {
            System.arraycopy(this.array, this.next + 1, o, 0, this.max - (this.next + 1));
            System.arraycopy(this.array, 0, o, this.max - (this.next + 1), num - (this.max - (this.next + 1)));
         }

         return num;
      }
   }
}
