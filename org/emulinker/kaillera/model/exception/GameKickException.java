package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class GameKickException extends ActionException {
   public GameKickException(String message) {
      super(message);
   }

   public GameKickException(String message, Exception source) {
      super(message, source);
   }
}
