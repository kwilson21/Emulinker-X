package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class GameDataException extends ActionException {
   private byte[] response;

   public GameDataException(String message) {
      super(message);
   }

   public GameDataException(String message, byte[] data, int actionsPerMessage, int playerNumber, int numPlayers) {
      super(message);
      int bytesPerAction = data.length / actionsPerMessage;
      int arraySize = numPlayers * actionsPerMessage * bytesPerAction;
      this.response = new byte[arraySize];

      for(int actionCounter = 0; actionCounter < actionsPerMessage; ++actionCounter) {
         System.arraycopy(data, 0, this.response, actionCounter * numPlayers * bytesPerAction + (playerNumber - 1) * bytesPerAction, bytesPerAction);
      }

   }

   public boolean hasResponse() {
      return this.response != null;
   }

   public byte[] getResponse() {
      return !this.hasResponse()?null:this.response;
   }
}
