package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.event.PlayerDesynchEvent;
import org.emulinker.util.EmuLang;

public class PlayerDesynchAction implements V086GameEventHandler {
   private static Log log = LogFactory.getLog(PlayerDesynchAction.class);
   private static final String desc = PlayerDesynchAction.class.getSimpleName();
   private static PlayerDesynchAction singleton = new PlayerDesynchAction();
   private int handledCount = 0;

   public static PlayerDesynchAction getInstance() {
      return singleton;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return desc;
   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      PlayerDesynchEvent desynchEvent = (PlayerDesynchEvent)event;

      try {
         clientHandler.send(new GameChat_Notification(clientHandler.getNextMessageNumber(), EmuLang.getString("PlayerDesynchAction.DesynchDetected"), desynchEvent.getMessage()));
      } catch (MessageFormatException var5) {
         log.error("Failed to contruct GameChat_Notification message: " + var5.getMessage(), var5);
      }

   }
}
