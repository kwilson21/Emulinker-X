package org.emulinker.kaillera.controller.v086.protocol;

import org.emulinker.kaillera.controller.messaging.MessageFormatException;
import org.emulinker.kaillera.controller.v086.protocol.StartGame;

public class StartGame_Notification extends StartGame {
   public static final String DESC = "Start Game Notification";

   public StartGame_Notification(int messageNumber, int val1, short playerNumber, short numPlayers) throws MessageFormatException {
      super(messageNumber, val1, playerNumber, numPlayers);
   }

   public String getDescription() {
      return "Start Game Notification";
   }

   public String toString() {
      return this.getInfoString() + "[val1=" + this.getVal1() + " playerNumber=" + this.getPlayerNumber() + " numPlayers=" + this.getNumPlayers() + "]";
   }
}
