package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.event.GameEvent;

public class GameDataEvent implements GameEvent {
   private KailleraGame game;
   private byte[] data;

   public GameDataEvent(KailleraGame game, byte[] data) {
      this.game = game;
      this.data = data;
   }

   public String toString() {
      return "GameDataEvent";
   }

   public KailleraGame getGame() {
      return this.game;
   }

   public byte[] getData() {
      return this.data;
   }
}
