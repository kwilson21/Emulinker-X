package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class QuitGameException extends ActionException {
   public QuitGameException(String message) {
      super(message);
   }

   public QuitGameException(String message, Exception source) {
      super(message, source);
   }
}
