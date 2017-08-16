package net.sf.saxon.event;
import net.sf.saxon.trans.XPathException;

import javax.xml.transform.Result;
/**
  * Receiver: This interface represents a recipient of XML tree-walking (push) events. It is
  * based on SAX2's ContentHandler, but adapted to handle additional events, and
  * to use Saxon's name pool. Namespaces and Attributes are handled by separate events
  * following the startElement event. Schema types can be defined for elements and attributes.
  * <p>
  * The Receiver interface is an important internal interface within Saxon, and provides a powerful
  * mechanism for integrating Saxon with other applications. It has been designed with extensibility
  * and stability in mind. However, it should be considered as an interface designed primarily for
  * internal use, and not as a completely stable part of the public Saxon API.
  * <p>
  * @author Michael H. Kay
  */

public interface Receiver extends Result {

    /**
     * Set the pipeline configuration
     * @param config the pipeline configuration
     */

    void setPipelineConfiguration(PipelineConfiguration config);

    /**
     * Get the pipeline configuration
     * @return the pipeline configuration
     */

    PipelineConfiguration getPipelineConfiguration();

	/**
	 * Set the System ID of the tree represented by this event stream
     * @param systemId the system ID (which is used as the base URI of the nodes
     * if there is no xml:base attribute)
	*/

	void setSystemId(String systemId);

    /**
    * Notify the start of the event stream
    */

    void open() throws XPathException;

    /**
     * Notify the start of a document node
     * @param properties bit-significant integer indicating properties of the document node.
     * The definitions of the bits are in class {@link ReceiverOptions}
     */

    void startDocument(int properties) throws XPathException;

    /**
     * Notify the end of a document node
     */

    void endDocument() throws XPathException;

    /**
    * Notify an unparsed entity URI.
    * @param name The name of the unparsed entity
    * @param systemID The system identifier of the unparsed entity
    * @param publicID The identifier of the unparsed entity
    */

    void setUnparsedEntity(String name, String systemID, String publicID) throws XPathException;

    /**
     * Notify the start of an element
     * @param nameCode integer code identifying the name of the element within the name pool.
     * @param typeCode integer code identifying the element's type within the name pool. The value -1
     * indicates the default type, xs:untyped.
     * @param locationId an integer which can be interpreted using a {@link net.sf.saxon.event.LocationProvider} to return
     * information such as line number and system ID. If no location information is available,
     * the value zero is supplied.
     * @param properties bit-significant properties of the element node. If there are no revelant
     * properties, zero is supplied. The definitions of the bits are in class {@link net.sf.saxon.event.ReceiverOptions}
     */

    void startElement(int nameCode, int typeCode, int locationId, int properties)
            throws XPathException;

    /**
     * Notify a namespace. Namespaces are notified <b>after</b> the startElement event, and before
     * any children for the element. The namespaces that are reported are only required
     * to include those that are different from the parent element. The events represent namespace
     * declarations and undeclarations rather than in-scope namespace nodes: an undeclaration is represented
     * by a namespace code of zero. If the sequence of namespace events contains two
     * A namespace must not conflict with any namespaces already used for element or attribute names.
     * @param namespaceCode an integer: the top half is a prefix code, the bottom half a URI code.
     * These may be translated into an actual prefix and URI using the name pool. A prefix code of
     * zero represents the empty prefix (that is, the default namespace). A URI code of zero represents
     * a URI of "", that is, a namespace undeclaration.
     * @param properties The most important property is REJECT_DUPLICATES. If this property is set, the
     * namespace declaration will be rejected if it conflicts with a previous declaration of the same
     * prefix. If the property is not set, the namespace declaration will be ignored if it conflicts
     * with a previous declaration. This reflects the fact that when copying a tree, namespaces for child
     * elements are emitted before the namespaces of their parent element. Unfortunately this conflicts
     * with the XSLT rule for complex content construction, where the recovery action in the event of
     * conflicts is to take the namespace that comes last. XSLT therefore doesn't recover from this error:
     * it sets the REJECT_DUPLICATES flag, and this is treated as a hard error.
    */

    void namespace(int namespaceCode, int properties) throws XPathException;

    /**
     * Notify an attribute. Attributes are notified after the startElement event, and before any
     * children. Namespaces and attributes may be intermingled.
     * @param nameCode The name of the attribute, as held in the name pool
     * @param typeCode The type of the attribute, as held in the name pool. The additional bit
     * NodeInfo.IS_DTD_TYPE may be set to indicate a DTD-derived type.
     * @param value the string value of the attribute
     * @param locationId an integer which can be interpreted using a {@link net.sf.saxon.event.LocationProvider} to return
     * information such as line number and system ID. If no location information is available,
     * the value zero is supplied.
     * @param properties Bit significant value. The following bits are defined:
     *      <table summary=""><tr>
     *        <td>DISABLE_ESCAPING</td>    <td>Disable escaping for this attribute</td></tr><tr>
     *        <td>NO_SPECIAL_CHARACTERS</td>      <td>Attribute value contains no special characters</td></tr>
     *      </table>
     * @throws IllegalStateException on any attempt to output an attribute when there is no open element
    */

    void attribute(int nameCode, int typeCode, CharSequence value, int locationId, int properties)
            throws XPathException;

    /**
    * Notify the start of the content, that is, the completion of all attributes and namespaces.
    * Note that the initial receiver of output from XSLT instructions will not receive this event,
    * it has to detect it itself. Note that this event is reported for every element even if it has
    * no attributes, no namespaces, and no content.
    */

    void startContent() throws XPathException;

    /**
    * Notify the end of an element. The receiver must maintain a stack if it needs to know which
    * element is ending.
    */

    void endElement() throws XPathException;

    /**
     * Notify character data. Note that some receivers may require the character data to be
     * sent in a single event, but in general this is not a requirement.
     * @param chars The characters
     * @param locationId an integer which can be interpreted using a {@link net.sf.saxon.event.LocationProvider}
     * to return information such as line number and system ID. If no location information is available,
     * the value zero is supplied.
     * @param properties Bit significant value. The following bits are defined:
     *  <table summary=""><tr>
     *        <td>DISABLE_ESCAPING</td>           <td>Disable escaping for this text node</td></tr><tr>
     *        <td>USE_CDATA</td>                  <td>Output as a CDATA section</td></tr>
     *  </table>
     */

    void characters(CharSequence chars, int locationId, int properties)
            throws XPathException;

    /**
     * Output a processing instruction
     * @param name The PI name. This must be a legal name (it will not be checked).
     * @param data The data portion of the processing instruction
     * @param locationId an integer which can be interpreted using a {@link LocationProvider} to return
     * information such as line number and system ID. If no location information is available,
     * the value zero is supplied.
     * @param properties Additional information about the PI. The following bits are
     * defined:
     *  <table summary=""><tr>     
     *        <td>CHECKED</td>    <td>Data is known to be legal (e.g. doesn't contain "?&gt;")</td></tr>
     *  </table>
     * @throws IllegalArgumentException : the content is invalid for an XML processing instruction
     */

    void processingInstruction(String name, CharSequence data, int locationId, int properties)
            throws XPathException;

    /**
     * Notify a comment. Comments are only notified if they are outside the DTD.
     * @param content The content of the comment
     * @param locationId an integer which can be interpreted using a {@link LocationProvider} to return
     * information such as line number and system ID. If no location information is available,
     * the value zero is supplied.
     * @param properties Additional information about the comment. The following bits are
     * defined:
     *  <table summary=""><tr>     
     *        <td>CHECKED</td>    <td>Comment is known to be legal (e.g. doesn't contain "--")</td></tr>
     *  </table>
     * @throws IllegalArgumentException : the content is invalid for an XML comment
     */

    void comment(CharSequence content, int locationId, int properties) throws XPathException;

    /**
    * Notify the end of the event stream
    */

    void close() throws XPathException;



}

//
// The contents of this file are subject to the Mozilla Public License Version 1.0 (the "License");
// you may not use this file except in compliance with the License. You may obtain a copy of the
// License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the License for the specific language governing rights and limitations under the License.
//
// The Original Code is: all this file.
//
// The Initial Developer of the Original Code is Michael H. Kay
//
// Portions created by (your name) are Copyright (C) (your legal entity). All Rights Reserved.
//
// Contributor(s): none.
//
