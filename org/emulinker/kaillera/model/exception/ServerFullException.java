package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.NewConnectionException;

public class ServerFullException extends NewConnectionException {
   public ServerFullException(String message) {
      super(message);
   }

   public ServerFullException(String message, Exception source) {
      super(message, source);
   }
}
