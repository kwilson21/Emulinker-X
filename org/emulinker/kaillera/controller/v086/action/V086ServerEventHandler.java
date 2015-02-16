package org.emulinker.kaillera.controller.v086.action;

import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.model.event.ServerEvent;

public interface V086ServerEventHandler {
   String toString();

   void handleEvent(ServerEvent var1, V086Controller.V086ClientHandler var2);

   int getHandledEventCount();
}
