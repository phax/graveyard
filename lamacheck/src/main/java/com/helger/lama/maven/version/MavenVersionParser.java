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
package com.helger.lama.maven.version;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.ELockType;
import com.helger.commons.annotation.IsLocked;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.cache.AbstractNotifyingCache;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.StringParser;
import com.helger.commons.version.Version;

@ThreadSafe
public final class MavenVersionParser
{
  // Use a non-empty string with an as-small-as-possible value :)
  public static final String RELEASE_QUALIFIER = "\u0001";
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenVersionParser.class);
  private static final AbstractNotifyingCache <String, Version> s_aCache = new AbstractNotifyingCache <String, Version> ("MavenVersion")
  {
    @Override
    @Nonnull
    @IsLocked (ELockType.WRITE)
    protected Version getValueToCache (@Nullable final String sVersion)
    {
      return _parse (sVersion);
    }
  };

  private MavenVersionParser ()
  {}

  private static void _scanWithSeparator (final String sPart,
                                          final char cSep,
                                          @Nonnull final List <String> aResultParts)
  {
    final String [] aDashParts = StringHelper.getExplodedArray (cSep, sPart);
    for (int i = 0; i < aDashParts.length; ++i)
    {
      final String sDashPart = aDashParts[i];
      if (StringParser.isUnsignedInt (sDashPart))
        aResultParts.add (sDashPart);
      else
      {
        // take the rest into one part
        final StringBuilder aRest = new StringBuilder (sDashPart);
        while (++i < aDashParts.length)
          aRest.append (cSep).append (aDashParts[i]);
        aResultParts.add (aRest.toString ());
      }
    }
  }

  /**
   * Handle Maven versions. Examples:
   * <ul>
   * <li>2.2-beta-5</li>
   * </ul>
   *
   * @param sOriginalVersion
   * @return The parsed version
   */
  @Nonnull
  private static Version _parse (@Nonnull final String sOriginalVersion)// NOPMD
  {
    Version ret;
    // Special constants from Maven2!
    if (sOriginalVersion.equals ("LATEST"))
      ret = new Version (Integer.MAX_VALUE);
    else
      if (sOriginalVersion.equals ("RELEASE"))
        ret = new Version (Integer.MAX_VALUE - 1);
      else
        // Is it a date version (e.g. 20050911)?
        if (sOriginalVersion.length () == 8 && StringParser.isUnsignedInt (sOriginalVersion))
        {
          // year.month.day
          ret = Version.parse (sOriginalVersion.substring (0, 4) +
                               "." +
                               sOriginalVersion.substring (4, 6) +
                               "." +
                               sOriginalVersion.substring (6, 8));
        }
        else
          // Is it a date version (e.g. v20130227)?
          if (sOriginalVersion.length () == 9 &&
              sOriginalVersion.charAt (0) == 'v' &&
              StringParser.isUnsignedInt (sOriginalVersion.substring (1)))
          {
            // year.month.day
            ret = Version.parse (sOriginalVersion.substring (1, 5) +
                                 "." +
                                 sOriginalVersion.substring (5, 7) +
                                 "." +
                                 sOriginalVersion.substring (7, 9));
          }
          else
            if (sOriginalVersion.length () > 8 &&
                StringParser.isUnsignedInt (sOriginalVersion.substring (0, 8)) &&
                RegExHelper.stringMatchesPattern ("[0-9]+(\\.[0-9]+)+", sOriginalVersion))
            {
              // Very old fashioned - assume very early version!
              // Example from commons-configuration: "20041012.002804"
              ret = Version.parse ("0.0." + sOriginalVersion);
            }
            else
            {
              ret = Version.parse (sOriginalVersion);
              if (!ret.getAsString ().equals (sOriginalVersion))
              {
                // Not a simple version -> manual intervention
                // -> Use only 3 tokens otherwise problem with parsing
                // "2.0.0-M2.1"
                final String [] aDotParts = StringHelper.getExplodedArray ('.', sOriginalVersion, 3);
                final ICommonsList <String> aResultParts = new CommonsArrayList<> ();
                for (int i = 0; i < aDotParts.length; ++i)
                {
                  final String sPart = aDotParts[i];

                  // Any custom separator that is used
                  char cSep = 0;

                  // Number of found parts before this element is scanned
                  final int nPrevResultPartCount = aResultParts.size ();
                  if (StringParser.isUnsignedInt (sPart))
                    aResultParts.add (sPart);
                  else
                    if (sPart.contains ("-"))
                    {
                      cSep = '-';
                      _scanWithSeparator (sPart, cSep, aResultParts);
                    }
                    else
                      if (sPart.contains ("_"))
                      {
                        cSep = '_';
                        _scanWithSeparator (sPart, cSep, aResultParts);
                      }
                      else
                        if (sPart.startsWith ("r") && aDotParts.length == 1)
                        {
                          // Google closure-compiler: "r916" or "r1554M"
                          final StringBuilder aNum = new StringBuilder ();
                          String sEnd = null;
                          for (int nRIdx = 1; nRIdx < sPart.length (); ++nRIdx)
                          {
                            final char c = sPart.charAt (nRIdx);
                            if (Character.isDigit (c))
                              aNum.append (c);
                            else
                            {
                              sEnd = sPart.substring (nRIdx);
                              break;
                            }
                          }
                          final String sNum = aNum.toString ();
                          final int nValue = StringParser.parseInt (sNum, -1);
                          if (nValue != -1)
                          {
                            aResultParts.add (Integer.toString (nValue / 100));
                            aResultParts.add (Integer.toString ((nValue / 10) % 10));
                            aResultParts.add (Integer.toString (nValue % 10));

                            // Add the trailer (e.g. "M")
                            if (StringHelper.hasText (sEnd))
                              aResultParts.add (sEnd);
                          }
                          else
                            aResultParts.add (sPart);
                        }
                        else
                        {
                          // Required e.g. for the "1.7R2" version on Rhino :)
                          aResultParts.add (sPart);
                        }

                  if (aResultParts.size () > 4)
                  {
                    // Too many parts - make the last part a single part
                    final ICommonsList <String> aOldParts = new CommonsArrayList<> (aResultParts);

                    // Remove all added parts of this main part
                    while (aResultParts.size () > nPrevResultPartCount)
                      aResultParts.remove (nPrevResultPartCount);

                    if (cSep != 0)
                    {
                      // Split with only the possible remaining parts
                      final String [] aRestParts = StringHelper.getExplodedArray (cSep,
                                                                                  sPart,
                                                                                  4 - nPrevResultPartCount);
                      for (final String sRest : aRestParts)
                        aResultParts.add (sRest);
                    }
                    else
                      aResultParts.add (sPart);

                    // Add all remaining main separator texts
                    final StringBuilder aRest = new StringBuilder ();
                    for (int j = i + 1; j < aDotParts.length; ++j)
                      aRest.append ('.').append (aDotParts[j]);
                    aResultParts.set (aResultParts.size () -
                                      1,
                                      aResultParts.get (aResultParts.size () - 1) + aRest.toString ());

                    if (s_aLogger.isDebugEnabled ())
                      s_aLogger.debug ("Parsed version " +
                                       aOldParts +
                                       " contains too many fragments (" +
                                       sOriginalVersion +
                                       ") - merged to " +
                                       aResultParts);
                    break;
                  }
                }

                // Check how many leading numbers are present
                int nLeadingNumberCount = 0;
                for (final String sPart : aResultParts)
                  if (StringParser.isUnsignedInt (sPart))
                    nLeadingNumberCount++;
                  else
                    break;

                if (nLeadingNumberCount == aResultParts.size ())
                {
                  // Version only consisting of numbers!
                  ret = Version.parse (StringHelper.getImploded ('.', aResultParts));
                }
                else
                  if (nLeadingNumberCount > 0 && nLeadingNumberCount == aResultParts.size () - 1)
                  {
                    // Only the last part is not a number!
                    if (aResultParts.size () == 2)
                    {
                      ret = new Version (StringParser.parseInt (aResultParts.get (0), -1), 0, 0, aResultParts.get (1));
                    }
                    else
                      if (aResultParts.size () == 3)
                      {
                        ret = new Version (StringParser.parseInt (aResultParts.get (0), -1),
                                           StringParser.parseInt (aResultParts.get (1), -1),
                                           0,
                                           aResultParts.get (2));
                      }
                      else
                        if (aResultParts.size () == 4)
                        {
                          ret = new Version (StringParser.parseInt (aResultParts.get (0), -1),
                                             StringParser.parseInt (aResultParts.get (1), -1),
                                             StringParser.parseInt (aResultParts.get (2), -1),
                                             aResultParts.get (3));
                        }
                        else
                          throw new IllegalStateException (aResultParts.toString ());
                  }
                  else
                  {
                    if (nLeadingNumberCount == 1)
                    {
                      // Happens for "1.5R4.1"
                      // -> Use whatever is numeric and take the rest from the
                      // original version!
                      final String sMajor = aResultParts.get (0);
                      final String sQualifier = sOriginalVersion.substring (sMajor.length () + 1);
                      ret = new Version (StringParser.parseInt (sMajor, -1), 0, 0, sQualifier);
                    }
                    else
                      if (nLeadingNumberCount == 2)
                      {
                        // Happens for "1.0-dev-2.20021231.045254" or
                        // "1.0-incubating-M2.1"
                        // -> Use whatever is numeric and take the rest from the
                        // original version!
                        final String sMajor = aResultParts.get (0);
                        final String sMinor = aResultParts.get (1);
                        final String sQualifier = sOriginalVersion.substring (sMajor.length () +
                                                                              1 +
                                                                              sMinor.length () +
                                                                              1);
                        ret = new Version (StringParser.parseInt (sMajor, -1),
                                           StringParser.parseInt (sMinor, -1),
                                           0,
                                           sQualifier);
                      }
                      else
                      {
                        // Too lazy to handle it now but we don't want an error
                        final String sMsg = "Failed to parse version '" +
                                            sOriginalVersion +
                                            "' (" +
                                            ret.getAsString () +
                                            "): " +
                                            aResultParts;
                        if (true)
                          s_aLogger.warn (sMsg);
                        else
                          throw new IllegalArgumentException (sMsg);

                        // I don't want to handle this right now....
                        if (false && aResultParts.size () < 2)
                          throw new IllegalArgumentException ("Parsed version " +
                                                              aResultParts +
                                                              " contains no fragments (" +
                                                              sOriginalVersion +
                                                              ")");
                        ret = Version.parse ("0.0.0." + sOriginalVersion);
                      }
                  }
              }
            }

    // Avoid listing "1.6.0.alpha2" as an update of "1.6.0"
    if (ret.getQualifier () == null)
      ret = new Version (ret.getMajor (), ret.getMinor (), ret.getMicro (), RELEASE_QUALIFIER);

    if (false)
      s_aLogger.info ("Parsed '" + sOriginalVersion + "' to " + ret.getAsString ());

    return ret;
  }

  @Nonnull
  public static Version parse (@Nonnull final String sVersion)
  {
    if (sVersion == null)
      throw new NullPointerException ("version");

    return s_aCache.getFromCache (sVersion);
  }

  @Nonnull
  @Nonempty
  public static String asString (@Nonnull final Version aVersion)
  {
    if (aVersion == null)
      throw new NullPointerException ("version");

    final String sString = aVersion.getAsString ();
    if (RELEASE_QUALIFIER.equals (aVersion.getQualifier ()))
      return sString.substring (0, sString.length () - (1 + RELEASE_QUALIFIER.length ()));
    return sString;
  }
}
