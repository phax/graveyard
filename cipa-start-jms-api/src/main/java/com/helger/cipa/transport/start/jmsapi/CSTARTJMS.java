/**
 * Copyright (C) 2013-2015 Philip Helger (www.helger.com)
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
package com.helger.cipa.transport.start.jmsapi;

import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotations.PresentForCodeCoverage;

/**
 * This class contains all the constants used in this project.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSTARTJMS
{
  /** The classpath relative path to the PeppolReceiverResponse XML Schema */
  public static final String XSD_PEPPOL_RECEIVER_RESPONSE = "/schemas/peppol-receiver-response.xsd";

  /** The classpath relative path to the PeppolSenderResponse XML Schema */
  public static final String XSD_PEPPOL_SENDER_RESPONSE = "/schemas/peppol-sender-response.xsd";

  /** The classpath relative path to the WrappedPeppol XML Schema */
  public static final String XSD_WRAPPED_PEPPOL = "/schemas/wrapped-peppol.xsd";

  @PresentForCodeCoverage
  private static final CSTARTJMS s_aInstance = new CSTARTJMS ();

  private CSTARTJMS ()
  {}
}
