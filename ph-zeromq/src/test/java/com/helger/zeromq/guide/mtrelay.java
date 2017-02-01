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

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * Multithreaded relay
 */
public class mtrelay
{

  private static class Step1 extends Thread
  {
    private final Context _context;

    private Step1 (final Context context)
    {
      this._context = context;
    }

    @Override
    public void run ()
    {
      // Signal downstream to step 2
      try (final Socket xmitter = _context.socket (ZMQ.PAIR))
      {
        xmitter.connect ("inproc://step2");
        System.out.println ("Step 1 ready, signaling step 2");
        xmitter.send ("READY", 0);
      }
    }

  }

  private static class Step2 extends Thread
  {
    private final Context _context;

    private Step2 (final Context context)
    {
      this._context = context;
    }

    @Override
    public void run ()
    {
      // Bind to inproc: endpoint, then start upstream thread
      try (final Socket receiver = _context.socket (ZMQ.PAIR))
      {
        receiver.bind ("inproc://step2");
        final Thread step1 = new Step1 (_context);
        step1.start ();

        // Wait for signal
        receiver.recv (0);
      }

      // Connect to step3 and tell it we're ready
      try (final Socket xmitter = _context.socket (ZMQ.PAIR))
      {
        xmitter.connect ("inproc://step3");
        xmitter.send ("READY", 0);
      }
    }

  }

  public static void main (final String [] args)
  {
    try (final Context context = ZMQ.context (1))
    {
      // Bind to inproc: endpoint, then start upstream thread
      try (final Socket receiver = context.socket (ZMQ.PAIR))
      {
        receiver.bind ("inproc://step3");

        // Step 2 relays the signal to step 3
        final Thread step2 = new Step2 (context);
        step2.start ();

        // Wait for signal
        receiver.recv (0);

        System.out.println ("Test successful!");
      }
    }
  }
}
