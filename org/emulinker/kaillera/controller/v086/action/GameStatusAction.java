package org.emulinker.kaillera.controller.v086.action;

import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.V086ServerEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.GameStatus;
import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameStatusChangedEvent;
import org.emulinker.kaillera.model.event.ServerEvent;

public class GameStatusAction implements V086ServerEventHandler {
   private static Log log = LogFactory.getLog(GameStatusAction.class);
   private static GameStatusAction singleton = new GameStatusAction();
   private int handledCount = 0;

   public static GameStatusAction getInstance() {
      return singleton;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "GameStatusAction";
   }

   public void handleEvent(ServerEvent event, V086Controller.V086ClientHandler clientHandler) {
      ++this.handledCount;
      GameStatusChangedEvent statusChangeEvent = (GameStatusChangedEvent)event;

      try {
         KailleraGame e = statusChangeEvent.getGame();
         int num = 0;
         Iterator i$ = e.getPlayers().iterator();

         while(i$.hasNext()) {
            KailleraUser user = (KailleraUser)i$.next();
            if(!user.getStealth()) {
               ++num;
            }
         }

         clientHandler.send(new GameStatus(clientHandler.getNextMessageNumber(), e.getID(), 0, (byte)e.getStatus(), (byte)num, (byte)e.getMaxUsers()));
      } catch (MessageFormatException var8) {
         log.error("Failed to contruct CreateGame_Notification message: " + var8.getMessage(), var8);
      }

   }
}
