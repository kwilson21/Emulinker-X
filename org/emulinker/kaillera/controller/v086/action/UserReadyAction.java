package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.AllReady;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.exception.UserReadyException;

public class UserReadyAction implements V086Action, V086GameEventHandler {
   private static Log log = LogFactory.getLog(UserReadyAction.class);
   private static final String desc = "UserReadyAction";
   private static UserReadyAction singleton = new UserReadyAction();
   private int actionCount = 0;
   private int handledCount = 0;

   public static UserReadyAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "UserReadyAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      ++this.actionCount;

      try {
         clientHandler.getUser().playerReady();
      } catch (UserReadyException var4) {
         log.debug("Ready signal failed: " + var4.getMessage());
      }

   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      clientHandler.resetGameDataCache();

      try {
         clientHandler.send(new AllReady(clientHandler.getNextMessageNumber()));
      } catch (MessageFormatException var4) {
         log.error("Failed to contruct AllReady message: " + var4.getMessage(), var4);
      }

   }
}
