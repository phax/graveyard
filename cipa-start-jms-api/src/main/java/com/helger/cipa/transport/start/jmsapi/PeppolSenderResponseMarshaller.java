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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.bind.JAXBElement;

import com.helger.cipa.transport.start.jmsapi.peppolsenderresponse.ObjectFactory;
import com.helger.cipa.transport.start.jmsapi.peppolsenderresponse.PeppolSenderResponseType;
import com.helger.commons.GlobalDebug;
import com.helger.commons.io.IReadableResource;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.jaxb.utils.AbstractJAXBMarshaller;

/**
 * The default marshaller for objects of the {@link PeppolSenderResponseType}
 * type.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class PeppolSenderResponseMarshaller extends AbstractJAXBMarshaller <PeppolSenderResponseType>
{
  public static final IReadableResource XSD = new ClassPathResource (CSTARTJMS.XSD_PEPPOL_SENDER_RESPONSE);

  public PeppolSenderResponseMarshaller ()
  {
    super (PeppolSenderResponseType.class, XSD);
    setWriteFormatted (GlobalDebug.isDebugMode ());
  }

  @Override
  @Nonnull
  protected JAXBElement <PeppolSenderResponseType> wrapObject (final PeppolSenderResponseType aObject)
  {
    return new ObjectFactory ().createPeppolSenderResponse (aObject);
  }
}
