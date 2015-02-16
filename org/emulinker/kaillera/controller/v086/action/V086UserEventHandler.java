package org.emulinker.kaillera.controller.v086.action;

import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.model.event.UserEvent;

public interface V086UserEventHandler {
   String toString();

   void handleEvent(UserEvent var1, V086Controller.V086ClientHandler var2);

   int getHandledEventCount();
}
