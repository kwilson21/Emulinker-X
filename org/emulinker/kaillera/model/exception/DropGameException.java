package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class DropGameException extends ActionException {
   public DropGameException(String message) {
      super(message);
   }

   public DropGameException(String message, Exception source) {
      super(message, source);
   }
}
