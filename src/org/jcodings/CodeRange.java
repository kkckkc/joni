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
package org.jcodings;

public class CodeRange {
    public static boolean isInCodeRange(int[]p, int code) {
        int low = 0;        
        int n = p[0];
        int high = n;

        while (low < high) {
            int x = (low + high) >> 1;
            if (code > p[(x << 1) + 2]) {
                low = x + 1;
            } else {
                high = x;
            }
        }
        return low < n && code >= p[(low << 1) + 1];
    }

    public static boolean isInCodeRange(int[]p, int offset, int end, int code) {
//    	System.out.println("isInCodeRange " + offset + " - " + end);
    	for (int start = offset + 1; start < end; start += 2) {
    		int from = p[start];
    		int to = p[start + 1];
//    		System.out.println((char) from + " " + (char) to);
    		if (code >= from && code <= to) return true;
    	}
    	return false;
    	
//        int low = 0;        
//        int n = p[offset];
//        int high = n ;
//
//        while (low < high) {
//            int x = (low + high) >> 1;
//            if (code > p[(x << 1) + 2 + offset]) {
//                low = x + 1;
//            } else {
//                high = x;
//            }
//        }
//        return low < n && code >= p[(low << 1) + 1 + offset];        
    }
}
