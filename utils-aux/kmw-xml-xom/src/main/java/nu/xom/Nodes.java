/* Copyright 2002, 2003, 2005 Elliotte Rusty Harold
   
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

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Implements a list of nodes for traversal purposes.
 * Changes to the document from which this list was generated
 * are not reflected in this list, nor are changes to the list
 * reflected in the document. Changes to the individual
 * <code>Node</code> objects in the list or the document
 * are reflected in the respective other one.
 * 
 * <p>
 * There is no requirement that the list not contain duplicates,
 * or that all the members come from the same document. It is simply
 * a list of nodes. 
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b4
 *
 */
public final class Nodes {
    
    private static final String MSG_NODE_NOT_NULL = "Node must not be null";
    
    private final List<Node> nodes;
    
    
    /**
     * Create an empty Nodes list. 
     */
    public Nodes() {
        nodes = new ArrayList();
    }
    
    
    /**
     * Create a Nodes list from a single node.
     * 
     * @param node the node to insert in the list
     * 
     * @throws NullPointerException if <code>node</code> is null
     */
    public Nodes(@Nonnull Node node) {
        checkNotNull(node, MSG_NODE_NOT_NULL);
        nodes = new ArrayList(1);
        nodes.add(node);
        
    }
    
    /**
     * Create a Nodes list from a list of nodes.
     * @param nodes 
     */
    Nodes(@Nonnull List<Node> nodes) {
        checkNotNull(nodes, MSG_NODE_NOT_NULL);
        this.nodes = nodes;
    }


    /**
     * Returns the number of nodes in the list.
     * This is guaranteed to be non-negative. 
     * 
     * @return the size of the list
     */
    public int size() {
        return nodes.size(); 
    }
    
    
    /**
     * Returns the index<sup>th</sup> node in the list.
     * The first node has index 0. The last node
     * has index <code>size()-1</code>.
     * 
     * @param index the node to return
     * 
     * @return the node at the specified position
     * 
     * @throws IndexOutOfBoundsException if <code>index</code> is  
     *     negative or greater than or equal to the size of the list
     */
    public Node get(int index) {
        return nodes.get(index);   
    }

    
    /**
     * Removes the index<sup>th</sup>node in the list.
     * Subsequent nodes have their indexes reduced by one.
     * 
     * @param index the node to remove
     * 
     * @return the node at the specified position
     * 
     * @throws IndexOutOfBoundsException if index is  
     *     negative or greater than or equal to the size of the list
     */
    public Node remove(int index) {
        return nodes.remove(index);   
    }
    
    
    /**
     * Inserts a node at the index<sup>th</sup> position in the list.
     * Subsequent nodes have their indexes increased by one.
     * 
     * @param node the node to insert
     * @param index the position at which to insert the node
     * 
     * @throws IndexOutOfBoundsException if <code>index</code> is  
     *     negative or strictly greater than the size of the list
     * @throws NullPointerException if <code>node</code> is null
     */
    public void insert(@Nonnull Node node, int index) {
        checkNotNull(node, MSG_NODE_NOT_NULL);
        nodes.add(index, node);   
    }
    
    
    /**
     * Adds a node at the end of this list.
     * 
     * @param node the node to add to the list
     * 
     * @throws NullPointerException if <code>node</code> is null
     */
    public void append(@Nonnull Node node) {
        checkNotNull(node, MSG_NODE_NOT_NULL);
        nodes.add(node);
    }


    /**
     * Determines whether a node is contained in this list.
     * 
     * @param node the node to search for
     * 
     * @return true if this list contains the specified node;
     *     false otherwise
     */
    public boolean contains(@Nonnull Node node) {
        checkNotNull(node, MSG_NODE_NOT_NULL);
        return nodes.contains(node);
    }

    /**
     * 
     * @return 
     */
    public List<Node> list() {
        return nodes;
    }
    
    /**
     * 
     * @return 
     */
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

}