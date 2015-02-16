package org.emulinker.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class EmuLinkerXMLConfig extends XMLConfiguration {
   public EmuLinkerXMLConfig() throws ConfigurationException {
      super(EmuLinkerXMLConfig.class.getResource("/emulinker.xml"));
      this.setThrowExceptionOnMissing(true);
   }
}
