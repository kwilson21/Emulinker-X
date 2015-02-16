package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.LoginException;

public class ConnectionTypeException extends LoginException {
   public ConnectionTypeException(String message) {
      super(message);
   }

   public ConnectionTypeException(String message, Exception source) {
      super(message, source);
   }
}
