/* Copyright 2002-2006 Elliotte Rusty Harold
   
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

import static com.google.common.base.Preconditions.*;
import javax.annotation.Nonnull;

/**
 * This class represents an xml attribute such as 
 * <code>type="empty"</code> or 
 * <code>xlink:href="http://www.example.com"</code>.
 * 
 * <p>
 * Attributes that declare namespaces such as
 * <code>xmlns="http://www.w3.org/TR/1999/xhtml"</code>
 * or <code>xmlns:xlink="http://www.w3.org/TR/1999/xlink"</code>
 * are stored separately on the elements where they
 * appear. They are never represented as <code>Attribute</code>
 * objects.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.2b2
 * 
 */
public class Attribute extends Node {

    private static final long serialVersionUID = 1L;
    
    private String localName;
    private String prefix;
    private String uri;
    private String value = "";
    private Type   type;

    
    /**
     * <p>
     * Creates a new attribute in no namespace with the
     * specified name and value and undeclared type.
     * </p>
     * 
     * @param localName the unprefixed attribute name
     * @param value the attribute value
     * 
     * @throws IllegalNameException if the local name is not 
     *     a well-formed, non-colonized name
     * @throws IllegalDataException if the value contains characters  
     *     which are not legal in XML such as vertical tab or a null.
     *     Characters such as " and &amp; are legal, but will be  
     *     automatically escaped when the attribute is serialized.
     */
    public Attribute(String localName, String value) {
        this(localName, "", value, Type.UNDECLARED);
    }

    
    /**
     * <p>
     * Creates a new attribute in no namespace with the
     * specified name, value, and type.
     * </p>
     * 
     * @param localName the unprefixed attribute name
     * @param value the attribute value
     * @param type the attribute type
     * 
     * @throws IllegalNameException if the local name is 
     *     not a well-formed non-colonized name
     * @throws IllegalDataException if the value contains 
     *     characters which are not legal in
     *     XML such as vertical tab or a null. Note that 
     *     characters such as " and &amp; are legal,
     *     but will be automatically escaped when the 
     *     attribute is serialized.
     */
    public Attribute(String localName, String value, Type type) {
        this(localName, "", value, type);
    }

    
    /**
     * <p>
     * Creates a new attribute in the specified namespace with the
     * specified name and value and undeclared type.
     * </p>
     * 
     * @param name the prefixed attribute name
     * @param uri the namespace URI
     * @param value the attribute value
     * 
     * @throws IllegalNameException  if the name is not a namespace 
     *     well-formed name
     * @throws IllegalDataException if the value contains characters 
     *     which are not legal in XML such as vertical tab or a null. 
     *     Note that characters such as " and &amp; are legal, but will
     *     be automatically escaped when the attribute is serialized.
     * @throws MalformedURIException if <code>URI</code> is not 
     *     an RFC 3986 URI reference
     * @throws NamespaceConflictException if there's no prefix,
     *     but the URI is not the empty string, or the prefix is 
     *     <code>xml</code> and the URI is not 
     *     http://www.w3.org/XML/1998/namespace
     */
    public Attribute(String name, String uri, String value) {
        this(name, uri, value, Type.UNDECLARED);
    }

    
    /**
     * <p>
     * Creates a new attribute in the specified namespace with the
     * specified name, value, and type.
     * </p>
     * 
     * @param name  the prefixed attribute name
     * @param uri the namespace URI
     * @param value the attribute value
     * @param type the attribute type
     * 
     * @throws IllegalNameException if the name is not a namespace 
     *     well-formed prefixed name
     * @throws IllegalDataException if the value contains 
     *     characters which are not legal in XML such as 
     *     vertical tab or a null. Note that characters such as 
     *     " and &amp; are legal, but will be automatically escaped 
     *     when the attribute is serialized.
     * @throws MalformedURIException if <code>URI</code> is not 
     *     an RFC 3986 absolute URI reference
     */
    public Attribute(
      String name, String uri, String value, Type type) {

        prefix = "";
        String locName = name;
        int prefixPosition = name.indexOf(':');
        if (prefixPosition > 0) {
            prefix = name.substring(0, prefixPosition);   
            locName = name.substring(prefixPosition + 1);
        }

        try {
            _setLocalName(locName);
        }
        catch (IllegalNameException ex) {
            ex.setData(name);
            throw ex;
        }
        _setNamespace(prefix, uri);
        _setValue(value);
        if (isXMLID()) {
            _setType(Attribute.Type.ID);
        }   
        else {
            _setType(type);
        }
        
    }

    
    /**
     * Creates a copy of the specified attribute.
     * 
     * @param attribute the attribute to copy
     * 
     */
    public Attribute(Attribute attribute) {
        
        // These are all immutable types
        this.localName = attribute.localName;
        this.prefix    = attribute.prefix;
        this.uri       = attribute.uri;
        this.value     = attribute.value;
        this.type      = attribute.type;
        
    }

    
    private Attribute() {}
    
    static Attribute build(
      String qualifiedName, String uri, String value, Type type, String localName) {
        
        Attribute result = new Attribute();
        String prefix = "";
        int prefixPosition = qualifiedName.indexOf(':');     
        if (prefixPosition >= 0) {
            prefix = qualifiedName.substring(0, prefixPosition);
            if ("xml:id".equals(qualifiedName)) {
                type = Attribute.Type.ID;
                value = normalize(value);
            }
        }   
        
        result.localName = localName;
        result.prefix = prefix;
        result.type = type;
        result.uri = uri;
        result.value = value;
        
        return result;
        
    }


    private static String normalize(String s) {

        int length = s.length();
        int pos = 0;
        while (pos < length && s.charAt(pos) == ' ') pos++;
        s = s.substring(pos);
        int end = s.length()-1;
        while (end > 0 && s.charAt(end) == ' ') end--;
        s = s.substring(0, end+1);
        
        length = s.length();
        StringBuilder sb = new StringBuilder(length);
        boolean wasSpace = false;
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                if (wasSpace) continue;
                sb.append(' ');
                wasSpace = true;
            }
            else {
                sb.append(c);
                wasSpace = false;
            }
        }
        return sb.toString();
        
    }


    /**
     * Returns the DTD type of this attribute. 
     * If this attribute does not have a type, then
     * <code>Type.UNDECLARED</code> is returned.
     * 
     * @return the DTD type of this attribute
     */
    public final Type getType() {
        return type;
    }

    
    /**
     * Sets the type of this attribute to one of the ten
     * DTD types or <code>Type.UNDECLARED</code>. 
     * 
     * @param type the DTD type of this attribute
     * @throws NullPointerException if <code>type</code> is null
     * @throws IllegalDataException if this is an <code>xml:id</code>
     *     attribute and the <code>type</code> is not ID
     */
    public void setType(@Nonnull Type type) {
        checkNotNull(type, "Attribute type must not be null");
        if (isXMLID() && !Type.ID.equals(type)) {
            throw new IllegalDataException(
              "Cannot change type of xml:id attribute to " + type);
        }
        _setType(type);
    }
    
    
    private boolean isXMLID() {
        return "xml".equals(this.prefix) && "id".equals(this.localName);
    }

    
    private void _setType(Type type) {
        this.type = type;
    }


    /**
     * Returns the attribute value. If the attribute was
     * originally created by a parser, it will have been
     * normalized according to its type.
     * However, attributes created in memory are not normalized.
     * 
     * @return the value of the attribute
     * 
     */
    @Override
    public final String getValue() {
        return value;
    }

    
    /**
     * <p>
     * Sets the attribute's value to the specified string,
     * replacing any previous value. The value is not normalized
     * automatically.
     * </p>
     * 
     * @param value the value assigned to the attribute
     * 
     * @throws IllegalDataException if the value contains characters 
     *     which are not legal in XML such as vertical tab or a null. 
     *     Characters such as " and &amp; are legal, but will be 
     *     automatically escaped when the attribute is serialized.
     */
    public void setValue(String value) {
        _setValue(value);
    }

    
    private void _setValue(String value) {
        Verifier.checkPCDATA(value);
        if (this.isXMLID()) {
            value = normalize(value);
        }
        this.value = value;
    }


    /**
     * <p>
     * Returns the local name of this attribute,
     * not including the prefix.
     * </p>
     * 
     * @return the attribute's local name
     */
    public final String getLocalName() {
        return localName;
    }

    
    /**
     * <p>
     * Sets the local name of the attribute.
     * </p>
     * 
     * @param localName the new local name
     * 
     * @throws IllegalNameException if <code>localName</code>
     *      is not a namespace well-formed, non-colonized name
     * 
     */
    public void setLocalName(String localName) {
        
        if ("id".equals(localName) &&
          Namespace.XML_NAMESPACE.equals(this.uri)) {
            Verifier.checkNCName(this.value);
        }
        _setLocalName(localName);
        if (isXMLID()) {
            this.setType(Attribute.Type.ID);
        }
        
    }   
    
    
    private void _setLocalName(String localName) {
        Verifier.checkNCName(localName);
        if (localName.equals("xmlns")) {
            throw new IllegalNameException("The Attribute class is not"
              + " used for namespace declaration attributes.");
        }
        this.localName = localName;
    }


    /**
     * <p>
     * Returns the qualified name of this attribute,
     * including the prefix if this attribute is in a namespace.
     * </p>
     * 
     * @return the attribute's qualified name
     */
    public final String getQualifiedName() {
        if (prefix.length() == 0) return localName;
        else return prefix + ":" + localName;
    }
    
    
    /**
     * <p>
     * Returns the namespace URI of this attribute, or the empty string
     * if this attribute is not in a namespace.
     * </p>
     * 
     * @return the attribute's namespace URI
     */ 
    public final String getNamespaceURI() {
        return uri;
    }

    
    /**
     * <p>
     * Returns the prefix of this attribute,
     * or the empty string if this attribute 
     * is not in a namespace.
     * </p>
     * 
     * @return the attribute's prefix
     */
    public final String getNamespacePrefix() {
        return prefix;
    }

    
    /**
     * <p>
     * Sets the attribute's namespace prefix and URI.
     * Because attributes must be prefixed in order to have a  
     * namespace URI (and vice versa) this must be done 
     * simultaneously.
     * </p>
     * 
     * @param prefix the new namespace prefix
     * @param uri the new namespace URI
     * 
     * @throws MalformedURIException if <code>URI</code> is 
     *     not an RFC 3986 URI reference
     * @throws IllegalNameException if
     *  <ul>
     *      <li>The prefix is <code>xmlns</code>.</li>
     *      <li>The prefix is null or the empty string.</li>
     *      <li>The URI is null or the empty string.</li>
     * </ul>
     * @throws NamespaceConflictException if
     *  <ul>
     *      <li>The prefix is <code>xml</code> and the namespace URI is
     *          not <code>http://www.w3.org/XML/1998/namespace</code>.</li>
     *      <li>The prefix conflicts with an existing declaration
     *          on the attribute's parent element.</li>
     * </ul>
     */
    public void setNamespace(String prefix, String uri) {
               
        _setNamespace(prefix, uri);
        if (isXMLID()) {
            this.setType(Attribute.Type.ID);
        }

    }

    
    private void _setNamespace(String prefix, String uri) {
        
        if (uri == null) uri = "";
        if (prefix == null) prefix = "";
        
        if (prefix.equals("xmlns")) {
            throw new IllegalNameException(
              "Attribute objects are not used to represent "
              + " namespace declarations"); 
        }
        else if (prefix.equals("xml") 
          && !(uri.equals(Namespace.XML_NAMESPACE))) {
            throw new NamespaceConflictException(
              "Wrong namespace URI for xml prefix: " + uri); 
        }
        else if (uri.equals(Namespace.XML_NAMESPACE)
          && !prefix.equals("xml")) {
            throw new NamespaceConflictException(
              "Wrong prefix for the XML namespace: " + prefix); 
        }
        else if (prefix.length() == 0) {
            if (uri.length() == 0) {
                this.prefix = "";
                this.uri = "";
                return; 
            }
            else {
                throw new NamespaceConflictException(
                  "Unprefixed attribute " + this.localName 
                  + " cannot be in default namespace " + uri);
            }
        }
        else if (uri.length() == 0) {
            throw new NamespaceConflictException(
             "Attribute prefixes must be declared.");
        }
        
        ParentNode parent = this.getParent();
        if (parent != null) {
           // test for namespace conflicts 
           Element element = (Element) parent;
           String  currentURI = element.getLocalNamespaceURI(prefix);
           if (currentURI != null && !currentURI.equals(uri)) {
                throw new NamespaceConflictException(
                  "New prefix " + prefix 
                  + "conflicts with existing namespace declaration"
                );
           } 
        }
        
        Verifier.checkAbsoluteURIReference(uri);
        Verifier.checkNCName(prefix);
        
        this.uri = uri;
        this.prefix = prefix;
        
    }
    
    
    /**
     *  Throws <code>IndexOutOfBoundsException</code>
     *  because attributes do not have children.
     *
     * @param position the child to return
     *
     * @return nothing. This method always throws an exception.
     *
     * @throws IndexOutOfBoundsException because attributes do 
     *     not have children
     */
    @Override
    public final Node getChild(int position) {
        throw new IndexOutOfBoundsException(
          "Attributes do not have children"
        );        
    }

    
    /**
     * Returns 0 because attributes do not have children.
     * 
     * @return zero
     */
    @Override
    public final int getChildCount() {
        return 0;   
    }

    
    /**
     * Creates a deep copy of this attribute that   
     * is not attached to an element.
     * 
     * @return a copy of this attribute
     * 
     */ 
    @Override
    public Node copy() {
        return new Attribute(this);
    }

    
    /**
     * Returns a string representation of the attribute 
     * that is a well-formed XML attribute. 
     * 
     * @return a string containing the XML form of this attribute
     */
    @Override
    public final String toXML() {
        // It's a common belief that methods like this one should be
        // implemented using StringBuffers rather than String 
        // concatenation for maximum performance. However, 
        // disassembling the code shows that today's compilers are 
        // smart enough to figure this out for themselves. The compiled
        // version of this class only uses a single StringBuffer. No 
        // benefit would be gained by making the code more opaque here. 
        return getQualifiedName() + "=\"" + escapeText(value) + "\"";    
    }

    
    /**
     * Returns a string representation of the attribute suitable for 
     * debugging and diagnosis. However, this is not necessarily 
     * a well-formed XML attribute.
     * 
     *  @return a non-XML string representation of this attribute
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "[" + getClass().getName() + ": " 
         + getQualifiedName() + "=\"" 
         + Text.escapeLineBreaksAndTruncate(getValue()) + "\"]";
    }

    
    private static String escapeText(String s) {
        
        final int length = s.length();
        // Give the string builder enough room for a couple of escaped characters 
        final StringBuilder result = new StringBuilder(length+12);
        
        for (int i = 0; i < length; i++) {
            final char c = s.charAt(i);
            switch (c) {
                case '\t':
                    result.append("&#x09;");
                    break;
                case '\n':
                    result.append("&#x0A;");
                    break;
                case '\r':
                    result.append("&#x0D;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                // impossible
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                    //TODO: throw exception ?
                    break;
                default: 
                    result.append(c);
            }
        }
        return result.toString();
    }

    
    @Override
    boolean isAttribute() {
        return true;   
    } 
    
    
    public static enum Type {
        UNDECLARED,
        CDATA,
        ID,
        IDREF,
        IDREFS,
        NMTOKEN,
        NMTOKENS,
        NOTATION,
        ENTITY,
        ENTITIES,
        ENUMERATION;
        
        /** Returns a string representation of the attribute type  
         * suitable for debugging and diagnosis. 
         * 
         * @return a non-XML string representation of this type
         *
         * @see java.lang.Object#toString()
         */
        @Override
         public String toString() {    
            StringBuilder result = new StringBuilder("[Attribute.Type: ");
            result.append(name()); 
            result.append(']');
            return result.toString();    
        }         

    }
    
}
