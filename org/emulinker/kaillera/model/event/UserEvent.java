package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.KailleraUser;
import org.emulinker.kaillera.model.event.KailleraEvent;

public interface UserEvent extends KailleraEvent {
   KailleraUser getUser();
}
