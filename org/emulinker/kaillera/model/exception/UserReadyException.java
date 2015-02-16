package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class UserReadyException extends ActionException {
   public UserReadyException(String message) {
      super(message);
   }

   public UserReadyException(String message, Exception source) {
      super(message, source);
   }
}
