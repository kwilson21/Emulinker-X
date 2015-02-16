package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraServer;
import org.emulinker.kaillera.model.event.KailleraEvent;

public interface ServerEvent extends KailleraEvent {
   KailleraServer getServer();
}
