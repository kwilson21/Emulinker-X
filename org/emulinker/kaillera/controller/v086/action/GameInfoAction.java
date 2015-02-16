package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.event.GameInfoEvent;

public class GameInfoAction implements V086GameEventHandler {
   private static Log log = LogFactory.getLog(GameInfoAction.class);
   private static final String desc = "GameInfoAction";
   private static GameInfoAction singleton = new GameInfoAction();
   private int handledCount = 0;

   public static GameInfoAction getInstance() {
      return singleton;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "GameInfoAction";
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameInfoEvent infoEvent = (GameInfoEvent)event;
      if(infoEvent.getUser() == null || infoEvent.getUser() == clientHandler.getUser()) {
         try {
            clientHandler.send(new GameChat_Notification(clientHandler.getNextMessageNumber(), "Server", infoEvent.getMessage()));
         } catch (MessageFormatException var5) {
            log.error("Failed to contruct GameChat_Notification message: " + var5.getMessage(), var5);
         }

      }
   }
}
