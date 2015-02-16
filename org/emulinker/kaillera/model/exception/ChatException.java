package org.emulinker.kaillera.model.exception;

import org.emulinker.kaillera.model.exception.ActionException;

public class ChatException extends ActionException {
   public ChatException(String message) {
      super(message);
   }

   public ChatException(String message, Exception source) {
      super(message, source);
   }
}
