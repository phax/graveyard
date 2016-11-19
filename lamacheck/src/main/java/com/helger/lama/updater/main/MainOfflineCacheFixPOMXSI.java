/**
 * Copyright (C) 2011-2014 Philip Helger <philip[at]helger[dot]com>
 * All Rights Reserved
 *
 * This file is part of the LaMaCheck service.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.helger.lama.updater.main;

import java.io.File;

import com.helger.commons.charset.CCharset;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.lama.updater.utils.OfflineCache;
import com.helger.photon.basic.app.io.WebFileIO;

public final class MainOfflineCacheFixPOMXSI
{
  public static void main (final String [] args) throws Exception
  {
    LamaRunner.run ( () -> {
      final File aBaseDir = WebFileIO.getDataIO ().getFile (OfflineCache.DIR_OFFLINE_CACHE);
      for (final File aFile : new FileSystemRecursiveIterator (aBaseDir))
        if (aFile.getName ().endsWith (".pom"))
        {
          final String sFile = SimpleFileIO.getFileAsString (aFile, CCharset.CHARSET_UTF_8_OBJ);
          final int nIndex = sFile.indexOf ("xsi:schemaLocation");
          if (nIndex >= 0)
          {
            final int nIndex2 = sFile.indexOf ("xmlns:xsi=");
            if (nIndex2 < 0)
            {
              final String sNewFile1 = sFile.substring (0, nIndex) +
                                       "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                                       sFile.substring (nIndex);
              SimpleFileIO.writeFile (aFile, sNewFile1, CCharset.CHARSET_UTF_8_OBJ);
              System.out.println (aFile.getAbsolutePath ());
            }
            else
            {
              final String s = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"xsi";
              final int nIndex3 = sFile.indexOf (s);
              if (nIndex3 >= 0)
              {
                final String sNewFile2 = sFile.substring (0, nIndex3 + s.length () - 3) + " " + sFile.substring (nIndex3 + s.length () - 3);
                SimpleFileIO.writeFile (aFile, sNewFile2, CCharset.CHARSET_UTF_8_OBJ);
                System.out.println (aFile.getAbsolutePath ());
              }
            }
          }
        }
    });
  }
}
