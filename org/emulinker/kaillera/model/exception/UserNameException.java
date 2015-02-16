package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.LoginException;

public class UserNameException extends LoginException {
   public UserNameException(String message) {
      super(message);
   }

   public UserNameException(String message, Exception source) {
      super(message, source);
   }
}
