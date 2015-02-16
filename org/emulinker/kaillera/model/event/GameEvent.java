package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.event.KailleraEvent;

public interface GameEvent extends KailleraEvent {
   KailleraGame getGame();
}
