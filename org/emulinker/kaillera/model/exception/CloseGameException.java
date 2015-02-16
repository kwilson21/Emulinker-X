package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class CloseGameException extends ActionException {
   public CloseGameException(String message) {
      super(message);
   }

   public CloseGameException(String message, Exception source) {
      super(message, source);
   }
}
