/*    */ package org.emulinker.kaillera.access;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ 
/*    */ public abstract interface CopyOfAccessManager
/*    */ {
/*    */   public static final int ACCESS_BANNED = 0;
/*    */   public static final int ACCESS_NORMAL = 1;
/*    */   public static final int ACCESS_ELEVATED = 2;
/*    */   public static final int ACCESS_MODERATOR = 3;
/*    */   public static final int ACCESS_ADMIN = 4;
/*    */   public static final int ACCESS_SUPERADMIN = 5;
/* 27 */   public static final String[] ACCESS_NAMES = { "Banned", "Normal", "Elevated", "Moderator", "Admin", "SuperAdmin" };
/*    */ 
/*    */   public abstract boolean isAddressAllowed(InetAddress paramInetAddress);
/*    */ 
/*    */   public abstract boolean isSilenced(InetAddress paramInetAddress);
/*    */ 
/*    */   public abstract boolean isEmulatorAllowed(String paramString);
/*    */ 
/*    */   public abstract boolean isGameAllowed(String paramString);
/*    */ 
/*    */   public abstract int getAccess(InetAddress paramInetAddress);
/*    */ 
/*    */   public abstract String getAnnouncement(InetAddress paramInetAddress);
/*    */ 
/*    */   public abstract void addTempBan(String paramString, int paramInt);
/*    */ 
/*    */   public abstract void addTempAdmin(String paramString, int paramInt);
/*    */ 
/*    */   public abstract void addTempElevated(String paramString, int paramInt);
/*    */ 
/*    */   public abstract void addSilenced(String paramString, int paramInt);
/*    */ 
/*    */   public abstract boolean clearTemp(InetAddress paramInetAddress);
/*    */
/*    */   public abstract boolean clearTempSilence(int userID);
/*    */ }