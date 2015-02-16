package org.emulinker.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.nanocontainer.script.xml.XMLContainerBuilder;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.SimpleReference;

public class PicoUtil {
   public static PicoContainer buildContainer(PicoContainer parentContainer, Object scope, String resourceName) throws Exception {
      InputStream stream = PicoUtil.class.getResourceAsStream(resourceName);
      if(stream == null) {
         throw new IOException("Unable to find or open resource " + resourceName);
      } else {
         InputStreamReader reader = new InputStreamReader(stream);
         XMLContainerBuilder builder = new XMLContainerBuilder(reader, PicoUtil.class.getClassLoader());
         SimpleReference containerRef = new SimpleReference();
         SimpleReference parentContainerRef = new SimpleReference();
         parentContainerRef.set(parentContainer);
         builder.buildContainer(containerRef, parentContainerRef, scope, true);
         return (PicoContainer)containerRef.get();
      }
   }
}
