package org.emulinker.kaillera.model.impl;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.impl.AutoFireDetector;

public interface AutoFireDetectorFactory {
   AutoFireDetector getInstance(KailleraGame var1, int var2);
}
