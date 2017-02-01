package com.helger.zeromq.guide;

//
//  Hello World client in Java
//  Connects REQ socket to tcp://localhost:5555
//  Sends "Hello" to server, expects "World" back
//

import org.zeromq.ZMQ;

public class hwclient
{
  public static void main (final String [] args)
  {
    // Socket to talk to server
    System.out.println ("Connecting to hello world server…");

    try (final ZMQ.Context context = ZMQ.context (1); final ZMQ.Socket requester = context.socket (ZMQ.REQ))
    {
      requester.connect ("tcp://localhost:5555");

      for (int requestNbr = 0; requestNbr != 10; requestNbr++)
      {
        final String request = "Hello";
        System.out.println ("Sending Hello " + requestNbr);
        requester.send (request.getBytes (), 0);

        final byte [] reply = requester.recv (0);
        System.out.println ("Received " + new String (reply) + " " + requestNbr);
      }
    }
  }
}
