/* Copyright 2003, 2004 Elliotte Rusty Harold
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 2.1 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the 
   Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
   Boston, MA 02111-1307  USA
   
   You can contact Elliotte Rusty Harold by sending e-mail to
   elharo@metalab.unc.edu. Please include the word "XOM" in the
   subject line. The XOM home page is located at http://www.xom.nu/
*/

package nu.xom;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 *   This class provides unit tests for the <code>XMLException</code>
 *   class.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b2
 *
 */
public class XMLExceptionTest extends XOMTestCase {
    
    private XMLException ex;
    private Exception cause;
    private String message = "testing 1-2-3";
    
    public XMLExceptionTest(String name) {
        super(name);
    }

    
    protected void setUp() {
        ex = new XMLException("message");
        cause = new Exception();
    }

    
    public void testConstructor() {
        XMLException ex = new XMLException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testPrintStackTrace() {
        XMLException ex = new XMLException(message, cause);
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        ex.printStackTrace(pw);
        pw.close();
        assertTrue(out.toString().indexOf("Caused by: ") > 0);
    }
    
    
    public void testMalformedURIExceptionConstructor() {
        XMLException ex = new MalformedURIException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testValidityExceptionConstructor() {
        XMLException ex = new MalformedURIException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testNamespaceConflictExceptionConstructor() {
        XMLException ex = new NamespaceConflictException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testMultipleParentExceptionConstructor() {
        XMLException ex = new MultipleParentException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testNoSuchAttributeExceptionConstructor() {
        XMLException ex = new NoSuchAttributeException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testNoSuchChildExceptionConstructor() {
        XMLException ex = new NoSuchChildException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testCycleExceptionConstructor() {
        XMLException ex = new CycleException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testIllegalNameExceptionConstructor() {
        XMLException ex = new IllegalNameException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testIllegalTargetExceptionConstructor() {
        XMLException ex = new IllegalTargetException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testIllegalAddExceptionConstructor() {
        XMLException ex = new IllegalAddException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    
    public void testIllegalDataExceptionConstructor() {
        XMLException ex = new IllegalDataException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause()); 
    }
    
    public void testInitCause() {
        
        assertNull(ex.getCause());
        ex.initCause(cause);
        assertEquals(cause, ex.getCause());
        
        try {
            ex.initCause(null);   
            fail("Reinitialized cause over null");   
        }
        catch (IllegalStateException result) {
            // success   
        }
        
        try {
            ex.initCause(new Exception());   
            fail("Reinitialized cause over null");   
        }
        catch (IllegalStateException result) {
            // success   
        }
        
    }


    public void testNullInitCause() {
        
        ex = new XMLException(null, null);
        assertNull(ex.getCause());
        
        try {
            ex.initCause(new Exception());
            fail("Reinitialized cause over null");   
        }
        catch (IllegalStateException result) {
            // success   
        }

        try {
            ex.initCause(null);   
            fail("Reinitialized cause over null");   
        }
        catch (IllegalStateException result) {
            // success   
        }
        
    }

    
    public void testSelfCause() {
        
        try {
            ex.initCause(ex);   
            fail("Allowed self-causation");   
        }
        catch (IllegalArgumentException result) {
            // success   
        }
        
    }

    
    public void testGetMessage() {      
        Exception ex = new XMLException("testing");
        assertEquals("testing", ex.getMessage());
    }

    
}
