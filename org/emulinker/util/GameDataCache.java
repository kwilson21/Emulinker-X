package org.emulinker.util;

public interface GameDataCache {
   byte[] get(int var1);

   int add(byte[] var1);

   int indexOf(byte[] var1);

   int size();

   boolean isEmpty();

   void clear();

   boolean contains(byte[] var1);

   byte[] set(int var1, byte[] var2);

   byte[] remove(int var1);
}
