package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.V086ServerEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.CloseGame;
import org.emulinker.kaillera.model.event.GameClosedEvent;
import org.emulinker.kaillera.model.event.ServerEvent;

public class CloseGameAction implements V086ServerEventHandler {
   private static Log log = LogFactory.getLog(CloseGameAction.class);
   private static final String desc = "CloseGameAction";
   private static CloseGameAction singleton = new CloseGameAction();
   private int handledCount;

   public static CloseGameAction getInstance() {
      return singleton;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "CloseGameAction";
   }

   public void handleEvent(ServerEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameClosedEvent gameClosedEvent = (GameClosedEvent)event;

      try {
         clientHandler.send(new CloseGame(clientHandler.getNextMessageNumber(), gameClosedEvent.getGame().getID(), 0));
      } catch (MessageFormatException var5) {
         log.error("Failed to contruct CloseGame_Notification message: " + var5.getMessage(), var5);
      }

   }
}
