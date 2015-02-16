package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class GameChatException extends ActionException {
   public GameChatException(String message) {
      super(message);
   }

   public GameChatException(String message, Exception source) {
      super(message, source);
   }
}
