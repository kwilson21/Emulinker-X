package org.emulinker.kaillera.model.event;

import org.emulinker.kaillera.model.event.KailleraEvent;

public interface KailleraEventListener {
   void actionPerformed(KailleraEvent var1);

   void stop();
}
