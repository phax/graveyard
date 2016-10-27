/**
 * Free Message Converter Copyleft 2007 - 2014 Matthias Fricke mf@sapstern.com
 * Copyright (C) 2016 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openedi.unece.xsd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.openedi.unece.xsd.data.CompositeDataDefinition;
import com.openedi.unece.xsd.data.CompositeDefinition;
import com.openedi.unece.xsd.data.SegmentData;
import com.openedi.unece.xsd.data.SegmentStructureData;
import com.openedi.unece.xsd.data.SegmentStructureElement;
import com.openedi.unece.xsd.data.TypeDef;
import com.openedi.unece.xsd.sax.SaxParserUNECEToXSD;
import com.openedi.unece.xsd.utils.StringChecker;

public class XSDFileGenerator
{

  private static String theProjectDirectory = "C:/Users/matthias/workspaceOtto/EDIFACT_DEVEL_UNECE/UNECE";
  private static String theMessageDirectory = theProjectDirectory + "/<VERSION>/IN/EDMD";
  private static String theDirectory = theProjectDirectory + "/<VERSION>/IN";
  private static String messageType = "EDIFACTINTERCHANGE";
  private static String theVersion = "11B";

  /**
   * @param args
   */
  public static void main (final String [] args)
  {

    parseArgs (args);
    theMessageDirectory = theMessageDirectory.replace ("<VERSION>", theVersion);
    theDirectory = theDirectory.replace ("<VERSION>", theVersion);
    final File theDirectoryFile = new File (theMessageDirectory);
    final String [] arryFileNames = theDirectoryFile.list ();
    for (final String arryFileName : arryFileNames)
    {
      // System.out.println(arryFileNames[z]);
      if (arryFileName.equalsIgnoreCase ("CVS"))
        continue;
      process (arryFileName);
    }

  }

  private static void process (final String s)
  {
    String theMessageName = s;
    final List <String> hierarcyList = new LinkedList<> ();

    try (final BufferedReader in = new BufferedReader (new FileReader (theMessageDirectory + "/" + theMessageName)))
    {
      theMessageName = theMessageName.replace ("D.", "");
      boolean isProcessing = false;
      String line = null;
      while (true)
      {
        line = in.readLine ();
        if (line != null)
        {
          if (line.startsWith ("0010   UNH Message header                            M   1"))
            isProcessing = true;
          if (!isProcessing)
            continue;
          if (line.length () < 4)
            continue;

          try
          {
            final String theNumberToken = line.substring (0, 3);
            Integer.parseInt (theNumberToken);
            hierarcyList.add (line);
          }
          catch (final NumberFormatException ignore)
          {
            if (line.length () > 54)
            {
              if (line.substring (53, 54).equals ("M") || line.substring (53, 54).equals ("C"))
                hierarcyList.add (line);
            }
          }
        }
        else
          break;
      }
    }
    catch (final IOException ioe)
    {
      ioe.printStackTrace ();
    }

    final List <SegmentData> theSegmentList = new LinkedList<> ();
    // data.theNameOfSegmentGroup = theLevelTab.get(data.theLevel);
    final Hashtable <Integer, String> theLevelTab = new Hashtable<> ();
    theLevelTab.put (Integer.valueOf (0), "M_" + theMessageName);
    for (int i = 0; i < hierarcyList.size (); i++)
    {
      String line = hierarcyList.get (i);
      final SegmentData data = new SegmentData ();

      data.theSegmentDescr = line.substring (11, 52).trim ();
      if (line.substring (53, 54).equals ("M"))
        data.isMandatory = true;
      else
        data.isMandatory = false;
      final String theSegmentName = line.substring (7, 10);
      try
      {
        setCardinality (line, data);
      }
      catch (final NumberFormatException ignore)
      {
        i++;
        line = hierarcyList.get (i);
        setCardinality (line, data);
      }
      final int theFirstLevelChar = line.indexOf ("|");
      final int theFirstPlusIndex = line.indexOf ("+");
      if (theFirstLevelChar == -1 && theFirstPlusIndex == -1)
        data.theLevel = 0;
      else
        if (theFirstPlusIndex != -1)
        {
          final String theLevelTmp = line.substring (theFirstPlusIndex, line.length ());
          data.theLevel = theLevelTmp.length ();
        }
        else
        {
          final String theLevelTmp = line.substring (theFirstLevelChar, line.length ());
          data.theLevel = theLevelTmp.length ();
        }
      if (line.substring (17, 30).equals ("Segment group"))
      {
        data.theLevel--;
        // Fehler: Groupnummer mehrstelli
        String theGroupNo = null;
        if (line.substring (33, 34).equals (" "))
          theGroupNo = line.substring (31, 34);
        else
          theGroupNo = line.substring (31, 33);
        final int theGroupNumber = Integer.parseInt (theGroupNo.trim ());
        data.theSegmentName = "G_" + theMessageName + "_SG" + theGroupNumber;

        data.theNameOfSegmentGroup = theLevelTab.get (Integer.valueOf (data.theLevel));
        final int newlevel = data.theLevel + 1;
        theLevelTab.put (Integer.valueOf (newlevel), data.theSegmentName);
      }
      else
      {
        data.theSegmentName = "S_" + theSegmentName;
        data.theNameOfSegmentGroup = theLevelTab.get (Integer.valueOf (data.theLevel));
      }

      theSegmentList.add (data);
      System.out.println (data.theSegmentName +
                          " " +
                          data.theSegmentDescr +
                          " " +
                          data.isMandatory +
                          " " +
                          data.theCardinality +
                          " " +
                          data.theLevel +
                          " " +
                          data.theNameOfSegmentGroup);

    }
    // Hole Alle Segmente
    Map <String, SegmentStructureData> theSegmentTab = getSegmentStructureTab (theDirectory +
                                                                               "/EDSD." +
                                                                               theVersion,
                                                                               null);
    theSegmentTab = getSegmentStructureTab (theDirectory + "/EDSD.XXX", theSegmentTab);
    // Hole die Segmente fuer den aktuellen Nachrichtentyp
    final List <SegmentStructureData> theSegmentStructureList = getSegmentStructureDataList (theSegmentTab,
                                                                                             theSegmentList);
    // Hole alle Composite Typen
    Map <String, CompositeDefinition> theCompositeElementTab = getCompositeElementsTab (theDirectory +
                                                                                        "/EDCD." +
                                                                                        theVersion,
                                                                                        null);
    theCompositeElementTab = getCompositeElementsTab (theDirectory + "/EDCD.XXX", theCompositeElementTab);
    // Hole alle Datenelemnte
    Map <String, Map <String, String>> theDataElementTab = getDataElementsTab (theDirectory +
                                                                               "/EDED." +
                                                                               theVersion,
                                                                               null);
    theDataElementTab = getDataElementsTab (theDirectory + "/EDED.XXX", theDataElementTab);

    try (BufferedWriter out = new BufferedWriter (new FileWriter (theProjectDirectory +
                                                                  "/" +
                                                                  theVersion +
                                                                  "/OUT/" +
                                                                  messageType +
                                                                  "_" +
                                                                  theMessageName +
                                                                  ".xsd")))
    {
      final SaxParserUNECEToXSD parser = new SaxParserUNECEToXSD (theMessageName);
      parser.setMessageName (theMessageName);
      parser.setMessageType (messageType);
      parser.setTheSegmentList (theSegmentList);
      parser.setTheSegmentStructureList (theSegmentStructureList);
      parser.setTheSegmentTab (theSegmentTab);
      parser.setTheCompositeTab (theCompositeElementTab);
      parser.setTheDataElementTab (theDataElementTab);

      final String result = parser.parseUNECE ();

      out.write (result.toCharArray ());
      out.flush ();
    }
    catch (final SAXException | TransformerException | IOException ioe)
    {
      // TODO Auto-generated catch block
      ioe.printStackTrace ();
    }
  }

  /**
   * @param line
   * @param data
   */
  private static void setCardinality (final String line, final SegmentData data) throws NumberFormatException
  {
    String theNumberTemp = line.substring (57, 63);
    theNumberTemp = theNumberTemp.replaceAll ("-", "");
    theNumberTemp = theNumberTemp.replaceAll ("Ä", "");
    data.theCardinality = Integer.parseInt (theNumberTemp.trim ());
  }

  /**
   * @param theSegmentTab
   * @param theSegmentList
   * @return
   */
  private static List <SegmentStructureData> getSegmentStructureDataList (final Map <String, SegmentStructureData> theSegmentTab,
                                                                          final List <SegmentData> theSegmentList)
  {
    final List <SegmentStructureData> theList = new LinkedList<> ();
    final Iterator <SegmentData> theIterator = theSegmentList.iterator ();
    while (theIterator.hasNext ())
    {
      final SegmentData theSegment = theIterator.next ();

      if (theSegmentTab.get (theSegment.theSegmentName) == null)
      {
        // System.out.println("Not found in Tab: "+theSegment.theSegmentName);
        continue;
      }

      final SegmentStructureData theSegmentStructure = theSegmentTab.get (theSegment.theSegmentName);
      // System.out.println(theSegment.theSegmentName);
      if (!theList.contains (theSegmentStructure))
        theList.add (theSegmentStructure);
    }
    return theList;
  }

  /**
   * @param theFileName
   * @param theTab
   * @return
   */
  private static Map <String, SegmentStructureData> getSegmentStructureTab (final String theFileName,
                                                                            final Map <String, SegmentStructureData> theTab)
  {
    Map <String, SegmentStructureData> theSegmentTab = null;
    if (theTab == null)
      theSegmentTab = new Hashtable<> ();
    else
      theSegmentTab = theTab;

    try (final BufferedReader in = new BufferedReader (new FileReader (theFileName)))
    {
      String line = null;

      SegmentStructureData theData = null;
      String theSegmentKey = null;
      // String theCompositeElemKey = null;
      while (true)
      {
        line = in.readLine ();
        if (line != null)
        {
          if (line.length () < 11)
            continue;
          final String theSegmentNameToken = line.substring (7, 11);
          // if (theSegmentNameToken.contains("UNZ"))
          // System.out.println(line);
          if (StringChecker.containsAll (TypeDef.SEGMENT_NAME_CHARS_STRING, theSegmentNameToken.toCharArray ()) &&
              theSegmentNameToken.endsWith (" ") &&
              !theSegmentNameToken.trim ().equals (""))
          {
            if (theData != null)
              theSegmentTab.put (theSegmentKey, theData);

            theData = new SegmentStructureData ();
            theSegmentKey = "S_" + theSegmentNameToken.trim ();
            theData.theNameOfSegment = theSegmentKey;
            theData.theDescription = line.substring (12);
            theData.theMemberList = new LinkedList<> ();
            continue;

          }
          try
          {
            final String theNumberToken = line.substring (0, 2);
            Integer.parseInt (theNumberToken);
            final SegmentStructureElement theElement = new SegmentStructureElement ();
            final String theNameToken = line.substring (7, 11);

            if (theNameToken.startsWith ("C") || theNameToken.startsWith ("S"))
            {
              theElement.theName = "C_" + theNameToken;
              theElement.theType = TypeDef.TYPE_COMPOSITE;
            }
            else
            {
              theElement.theName = "D_" + theNameToken;
              theElement.theType = TypeDef.TYPE_DATAELEMENT;
            }
            if (line.length () < 56)
              line = in.readLine ();
            if (line.substring (55, 56).equals ("M"))
              theElement.isMandatory = "1";
            else
              theElement.isMandatory = "0";

            theData.theMemberList.add (theElement);
          }
          catch (final NumberFormatException ignore)
          {}
        }
        else
          break;
      }
      if (theData != null)
        theSegmentTab.put (theSegmentKey, theData);

    }
    catch (final IOException ioe)
    {
      ioe.printStackTrace ();
    }

    return theSegmentTab;
  }

  /**
   * @return
   */
  private static Map <String, CompositeDefinition> getCompositeElementsTab (final String theFileName,
                                                                            final Map <String, CompositeDefinition> theTab)
  {
    Map <String, CompositeDefinition> theGlobCompositeTab = null;
    if (theTab == null)
      theGlobCompositeTab = new Hashtable<> ();
    else
      theGlobCompositeTab = theTab;
    try (final BufferedReader in = new BufferedReader (new FileReader (theFileName)))
    {
      String line = null;

      CompositeDefinition theDefinition = null;
      String theCompositeKey = null;
      // String theCompositeElemKey = null;
      while (true)
      {
        line = in.readLine ();
        if (line != null)
        {
          if (line.length () < 10)
            continue;
          if (line.substring (7, 8).equals ("C") || line.substring (7, 8).equals ("S"))
          {
            if (theDefinition != null)
              theGlobCompositeTab.put (theCompositeKey, theDefinition);

            theDefinition = new CompositeDefinition ();
            theCompositeKey = "C_" + line.substring (7, 11);
            theDefinition.theName = theCompositeKey;
            theDefinition.theDataList = new LinkedList<> ();

          }
          try
          {
            final String theNumberToken = line.substring (7, 11);
            Integer.parseInt (theNumberToken);
            final CompositeDataDefinition theDataDefinitition = new CompositeDataDefinition ();
            theDataDefinitition.theName = "D_" + theNumberToken;

            // System.out.println(line);
            // Bei Zeilenumbruch auf die naechste Zeile wechseln
            if (line.length () < 56)
              setMandatory (in.readLine (), theDataDefinitition);
            else
              setMandatory (line, theDataDefinitition);

            theDefinition.theDataList.add (theDataDefinitition);

          }
          catch (final NumberFormatException ignore)
          {}
          if (line.substring (7, 12).equals ("Desc:"))
          {
            final String theDescription = line.substring (12).trim ();
            theDefinition.theDescription = theDescription;

          }

        }
        else
          break;
      }
      if (theDefinition != null)
        theGlobCompositeTab.put (theCompositeKey, theDefinition);

    }
    catch (final IOException ioe)
    {
      ioe.printStackTrace ();
    }

    return theGlobCompositeTab;
  }

  /**
   * @param line
   * @param theDataDefinitition
   */
  private static void setMandatory (final String line, final CompositeDataDefinition theDataDefinitition)
  {
    if (line.substring (55, 56).equals ("C"))
      theDataDefinitition.isManadatory = "0";
    else
      theDataDefinitition.isManadatory = "1";
  }

  /**
   * @return
   */
  private static Map <String, Map <String, String>> getDataElementsTab (final String theFileName,
                                                                        final Map <String, Map <String, String>> theTab)
  {
    // TODO Auto-generated method stub
    Map <String, Map <String, String>> theGlobTab = null;
    if (theTab == null)
      theGlobTab = new Hashtable<> ();
    else
      theGlobTab = theTab;
    try (final BufferedReader in = new BufferedReader (new FileReader (theFileName)))
    {
      String line = null;
      Hashtable <String, String> theElemTab = null;
      String theTabKey = null;
      while (true)
      {
        line = in.readLine ();
        if (line != null)
        {
          if (line.length () < 10)
            continue;
          String theNumberToken = null;
          try
          {
            theNumberToken = line.substring (5, 9);
            Integer.parseInt (theNumberToken);
            if (theElemTab != null)
            {
              theGlobTab.put (theTabKey, theElemTab);
            }
            theElemTab = new Hashtable<> ();
            theTabKey = "D_" + theNumberToken;
            theElemTab.put ("name", theTabKey);

          }
          catch (final NumberFormatException ignore)
          {
            // Spezialbehandlung UNA Datenelemente
            if (theNumberToken.contains ("UNA"))
            {
              if (theElemTab != null)
              {
                theGlobTab.put (theTabKey, theElemTab);
              }
              theElemTab = new Hashtable<> ();
              theTabKey = "D_" + theNumberToken;
              theElemTab.put ("name", theTabKey);

            }

          }
          setDataElemTab ("Desc", line, theElemTab);
          setDataElemTab ("Repr", line, theElemTab);

        }
        else
          break;
      }
      if (theElemTab != null)
      {
        theGlobTab.put (theTabKey, theElemTab);
      }

    }
    catch (final IOException ioe)
    {
      ioe.printStackTrace ();
    }
    return theGlobTab;
  }

  private static void setDataElemTab (final String tokenName,
                                      final String line,
                                      final Hashtable <String, String> theTab)
  {
    if (line.substring (5, 9).equals (tokenName))
    {
      final String theToken = line.substring (11).trim ();
      theTab.put (tokenName, theToken);

    }
  }

  /**
   * Parst die Argumentenliste (commandline) nach user, password, URL.<BR>
   */
  static void parseArgs (final String args[])
  {
    if ((args == null) || (args.length == 0))
      return;
    for (int i = 0; i < args.length; i++)
    {
      if (args[i].equals ("-directoryName"))
        theProjectDirectory = args[++i];
      if (args[i].equals ("-messageType"))
        messageType = args[++i];
      if (args[i].equals ("-version"))
        theVersion = args[++i];

    }
  }

}
