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
package com.helger.cipa.transport.start.jmssender.jms;

import javax.annotation.Nonnull;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.cipa.transport.start.jmsapi.PeppolSenderResponseMarshaller;
import com.helger.cipa.transport.start.jmsapi.WrappedPeppolMarshaller;
import com.helger.cipa.transport.start.jmsapi.peppolsenderresponse.PeppolSenderErrorType;
import com.helger.cipa.transport.start.jmsapi.peppolsenderresponse.PeppolSenderResponseType;
import com.helger.cipa.transport.start.jmsapi.wrappedpeppol.WrappedPeppolType;
import com.helger.cipa.transport.start.jmssender.config.CSJConfig;
import com.helger.commons.string.StringHelper;
import com.helger.commons.xml.XMLFactory;
import com.helger.commons.xml.transform.TransformSourceFactory;
import com.helger.jms.simple.JMSMessageCreatorXML;
import com.helger.jms.stream.BytesMessageInputStream;

import eu.europa.ec.cipa.peppol.identifier.IdentifierUtils;
import eu.europa.ec.cipa.peppol.sml.ESML;
import eu.europa.ec.cipa.smp.client.ESMPTransportProfile;
import eu.europa.ec.cipa.smp.client.SMPServiceCaller;
import eu.europa.ec.cipa.transport.IMessageMetadata;
import eu.europa.ec.cipa.transport.MessageMetadata;
import eu.europa.ec.cipa.transport.start.client.AccessPointClient;
import eu.europa.ec.cipa.transport.start.client.AccessPointClientSendResult;

public class CSJMessageListener implements MessageListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSJMessageListener.class);

  @Nonnull
  private static PeppolSenderErrorType _createError (final String sErrorMsg)
  {
    final PeppolSenderErrorType aError = new PeppolSenderErrorType ();
    aError.setText (sErrorMsg);
    return aError;
  }

  public void onMessage (@Nonnull final Message aMessage)
  {
    if (!(aMessage instanceof BytesMessage))
      throw new IllegalArgumentException ("Expecting only BytesMessages!");

    final WrappedPeppolType aWrappedPeppol = new WrappedPeppolMarshaller ().read (TransformSourceFactory.create (new BytesMessageInputStream ((BytesMessage) aMessage)));
    if (aWrappedPeppol == null)
      throw new IllegalStateException ("Failed to read WrappedErpel from queue!");

    s_aLogger.info ("Got WrappedPeppol document from queue!");

    final PeppolSenderResponseType aSenderResponse = new PeppolSenderResponseType ();

    // Set message ID anyway - even if it might be null. But if it is present,
    // it helps :)
    aSenderResponse.setMessageID (aWrappedPeppol.getMessageID ());

    if (StringHelper.hasNoText (aWrappedPeppol.getMessageID ()))
      aSenderResponse.getErrorMessage ().add (_createError ("MessageID is missing"));
    if (!IdentifierUtils.isValidParticipantIdentifier (aWrappedPeppol.getSenderID ()))
      aSenderResponse.getErrorMessage ().add (_createError ("SenderID '" +
                                                            aWrappedPeppol.getSenderID () +
                                                            "' is invalid"));
    if (!IdentifierUtils.isValidParticipantIdentifier (aWrappedPeppol.getRecipientID ()))
      aSenderResponse.getErrorMessage ().add (_createError ("RecipientID '" +
                                                            aWrappedPeppol.getRecipientID () +
                                                            "' is invalid"));
    if (!IdentifierUtils.isValidDocumentTypeIdentifier (aWrappedPeppol.getDocumentTypeID ()))
      aSenderResponse.getErrorMessage ().add (_createError ("DocumentTypeID '" +
                                                            aWrappedPeppol.getDocumentTypeID () +
                                                            "' is invalid"));
    if (!IdentifierUtils.isValidProcessIdentifier (aWrappedPeppol.getProcessID ()))
      aSenderResponse.getErrorMessage ().add (_createError ("ProcessID '" +
                                                            aWrappedPeppol.getProcessID () +
                                                            "' is invalid"));
    if (aWrappedPeppol.getAny () == null)
      aSenderResponse.getErrorMessage ().add (_createError ("No PEPPOL document attached"));

    if (aSenderResponse.hasNoErrorMessageEntries ())
    {
      // Create metadata
      final IMessageMetadata aMetadata = new MessageMetadata (aWrappedPeppol.getMessageID (),
                                                              aWrappedPeppol.getChannelID (),
                                                              IdentifierUtils.createParticipantIdentifierFromURIPart (aWrappedPeppol.getSenderID ()),
                                                              IdentifierUtils.createParticipantIdentifierFromURIPart (aWrappedPeppol.getRecipientID ()),
                                                              IdentifierUtils.createDocumentTypeIdentifierFromURIPart (aWrappedPeppol.getDocumentTypeID ()),
                                                              IdentifierUtils.createProcessIdentifierFromURIPart (aWrappedPeppol.getProcessID ()));

      String sEndpointURL = aWrappedPeppol.getEndpointURL ();
      if (StringHelper.hasNoText (sEndpointURL))
      {
        // Lookup in SMP
        try
        {
          sEndpointURL = new SMPServiceCaller (aMetadata.getRecipientID (), ESML.PRODUCTION).getEndpointAddress (aMetadata.getRecipientID (),
                                                                                                                 aMetadata.getDocumentTypeID (),
                                                                                                                 aMetadata.getProcessID (),
                                                                                                                 ESMPTransportProfile.TRANSPORT_PROFILE_START);
          if (StringHelper.hasText (sEndpointURL))
            s_aLogger.info ("  Using SMP retrieved endpoint URI '" + sEndpointURL + "'");
          else
          {
            // No such participant!
            aSenderResponse.getErrorMessage ().add (_createError ("No endpoint URL from SMP for " +
                                                                  aMetadata.getRecipientID ().getURIEncoded () +
                                                                  "/" +
                                                                  aMetadata.getDocumentTypeID ().getURIEncoded () +
                                                                  "/" +
                                                                  aMetadata.getProcessID ().getURIEncoded ()));
          }
        }
        catch (final Exception ex)
        {
          // SMP lookup failed
          aSenderResponse.getErrorMessage ().add (_createError ("Failed to retrieve endpoint address for " +
                                                                aMetadata.getRecipientID ().getURIEncoded () +
                                                                "/" +
                                                                aMetadata.getDocumentTypeID ().getURIEncoded () +
                                                                "/" +
                                                                aMetadata.getProcessID ().getURIEncoded () +
                                                                " - " +
                                                                ex.getMessage ()));
        }
      }
      else
        s_aLogger.info ("  Using provided endpoint URI '" + sEndpointURL + "'");

      if (aSenderResponse.hasNoErrorMessageEntries ())
      {
        // Main sending
        final Document aSendDoc = XMLFactory.newDocument ();
        aSendDoc.appendChild (aSendDoc.adoptNode (aWrappedPeppol.getAny ()));
        final AccessPointClientSendResult aResult = AccessPointClient.send (sEndpointURL, aMetadata, aSendDoc);

        s_aLogger.info ("  Got " + (aResult.isSuccess () ? "success" : "failure") + " from AP START sender");

        // Send message back via JMS
        aSenderResponse.setSuccess (aResult.isSuccess ());
        if (aResult.isFailure ())
        {
          // Create error messages
          for (final String sErrorMsg : aResult.getAllErrorMessages ())
          {
            final PeppolSenderErrorType aError = new PeppolSenderErrorType ();
            aError.setText (sErrorMsg);
            aSenderResponse.getErrorMessage ().add (aError);
          }
        }
      }
    }

    if (aSenderResponse.hasErrorMessageEntries ())
    {
      // Preconditions not matched - message ID of response may stay null!
      aSenderResponse.setSuccess (false);
    }

    // Convert to XML
    final Document aPeppolSenderResponseDoc = new PeppolSenderResponseMarshaller ().write (aSenderResponse);
    if (aPeppolSenderResponseDoc == null)
      throw new IllegalStateException ("Failed to create PeppolSenderResponse XML!");

    // Send into TO_PEPPOL_RESPONSE
    if (new CSJSender ().sendNonTransactional (CSJConfig.getToPeppolResponseQueueName (),
                                               new JMSMessageCreatorXML (aPeppolSenderResponseDoc)).isSuccess ())
    {
      // Store in local queue as well
      s_aLogger.info ("Sent PeppolSenderResponse with " +
                      (aSenderResponse.isSuccess () ? "success" : "error") +
                      " back!");
    }
    else
      s_aLogger.error ("Failed to send PeppolSenderResponse back!");
  }
}
