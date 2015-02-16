package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class QuitException extends ActionException {
   public QuitException(String message) {
      super(message);
   }

   public QuitException(String message, Exception source) {
      super(message, source);
   }
}
