package org.emulinker.kaillera.model.impl;

import org.emulinker.kaillera.model.KailleraGame;
import org.emulinker.kaillera.model.impl.AutoFireDetector;
import org.emulinker.kaillera.model.impl.AutoFireDetectorFactory;
import org.emulinker.kaillera.model.impl.AutoFireScanner2;

public class AutoFireDetectorFactoryImpl implements AutoFireDetectorFactory {
   public AutoFireDetector getInstance(KailleraGame game, int defaultSensitivity) {
      return new AutoFireScanner2(game, defaultSensitivity);
   }
}
