public class IntList
{
    private IntNode _head;

    public IntList( )
    {
        _head = null;
    }

    public IntList(IntNode node)
    {
        _head = node;
    }

    public boolean empty( ) {
        return _head == null;
    }

    public IntNode nextElement (IntNode node) {
        return node.getNext();
    }

    public int getValueOfNode (IntNode node) {
        return node.getValue();
    }

    // add a link to the beginning of the list
    public void addToHead (IntNode node){
        IntNode temp = _head;
        _head = node;
        node.setNext(temp);
    }

    public void addToEnd(IntNode node) {
        if (empty( ))
            _head = node;
        else {
            IntNode ptr = _head;
            while (ptr.getNext( ) != null)
                ptr = ptr.getNext( );   
            ptr.setNext(node);
        }
    }

    public void remove (int num) {
        if (_head != null) {
            if (_head.getValue() == num)
                _head = _head.getNext();
            else {
                IntNode behind = _head;
                while (behind.getNext() != null)    
                {
                    if (behind.getNext().getValue() == num) {
                        behind.setNext(behind.getNext().getNext());
                        return;
                    }
                    else
                        behind = behind.getNext();
                } //End of while
            } //End of else (if num is not in _head)
        } //End of if (the list is not empty)
    } //End of the method

    public void printList()
    {
        IntNode temp = _head;
        while (temp != null){
            System.out.print (temp.getValue() + " --> ");    
            temp = temp.getNext();
        }
        System.out.println (" null");
    }

    public int length()
    {
        IntNode temp = _head;
        int count = 0;
        while (temp != null){
            count++;
            temp = temp.getNext();
        }
        return count;
    }

    public IntNode predecessor (IntNode node)
    {
        if (_head == null || _head == node)
            return null;
        IntNode behind = _head;
        while (behind.getNext() != null)
        {
            if (behind.getNext() == node)
                return behind;
            else
                behind = behind.getNext(); 
        }
        return null;
    }
}