package org.emulinker.util;

import java.nio.ByteBuffer;

public class UnsignedUtil {
   public static short getUnsignedByte(ByteBuffer bb) {
      return (short)(bb.get() & 255);
   }

   public static void putUnsignedByte(ByteBuffer bb, int value) {
      bb.put((byte)(value & 255));
   }

   public static short getUnsignedByte(ByteBuffer bb, int position) {
      return (short)(bb.get(position) & 255);
   }

   public static void putUnsignedByte(ByteBuffer bb, int position, int value) {
      bb.put(position, (byte)(value & 255));
   }

   public static int getUnsignedShort(ByteBuffer bb) {
      return bb.getShort() & '\uffff';
   }

   public static void putUnsignedShort(ByteBuffer bb, int value) {
      bb.putShort((short)(value & '\uffff'));
   }

   public static int getUnsignedShort(ByteBuffer bb, int position) {
      return bb.getShort(position) & '\uffff';
   }

   public static void putUnsignedShort(ByteBuffer bb, int position, int value) {
      bb.putShort(position, (short)(value & '\uffff'));
   }

   public static long getUnsignedInt(ByteBuffer bb) {
      return (long)bb.getInt() & 4294967295L;
   }

   public static void putUnsignedInt(ByteBuffer bb, long value) {
      bb.putInt((int)(value & 4294967295L));
   }

   public static long getUnsignedInt(ByteBuffer bb, int position) {
      return (long)bb.getInt(position) & 4294967295L;
   }

   public static void putUnsignedInt(ByteBuffer bb, int position, long value) {
      bb.putInt(position, (int)(value & 4294967295L));
   }

   public static short readUnsignedByte(byte[] bytes, int offset) {
      return (short)(bytes[offset] & 255);
   }

   public static void writeUnsignedByte(short s, byte[] bytes, int offset) {
      bytes[offset] = (byte)(s & 255);
   }

   public static int readUnsignedShort(byte[] bytes, int offset) {
      return readUnsignedShort(bytes, offset, false);
   }

   public static int readUnsignedShort(byte[] bytes, int offset, boolean littleEndian) {
      return littleEndian?((bytes[offset + 1] & 255) << 8) + (bytes[offset] & 255):((bytes[offset] & 255) << 8) + (bytes[offset + 1] & 255);
   }

   public static void writeUnsignedShort(int s, byte[] bytes, int offset) {
      writeUnsignedShort(s, bytes, offset);
   }

   public static void writeUnsignedShort(int s, byte[] bytes, int offset, boolean littleEndian) {
      if(littleEndian) {
         bytes[offset] = (byte)(s & 255);
         bytes[offset + 1] = (byte)(s >>> 8 & 255);
      } else {
         bytes[offset] = (byte)(s >>> 8 & 255);
         bytes[offset + 1] = (byte)(s & 255);
      }

   }

   public static long readUnsignedInt(byte[] bytes, int offset) {
      return readUnsignedInt(bytes, offset, false);
   }

   public static long readUnsignedInt(byte[] bytes, int offset, boolean littleEndian) {
      int i1 = bytes[offset + 0] & 255;
      int i2 = bytes[offset + 1] & 255;
      int i3 = bytes[offset + 2] & 255;
      int i4 = bytes[offset + 3] & 255;
      return littleEndian?(long)((i4 << 24) + (i3 << 16) + (i2 << 8) + i1):(long)((i1 << 24) + (i2 << 16) + (i3 << 8) + i4);
   }

   public static void writeUnsignedInt(long i, byte[] bytes, int offset) {
      writeUnsignedInt(i, bytes, offset, false);
   }

   public static void writeUnsignedInt(long i, byte[] bytes, int offset, boolean littleEndian) {
      if(littleEndian) {
         bytes[offset + 0] = (byte)((int)(i & 255L));
         bytes[offset + 1] = (byte)((int)(i >>> 8 & 255L));
         bytes[offset + 2] = (byte)((int)(i >>> 16 & 255L));
         bytes[offset + 3] = (byte)((int)(i >>> 24 & 255L));
      } else {
         bytes[offset + 0] = (byte)((int)(i >>> 24 & 255L));
         bytes[offset + 1] = (byte)((int)(i >>> 16 & 255L));
         bytes[offset + 2] = (byte)((int)(i >>> 8 & 255L));
         bytes[offset + 3] = (byte)((int)(i & 255L));
      }

   }
}
