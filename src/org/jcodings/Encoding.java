package org.jcodings;

import org.jcodings.ascii.AsciiTables;
import org.jcodings.constants.CharacterType;
import org.jcodings.constants.PosixBracket;
import org.jcodings.exception.CharacterPropertyException;
import org.jcodings.exception.EncodingException;
import org.jcodings.exception.ErrorMessages;
import org.joni.ApplyCaseFoldArg;

public class Encoding {
	public static final Encoding INSTANCE = new Encoding();  

	public static final char NEW_LINE = '\n';

	public final boolean isDigit(int c) {
        return Character.isDigit((char) c); 
    }

	public static int digitVal(int c) {
        return c - '0';
    }

	public final boolean isXDigit(int c) {
		return Character.isDigit((char)c) || 
			c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f' ||
			c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F';
    }

	public final int xdigitVal(int c) {
        if (isDigit(c)) {
            return digitVal(c);
        } else {
            return isUpper(c) ? c - 'A' + 10 : c - 'a' + 10;
        }
    }
	
    public final boolean isUpper(int code) {
    	return Character.isUpperCase(code);
    }
	
	public static int odigitVal(int c) {
        return digitVal(c);
    }

	public  int length(char[] bytes, int p, int stop) {
	    return 1;
    }

	public final int mbcToCode(char[] bytes, int p, int stop) {
	    return bytes[p];
    }

	public final boolean isWord(int c) {
		return Character.isLetter(c) || Character.isDigit(c) || c == '_';
    }

	public final boolean isNewLine(int c) {
		return c == '\n';
    }

	public final int propertyNameToCType(char[] bytes, int p, int end) {
        Integer ctype = PosixBracket.PBSTableUpper.get(bytes, p, end);
        if (ctype != null) return ctype;
        throw new CharacterPropertyException(ErrorMessages.ERR_INVALID_CHAR_PROPERTY_NAME, new String(bytes, p, end - p));
    }

	public final int strLength(char[] bytes, int p, int end) {
        return end - p;
    }

	public final int strNCmp(char[] chars, int p, int end, char[] compareWith, int asciiP, int n) {
        while (n-- > 0) {
            if (p >= end) return compareWith[asciiP];
            int c = mbcToCode(chars, p, end);
            int x = compareWith[asciiP] - c;
            if (x != 0) return x;
            
            asciiP++;
            p += 1;
        }
        return 0;
    }

	public final int step(char[] bytes, int p, int end, int n) {
		int q = p + n;
        return q <= end ? q : -1;
    }

	public final int codeToMbcLength(int code) {
        if ((code & 0xffffff80) == 0) {
            return 1;
        } else if ((code & 0xfffff800) == 0) {
            return 2;
        } else if ((code & 0xffff0000) == 0) {
            return 3;
        } else if ((code & 0xffe00000) == 0) {
            return 4;
        } else if ((code & 0xfc000000) == 0) {
            return 5;
        } else if ((code & 0x80000000) == 0) {
            return 6;
        } else {
            throw new EncodingException(ErrorMessages.ERR_INVALID_CODE_POINT_VALUE);
        }
    }

	public final boolean isSingleByte() {
	    // TODO: Check
		return false;
    }

	public final int maxLength() {
	    return 6;
    }

	public final int minLength() {
	    return 1;
    }

	public final int codeToMbc(int code, char[] buf, int i) {
        buf[i] = (char) code;
        return 1;
    }

	public final int maxLengthDistance() {
        return 6;
    }

	public final boolean isSbWord(int i) {
        return isAscii(i) && isWord(i);
    }

	public static boolean isAscii(int code) {
        return code < 128; 
    }

	public final boolean isMbcWord(char[] bytes, int p, int end) {
        return isWord(bytes[p]);
    }

	public final void applyAllCaseFold(int caseFoldFlag, ApplyAllCaseFoldFunction fun, ApplyCaseFoldArg arg) {
        // TODO: Add support for unicode characters
		
		int[]code = new int[]{0};
        
        for (int i=0; i<AsciiTables.LowerMap.length; i++) {
            code[0] = AsciiTables.LowerMap[i][1];
            fun.apply(AsciiTables.LowerMap[i][0], code, 1, arg);
            
            code[0] = AsciiTables.LowerMap[i][0];
            fun.apply(AsciiTables.LowerMap[i][1], code, 1, arg);
        }        
	}

	public final int mbcCaseFold(int flag, char[]bytes, IntHolder pp, int end, char[] lower) {
        lower[0] = Character.toLowerCase(bytes[pp.value]);
        pp.value++;
        return 1;        
    }

    protected static final CaseFoldCodeItem[] EMPTY_FOLD_CODES = new CaseFoldCodeItem[]{};
	public final CaseFoldCodeItem[] caseFoldCodesByString(int flag, char[] bytes, int p, int end) {
        char b = bytes[p];
        
        if (Character.isUpperCase(b)) {
        	return new CaseFoldCodeItem[] { new CaseFoldCodeItem(1, 1, new int[] { Character.toLowerCase(b) }) };
        } else if (Character.isLowerCase(b)) {
        	return new CaseFoldCodeItem[] { new CaseFoldCodeItem(1, 1, new int[] { Character.toUpperCase(b) }) };
        } else {
            return EMPTY_FOLD_CODES;
        }
    }

	public final char[] toLowerCaseTable() {
	    return null;
    }

	public final boolean isReverseMatchAllowed(char[] exact, int exactP, int exactEnd) {
	    // TODO: Check
		return true;
    }

	public final boolean isPrint(int code) {
        return isCodeCType(code, CharacterType.PRINT);
    }

	public final boolean isMbcHead(char[] bytes, int p, int end) {
        return true; 
    }

	public final boolean isNewLine(char[] bytes, int p, int end) {
		if (p >= end) return false;
        return bytes[p] == '\n';
    }

	public final boolean isMbcCrnl(char[] bytes, int p, int end) {
        return bytes[p] == 13 && isNewLine(bytes, p + 1, end);
    }

	public final int stepBack(char[] bytes, int p, int s, int end, int n) {
        while (s != -1 && n-- > 0) {
            if (s <= p) return -1;
            s = s - 1; 
        }
        return s;
    }

	public final int prevCharHead(char[] bytes, int p, int s, int end) {
        if (s <= p) return -1; // ??      
        return s - 1;
    }

	public final int mbcodeStartPosition() {
        return 0x80;      
    }

	public final int rightAdjustCharHeadWithPrev(char[] bytes, int p, int s, int end, IntHolder prev) {
        if (prev != null) prev.value = -1; /* Sorry */
        return s;
    }

	public final int rightAdjustCharHead(char[] bytes, int p, int s, int end) {
		return s;
    }

	public final int leftAdjustCharHead(char[] bytes, int p, int s, int end) {
        return s;
    }

	public final int[] ctypeCodeRange(int ctype, IntHolder sbOut) {
        if (ctype >= UnicodeCodeRanges.CodeRangeTable.length) throw new InternalError(ErrorMessages.ERR_TYPE_BUG);
        
        return UnicodeCodeRanges.CodeRangeTable[ctype];
    }

	public final boolean isCodeCType(int code, int ctype) {
        if (Config.USE_UNICODE_PROPERTIES) {
            if (ctype <= CharacterType.MAX_STD_CTYPE && code < 256)
                return isCodeCTypeInternal(code, ctype); 
        } else {
            if (code < 256) return isCodeCTypeInternal(code, ctype);
        }

        if (ctype > UnicodeCodeRanges.CodeRangeTable.length) throw new InternalError(ErrorMessages.ERR_TYPE_BUG);

        return CodeRange.isInCodeRange(UnicodeCodeRanges.CodeRangeTable[ctype], code);
    }

    protected final boolean isCodeCTypeInternal(int code, int ctype) {
    	return true;
    }    

	public final int strByteLengthNull(char[] bytes, int p, int end) {
		return end - p;
    }

}
