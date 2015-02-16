package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class LoginException extends ActionException {
   public LoginException(String message) {
      super(message);
   }

   public LoginException(String message, Exception source) {
      super(message, source);
   }
}
