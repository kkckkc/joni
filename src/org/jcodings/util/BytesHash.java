/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in 
 * the Software without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package org.jcodings.util;

public final class BytesHash<V> extends Hash<V>{

    public BytesHash() {
        super();
    }

    public BytesHash(int size) {
        super(size);
    }

    protected void init() {
        head = new BytesHashEntry<V>();
    }

    public final static class BytesHashEntry<V> extends HashEntry<V> {
        public final char[]bytes;
        public final int p;
        public final int end;

        public BytesHashEntry(int hash, HashEntry<V> next, V value, char[]bytes, int p, int end, HashEntry<V> head) {
            super(hash, next, value, head);
            this.bytes = bytes;
            this.p = p;
            this.end = end;
        }

        public BytesHashEntry() {
            super();
            bytes = null;
            p = end = 0;            
        }

        public boolean equals(char[]bytes, int p, int end) {
            if (this.end - this.p != end - p) return false;
            if (this.bytes == bytes) return true;
            int q = this.p;
            while (q < this.end) if (this.bytes[q++] != bytes[p++]) return false;
            return true;            
        }       
    }
    
    public static int hashCode(char[]bytes, int p, int end) {
        int key = 0;
        while (p < end) key = ((key << 16) + (key << 6) - key) + (int)(bytes[p++]); // & 0xff ? we have to match jruby string hash 
        key = key + (key >> 5);
        return key;     
    }
    
    public V put(char[]bytes, V value) { 
        return put(bytes, 0, bytes.length, value);      
    }
    
    public V put(char[]bytes, int p, int end, V value) {
        checkResize();
        int hash = hashValue(hashCode(bytes, p, end));
        int i = bucketIndex(hash, table.length);
        
        for (BytesHashEntry<V> entry = (BytesHashEntry<V>)table[i]; entry != null; entry = (BytesHashEntry<V>)entry.next) {
            if (entry.hash == hash && entry.equals(bytes, p, end)) {                
                entry.value = value;                
                return value;
            }
        }

        table[i] = new BytesHashEntry<V>(hash, table[i], value, bytes, p, end, head);        
        size++;        
        return null;        
    }
    
    public void putDirect(char[]bytes, V value) {
        putDirect(bytes, 0, bytes.length, value);
    }
    
    public void putDirect(char[]bytes, int p, int end, V value) {
        checkResize();
        final int hash = hashValue(hashCode(bytes, p, end));
        final int i = bucketIndex(hash, table.length);
        table[i] = new BytesHashEntry<V>(hash, table[i], value, bytes, p, end, head);
        size++;
    }

    public V get(char[]bytes) {
        return get(bytes, 0, bytes.length);
    }
    
    public V get(char[]bytes, int p, int end) {
        int hash = hashValue(hashCode(bytes, p, end));
         for (BytesHashEntry<V> entry = (BytesHashEntry<V>)table[bucketIndex(hash, table.length)]; entry != null; entry = (BytesHashEntry<V>)entry.next) {
             if (entry.hash == hash && entry.equals(bytes, p, end)) return entry.value;
         }
         return null;
    }

    public V delete(char[]bytes) {
        return delete(bytes, 0, bytes.length);
    }
    
    public V delete(char[]bytes, int p, int end) {
        int hash = hashValue(hashCode(bytes, p, end));
        int i = bucketIndex(hash, table.length);

        BytesHashEntry<V> entry = (BytesHashEntry<V>)table[i];

        if (entry == null) return null;

        if (entry.hash == hash && entry.equals(bytes, p, end)) {
            table[i] = entry.next;
            size--;
            entry.remove();
            return entry.value;
        }
        
        for (; entry.next != null; entry = (BytesHashEntry<V>)entry.next) {
            HashEntry<V> tmp = entry.next;
            if (tmp.hash == hash && entry.equals(bytes, p, end)) {
                entry.next = entry.next.next;
                size--;
                tmp.remove();
                return tmp.value;
            }
        }
        return null;
    }
    
}
