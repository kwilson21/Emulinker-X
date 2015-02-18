package org.emulinker.kaillera.model.impl;

import org.emulinker.kaillera.model.impl.KailleraUserImpl;
import org.emulinker.kaillera.model.impl.PlayerTimeoutException;

public class PlayerActionQueue {
   private int gameBufferSize;
   private int gameTimeoutMillis;
   private int thisPlayerNumber;
   private KailleraUserImpl thisPlayer;
   private boolean synched = false;
   private PlayerTimeoutException lastTimeout;
   private byte[] array;
   private int[] heads;
   private int tail = 0;

   public PlayerActionQueue(int playerNumber, KailleraUserImpl player, int numPlayers, int gameBufferSize, int gameTimeoutMillis, boolean capture) {
      this.thisPlayerNumber = playerNumber;
      this.thisPlayer = player;
      this.gameBufferSize = gameBufferSize;
      this.gameTimeoutMillis = gameTimeoutMillis;
      this.array = new byte[gameBufferSize];
      this.heads = new int[numPlayers];
   }

   public int getPlayerNumber() {
      return this.thisPlayerNumber;
   }

   public KailleraUserImpl getPlayer() {
      return this.thisPlayer;
   }

   public void setSynched(boolean synched) {
      this.synched = synched;
      if(!synched) {
         synchronized(this) {
            this.notifyAll();
         }
      }

   }

   public boolean isSynched() {
      return this.synched;
   }

   public void setLastTimeout(PlayerTimeoutException e) {
      this.lastTimeout = e;
   }

   public PlayerTimeoutException getLastTimeout() {
      return this.lastTimeout;
   }

   public void addActions(byte[] actions) {
      if(this.synched) {
         for(int i = 0; i < actions.length; ++i) {
            this.array[this.tail] = actions[i];
            ++this.tail;
            if(this.tail == this.gameBufferSize) {
               this.tail = 0;
            }
         }

         synchronized(this) {
            this.notifyAll();
         }

         this.lastTimeout = null;
      }
   }

   public void getAction(int playerNumber, byte[] actions, int location, int actionLength) throws PlayerTimeoutException {
      synchronized(this) {
         if(this.getSize(playerNumber) < actionLength && this.synched) {
            try {
               this.wait((long)this.gameTimeoutMillis);
            } catch (InterruptedException var8) {
               ;
            }
         }
      }

      if(this.getSize(playerNumber) >= actionLength) {
         for(int i = 0; i < actionLength; ++i) {
            actions[location + i] = this.array[this.heads[playerNumber - 1]];
            ++this.heads[playerNumber - 1];
            if(this.heads[playerNumber - 1] == this.gameBufferSize) {
               this.heads[playerNumber - 1] = 0;
            }
         }

      } else if(this.synched) {
         throw new PlayerTimeoutException(this.thisPlayerNumber, this.thisPlayer);
      }
   }

   private int getSize(int playerNumber) {
      return (this.tail + this.gameBufferSize - this.heads[playerNumber - 1]) % this.gameBufferSize;
   }
}
