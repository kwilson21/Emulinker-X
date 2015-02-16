package org.emulinker.kaillera.controller.v086.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.V086Controller;
import org.emulinker.kaillera.controller.v086.action.FatalActionException;
import org.emulinker.kaillera.controller.v086.action.V086Action;
import org.emulinker.kaillera.controller.v086.protocol.CachedGameData;
import org.emulinker.kaillera.controller.v086.protocol.GameChat_Notification;
import org.emulinker.kaillera.controller.v086.protocol.GameData;
import org.emulinker.kaillera.controller.v086.protocol.V086Message;
import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.exception.GameDataException;

public class CachedGameDataAction implements V086Action {
   private static Log log = LogFactory.getLog(CachedGameDataAction.class);
   private static final String desc = "CachedGameDataAction";
   private static CachedGameDataAction singleton = new CachedGameDataAction();
   private static Log keyLog = LogFactory.getLog("KEYLOG");
   private int actionCount = 0;

   public static CachedGameDataAction getInstance() {
      return singleton;
   }

   public int getActionPerformedCount() {
      return this.actionCount;
   }

   public String toString() {
      return "CachedGameDataAction";
   }

   public void performAction(V086Message message, V086Controller.V086ClientHandler clientHandler) throws FatalActionException {
      try {
         KailleraUser e1 = clientHandler.getUser();
         int e2 = ((CachedGameData)message).getKey();
         byte[] data = clientHandler.getClientGameDataCache().get(e2);
         if(data == null) {
            log.debug("Game Cache Error: null data");
            return;
         }

         e1.addGameData(data);
      } catch (GameDataException var8) {
         GameDataException e = var8;
         log.debug("Game data error: " + var8.getMessage());
         if(var8.hasResponse()) {
            try {
               clientHandler.send(new GameData(clientHandler.getNextMessageNumber(), e.getResponse()));
            } catch (MessageFormatException var7) {
               log.error("Failed to contruct GameData message: " + var7.getMessage(), var7);
            }
         }
      } catch (IndexOutOfBoundsException var9) {
         log.error("Game data error!  The client cached key " + ((CachedGameData)message).getKey() + " was not found in the cache!", var9);

         try {
            clientHandler.send(new GameChat_Notification(clientHandler.getNextMessageNumber(), "Error", "Game Data Error!  Game state will be inconsistent!"));
         } catch (MessageFormatException var6) {
            log.error("Failed to contruct new GameChat_Notification", var9);
         }
      }

   }
}
