package com.helger.zeromq.guide;

import org.zeromq.ZMQ;

//  Report 0MQ version
public class version
{
  public static void main (final String [] args)
  {
    System.out.println ("Version string: " + ZMQ.getVersionString () + ", Version int: " + ZMQ.getFullVersion ());
  }

}
