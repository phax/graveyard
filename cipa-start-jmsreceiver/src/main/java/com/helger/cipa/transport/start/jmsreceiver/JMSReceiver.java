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
package com.helger.cipa.transport.start.jmsreceiver;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.xml.ws.WebServiceContext;

import org.w3._2009._02.ws_tra.Create;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.helger.cipa.transport.start.jmsapi.PeppolReceiverResponseMarshaller;
import com.helger.cipa.transport.start.jmsapi.WrappedPeppolMarshaller;
import com.helger.cipa.transport.start.jmsapi.peppolreceiverresponse.PeppolReceiverMessageType;
import com.helger.cipa.transport.start.jmsapi.peppolreceiverresponse.PeppolReceiverResponseType;
import com.helger.cipa.transport.start.jmsapi.wrappedpeppol.WrappedPeppolType;
import com.helger.cipa.transport.start.jmsreceiver.config.JMSReceiverConfig;
import com.helger.cipa.transport.start.jmsreceiver.jms.ActiveMQJMSFactorySingleton;
import com.helger.cipa.transport.start.jmsreceiver.jms.JMSResponseHandler;
import com.helger.commons.annotations.IsSPIImplementation;
import com.helger.commons.collections.CollectionHelper;
import com.helger.commons.state.ESuccess;
import com.helger.commons.state.impl.SuccessWithValue;
import com.helger.commons.string.StringHelper;
import com.helger.jms.simple.IJMSMessageCreator;
import com.helger.jms.simple.JMSSimpleSender;
import com.helger.jms.util.JMSXMLUtils;

import eu.europa.ec.cipa.transport.IMessageMetadata;
import eu.europa.ec.cipa.transport.start.server.AccessPointReceiveError;
import eu.europa.ec.cipa.transport.start.server.IAccessPointServiceReceiverSPI;

/**
 * This is the main SPI interface implementation that retrieves incoming PEPPOL
 * messages and puts it into a JMS queue. It is invoked by the START server when
 * a new document arrives.
 *
 * @author Philip Helger
 */
@IsSPIImplementation
public final class JMSReceiver implements IAccessPointServiceReceiverSPI
{
  public JMSReceiver ()
  {
    // Do some initialization
    JMSReceiverConfig.validateConfiguration ();
  }

  /**
   * Create the WrappedPeppol object
   *
   * @param aMetadata
   *        The request metadata
   * @param aMessageElement
   *        The payload XML element
   * @param aLogger
   *        The error logger
   * @return <code>null</code> in case of an error
   */
  @Nullable
  private WrappedPeppolType _createWrappedPeppol (@Nonnull final IMessageMetadata aMetadata,
                                                  @Nonnull final Element aMessageElement,
                                                  @Nonnull final AccessPointReceiveError aLogger)
  {
    // Validate data
    if (aMetadata.getSenderID () == null)
    {
      aLogger.error ("Metadata is missing sender ID!");
      return null;
    }
    if (aMetadata.getRecipientID () == null)
    {
      aLogger.error ("Metadata is missing recipient ID!");
      return null;
    }
    if (aMetadata.getDocumentTypeID () == null)
    {
      aLogger.error ("Metadata is missing document type ID!");
      return null;
    }
    if (aMetadata.getProcessID () == null)
    {
      aLogger.error ("Metadata is missing process ID!");
      return null;
    }

    // Create the WrappedPeppol document which will be put in the JMS queue
    final WrappedPeppolType ret = new WrappedPeppolType ();
    // No endpoint URL is needed - this is only relevant for the sending mode,
    // not for the receiving mode.
    ret.setEndpointURL (null);
    ret.setMessageID (aMetadata.getMessageID ());
    ret.setSenderID (aMetadata.getSenderID ().getURIEncoded ());
    ret.setRecipientID (aMetadata.getRecipientID ().getURIEncoded ());
    ret.setDocumentTypeID (aMetadata.getDocumentTypeID ().getURIEncoded ());
    ret.setProcessID (aMetadata.getProcessID ().getURIEncoded ());
    ret.setChannelID (aMetadata.getChannelID ());
    ret.setAny (aMessageElement);
    return ret;
  }

  @Nullable
  private WrappedPeppolType _extractWrappedPeppol (final IMessageMetadata aMetadata,
                                                   final Create aBody,
                                                   final AccessPointReceiveError aLogger)
  {
    WrappedPeppolType aWrappedPeppol = null;
    {
      final List <Object> aObjects = aBody.getAny ();
      if (CollectionHelper.getSize (aObjects) == 1)
      {
        // It must be an Element
        final Element aMessageElement = (Element) aObjects.iterator ().next ();
        aWrappedPeppol = _createWrappedPeppol (aMetadata, aMessageElement, aLogger);
      }
      else
      {
        aLogger.error ("The received message contains not exactly one element but " +
                       CollectionHelper.getSize (aObjects) +
                       "!");
      }
      if (aWrappedPeppol != null)
        aLogger.info ("Successfully created WrappedPeppol document");
    }
    return aWrappedPeppol;
  }

  @Nonnull
  public SuccessWithValue <AccessPointReceiveError> receiveDocument (@Nonnull final WebServiceContext aWebServiceContext,
                                                                     @Nonnull final IMessageMetadata aMetadata,
                                                                     @Nonnull final Create aBody)
  {
    final AccessPointReceiveError aLogger = new AccessPointReceiveError ();

    // 1. create the WrappedPeppol document from the passed data
    final WrappedPeppolType aWrappedPeppol = _extractWrappedPeppol (aMetadata, aBody, aLogger);
    if (aWrappedPeppol == null)
      return SuccessWithValue.createFailure (aLogger);

    // 2. put XML in the inbox queue
    final String sJMSCorrelationID = UUID.randomUUID ().toString ();
    {
      final Document aWrappedPeppolDoc = new WrappedPeppolMarshaller ().write (aWrappedPeppol);
      if (aWrappedPeppolDoc == null)
      {
        aLogger.error ("Failed to convert the WrappedPeppol document to XML");
        return SuccessWithValue.createFailure (aLogger);
      }

      // The request-response-correlation ID
      final IJMSMessageCreator aMessageCreator = new IJMSMessageCreator ()
      {
        @Nonnull
        public Message createMessage (@Nonnull final Session aSession) throws JMSException
        {
          // File the message content
          final Message aMsg = JMSXMLUtils.createMessageForXML (aSession, aWrappedPeppolDoc);

          // The correlation ID must be repeated by the handler of the message
          aMsg.setJMSCorrelationID (sJMSCorrelationID);
          // Send the response to this queue
          aMsg.setJMSReplyTo (JMSResponseHandler.getInstance ().getJMSDestination ());
          return aMsg;
        }
      };
      final JMSSimpleSender aHandler = new JMSSimpleSender (ActiveMQJMSFactorySingleton.getInstance ().getFactory ());
      final ESuccess eSendSuccess = aHandler.sendNonTransactional (JMSReceiverConfig.getInboxQueueName (),
                                                                   aMessageCreator);
      if (eSendSuccess.isFailure ())
      {
        aLogger.error ("Failed to send the WrappedPeppol document to queue '" +
                       JMSReceiverConfig.getInboxQueueName () +
                       "'");
        return SuccessWithValue.createFailure (aLogger);
      }
      aLogger.info ("Successfully transmitted WrappedPeppol document to queue '" +
                    JMSReceiverConfig.getInboxQueueName () +
                    "'");
    }

    // 3. wait synchronously for the response message
    final Message aResponse = JMSResponseHandler.getInstance ().waitForResponse (sJMSCorrelationID);
    {
      if (aResponse == null)
      {
        aLogger.error ("Failed to get the PeppolReceiverResponse document from queue '" +
                       JMSReceiverConfig.getResponseQueueName () +
                       "' within the timeout of " +
                       JMSReceiverConfig.getResponseTimeoutMilliSeconds () +
                       " milli seconds");
        return SuccessWithValue.createFailure (aLogger);
      }
      if (!(aResponse instanceof BytesMessage))
      {
        aLogger.error ("The response message from queue '" +
                       JMSReceiverConfig.getResponseQueueName () +
                       "' is not a BytesMessage but a " +
                       aResponse.getClass ().getName ());
        return SuccessWithValue.createFailure (aLogger);
      }
      aLogger.info ("Successfully retrieved the JMS response message from queue '" +
                    JMSReceiverConfig.getResponseQueueName () +
                    "'");
    }

    // 4. read the response as a PeppolReceiverResponseType
    PeppolReceiverResponseType aResponseObj;
    {
      // 4.1. interpret message as XML
      final Document aResponseDoc = JMSXMLUtils.createXMLFromMessage ((BytesMessage) aResponse);
      if (aResponseDoc == null)
      {
        aLogger.error ("Error interpreting the JMS response message as XML");
        return SuccessWithValue.createFailure (aLogger);
      }
      // 4.2. convert XML to domain object
      aResponseObj = new PeppolReceiverResponseMarshaller ().read (aResponseDoc);
      if (aResponseObj == null)
      {
        aLogger.error ("Error converting the content of the JMS response message to an XML document");
        return SuccessWithValue.createFailure (aLogger);
      }
      aLogger.info ("Successfully interpreted the JMS response message as a PeppolReceiverResponse document");
    }

    // 5. convert the response to an AccessPointReceiveError
    for (final PeppolReceiverMessageType aMessage : aResponseObj.getMessage ())
    {
      String sMessage = aMessage.getText ();
      if (StringHelper.hasText (aMessage.getException ()))
        sMessage += " caused by " + aMessage.getException ();

      switch (aMessage.getLevel ())
      {
        case INFO:
          aLogger.info (sMessage);
          break;
        case WARN:
          aLogger.warn (sMessage);
          break;
        case ERROR:
          aLogger.error (sMessage);
          break;
        default:
          // Just in case :)
          aLogger.error ("!!Unsupported message level!! " + sMessage);
          break;
      }
    }
    if (aResponseObj.isSuccess ())
      return SuccessWithValue.createSuccess (aLogger);
    return SuccessWithValue.createFailure (aLogger);
  }
}
