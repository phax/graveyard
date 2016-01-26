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

import com.helger.cipa.transport.start.jmsapi.wrappedpeppol.ObjectFactory;
import com.helger.cipa.transport.start.jmsapi.wrappedpeppol.WrappedPeppolType;
import com.helger.commons.GlobalDebug;
import com.helger.commons.io.IReadableResource;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.jaxb.utils.AbstractJAXBMarshaller;

/**
 * The default marshaller for objects of the {@link WrappedPeppolType} type.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class WrappedPeppolMarshaller extends AbstractJAXBMarshaller <WrappedPeppolType>
{
  public static final IReadableResource XSD = new ClassPathResource (CSTARTJMS.XSD_WRAPPED_PEPPOL);

  public WrappedPeppolMarshaller ()
  {
    super (WrappedPeppolType.class, XSD);
    setWriteFormatted (GlobalDebug.isDebugMode ());
  }

  @Override
  @Nonnull
  protected JAXBElement <WrappedPeppolType> wrapObject (final WrappedPeppolType aObject)
  {
    return new ObjectFactory ().createWrappedPeppol (aObject);
  }
}
