package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class CreateGameException extends ActionException {
   public CreateGameException(String message) {
      super(message);
   }

   public CreateGameException(String message, Exception source) {
      super(message, source);
   }
}
