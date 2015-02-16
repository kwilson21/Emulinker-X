package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class FloodException extends ActionException {
   public FloodException() {
   }

   public FloodException(String message) {
      super(message);
   }

   public FloodException(String message, Exception source) {
      super(message, source);
   }
}
