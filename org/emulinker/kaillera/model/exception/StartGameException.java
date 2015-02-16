package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class StartGameException extends ActionException {
   public StartGameException(String message) {
      super(message);
   }

   public StartGameException(String message, Exception source) {
      super(message, source);
   }
}
