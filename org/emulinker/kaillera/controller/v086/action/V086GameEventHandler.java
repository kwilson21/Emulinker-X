package org.emulinker.kaillera.controller.v086.action;

import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.model.event.GameEvent;

public interface V086GameEventHandler {
   String toString();

   void handleEvent(GameEvent var1, V086Controller.V086ClientHandler var2);

   int getHandledEventCount();
}
