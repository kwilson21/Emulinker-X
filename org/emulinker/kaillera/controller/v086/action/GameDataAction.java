package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.action.V086GameEventHandler;
import org.emulinker.kaillera.controller.v086.protocol.CachedGameData;
import org.emulinker.kaillera.controller.v086.protocol.GameData;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.GameDataEvent;
import org.emulinker.kaillera.model.event.GameEvent;
import org.emulinker.kaillera.model.exception.GameDataException;

public class GameDataAction implements V086Action, V086GameEventHandler {
   private static Log log = LogFactory.getLog(GameDataAction.class);
   private static GameDataAction singleton = new GameDataAction();
   private static Log keyLog = LogFactory.getLog("KEYLOG");
   private int actionCount = 0;
   private int handledCount = 0;

   public static GameDataAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public int getHandledEventCount() {
      return this.handledCount;
   }

   public String toString() {
      return "GameDataAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      try {
         KailleraUser e1 = clientHandler.getUser();
         byte[] e2 = ((GameData)message).getGameData();
         clientHandler.getClientGameDataCache().add(e2);
         e1.addGameData(e2);
      } catch (GameDataException var6) {
         GameDataException e = var6;
         log.debug("Game data error: " + var6.getMessage());
         if(var6.hasResponse()) {
            try {
               clientHandler.send(new GameData(clientHandler.getNextMessageNumber(), e.getResponse()));
            } catch (MessageFormatException var5) {
               log.error("Failed to contruct GameData message: " + var5.getMessage(), var5);
            }
         }
      }

   }

   public void handleEvent(GameEvent event, V086Controller.V086ClientHandler clientHandler) {
      byte[] data = ((GameDataEvent)event).getData();
      int key = clientHandler.getServerGameDataCache().indexOf(data);
      if(key < 0) {
         clientHandler.getServerGameDataCache().add(data);

         try {
            clientHandler.send(new GameData(clientHandler.getNextMessageNumber(), data));
         } catch (MessageFormatException var7) {
            log.error("Failed to contruct GameData message: " + var7.getMessage(), var7);
         }
      } else {
         try {
            clientHandler.send(new CachedGameData(clientHandler.getNextMessageNumber(), key));
         } catch (MessageFormatException var6) {
            log.error("Failed to contruct CachedGameData message: " + var6.getMessage(), var6);
         }
      }

   }
}
