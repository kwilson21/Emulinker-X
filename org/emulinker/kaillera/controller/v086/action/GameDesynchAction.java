package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification;
import org.emulinker.kaillera.model.event.GameDesynchEvent;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.util.EmuLang;

public class GameDesynchAction implements V086GameEventHandler {
   private static Log log = LogFactory.getLog(GameDesynchAction.class);
   private static final String desc = "GameDesynchAction";
   private static GameDesynchAction singleton = new GameDesynchAction();
   private int handledCount = 0;

   public static GameDesynchAction getInstance() {
      return singleton;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "GameDesynchAction";
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameDesynchEvent desynchEvent = (GameDesynchEvent)event;

      try {
         clientHandler.send(new GameChat_Notification(clientHandler.getNextMessageNumber(), EmuLang.getString("GameDesynchAction.DesynchDetected"), desynchEvent.getMessage()));
      } catch (MessageFormatException var5) {
         log.error("Failed to contruct GameChat_Notification message: " + var5.getMessage(), var5);
      }

   }
}
