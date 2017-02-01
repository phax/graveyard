package com.helger.zeromq.guide;

//
//  Hello World server in Java
//  Binds REP socket to tcp://*:5555
//  Expects "Hello" from client, replies with "World"
//

import org.zeromq.ZMQ;

public class hwserver
{

  public static void main (final String [] args) throws Exception
  {
    // Socket to talk to clients
    try (final ZMQ.Context context = ZMQ.context (1); final ZMQ.Socket responder = context.socket (ZMQ.REP))
    {
      responder.bind ("tcp://*:5555");

      while (!Thread.currentThread ().isInterrupted ())
      {
        // Wait for next request from the client
        final byte [] request = responder.recv (0);
        System.out.println ("Received " + new String (request));

        // Do some 'work'
        Thread.sleep (1000);

        // Send reply back to client
        final String reply = "World";
        responder.send (reply.getBytes (), 0);
      }
    }
  }
}
