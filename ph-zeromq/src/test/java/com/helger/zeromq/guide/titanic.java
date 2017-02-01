/*
    Copyright (c) 2007-2014 Contributors as noted in the AUTHORS file

    This file is part of 0MQ.

    0MQ is free software; you can redistribute it and/or modify it under
    the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    0MQ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.helger.zeromq.guide;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;
import org.zeromq.ZThread;
import org.zeromq.ZThread.IAttachedRunnable;
import org.zeromq.ZThread.IDetachedRunnable;

public class titanic
{
  // Return a new UUID as a printable character string
  // Caller must free returned string when finished with it

  static String generateUUID ()
  {
    return UUID.randomUUID ().toString ();
  }

  private static final String TITANIC_DIR = ".titanic";

  // Returns freshly allocated request filename for given UUID
  private static String requestFilename (final String uuid)
  {
    final String filename = String.format ("%s/%s.req", TITANIC_DIR, uuid);
    return filename;
  }

  // Returns freshly allocated reply filename for given UUID
  private static String replyFilename (final String uuid)
  {
    final String filename = String.format ("%s/%s.rep", TITANIC_DIR, uuid);
    return filename;
  }

  // .split Titanic request service
  // The {{titanic.request}} task waits for requests to this service. It writes
  // each request to disk and returns a UUID to the client. The client picks
  // up the reply asynchronously using the {{titanic.reply}} service:

  static class TitanicRequest implements IAttachedRunnable
  {

    @Override
    public void run (final Object [] args, final ZContext ctx, final Socket pipe)
    {
      final mdwrkapi worker = new mdwrkapi ("tcp://localhost:5555", "titanic.request", false);
      ZMsg reply = null;

      while (true)
      {
        // Send reply if it's not null
        // And then get next request from broker
        final ZMsg request = worker.receive (reply);
        if (request == null)
          break; // Interrupted, exit

        // Ensure message directory exists
        new File (TITANIC_DIR).mkdirs ();

        // Generate UUID and save message to disk
        final String uuid = generateUUID ();
        final String filename = requestFilename (uuid);
        try (DataOutputStream file = new DataOutputStream (new FileOutputStream (filename)))
        {
          ZMsg.save (request, file);
        }
        catch (final IOException e)
        {
          e.printStackTrace ();
        }
        request.destroy ();

        // Send UUID through to message queue
        reply = new ZMsg ();
        reply.add (uuid);
        reply.send (pipe);

        // Now send UUID back to client
        // Done by the mdwrk_recv() at the top of the loop
        reply = new ZMsg ();
        reply.add ("200");
        reply.add (uuid);
      }
      worker.destroy ();
    }
  }
  // .split Titanic reply service
  // The {{titanic.reply}} task checks if there's a reply for the specified
  // request (by UUID), and returns a 200 (OK), 300 (Pending), or 400
  // (Unknown) accordingly:

  static class TitanicReply implements IDetachedRunnable
  {

    @Override
    public void run (final Object [] args)
    {
      final mdwrkapi worker = new mdwrkapi ("tcp://localhost:5555", "titanic.reply", false);
      ZMsg reply = null;

      while (true)
      {
        final ZMsg request = worker.receive (reply);
        if (request == null)
          break; // Interrupted, exit

        final String uuid = request.popString ();
        final String reqFilename = requestFilename (uuid);
        final String repFilename = replyFilename (uuid);

        if (new File (repFilename).exists ())
        {
          try (DataInputStream file = new DataInputStream (new FileInputStream (repFilename)))
          {
            reply = ZMsg.load (file);
            reply.push ("200");
          }
          catch (final IOException e)
          {
            e.printStackTrace ();
          }
        }
        else
        {
          reply = new ZMsg ();
          if (new File (reqFilename).exists ())
            reply.push ("300"); // Pending
          else
            reply.push ("400"); // Unknown
        }
        request.destroy ();
      }
      worker.destroy ();
    }
  }

  // .split Titanic close task
  // The {{titanic.close}} task removes any waiting replies for the request
  // (specified by UUID). It's idempotent, so it is safe to call more than
  // once in a row:
  static class TitanicClose implements IDetachedRunnable
  {
    @Override
    public void run (final Object [] args)
    {
      final mdwrkapi worker = new mdwrkapi ("tcp://localhost:5555", "titanic.close", false);
      ZMsg reply = null;

      while (true)
      {
        final ZMsg request = worker.receive (reply);
        if (request == null)
          break; // Interrupted, exit

        final String uuid = request.popString ();
        final String req_filename = requestFilename (uuid);
        final String rep_filename = replyFilename (uuid);
        new File (rep_filename).delete ();
        new File (req_filename).delete ();

        request.destroy ();
        reply = new ZMsg ();
        reply.add ("200");
      }
      worker.destroy ();
    }
  }

  // .split worker task
  // This is the main thread for the Titanic worker. It starts three child
  // threads; for the request, reply, and close services. It then dispatches
  // requests to workers using a simple brute force disk queue. It receives
  // request UUIDs from the {{titanic.request}} service, saves these to a disk
  // file, and then throws each request at MDP workers until it gets a
  // response.

  public static void main (final String [] args)
  {
    final boolean verbose = (args.length > 0 && "-v".equals (args[0]));

    final ZContext ctx = new ZContext ();

    final Socket requestPipe = ZThread.fork (ctx, new TitanicRequest ());
    ZThread.start (new TitanicReply ());
    ZThread.start (new TitanicClose ());

    // Main dispatcher loop
    while (true)
    {
      // We'll dispatch once per second, if there's no activity
      final PollItem items[] = { new PollItem (requestPipe, ZMQ.Poller.POLLIN) };
      final int rc = ZMQ.poll (items, 1, 1000);
      if (rc == -1)
        break; // Interrupted
      if (items[0].isReadable ())
      {
        // Ensure message directory exists
        new File (TITANIC_DIR).mkdirs ();

        // Append UUID to queue, prefixed with '-' for pending
        final ZMsg msg = ZMsg.recvMsg (requestPipe);
        if (msg == null)
          break; // Interrupted
        final String uuid = msg.popString ();
        try (BufferedWriter wfile = new BufferedWriter (new FileWriter (TITANIC_DIR + "/queue", true)))
        {
          wfile.write ("-" + uuid + "\n");
        }
        catch (final IOException e)
        {
          e.printStackTrace ();
          break;
        }
        msg.destroy ();
      }
      // Brute force dispatcher
      final byte [] entry = new byte [37]; // "?........:....:....:....:............:";
      try (RandomAccessFile file = new RandomAccessFile (TITANIC_DIR + "/queue", "rw"))
      {
        while (file.read (entry) > 0)
        {

          // UUID is prefixed with '-' if still waiting
          if (entry[0] == '-')
          {
            if (verbose)
              System.out.printf ("I: processing request %s\n", new String (entry, 1, entry.length - 1, ZMQ.CHARSET));
            if (serviceSuccess (new String (entry, 1, entry.length - 1, ZMQ.CHARSET)))
            {
              // Mark queue entry as processed
              file.seek (file.getFilePointer () - 37);
              file.writeBytes ("+");
              file.seek (file.getFilePointer () + 36);
            }
          }
          // Skip end of line, LF or CRLF
          if (file.readByte () == '\r')
            file.readByte ();
          if (Thread.currentThread ().isInterrupted ())
            break;
        }
      }
      catch (final FileNotFoundException e)
      {}
      catch (final IOException e)
      {
        e.printStackTrace ();
      }
    }
  }
  // .split try to call a service
  // Here, we first check if the requested MDP service is defined or not,
  // using a MMI lookup to the Majordomo broker. If the service exists,
  // we send a request and wait for a reply using the conventional MDP
  // client API. This is not meant to be fast, just very simple:

  static boolean serviceSuccess (final String uuid)
  {
    // Load request message, service will be first frame
    String filename = requestFilename (uuid);

    // If the client already closed request, treat as successful
    if (!new File (filename).exists ())
      return true;

    ZMsg request;
    try (DataInputStream file = new DataInputStream (new FileInputStream (filename)))
    {
      request = ZMsg.load (file);
    }
    catch (final IOException e)
    {
      e.printStackTrace ();
      return true;
    }
    final ZFrame service = request.pop ();
    final String serviceName = service.toString ();

    // Create MDP client session with short timeout
    final mdcliapi client = new mdcliapi ("tcp://localhost:5555", false);
    client.setTimeout (1000); // 1 sec
    client.setRetries (1); // only 1 retry

    // Use MMI protocol to check if service is available
    final ZMsg mmiRequest = new ZMsg ();
    mmiRequest.add (service);
    final ZMsg mmiReply = client.send ("mmi.service", mmiRequest);
    final boolean serviceOK = (mmiReply != null && mmiReply.getFirst ().toString ().equals ("200"));
    if (mmiReply != null)
      mmiReply.destroy ();

    boolean result = false;
    if (serviceOK)
    {
      final ZMsg reply = client.send (serviceName, request);
      if (reply != null)
      {
        filename = replyFilename (uuid);
        try (DataOutputStream ofile = new DataOutputStream (new FileOutputStream (filename)))
        {
          ZMsg.save (reply, ofile);
        }
        catch (final IOException e)
        {
          e.printStackTrace ();
          return true;
        }
        result = true;
        reply.destroy ();
      }
    }
    else
      request.destroy ();

    client.destroy ();
    return result;
  }
}