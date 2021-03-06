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
package org.joni;

import static org.joni.Option.isSingleline;
import static org.joni.ast.QuantifierNode.isRepeatInfinite;

import org.jcodings.constants.CharacterType;
import org.jcodings.exception.CharacterPropertyException;
import org.joni.ast.QuantifierNode;
import org.joni.constants.AnchorType;
import org.joni.constants.MetaChar;
import org.joni.constants.TokenType;
import org.joni.exception.ErrorMessages;

class Lexer extends ScannerSupport {
    protected final ScanEnvironment env; 
    protected final Syntax syntax;              // fast access to syntax
    protected final Token token = new Token();  // current token

    protected Lexer(ScanEnvironment env, char[]bytes, int p, int end) {
        super(env.enc, bytes, p, end);
        this.env = env;
        this.syntax = env.syntax;
    }
    
    /**
     * @return 0: normal {n,m}, 2: fixed {n}
     * !introduce returnCode here 
     */
    private int fetchRangeQuantifier() {
        mark();
        boolean synAllow = syntax.allowInvalidInterval();
        
        if (!left()) {
            if (synAllow) { 
                return 1; /* "....{" : OK! */
            } else {
                newSyntaxException(ERR_END_PATTERN_AT_LEFT_BRACE);
            }
        }

        if (!synAllow) {
            c = peek();
            if (c == ')' || c == '(' || c == '|') {
                newSyntaxException(ERR_END_PATTERN_AT_LEFT_BRACE);
            }
        }
        
        int low = scanUnsignedNumber();
        if (low < 0) {
        	newSyntaxException(ErrorMessages.ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE);
        }
        if (low > Config.MAX_REPEAT_NUM) newSyntaxException(ErrorMessages.ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE);

        boolean nonLow = false;
        if (p == _p) { /* can't read low */
            if (syntax.allowIntervalLowAbbrev()) {
                low = 0;
                nonLow = true;
            } else {
                return invalidRangeQuantifier(synAllow);
            }
        }
        
        if (!left()) return invalidRangeQuantifier(synAllow);
        
        fetch();
        int up;
        int ret = 0;
        if (c == ',') {
            int prev = p; // ??? last            
            up = scanUnsignedNumber();
            if (up < 0) newValueException(ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE);
            if (up > Config.MAX_REPEAT_NUM) newValueException(ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE);
            
            if (p == prev) {
                if (nonLow) return invalidRangeQuantifier(synAllow);
                up = QuantifierNode.REPEAT_INFINITE; /* {n,} : {n,infinite} */
            }
        } else {
            if (nonLow) return invalidRangeQuantifier(synAllow);
            unfetch();
            up = low; /* {n} : exact n times */
            ret = 2; /* fixed */
        }
        
        if (!left()) return invalidRangeQuantifier(synAllow);
        fetch();
        
        if (syntax.opEscBraceInterval()) {
            if (c != syntax.metaCharTable.esc) return invalidRangeQuantifier(synAllow);
            fetch();
        }
        
        if (c != '}') return invalidRangeQuantifier(synAllow);
        
        if (!isRepeatInfinite(up) && low > up) {
            newValueException(ERR_UPPER_SMALLER_THAN_LOWER_IN_REPEAT_RANGE);
        }
        
        token.type = TokenType.INTERVAL;
        token.setRepeatLower(low);
        token.setRepeatUpper(up);
        
        return ret; /* 0: normal {n,m}, 2: fixed {n} */
    }
    
    private int invalidRangeQuantifier(boolean synAllow) {
        if (synAllow) {
            restore();
            return 1;
        } else {
            newSyntaxException(ERR_INVALID_REPEAT_RANGE_PATTERN);
            return 0; // not reached
        }
    }
    
    /* \M-, \C-, \c, or \... */
    private int fetchEscapedValue() {
        if (!left()) newSyntaxException(ERR_END_PATTERN_AT_ESCAPE);
        fetch();

        switch(c) {

        case 'M':
            if (syntax.op2EscCapitalMBarMeta()) {
                if (!left()) newSyntaxException(ERR_END_PATTERN_AT_META);
                fetch();
                if (c != '-') newSyntaxException(ERR_META_CODE_SYNTAX);
                if (!left()) newSyntaxException(ERR_END_PATTERN_AT_META);
                fetch();
                if (c == syntax.metaCharTable.esc) {
                    c = fetchEscapedValue();
                }
                c = ((c & 0xff) | 0x80);
            } else {
                fetchEscapedValueBackSlash();
            }
            break;

        case 'C':
            if (syntax.op2EscCapitalCBarControl()) {
                if (!left()) newSyntaxException(ERR_END_PATTERN_AT_CONTROL);
                fetch();
                if (c != '-') newSyntaxException(ERR_CONTROL_CODE_SYNTAX);
                fetchEscapedValueControl();
            } else {
                fetchEscapedValueBackSlash();
            }
            break;
            
        case 'c':
            if (syntax.opEscCControl()) {
                fetchEscapedValueControl();
            }
            /* fall through */
            
        default:
            fetchEscapedValueBackSlash();
        } // switch
        
        return c; // ???
    }
    
    private void fetchEscapedValueBackSlash() {
        c = env.convertBackslashValue(c);
    }

    private void fetchEscapedValueControl() {
        if (!left()) newSyntaxException(ERR_END_PATTERN_AT_CONTROL);
        fetch();
        if (c == '?') {
            c = 0177;
        } else {
            if (c == syntax.metaCharTable.esc) {
                c = fetchEscapedValue();
            }
            c &= 0x9f;
        }
    }
    
    private int nameEndCodePoint(int start) {
        switch(start) {
        case '<':
            return '>';
        case '\'':
            return '\'';
        default:
            return 0;
        }
    }

    // USE_NAMED_GROUP && USE_BACKREF_AT_LEVEL
    /*
        \k<name+n>, \k<name-n>
        \k<num+n>,  \k<num-n>
        \k<-num+n>, \k<-num-n>
     */
    
    // value implicit (rnameEnd)
    private boolean fetchNameWithLevel(int startCode, int[]rbackNum, int[]rlevel) {
        int src = p;
        boolean existLevel = false;
        int isNum = 0;
        int sign = 1;
        
        int endCode = nameEndCodePoint(startCode);
        int pnumHead = p; 
        int nameEnd = stop;

        String err = null;
        if (!left()) {
            newValueException(ERR_EMPTY_GROUP_NAME);
        } else {
            fetch();
            if (c == endCode) newValueException(ERR_EMPTY_GROUP_NAME);
            if (enc.isDigit(c)) {
                isNum = 1;
            } else if (c == '-') { 
                isNum = 2;
                sign = -1;
                pnumHead = p;
            } else if (!enc.isWord(c)) { 
                err = ERR_INVALID_GROUP_NAME;
            }
        }
        
        while (left()) {
            nameEnd = p;
            fetch();
            if (c == endCode || c == ')' || c == '+' || c == '-') {
                if (isNum == 2) err = ERR_INVALID_GROUP_NAME;
                break;
            }
            
            if (isNum != 0) {
                if (enc.isDigit(c)) {
                    isNum = 1;
                } else {
                    err = ERR_INVALID_GROUP_NAME;
                    // isNum = 0;
                }
            } else if (!enc.isWord(c)) {
            	err = ERR_INVALID_CHAR_IN_GROUP_NAME;
            }
        }

        boolean isEndCode = false;
        if (err == null && c != endCode) {
            if (c == '+' || c == '-') {
                int flag = c == '-' ? -1 : 1;

                fetch();
                if (!enc.isDigit(c)) newValueException(ERR_INVALID_GROUP_NAME, src, stop);
                unfetch();
                int level = scanUnsignedNumber();
                if (level < 0) newValueException(ERR_TOO_BIG_NUMBER);
                rlevel[0] = level * flag;
                existLevel = true;
                
                fetch();
                isEndCode = c == endCode;
            }
            
            if (!isEndCode) {
                err = ERR_INVALID_GROUP_NAME;
                nameEnd = stop;
            }
        }

        if (err == null) {
            if (isNum != 0) {
                mark();
                p = pnumHead;
                int backNum = scanUnsignedNumber();
                restore();
                if (backNum < 0) {
                    newValueException(ERR_TOO_BIG_NUMBER);
                } else if (backNum == 0) {
                    newValueException(ERR_INVALID_GROUP_NAME, src, stop);
                }
                rbackNum[0] = backNum * sign; 
            }
            value = nameEnd;
            return existLevel;
        } else {
            newValueException(ERR_INVALID_GROUP_NAME, src, nameEnd);
            return false; // not reached
        }
    }
    
    // USE_NAMED_GROUP
    // ref: 0 -> define name    (don't allow number name)
    //      1 -> reference name (allow number name)
    private int fetchNameForNamedGroup(int startCode, boolean ref) {
        int src = p;
        value = 0;
        
        int isNum = 0;
        int sign = 1;

        int endCode = nameEndCodePoint(startCode);
        int pnumHead = p;
        int nameEnd = stop;

        String err = null;
        if (!left()) {
            newValueException(ERR_EMPTY_GROUP_NAME);
        } else {
            fetch();
            if (c == endCode) newValueException(ERR_EMPTY_GROUP_NAME);
            if (enc.isDigit(c)) {
                if (ref) {
                    isNum = 1;
                } else {
                    err = ERR_INVALID_GROUP_NAME;
                    // isNum = 0;
                }
            } else if (c == '-') { 
                if (ref) {
                    isNum = 2;
                    sign = -1;
                    pnumHead = p;
                } else {
                    err = ERR_INVALID_GROUP_NAME;
                    // isNum = 0;
                }
            } else if (!enc.isWord(c)) {
                err = ERR_INVALID_CHAR_IN_GROUP_NAME; 
            }
        }
        
        if (err == null) {
            while (left()) {
                nameEnd = p;
                fetch();
                if (c == endCode || c == ')') {
                    if (isNum == 2) err = ERR_INVALID_GROUP_NAME;
                    break;
                }
                
                if (isNum != 0) {
                    if (enc.isDigit(c)) {
                        isNum = 1;
                    } else {
                        if (!enc.isWord(c)) {
                            err = ERR_INVALID_CHAR_IN_GROUP_NAME;
                        } else {
                            err = ERR_INVALID_GROUP_NAME;
                        }
                        // isNum = 0;
                    }
                } else {
                    if (!enc.isWord(c)) {
                        err = ERR_INVALID_CHAR_IN_GROUP_NAME;
                    }
                }
            }
            
            if (c != endCode) {
                err = ERR_INVALID_GROUP_NAME;
                nameEnd = stop;
            }

            int backNum = 0;
            if (isNum != 0) {
                mark();
                p = pnumHead;
                backNum = scanUnsignedNumber();
                restore();
                if (backNum < 0) {
                    newValueException(ERR_TOO_BIG_NUMBER);
                } else if (backNum == 0) {
                    newValueException(ERR_INVALID_GROUP_NAME, src, nameEnd);
                }
                backNum *= sign;
            }
            value = nameEnd;
            return backNum;
        } else {
            while (left()) {
                nameEnd = p;
                fetch();
                if (c == endCode || c == ')') break;
            }
            if (!left()) nameEnd = stop;
            newValueException(err, src, nameEnd);
            return 0; // not reached
        }
    }

    // #else USE_NAMED_GROUP
    // make it return nameEnd!
    private final int fetchNameForNoNamedGroup(int startCode, boolean ref) {
        int src = p;
        value = 0;
        
        int isNum = 0;
        int sign = 1;
        
        int endCode = nameEndCodePoint(startCode);
        int pnumHead = p; 
        int nameEnd = stop;

        String err = null;
        if (!left()) {
            newValueException(ERR_EMPTY_GROUP_NAME);
        } else {
            fetch();
            if (c == endCode) newValueException(ERR_EMPTY_GROUP_NAME);
            
            if (enc.isDigit(c)) {
                isNum = 1;
            } else if (c == '-') {
                isNum = 2;
                sign = -1;
                pnumHead = p;
            } else {
                err = ERR_INVALID_CHAR_IN_GROUP_NAME;
            }
        }

        while(left()) {
            nameEnd = p;
            
            fetch();
            if (c == endCode || c == ')') break;
            if (!enc.isDigit(c)) err = ERR_INVALID_CHAR_IN_GROUP_NAME;
        }
            
        if (err == null && c != endCode) { 
            err = ERR_INVALID_GROUP_NAME;
            nameEnd = stop;
        }
        
        if (err == null) {
            mark();
            p = pnumHead;
            int backNum = scanUnsignedNumber();
            restore();
            if (backNum < 0) {
                newValueException(ERR_TOO_BIG_NUMBER);
            } else if (backNum == 0){
                newValueException(ERR_INVALID_GROUP_NAME, src, nameEnd);
            }
            backNum *= sign;
            
            value = nameEnd;
            return backNum;
        } else {
            newValueException(err, src, nameEnd);
            return 0; // not reached
        }
    }
    
    protected final int fetchName(int startCode, boolean ref) {
        if (Config.USE_NAMED_GROUP) {
            return fetchNameForNamedGroup(startCode, ref);
        } else {
            return fetchNameForNoNamedGroup(startCode, ref);
        }
    }
    
    private boolean strExistCheckWithEsc(int[]s, int n, int bad) {
        int p = this.p;
        int to = this.stop;
        
        boolean inEsc = false;
        int i=0;

        while(p < to) {
            if (inEsc) {
                inEsc = false;
                p += enc.length(bytes, p, to);
            } else {
                int x = enc.mbcToCode(bytes, p, to);
                int q = p + enc.length(bytes, p, to);
                if (x == s[0]) {
                    for (i=1; i<n && q < to; i++) {
                        x = enc.mbcToCode(bytes, q, to);
                        if (x != s[i]) break;
                        q += enc.length(bytes, q, to);
                    }
                    if (i >= n) return true;
                    p += enc.length(bytes, p, to);
                } else {
                    x = enc.mbcToCode(bytes, p, to);
                    if (x == bad) return false;
                    else if (x == syntax.metaCharTable.esc) inEsc = true;
                    p = q;
                }
            }
        }
        return false;
    }    
    
    private static final int send[] = new int[]{':', ']'}; 
    
    protected final TokenType fetchTokenInCC() {
        int last;
        int c2;
        
        if (!left()) {
            token.type = TokenType.EOT;
            return token.type;
        }

        fetch();
        token.type = TokenType.CHAR;
        token.base = 0;
        token.setC(c);
        token.escaped = false;
        
        if (c == ']') {
            token.type = TokenType.CC_CLOSE;
        } else if (c == '-') {
            token.type = TokenType.CC_RANGE;
        } else if (c == syntax.metaCharTable.esc) {
            if (!syntax.backSlashEscapeInCC()) return token.type;
            if (!left()) newSyntaxException(ERR_END_PATTERN_AT_ESCAPE);
            fetch();
            token.escaped = true;
            token.setC(c);

            switch (c) {
            
            case 'w':
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.WORD);
                token.setPropNot(false);
                break;
                
            case 'W':
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.WORD);
                token.setPropNot(true);
                break;
                
            case 'd':
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.DIGIT);
                token.setPropNot(false);
                break;

            case 'D':
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.DIGIT);
                token.setPropNot(true);
                break;

            case 's':
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.SPACE);
                token.setPropNot(false);
                break;
            
            case 'S':
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.SPACE);
                token.setPropNot(true);
                break;

            case 'h':
                if (!syntax.op2EscHXDigit()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.XDIGIT);
                token.setPropNot(false);
                break;

            case 'H':
                if (!syntax.op2EscHXDigit()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.XDIGIT);
                token.setPropNot(true);
                break;
            
            case 'p':
            case 'P':
                c2 = peek(); // !!! migrate to peekIs 
                if (c2 == '{' && syntax.op2EscPBraceCharProperty()) {
                    inc();
                    token.type = TokenType.CHAR_PROPERTY;
                    token.setPropNot(c == 'P');
                    
                    if (syntax.op2EscPBraceCircumflexNot()) {
                        c2 = fetchTo();
                        if (c2 == '^') {
                            token.setPropNot(!token.getPropNot()); 
                        } else {
                            unfetch();
                        }
                    }
                }
                break;
                
            case 'x':
                if (!left()) break;
                last = p;
                
                if (peekIs('{') && syntax.opEscXBraceHex8()) {
                    inc();
                    int num = scanUnsignedHexadecimalNumber(8);
                    if (num < 0) newValueException(ERR_TOO_BIG_WIDE_CHAR_VALUE);
                    if (left()) {
                        c2 = peek();
                        if (enc.isXDigit(c2)) newValueException(ERR_TOO_LONG_WIDE_CHAR_VALUE); 
                    }
                    
                    if (p > last + enc.length(bytes, last, stop) && left() && peekIs('}')) {
                        inc();                      
                        token.type = TokenType.CODE_POINT;
                        token.base = 16;
                        token.setCode(num);
                    } else {
                        /* can't read nothing or invalid format */
                        p = last;
                    }
                } else if (syntax.opEscXHex2()) {
                    int num = scanUnsignedHexadecimalNumber(2);
                    if (num < 0) newValueException(ERR_TOO_BIG_NUMBER);
                    if (p == last) { /* can't read nothing. */
                        num = 0; /* but, it's not error */
                    }
                    token.type = TokenType.RAW_BYTE;
                    token.base = 16;
                    token.setC(num);
                }
                break;
                
            case 'u':
                if (!left()) break;
                last = p;
                
                if (syntax.op2EscUHex4()) {
                    int num = scanUnsignedHexadecimalNumber(4);
                    if (num < 0) newValueException(ERR_TOO_BIG_NUMBER);
                    if (p == last) {  /* can't read nothing. */
                        num = 0; /* but, it's not error */
                    }
                    token.type = TokenType.CODE_POINT;
                    token.base = 16;
                    token.setCode(num);
                }
                break;
                
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
                if (syntax.opEscOctal3()) {
                    unfetch();
                    last = p;
                    int num = scanUnsignedOctalNumber(3);
                    if (num < 0) newValueException(ERR_TOO_BIG_NUMBER);
                    if (p == last) {  /* can't read nothing. */
                        num = 0; /* but, it's not error */
                    }
                    token.type = TokenType.RAW_BYTE;
                    token.base = 8;
                    token.setC(num);
                }
                break;
                
            default:
                unfetch();
                int num = fetchEscapedValue();
                if (token.getC() != num) {
                    token.setCode(num);
                    token.type = TokenType.CODE_POINT;
                }
                break;
            } // switch
            
        } else if (c == '[') {
            if (syntax.opPosixBracket() && peekIs(':')) {
                token.backP = p; /* point at '[' is readed */
                inc();
                if (strExistCheckWithEsc(send, send.length, ']')) {
                    token.type = TokenType.POSIX_BRACKET_OPEN;
                } else {
                    unfetch();
                    // remove duplication, goto cc_in_cc;
                    if (syntax.op2CClassSetOp()) {
                        token.type = TokenType.CC_CC_OPEN;
                    } else {
                        env.ccEscWarn("[");
                    }
                }
            } else { // cc_in_cc:
                if (syntax.op2CClassSetOp()) {
                    token.type = TokenType.CC_CC_OPEN;
                } else {
                    env.ccEscWarn("[");
                }
            }
        } else if (c == '&') {
            if (syntax.op2CClassSetOp() && left() && peekIs('&')) {
                inc();
                token.type = TokenType.CC_AND;
            }
        }
        return token.type;
    }
    
    protected final int backrefRelToAbs(int relNo) {
        return env.numMem + 1 + relNo;
    }
    
    protected final TokenType fetchToken() {
        int last;
        
        // mark(); // out
        
        start:
        while(true) {
            
        if (!left()) {
            token.type = TokenType.EOT;
            return token.type;
        }
        
        token.type = TokenType.STRING;
        token.base = 0;
        token.backP = p;

        fetch();

        if (c == syntax.metaCharTable.esc && !syntax.op2IneffectiveEscape()) { // IS_MC_ESC_CODE(code, syn)
            if (!left()) newSyntaxException(ERR_END_PATTERN_AT_ESCAPE);

            token.backP = p;
            fetch();

            token.setC(c);
            token.escaped = true;
            switch(c) {

            case '*':
                if (!syntax.opEscAsteriskZeroInf()) break;
                token.type = TokenType.OP_REPEAT;
                token.setRepeatLower(0);
                token.setRepeatUpper(QuantifierNode.REPEAT_INFINITE);
                greedyCheck();
                break;

            case '+':
                if (!syntax.opEscPlusOneInf()) break;
                token.type = TokenType.OP_REPEAT;
                token.setRepeatLower(1);
                token.setRepeatUpper(QuantifierNode.REPEAT_INFINITE);
                greedyCheck();
                break;

            case '?':
                if (!syntax.opEscQMarkZeroOne()) break;
                token.type = TokenType.OP_REPEAT;
                token.setRepeatLower(0);
                token.setRepeatUpper(1);
                greedyCheck();
                break;

            case '{':
                if (!syntax.opEscBraceInterval()) break;
                switch (fetchRangeQuantifier()) {
                case 0:
                    greedyCheck();
                    break;
                case 2:
                    if (syntax.fixedIntervalIsGreedyOnly()) {
                        possessiveCheck();
                    } else {
                        greedyCheck();
                    }
                    break;
                default: /* 1 : normal char */
                } // inner switch
                break;

            case '|':
                if (!syntax.opEscVBarAlt()) break;
                token.type = TokenType.ALT;
                break;

            case '(':
                if (!syntax.opEscLParenSubexp()) break;
                token.type = TokenType.SUBEXP_OPEN;
                break;

            case ')':
                if (!syntax.opEscLParenSubexp()) break;
                token.type = TokenType.SUBEXP_CLOSE;
                break;

            case 'w':
                if (!syntax.opEscWWord()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.WORD);
                token.setPropNot(false);
                break;

            case 'W':
                if (!syntax.opEscWWord()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.WORD);
                token.setPropNot(true);
                break;

            case 'b':
                if (!syntax.opEscBWordBound()) break;
                token.type = TokenType.ANCHOR;
                token.setAnchor(AnchorType.WORD_BOUND);
                break;

            case 'B':
                if (!syntax.opEscBWordBound()) break;
                token.type = TokenType.ANCHOR;
                token.setAnchor(AnchorType.NOT_WORD_BOUND);
                break;

            case '<':
                if (Config.USE_WORD_BEGIN_END) {
                    if (!syntax.opEscLtGtWordBeginEnd()) break;
                    token.type = TokenType.ANCHOR;
                    token.setAnchor(AnchorType.WORD_BEGIN);
                    break;
                } // USE_WORD_BEGIN_END
                break; // ?
                
            case '>':
                if (Config.USE_WORD_BEGIN_END) {
                    if (!syntax.opEscLtGtWordBeginEnd()) break;
                    token.type = TokenType.ANCHOR;
                    token.setAnchor(AnchorType.WORD_END);
                    break;
                } // USE_WORD_BEGIN_END
                break; // ?

            case 's':
                if (!syntax.opEscSWhiteSpace()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.SPACE);
                token.setPropNot(false);
                break;

            case 'S':
                if (!syntax.opEscSWhiteSpace()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.SPACE);
                token.setPropNot(true);
                break;
                
            case 'd':
                if (!syntax.opEscDDigit()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.DIGIT);
                token.setPropNot(false);
                break;
                
            case 'D':
                if (!syntax.opEscDDigit()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.DIGIT);
                token.setPropNot(true);
                break;

            case 'h':
                if (!syntax.op2EscHXDigit()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.XDIGIT);
                token.setPropNot(false);
                break;

            case 'H':
                if (!syntax.op2EscHXDigit()) break;
                token.type = TokenType.CHAR_TYPE;
                token.setPropCType(CharacterType.XDIGIT);
                token.setPropNot(true);
                break;

            case 'A':
                if (!syntax.opEscAZBufAnchor()) break;
                // begin_buf label
                token.type = TokenType.ANCHOR;
                token.setSubtype(AnchorType.BEGIN_BUF);
                break;
                
            case 'Z':
                if (!syntax.opEscAZBufAnchor()) break;
                token.type = TokenType.ANCHOR;
                token.setSubtype(AnchorType.SEMI_END_BUF);
                break;
                
            case 'z':
                if (!syntax.opEscAZBufAnchor()) break;
                // end_buf label                
                token.type = TokenType.ANCHOR;                
                token.setSubtype(AnchorType.END_BUF);
                break;
                
            case 'G':
                if (!syntax.opEscCapitalGBeginAnchor()) break;
                token.type = TokenType.ANCHOR;
                token.setSubtype(AnchorType.BEGIN_POSITION);
                break;
                
            case '`':
                if (!syntax.op2EscGnuBufAnchor()) break;
                // goto begin_buf
                token.type = TokenType.ANCHOR;
                token.setSubtype(AnchorType.BEGIN_BUF);
                break;

            case '\'':
                if (!syntax.op2EscGnuBufAnchor()) break;
                // goto end_buf                
                token.type = TokenType.ANCHOR;                
                token.setSubtype(AnchorType.END_BUF);
                break;

            case 'x': // extract to helper for all 'x'
                if (!left()) break;
                last = p;
                if (peekIs('{') && syntax.opEscXBraceHex8()) {
                    inc();
                    int num = scanUnsignedHexadecimalNumber(8);
                    if (num < 0) newValueException(ERR_TOO_BIG_WIDE_CHAR_VALUE);
                    if (left()) {
                        if (enc.isXDigit(peek())) newValueException(ERR_TOO_LONG_WIDE_CHAR_VALUE); 
                    }
                    
                    if (p > last + enc.length(bytes, last, stop) && left() && peekIs('}')) {
                        inc();
                        token.type = TokenType.CODE_POINT;
                        token.setCode(num);
                    } else {
                        /* can't read nothing or invalid format */
                        p = last;
                    }
                } else if (syntax.opEscXHex2()) {
                    int num = scanUnsignedHexadecimalNumber(2);
                    if (num < 0) newValueException(ERR_TOO_BIG_NUMBER);
                    if (p == last) { /* can't read nothing. */
                        num = 0; /* but, it's not error */
                    }
                    token.type = TokenType.RAW_BYTE;
                    token.base = 16;
                    token.setC(num);
                }
                break;
                
            case 'u': // extract to helper
                if (!left()) break;
                last = p;
                
                if (syntax.op2EscUHex4()) {
                    int num = scanUnsignedHexadecimalNumber(4);
                    if (num < 0) newValueException(ERR_TOO_BIG_NUMBER);
                    if (p == last) { /* can't read nothing. */
                        num = 0; /* but, it's not error */
                    }
                    token.type = TokenType.CODE_POINT;
                    token.base = 16;
                    token.setCode(num);
                }
                break;
                
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':               
                unfetch();
                last = p;
                int num = scanUnsignedNumber();
                if (num < 0 || num > Config.MAX_BACKREF_NUM) { 
                    // goto skip_backref
                } else if (syntax.opDecimalBackref() && (num <= env.numMem || num <= 9)) { /* This spec. from GNU regex */
                    if (syntax.strictCheckBackref()) {
                        if (num > env.numMem || env.memNodes == null || env.memNodes[num] == null) newValueException(ERR_INVALID_BACKREF);
                    }
                    token.type = TokenType.BACKREF;
                    token.setBackrefNum(1);
                    token.setBackrefRef1(num);
                    token.setBackrefByName(false);
                    if (Config.USE_BACKREF_WITH_LEVEL) token.setBackrefExistLevel(false);
                    break;
                }
                // skip_backref:
                if (c == '8' || c == '9') {
                    /* normal char */
                    p = last;
                    inc();
                    break;
                }
                p = last;
                /* fall through */
                
            case '0':
                if (syntax.opEscOctal3()) {
                    last = p;
                    num = scanUnsignedOctalNumber(c == '0' ? 2 : 3);
                    if (num < 0) newValueException(ERR_TOO_BIG_NUMBER);
                    if (p == last) { /* can't read nothing. */
                        num = 0; /* but, it's not error */
                    }
                    token.type = TokenType.RAW_BYTE;
                    token.base = 8;
                    token.setC(num);
                } else if (c != '0') {
                    inc();
                }
                break;
                
            case 'k':
                if (Config.USE_NAMED_GROUP) {
                    if (syntax.op2EscKNamedBackref()) {
                        fetch();
                        if (c =='<' || c == '\'') {
                            last = p;
                            int backNum;
                            if (Config.USE_BACKREF_WITH_LEVEL) {
                                int[]rbackNum = new int[1];
                                int[]rlevel = new int[1];
                                token.setBackrefExistLevel(fetchNameWithLevel(c, rbackNum, rlevel));
                                token.setBackrefLevel(rlevel[0]);
                                backNum = rbackNum[0];
                            } else {
                                backNum = fetchName(c, true);
                            } // USE_BACKREF_AT_LEVEL
                            int nameEnd = value; // set by fetchNameWithLevel/fetchName
                            
                            if (backNum != 0) {
                                if (backNum < 0) {
                                    backNum = backrefRelToAbs(backNum);
                                    if (backNum <= 0) newValueException(ERR_INVALID_BACKREF);
                                }
                                
                                if (syntax.strictCheckBackref() && (backNum > env.numMem || env.memNodes == null)) {
                                    newValueException(ERR_INVALID_BACKREF);
                                }
                                token.type = TokenType.BACKREF;
                                token.setBackrefByName(false);
                                token.setBackrefNum(1);
                                token.setBackrefRef1(backNum);
                            } else {
                                NameEntry e = env.reg.nameToGroupNumbers(bytes, last, nameEnd);
                                if (e == null) newValueException(ERR_UNDEFINED_NAME_REFERENCE, last, nameEnd);

                                if (syntax.strictCheckBackref()) {
                                    if (e.backNum == 1) {
                                        if (e.backRef1 > env.numMem ||
                                            env.memNodes == null ||
                                            env.memNodes[e.backRef1] == null) newValueException(ERR_INVALID_BACKREF);
                                    } else {
                                        for (int i=0; i<e.backNum; i++) {
                                            if (e.backRefs[i] > env.numMem ||
                                                env.memNodes == null ||
                                                env.memNodes[e.backRefs[i]] == null) newValueException(ERR_INVALID_BACKREF);
                                        }
                                    }
                                }

                                token.type = TokenType.BACKREF;
                                token.setBackrefByName(true);

                                if (e.backNum == 1) {
                                    token.setBackrefNum(1);
                                    token.setBackrefRef1(e.backRef1);
                                } else {
                                    token.setBackrefNum(e.backNum);
                                    token.setBackrefRefs(e.backRefs);
                                }
                            }
                        } else {
                            unfetch();
                        }
                    }
                    
                    break;
                } // USE_NAMED_GROUP
                break;
                
            case 'g':
                if (Config.USE_SUBEXP_CALL) {
                    if (syntax.op2EscGSubexpCall()) {
                        fetch();
                        if (c == '<' || c == '\'') {
                            last = p;
                            int gNum = fetchName(c, true);
                            int nameEnd = value;
                            token.type = TokenType.CALL;
                            token.setCallNameP(last);
                            token.setCallNameEnd(nameEnd);
                            token.setCallGNum(gNum);
                        } else {
                            unfetch();
                        }
                    }
                    break;                    
                } // USE_SUBEXP_CALL
                break;
                
            case 'Q':
                if (syntax.op2EscCapitalQQuote()) {
                    token.type = TokenType.QUOTE_OPEN;
                }
                break;
                
            case 'p':
            case 'P':
                if (peekIs('{') && syntax.op2EscPBraceCharProperty()) {
                    inc();
                    token.type = TokenType.CHAR_PROPERTY;
                    token.setPropNot(c == 'P');
                    
                    if (syntax.op2EscPBraceCircumflexNot()) {
                        fetch();
                        if (c == '^') {
                            token.setPropNot(!token.getPropNot()); 
                        } else {
                            unfetch();
                        }
                    }
                }
                break;
                
            default:
                unfetch();
                num = fetchEscapedValue();

                /* set_raw: */
                if (token.getC() != num) {
                    token.type = TokenType.CODE_POINT;
                    token.setCode(num);
                } else { /* string */
                    p = token.backP + enc.length(bytes, token.backP, stop);
                }
                break;
                
            } // switch (c)
            
        } else {
            token.setC(c);
            token.escaped = false;
            
            // remove code duplication
            if (Config.USE_VARIABLE_META_CHARS) {
                if (c != MetaChar.INEFFECTIVE_META_CHAR && syntax.opVariableMetaCharacters()) {
                    if (c == syntax.metaCharTable.anyChar) { // goto any_char
                        token.type = TokenType.ANYCHAR;
                        break;
                    } else if (c == syntax.metaCharTable.anyTime) { // goto anytime
                        token.type = TokenType.OP_REPEAT;
                        token.setRepeatLower(0);
                        token.setRepeatUpper(QuantifierNode.REPEAT_INFINITE);
                        greedyCheck();
                        break;
                    }  else if (c == syntax.metaCharTable.zeroOrOneTime) { // goto zero_or_one_time
                        token.type = TokenType.OP_REPEAT;
                        token.setRepeatLower(0);
                        token.setRepeatUpper(1);
                        greedyCheck();
                        break;
                    } else if (c == syntax.metaCharTable.oneOrMoreTime) { // goto one_or_more_time
                        token.type = TokenType.OP_REPEAT;
                        token.setRepeatLower(1);
                        token.setRepeatUpper(QuantifierNode.REPEAT_INFINITE);
                        greedyCheck();
                        break;
                    } else if (c == syntax.metaCharTable.anyCharAnyTime) { // goto one_or_more_time
                        token.type = TokenType.ANYCHAR_ANYTIME;
                        break;
                        // goto out
                    }
                }
            } // USE_VARIABLE_META_CHARS
            
            { 
                switch(c) {
                
                case '.':
                    if (!syntax.opDotAnyChar()) break;
                    // any_char:
                    token.type = TokenType.ANYCHAR;
                    break;
                    
                case '*':
                    if (!syntax.opAsteriskZeroInf()) break;
                    // anytime:
                    token.type = TokenType.OP_REPEAT;
                    token.setRepeatLower(0);
                    token.setRepeatUpper(QuantifierNode.REPEAT_INFINITE);
                    greedyCheck();
                    break;

                case '+':
                    if (!syntax.opPlusOneInf()) break;
                    // one_or_more_time:
                    token.type = TokenType.OP_REPEAT;
                    token.setRepeatLower(1);
                    token.setRepeatUpper(QuantifierNode.REPEAT_INFINITE);
                    greedyCheck();
                    break;
                    
                case '?':                   
                    if (!syntax.opQMarkZeroOne()) break;
                    // zero_or_one_time:
                    token.type = TokenType.OP_REPEAT;
                    token.setRepeatLower(0);
                    token.setRepeatUpper(1);
                    greedyCheck();
                    break;
                    
                case '{':
                    if (!syntax.opBraceInterval()) break;
                    switch(fetchRangeQuantifier()) {
                    case 0:
                        greedyCheck();
                        break;
                    case 2:
                        if (syntax.fixedIntervalIsGreedyOnly()) {
                            possessiveCheck();
                        } else {
                            greedyCheck();
                        }
                        break;
                    default: /* 1 : normal char */
                    } // inner switch
                    break;
                    
                case '|':
                    if (!syntax.opVBarAlt()) break;
                    token.type = TokenType.ALT;
                    break;
                    
                case '(':
                    if (peekIs('?') && syntax.op2QMarkGroupEffect()) {
                        inc();
                        if (peekIs('#')) {
                            fetch();
                            while (true) {
                                if (!left()) newSyntaxException(ERR_END_PATTERN_IN_GROUP);
                                fetch();
                                if (c == syntax.metaCharTable.esc) {
                                    if (left()) fetch();
                                } else {
                                    if (c == ')') break;
                                }
                            }
                            continue start; // goto start
                        }
                        unfetch();
                    }
                    
                    if (!syntax.opLParenSubexp()) break;
                    token.type = TokenType.SUBEXP_OPEN;
                    break;
                    
                case ')':
                    if (!syntax.opLParenSubexp()) break;
                    token.type = TokenType.SUBEXP_CLOSE;                    
                    break;
                    
                case '^':
                    if (!syntax.opLineAnchor()) break;
                    token.type = TokenType.ANCHOR;
                    token.setSubtype(isSingleline(env.option) ? AnchorType.BEGIN_BUF : AnchorType.BEGIN_LINE);
                    break;
                    
                case '$':
                    if (!syntax.opLineAnchor()) break;
                    token.type = TokenType.ANCHOR;
                    token.setSubtype(isSingleline(env.option) ? AnchorType.SEMI_END_BUF : AnchorType.END_LINE);
                    break;
                    
                case '[':
                    if (!syntax.opBracketCC()) break;
                    token.type = TokenType.CC_CC_OPEN;
                    break;
                    
                case ']':
                    //if (*src > env->pattern)   /* /].../ is allowed. */
                    //CLOSE_BRACKET_WITHOUT_ESC_WARN(env, (UChar* )"]");
                    break;
                    
                case '#':
                    if (Option.isExtend(env.option)) {
                        while (left()) {
                            fetch();
                            if (enc.isNewLine(c)) break;
                        }
                        continue start; // goto start 
                        
                    }
                    break;
                    
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                case '\f':
                    if (Option.isExtend(env.option)) {
                        continue start; // goto start
                    }
                    break;
                    
                default: // string
                    break;
                    
                } // switch
            }
        }
        
        break;
        } // while
        return token.type;   
    }
    
    private void greedyCheck() {
        if (left() && peekIs('?') && syntax.opQMarkNonGreedy()) {
            
            fetch();

            token.setRepeatGreedy(false);
            token.setRepeatPossessive(false);
        } else {
            possessiveCheck();
        }
    }
    
    private void possessiveCheck() {
        if (left() && peekIs('+') && 
            (syntax.op2PlusPossessiveRepeat() && token.type != TokenType.INTERVAL ||
             syntax.op2PlusPossessiveInterval() && token.type == TokenType.INTERVAL)) {
            
            fetch();
            
            token.setRepeatGreedy(true);
            token.setRepeatPossessive(true);
        } else {
            token.setRepeatGreedy(true);
            token.setRepeatPossessive(false);
        }
    }

    protected final int fetchCharPropertyToCType() {
        mark();

        while (left()) {
            int last = p;
            fetch();
            if (c == '}') {
                return enc.propertyNameToCType(bytes, _p, last);
            } else if (c == '(' || c == ')' || c == '{' || c == '|') {
            	throw new RuntimeException(ERR_INVALID_CHAR_PROPERTY_NAME + " " + new String(bytes));
            }
        }
        newInternalException(ERR_PARSER_BUG);
        return 0; // not reached
    }
}
