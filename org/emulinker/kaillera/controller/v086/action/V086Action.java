package org.emulinker.kaillera.controller.v086.action;

import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;

public interface V086Action {
   String toString();

   void performAction(V086Message var1, V086Controller.V086ClientHandler var2) throws FatalActionException;

   int getActionPerformedCount();
}
