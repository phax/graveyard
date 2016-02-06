/*
* JPdfUnit- make your pdf green
* Copyright (C) 2005 Orientation in Objects GmbH
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the Apache License Version 2.0.
* There is a copy of this license along with this library.
* Otherwise see terms of license at apache.org
*
* Feel free to contact us:
*
* jpdfunit-users@lists.sourceforge.net
*
* $Id: DocumentAssertion.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit;

/**
 * The interface DocumentAssertions combines the interfaces MetaDataAssertion
 * and ContentAssertion for beeing a common base of the Assertions.
 *
 * @author bbratkus
 */
public interface IDocumentAssertion extends MetaDataAssertion, IContentAssertion, FontAssertion
{

}
