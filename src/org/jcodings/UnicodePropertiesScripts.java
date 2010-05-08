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

import org.jcodings.Config;

public interface UnicodePropertiesScripts {
    /* 'Arabic': Script */
    static final int CR_Arabic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      17,
      0x060b, 0x060b,
      0x060d, 0x0615,
      0x061e, 0x061e,
      0x0621, 0x063a,
      0x0641, 0x064a,
      0x0656, 0x065e,
      0x066a, 0x066f,
      0x0671, 0x06dc,
      0x06de, 0x06ff,
      0x0750, 0x076d,
      0xfb50, 0xfbb1,
      0xfbd3, 0xfd3d,
      0xfd50, 0xfd8f,
      0xfd92, 0xfdc7,
      0xfdf0, 0xfdfc,
      0xfe70, 0xfe74,
      0xfe76, 0xfefc
    } : null; /* CR_Arabic */

    /* 'Armenian': Script */
    static final int CR_Armenian[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      5,
      0x0531, 0x0556,
      0x0559, 0x055f,
      0x0561, 0x0587,
      0x058a, 0x058a,
      0xfb13, 0xfb17
    } : null; /* CR_Armenian */

    /* 'Bengali': Script */
    static final int CR_Bengali[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      14,
      0x0981, 0x0983,
      0x0985, 0x098c,
      0x098f, 0x0990,
      0x0993, 0x09a8,
      0x09aa, 0x09b0,
      0x09b2, 0x09b2,
      0x09b6, 0x09b9,
      0x09bc, 0x09c4,
      0x09c7, 0x09c8,
      0x09cb, 0x09ce,
      0x09d7, 0x09d7,
      0x09dc, 0x09dd,
      0x09df, 0x09e3,
      0x09e6, 0x09fa
    } : null; /* CR_Bengali */

    /* 'Bopomofo': Script */
    static final int CR_Bopomofo[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x3105, 0x312c,
      0x31a0, 0x31b7
    } : null; /* CR_Bopomofo */

    /* 'Braille': Script */
    static final int CR_Braille[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x2800, 0x28ff
    } : null; /* CR_Braille */

    /* 'Buginese': Script */
    static final int CR_Buginese[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x1a00, 0x1a1b,
      0x1a1e, 0x1a1f
    } : null; /* CR_Buginese */

    /* 'Buhid': Script */
    static final int CR_Buhid[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x1740, 0x1753
    } : null; /* CR_Buhid */

    /* 'Canadian_Aboriginal': Script */
    static final int CR_Canadian_Aboriginal[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x1401, 0x1676
    } : null; /* CR_Canadian_Aboriginal */

    /* 'Cherokee': Script */
    static final int CR_Cherokee[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x13a0, 0x13f4
    } : null; /* CR_Cherokee */

    /* 'Common': Script */
    static final int CR_Common[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      126,
      0x0000, 0x0040,
      0x005b, 0x0060,
      0x007b, 0x00a9,
      0x00ab, 0x00b9,
      0x00bb, 0x00bf,
      0x00d7, 0x00d7,
      0x00f7, 0x00f7,
      0x02b9, 0x02df,
      0x02e5, 0x02ff,
      0x037e, 0x037e,
      0x0387, 0x0387,
      0x0589, 0x0589,
      0x0600, 0x0603,
      0x060c, 0x060c,
      0x061b, 0x061b,
      0x061f, 0x061f,
      0x0640, 0x0640,
      0x0660, 0x0669,
      0x06dd, 0x06dd,
      0x0964, 0x0965,
      0x0970, 0x0970,
      0x0e3f, 0x0e3f,
      0x10fb, 0x10fb,
      0x16eb, 0x16ed,
      0x1735, 0x1736,
      0x2000, 0x200b,
      0x200e, 0x2063,
      0x206a, 0x2070,
      0x2074, 0x207e,
      0x2080, 0x208e,
      0x20a0, 0x20b5,
      0x2100, 0x2125,
      0x2127, 0x2129,
      0x212c, 0x214c,
      0x2153, 0x2183,
      0x2190, 0x23db,
      0x2400, 0x2426,
      0x2440, 0x244a,
      0x2460, 0x269c,
      0x26a0, 0x26b1,
      0x2701, 0x2704,
      0x2706, 0x2709,
      0x270c, 0x2727,
      0x2729, 0x274b,
      0x274d, 0x274d,
      0x274f, 0x2752,
      0x2756, 0x2756,
      0x2758, 0x275e,
      0x2761, 0x2794,
      0x2798, 0x27af,
      0x27b1, 0x27be,
      0x27c0, 0x27c6,
      0x27d0, 0x27eb,
      0x27f0, 0x27ff,
      0x2900, 0x2b13,
      0x2e00, 0x2e17,
      0x2e1c, 0x2e1d,
      0x2ff0, 0x2ffb,
      0x3000, 0x3004,
      0x3006, 0x3006,
      0x3008, 0x3020,
      0x3030, 0x3037,
      0x303c, 0x303f,
      0x309b, 0x309c,
      0x30a0, 0x30a0,
      0x30fb, 0x30fc,
      0x3190, 0x319f,
      0x31c0, 0x31cf,
      0x3220, 0x3243,
      0x3250, 0x325f,
      0x327e, 0x32fe,
      0x3300, 0x33ff,
      0x4dc0, 0x4dff,
      0xa700, 0xa716,
      0xe000, 0xf8ff,
      0xfd3e, 0xfd3f,
      0xfdfd, 0xfdfd,
      0xfe10, 0xfe19,
      0xfe30, 0xfe52,
      0xfe54, 0xfe66,
      0xfe68, 0xfe6b,
      0xfeff, 0xfeff,
      0xff01, 0xff20,
      0xff3b, 0xff40,
      0xff5b, 0xff65,
      0xff70, 0xff70,
      0xff9e, 0xff9f,
      0xffe0, 0xffe6,
      0xffe8, 0xffee,
      0xfff9, 0xfffd,
      0x10100, 0x10102,
      0x10107, 0x10133,
      0x10137, 0x1013f,
      0x1d000, 0x1d0f5,
      0x1d100, 0x1d126,
      0x1d12a, 0x1d166,
      0x1d16a, 0x1d17a,
      0x1d183, 0x1d184,
      0x1d18c, 0x1d1a9,
      0x1d1ae, 0x1d1dd,
      0x1d300, 0x1d356,
      0x1d400, 0x1d454,
      0x1d456, 0x1d49c,
      0x1d49e, 0x1d49f,
      0x1d4a2, 0x1d4a2,
      0x1d4a5, 0x1d4a6,
      0x1d4a9, 0x1d4ac,
      0x1d4ae, 0x1d4b9,
      0x1d4bb, 0x1d4bb,
      0x1d4bd, 0x1d4c3,
      0x1d4c5, 0x1d505,
      0x1d507, 0x1d50a,
      0x1d50d, 0x1d514,
      0x1d516, 0x1d51c,
      0x1d51e, 0x1d539,
      0x1d53b, 0x1d53e,
      0x1d540, 0x1d544,
      0x1d546, 0x1d546,
      0x1d54a, 0x1d550,
      0x1d552, 0x1d6a5,
      0x1d6a8, 0x1d7c9,
      0x1d7ce, 0x1d7ff,
      0xe0001, 0xe0001,
      0xe0020, 0xe007f,
      0xf0000, 0xffffd,
      0x100000, 0x10fffd
    } : null; /* CR_Common */

    /* 'Coptic': Script */
    static final int CR_Coptic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      3,
      0x03e2, 0x03ef,
      0x2c80, 0x2cea,
      0x2cf9, 0x2cff
    } : null; /* CR_Coptic */

    /* 'Cypriot': Script */
    static final int CR_Cypriot[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      6,
      0x10800, 0x10805,
      0x10808, 0x10808,
      0x1080a, 0x10835,
      0x10837, 0x10838,
      0x1083c, 0x1083c,
      0x1083f, 0x1083f
    } : null; /* CR_Cypriot */

    /* 'Cyrillic': Script */
    static final int CR_Cyrillic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      6,
      0x0400, 0x0486,
      0x0488, 0x04ce,
      0x04d0, 0x04f9,
      0x0500, 0x050f,
      0x1d2b, 0x1d2b,
      0x1d78, 0x1d78
    } : null; /* CR_Cyrillic */

    /* 'Deseret': Script */
    static final int CR_Deseret[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x10400, 0x1044f
    } : null; /* CR_Deseret */

    /* 'Devanagari': Script */
    static final int CR_Devanagari[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      6,
      0x0901, 0x0939,
      0x093c, 0x094d,
      0x0950, 0x0954,
      0x0958, 0x0963,
      0x0966, 0x096f,
      0x097d, 0x097d
    } : null; /* CR_Devanagari */

    /* 'Ethiopic': Script */
    static final int CR_Ethiopic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      27,
      0x1200, 0x1248,
      0x124a, 0x124d,
      0x1250, 0x1256,
      0x1258, 0x1258,
      0x125a, 0x125d,
      0x1260, 0x1288,
      0x128a, 0x128d,
      0x1290, 0x12b0,
      0x12b2, 0x12b5,
      0x12b8, 0x12be,
      0x12c0, 0x12c0,
      0x12c2, 0x12c5,
      0x12c8, 0x12d6,
      0x12d8, 0x1310,
      0x1312, 0x1315,
      0x1318, 0x135a,
      0x135f, 0x137c,
      0x1380, 0x1399,
      0x2d80, 0x2d96,
      0x2da0, 0x2da6,
      0x2da8, 0x2dae,
      0x2db0, 0x2db6,
      0x2db8, 0x2dbe,
      0x2dc0, 0x2dc6,
      0x2dc8, 0x2dce,
      0x2dd0, 0x2dd6,
      0x2dd8, 0x2dde
    } : null; /* CR_Ethiopic */

    /* 'Georgian': Script */
    static final int CR_Georgian[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      4,
      0x10a0, 0x10c5,
      0x10d0, 0x10fa,
      0x10fc, 0x10fc,
      0x2d00, 0x2d25
    } : null; /* CR_Georgian */

    /* 'Glagolitic': Script */
    static final int CR_Glagolitic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x2c00, 0x2c2e,
      0x2c30, 0x2c5e
    } : null; /* CR_Glagolitic */

    /* 'Gothic': Script */
    static final int CR_Gothic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x10330, 0x1034a
    } : null; /* CR_Gothic */

    /* 'Greek': Script */
    static final int CR_Greek[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      31,
      0x0374, 0x0375,
      0x037a, 0x037a,
      0x0384, 0x0386,
      0x0388, 0x038a,
      0x038c, 0x038c,
      0x038e, 0x03a1,
      0x03a3, 0x03ce,
      0x03d0, 0x03e1,
      0x03f0, 0x03ff,
      0x1d26, 0x1d2a,
      0x1d5d, 0x1d61,
      0x1d66, 0x1d6a,
      0x1f00, 0x1f15,
      0x1f18, 0x1f1d,
      0x1f20, 0x1f45,
      0x1f48, 0x1f4d,
      0x1f50, 0x1f57,
      0x1f59, 0x1f59,
      0x1f5b, 0x1f5b,
      0x1f5d, 0x1f5d,
      0x1f5f, 0x1f7d,
      0x1f80, 0x1fb4,
      0x1fb6, 0x1fc4,
      0x1fc6, 0x1fd3,
      0x1fd6, 0x1fdb,
      0x1fdd, 0x1fef,
      0x1ff2, 0x1ff4,
      0x1ff6, 0x1ffe,
      0x2126, 0x2126,
      0x10140, 0x1018a,
      0x1d200, 0x1d245
    } : null; /* CR_Greek */

    /* 'Gujarati': Script */
    static final int CR_Gujarati[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      14,
      0x0a81, 0x0a83,
      0x0a85, 0x0a8d,
      0x0a8f, 0x0a91,
      0x0a93, 0x0aa8,
      0x0aaa, 0x0ab0,
      0x0ab2, 0x0ab3,
      0x0ab5, 0x0ab9,
      0x0abc, 0x0ac5,
      0x0ac7, 0x0ac9,
      0x0acb, 0x0acd,
      0x0ad0, 0x0ad0,
      0x0ae0, 0x0ae3,
      0x0ae6, 0x0aef,
      0x0af1, 0x0af1
    } : null; /* CR_Gujarati */

    /* 'Gurmukhi': Script */
    static final int CR_Gurmukhi[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      15,
      0x0a01, 0x0a03,
      0x0a05, 0x0a0a,
      0x0a0f, 0x0a10,
      0x0a13, 0x0a28,
      0x0a2a, 0x0a30,
      0x0a32, 0x0a33,
      0x0a35, 0x0a36,
      0x0a38, 0x0a39,
      0x0a3c, 0x0a3c,
      0x0a3e, 0x0a42,
      0x0a47, 0x0a48,
      0x0a4b, 0x0a4d,
      0x0a59, 0x0a5c,
      0x0a5e, 0x0a5e,
      0x0a66, 0x0a74
    } : null; /* CR_Gurmukhi */

    /* 'Han': Script */
    static final int CR_Han[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      14,
      0x2e80, 0x2e99,
      0x2e9b, 0x2ef3,
      0x2f00, 0x2fd5,
      0x3005, 0x3005,
      0x3007, 0x3007,
      0x3021, 0x3029,
      0x3038, 0x303b,
      0x3400, 0x4db5,
      0x4e00, 0x9fbb,
      0xf900, 0xfa2d,
      0xfa30, 0xfa6a,
      0xfa70, 0xfad9,
      0x20000, 0x2a6d6,
      0x2f800, 0x2fa1d
    } : null; /* CR_Han */

    /* 'Hangul': Script */
    static final int CR_Hangul[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      12,
      0x1100, 0x1159,
      0x115f, 0x11a2,
      0x11a8, 0x11f9,
      0x3131, 0x318e,
      0x3200, 0x321e,
      0x3260, 0x327d,
      0xac00, 0xd7a3,
      0xffa0, 0xffbe,
      0xffc2, 0xffc7,
      0xffca, 0xffcf,
      0xffd2, 0xffd7,
      0xffda, 0xffdc
    } : null; /* CR_Hangul */

    /* 'Hanunoo': Script */
    static final int CR_Hanunoo[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x1720, 0x1734
    } : null; /* CR_Hanunoo */

    /* 'Hebrew': Script */
    static final int CR_Hebrew[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      10,
      0x0591, 0x05b9,
      0x05bb, 0x05c7,
      0x05d0, 0x05ea,
      0x05f0, 0x05f4,
      0xfb1d, 0xfb36,
      0xfb38, 0xfb3c,
      0xfb3e, 0xfb3e,
      0xfb40, 0xfb41,
      0xfb43, 0xfb44,
      0xfb46, 0xfb4f
    } : null; /* CR_Hebrew */

    /* 'Hiragana': Script */
    static final int CR_Hiragana[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x3041, 0x3096,
      0x309d, 0x309f
    } : null; /* CR_Hiragana */

    /* 'Inherited': Script */
    static final int CR_Inherited[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      15,
      0x0300, 0x036f,
      0x064b, 0x0655,
      0x0670, 0x0670,
      0x1dc0, 0x1dc3,
      0x200c, 0x200d,
      0x20d0, 0x20eb,
      0x302a, 0x302f,
      0x3099, 0x309a,
      0xfe00, 0xfe0f,
      0xfe20, 0xfe23,
      0x1d167, 0x1d169,
      0x1d17b, 0x1d182,
      0x1d185, 0x1d18b,
      0x1d1aa, 0x1d1ad,
      0xe0100, 0xe01ef
    } : null; /* CR_Inherited */

    /* 'Kannada': Script */
    static final int CR_Kannada[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      13,
      0x0c82, 0x0c83,
      0x0c85, 0x0c8c,
      0x0c8e, 0x0c90,
      0x0c92, 0x0ca8,
      0x0caa, 0x0cb3,
      0x0cb5, 0x0cb9,
      0x0cbc, 0x0cc4,
      0x0cc6, 0x0cc8,
      0x0cca, 0x0ccd,
      0x0cd5, 0x0cd6,
      0x0cde, 0x0cde,
      0x0ce0, 0x0ce1,
      0x0ce6, 0x0cef
    } : null; /* CR_Kannada */

    /* 'Katakana': Script */
    static final int CR_Katakana[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      5,
      0x30a1, 0x30fa,
      0x30fd, 0x30ff,
      0x31f0, 0x31ff,
      0xff66, 0xff6f,
      0xff71, 0xff9d
    } : null; /* CR_Katakana */

    /* 'Kharoshthi': Script */
    static final int CR_Kharoshthi[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      8,
      0x10a00, 0x10a03,
      0x10a05, 0x10a06,
      0x10a0c, 0x10a13,
      0x10a15, 0x10a17,
      0x10a19, 0x10a33,
      0x10a38, 0x10a3a,
      0x10a3f, 0x10a47,
      0x10a50, 0x10a58
    } : null; /* CR_Kharoshthi */

    /* 'Khmer': Script */
    static final int CR_Khmer[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      4,
      0x1780, 0x17dd,
      0x17e0, 0x17e9,
      0x17f0, 0x17f9,
      0x19e0, 0x19ff
    } : null; /* CR_Khmer */

    /* 'Lao': Script */
    static final int CR_Lao[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      18,
      0x0e81, 0x0e82,
      0x0e84, 0x0e84,
      0x0e87, 0x0e88,
      0x0e8a, 0x0e8a,
      0x0e8d, 0x0e8d,
      0x0e94, 0x0e97,
      0x0e99, 0x0e9f,
      0x0ea1, 0x0ea3,
      0x0ea5, 0x0ea5,
      0x0ea7, 0x0ea7,
      0x0eaa, 0x0eab,
      0x0ead, 0x0eb9,
      0x0ebb, 0x0ebd,
      0x0ec0, 0x0ec4,
      0x0ec6, 0x0ec6,
      0x0ec8, 0x0ecd,
      0x0ed0, 0x0ed9,
      0x0edc, 0x0edd
    } : null; /* CR_Lao */

    /* 'Latin': Script */
    static final int CR_Latin[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      23,
      0x0041, 0x005a,
      0x0061, 0x007a,
      0x00aa, 0x00aa,
      0x00ba, 0x00ba,
      0x00c0, 0x00d6,
      0x00d8, 0x00f6,
      0x00f8, 0x0241,
      0x0250, 0x02b8,
      0x02e0, 0x02e4,
      0x1d00, 0x1d25,
      0x1d2c, 0x1d5c,
      0x1d62, 0x1d65,
      0x1d6b, 0x1d77,
      0x1d79, 0x1dbf,
      0x1e00, 0x1e9b,
      0x1ea0, 0x1ef9,
      0x2071, 0x2071,
      0x207f, 0x207f,
      0x2090, 0x2094,
      0x212a, 0x212b,
      0xfb00, 0xfb06,
      0xff21, 0xff3a,
      0xff41, 0xff5a
    } : null; /* CR_Latin */

    /* 'Limbu': Script */
    static final int CR_Limbu[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      5,
      0x1900, 0x191c,
      0x1920, 0x192b,
      0x1930, 0x193b,
      0x1940, 0x1940,
      0x1944, 0x194f
    } : null; /* CR_Limbu */

    /* 'Linear_B': Script */
    static final int CR_Linear_B[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      7,
      0x10000, 0x1000b,
      0x1000d, 0x10026,
      0x10028, 0x1003a,
      0x1003c, 0x1003d,
      0x1003f, 0x1004d,
      0x10050, 0x1005d,
      0x10080, 0x100fa
    } : null; /* CR_Linear_B */

    /* 'Malayalam': Script */
    static final int CR_Malayalam[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      11,
      0x0d02, 0x0d03,
      0x0d05, 0x0d0c,
      0x0d0e, 0x0d10,
      0x0d12, 0x0d28,
      0x0d2a, 0x0d39,
      0x0d3e, 0x0d43,
      0x0d46, 0x0d48,
      0x0d4a, 0x0d4d,
      0x0d57, 0x0d57,
      0x0d60, 0x0d61,
      0x0d66, 0x0d6f
    } : null; /* CR_Malayalam */

    /* 'Mongolian': Script */
    static final int CR_Mongolian[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      4,
      0x1800, 0x180e,
      0x1810, 0x1819,
      0x1820, 0x1877,
      0x1880, 0x18a9
    } : null; /* CR_Mongolian */

    /* 'Myanmar': Script */
    static final int CR_Myanmar[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      6,
      0x1000, 0x1021,
      0x1023, 0x1027,
      0x1029, 0x102a,
      0x102c, 0x1032,
      0x1036, 0x1039,
      0x1040, 0x1059
    } : null; /* CR_Myanmar */

    /* 'New_Tai_Lue': Script */
    static final int CR_New_Tai_Lue[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      4,
      0x1980, 0x19a9,
      0x19b0, 0x19c9,
      0x19d0, 0x19d9,
      0x19de, 0x19df
    } : null; /* CR_New_Tai_Lue */

    /* 'Ogham': Script */
    static final int CR_Ogham[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x1680, 0x169c
    } : null; /* CR_Ogham */

    /* 'Old_Italic': Script */
    static final int CR_Old_Italic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x10300, 0x1031e,
      0x10320, 0x10323
    } : null; /* CR_Old_Italic */

    /* 'Old_Persian': Script */
    static final int CR_Old_Persian[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x103a0, 0x103c3,
      0x103c8, 0x103d5
    } : null; /* CR_Old_Persian */

    /* 'Oriya': Script */
    static final int CR_Oriya[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      14,
      0x0b01, 0x0b03,
      0x0b05, 0x0b0c,
      0x0b0f, 0x0b10,
      0x0b13, 0x0b28,
      0x0b2a, 0x0b30,
      0x0b32, 0x0b33,
      0x0b35, 0x0b39,
      0x0b3c, 0x0b43,
      0x0b47, 0x0b48,
      0x0b4b, 0x0b4d,
      0x0b56, 0x0b57,
      0x0b5c, 0x0b5d,
      0x0b5f, 0x0b61,
      0x0b66, 0x0b71
    } : null; /* CR_Oriya */

    /* 'Osmanya': Script */
    static final int CR_Osmanya[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x10480, 0x1049d,
      0x104a0, 0x104a9
    } : null; /* CR_Osmanya */

    /* 'Runic': Script */
    static final int CR_Runic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x16a0, 0x16ea,
      0x16ee, 0x16f0
    } : null; /* CR_Runic */

    /* 'Shavian': Script */
    static final int CR_Shavian[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x10450, 0x1047f
    } : null; /* CR_Shavian */

    /* 'Sinhala': Script */
    static final int CR_Sinhala[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      11,
      0x0d82, 0x0d83,
      0x0d85, 0x0d96,
      0x0d9a, 0x0db1,
      0x0db3, 0x0dbb,
      0x0dbd, 0x0dbd,
      0x0dc0, 0x0dc6,
      0x0dca, 0x0dca,
      0x0dcf, 0x0dd4,
      0x0dd6, 0x0dd6,
      0x0dd8, 0x0ddf,
      0x0df2, 0x0df4
    } : null; /* CR_Sinhala */

    /* 'Syloti_Nagri': Script */
    static final int CR_Syloti_Nagri[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0xa800, 0xa82b
    } : null; /* CR_Syloti_Nagri */

    /* 'Syriac': Script */
    static final int CR_Syriac[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      3,
      0x0700, 0x070d,
      0x070f, 0x074a,
      0x074d, 0x074f
    } : null; /* CR_Syriac */

    /* 'Tagalog': Script */
    static final int CR_Tagalog[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x1700, 0x170c,
      0x170e, 0x1714
    } : null; /* CR_Tagalog */

    /* 'Tagbanwa': Script */
    static final int CR_Tagbanwa[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      3,
      0x1760, 0x176c,
      0x176e, 0x1770,
      0x1772, 0x1773
    } : null; /* CR_Tagbanwa */

    /* 'Tai_Le': Script */
    static final int CR_Tai_Le[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x1950, 0x196d,
      0x1970, 0x1974
    } : null; /* CR_Tai_Le */

    /* 'Tamil': Script */
    static final int CR_Tamil[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      15,
      0x0b82, 0x0b83,
      0x0b85, 0x0b8a,
      0x0b8e, 0x0b90,
      0x0b92, 0x0b95,
      0x0b99, 0x0b9a,
      0x0b9c, 0x0b9c,
      0x0b9e, 0x0b9f,
      0x0ba3, 0x0ba4,
      0x0ba8, 0x0baa,
      0x0bae, 0x0bb9,
      0x0bbe, 0x0bc2,
      0x0bc6, 0x0bc8,
      0x0bca, 0x0bcd,
      0x0bd7, 0x0bd7,
      0x0be6, 0x0bfa
    } : null; /* CR_Tamil */

    /* 'Telugu': Script */
    static final int CR_Telugu[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      12,
      0x0c01, 0x0c03,
      0x0c05, 0x0c0c,
      0x0c0e, 0x0c10,
      0x0c12, 0x0c28,
      0x0c2a, 0x0c33,
      0x0c35, 0x0c39,
      0x0c3e, 0x0c44,
      0x0c46, 0x0c48,
      0x0c4a, 0x0c4d,
      0x0c55, 0x0c56,
      0x0c60, 0x0c61,
      0x0c66, 0x0c6f
    } : null; /* CR_Telugu */

    /* 'Thaana': Script */
    static final int CR_Thaana[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      1,
      0x0780, 0x07b1
    } : null; /* CR_Thaana */

    /* 'Thai': Script */
    static final int CR_Thai[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x0e01, 0x0e3a,
      0x0e40, 0x0e5b
    } : null; /* CR_Thai */

    /* 'Tibetan': Script */
    static final int CR_Tibetan[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      7,
      0x0f00, 0x0f47,
      0x0f49, 0x0f6a,
      0x0f71, 0x0f8b,
      0x0f90, 0x0f97,
      0x0f99, 0x0fbc,
      0x0fbe, 0x0fcc,
      0x0fcf, 0x0fd1
    } : null; /* CR_Tibetan */

    /* 'Tifinagh': Script */
    static final int CR_Tifinagh[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x2d30, 0x2d65,
      0x2d6f, 0x2d6f
    } : null; /* CR_Tifinagh */

    /* 'Ugaritic': Script */
    static final int CR_Ugaritic[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0x10380, 0x1039d,
      0x1039f, 0x1039f
    } : null; /* CR_Ugaritic */

    /* 'Yi': Script */
    static final int CR_Yi[] = Config.USE_UNICODE_PROPERTIES ? new int[]{
      2,
      0xa000, 0xa48c,
      0xa490, 0xa4c6
    } : null; /* CR_Yi */

    // #endif /* USE_UNICODE_PROPERTIES */
}
