
/**
 * Write a description of class BigNumber here.
 *
 * @author (Yoav Epstein)
 * @version (13/1/20)
 */
public class BigNumber
{
    //instance variable
    private IntNode _head;

    //Constructors    
    public BigNumber ()
    { 
        _head = new IntNode (0);
    }

    public BigNumber (long a)
    {
        while (a > 0){
            addToEnd (new IntNode ((int)a%10));
            a /= 10;
        } 
    }

    public BigNumber (BigNumber other)
    {
        IntNode ptr = other._head;
        while (ptr != null){
            addToEnd (new IntNode (ptr.getValue()));
            ptr = ptr.getNext();
        } 
    }

    //Method 
    private void addToEnd(IntNode node) {
        if (empty())
            _head = node;
        else {
            IntNode ptr = _head;
            while (ptr.getNext() != null)
                ptr = ptr.getNext();   
            ptr.setNext(node);
        }       
    }

    private boolean empty( ) {
        return _head == null;
    }

    public String toString()
    {
        return toString ("", _head);
    }

    private String toString(String num, IntNode ptr)
    {
        if (ptr.getNext() == null){
            num += (ptr.getValue());
            return num;
        }
        num = toString (num, ptr.getNext()) +  ptr.getValue();
        return num;
    }

    private int length()
    {
        IntNode temp = _head;
        int count = 0;
        while (temp != null){
            count++;
            temp = temp.getNext();
        }
        return count;
    }

    public int compareTo (BigNumber other)
    {
        int compare = 0;
        compare = compareTo (other, this);
        if (compare == 0){
            IntNode ptr = _head;
            IntNode ptrOther = other._head;
            while (ptr != null && ptrOther != null)
            {
                if (ptr.getValue() > ptrOther.getValue()){
                    compare = 1;
                    ptr = ptr.getNext();
                    ptrOther = ptrOther.getNext();
                }
                else if (ptr.getValue() < ptrOther.getValue()){
                    compare = -1;
                    ptr = ptr.getNext();
                    ptrOther = ptrOther.getNext();
                }
                else{
                    ptr = ptr.getNext();
                    ptrOther = ptrOther.getNext();
                }
            } 
        }
        return compare;
    }

    private int compareTo (BigNumber big1, BigNumber big2)
    {
        int compare = 0;
        int length1 = big1.length(), length2 = big2.length();
        if (length1 > length2)
            compare = -1;
        else if (length1 < length2)
            compare = 1;
        return compare;
    }

    public BigNumber addBigNumber (BigNumber other)
    {
        BigNumber big = new BigNumber (this);
        IntNode ptr = big._head;
        IntNode ptrOther = other._head;
        while (ptr != null & ptrOther != null)
        {
            int insert = ptr.getValue() + ptrOther.getValue();
            if (insert/10 == 0)
                ptr.setValue(insert);
            else if (ptr.getNext() == null){
                ptr.setValue(insert%10);
                big.addToEnd(new IntNode (insert/10));
            }
            else{
                ptr.setValue(insert%10);
                ptr.getNext().setValue(ptr.getNext().getValue() + insert/10);
            }
            ptr = ptr.getNext();
            ptrOther = ptrOther.getNext();
        }
        if (ptrOther != null){
            while (ptrOther != null){
                big.addToEnd (new IntNode (ptrOther.getValue()));
                ptrOther = ptrOther.getNext();
            }  
        }

        return big;
    }

    public BigNumber addLong (long num)
    {
        BigNumber big = new BigNumber (num);
        big = this.addBigNumber(big);
        return big;
    }

    public BigNumber subtractBigNumber (BigNumber other)
    {
        BigNumber big;
        IntNode ptr1, ptr2;
        int pivot = compareTo(other);
        if (pivot == 1){
            big = new BigNumber (this);
            ptr1 = big._head;
            ptr2 = other._head;
        }
        else if (pivot == -1){
            big = new BigNumber (other);
            ptr1 = big._head;
            ptr2 = _head;
        }
        else{
            big = new BigNumber ();
            ptr1 = null;//ptr1 & ptr2 must be initialized
            ptr2 = null;
        }

        while (ptr2 != null)
        {
            int insert = ptr1.getValue() - ptr2.getValue();
            if (insert >= 0)
                ptr1.setValue(insert);            
            else{
                ptr1.setValue(insert + 10);
                ptr1.getNext().setValue(ptr1.getNext().getValue() - 1);
            }
            ptr1 = ptr1.getNext();
            ptr2 = ptr2.getNext();

        }
        removeZeros (big);
        return big;
    }

    private void removeZeros (BigNumber big) {
        IntNode ptr = _head;
        IntNode pivot = null;
        while (ptr.getNext() != null){
            if (ptr.getValue() != 0 && ptr.getNext().getValue() == 0){
                pivot = ptr;
            }
            ptr = ptr.getNext();
        }
        if (pivot != null && ptr.getValue() != 0)
            pivot.setNext (null);
    } 

    public BigNumber multBigNumber (BigNumber other)
    {
        BigNumber big = new BigNumber ();
        IntNode ptr1, ptr2;
        int count = 0, pivot = compareTo(other);
        if (pivot == 1){
            ptr1 = other._head;  
        }
        else{
            ptr1 = _head;  
        }
        while (ptr1 != null) 
        {  
            if (pivot == 1){// Reinitualize ptr2
                ptr2 = _head;  
            }
            else
                ptr2 = other._head;
            int r = 0;
            BigNumber temp = new BigNumber ();// Resetting a temporary big number to add
            while (ptr2 != null)
            {
                int insert = ptr1.getValue() * ptr2.getValue();
                if (insert < 10 && insert/10 + r < 10){
                    temp.addToEnd(new IntNode (insert%10 + r));
                    r = 0;
                }
                else if (insert < 10 && insert/10 + r > 10){
                    temp.addToEnd(new IntNode ((insert%10 + r)%10));
                    r = (insert%10 + r)/10;
                }
                else if (insert > 10 && insert/10 + r < 10){
                    temp.addToEnd(new IntNode (insert%10 + r));
                    r = insert/10;
                }
                else {
                    temp.addToEnd(new IntNode ((insert%10 + r)%10));
                    r = (insert%10 + r)/10;
                }

                if (ptr2.getNext() == null && r != 0)
                    temp.addToEnd(new IntNode (r));
                ptr2 = ptr2.getNext();
            }
            temp.remove(0); 
            int tempCount = count;
            while (tempCount > 0){//Adding zeros to the temp sum
                temp.addToHead (new IntNode (0));
                tempCount--;
            }
            big = big.addBigNumber(temp);
            count++;
            ptr1 = ptr1.getNext();
        }

        return big; 
    }

    private void addToHead (IntNode node){
        IntNode temp = _head;
        _head = node;
        node.setNext(temp);
    }

    private void remove (int num) {
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
}
