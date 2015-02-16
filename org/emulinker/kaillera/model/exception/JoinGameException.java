package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class JoinGameException extends ActionException {
   public JoinGameException(String message) {
      super(message);
   }

   public JoinGameException(String message, Exception source) {
      super(message, source);
   }
}
