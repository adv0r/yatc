/*      */ package org.codehaus.jackson.impl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import org.codehaus.jackson.Base64Variant;
/*      */ import org.codehaus.jackson.JsonParseException;
/*      */ import org.codehaus.jackson.JsonParser.Feature;
/*      */ import org.codehaus.jackson.JsonToken;
/*      */ import org.codehaus.jackson.ObjectCodec;
/*      */ import org.codehaus.jackson.io.IOContext;
/*      */ import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
/*      */ import org.codehaus.jackson.sym.Name;
/*      */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*      */ import org.codehaus.jackson.util.CharTypes;
/*      */ import org.codehaus.jackson.util.TextBuffer;
/*      */ 
/*      */ public final class Utf8StreamParser extends StreamBasedParserBase
/*      */ {
/*      */   static final byte BYTE_LF = 10;
/*      */   private static final byte BYTE_0 = 0;
/*   21 */   private static final int[] sInputCodesUtf8 = CharTypes.getInputCodeUtf8();
/*      */ 
/*   23 */   private static final int[] sInputCodesLatin1 = CharTypes.getInputCodeLatin1();
/*      */   protected ObjectCodec _objectCodec;
/*      */   protected final BytesToNameCanonicalizer _symbols;
/*   50 */   protected int[] _quadBuffer = new int[16];
/*      */ 
/*   57 */   protected boolean _tokenIncomplete = false;
/*      */   private int _quad1;
/*      */ 
/*      */   public Utf8StreamParser(IOContext ctxt, int features, InputStream in, ObjectCodec codec, BytesToNameCanonicalizer sym, byte[] inputBuffer, int start, int end, boolean bufferRecyclable)
/*      */   {
/*   75 */     super(ctxt, features, in, inputBuffer, start, end, bufferRecyclable);
/*   76 */     this._objectCodec = codec;
/*   77 */     this._symbols = sym;
/*      */ 
/*   79 */     if (!JsonParser.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features))
/*      */     {
/*   81 */       _throwInternal();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectCodec getCodec()
/*      */   {
/*   87 */     return this._objectCodec;
/*      */   }
/*      */ 
/*      */   public void setCodec(ObjectCodec c)
/*      */   {
/*   92 */     this._objectCodec = c;
/*      */   }
/*      */ 
/*      */   public String getText()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  105 */     JsonToken t = this._currToken;
/*  106 */     if (t == JsonToken.VALUE_STRING) {
/*  107 */       if (this._tokenIncomplete) {
/*  108 */         this._tokenIncomplete = false;
/*  109 */         _finishString();
/*      */       }
/*  111 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  113 */     return _getText2(t);
/*      */   }
/*      */ 
/*      */   protected final String _getText2(JsonToken t)
/*      */   {
/*  118 */     if (t == null) {
/*  119 */       return null;
/*      */     }
/*  121 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
/*      */     case 1:
/*  123 */       return this._parsingContext.getCurrentName();
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  129 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  131 */     return t.asString();
/*      */   }
/*      */ 
/*      */   public char[] getTextCharacters()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  138 */     if (this._currToken != null) {
/*  139 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
/*      */       {
/*      */       case 1:
/*  142 */         if (!this._nameCopied) {
/*  143 */           String name = this._parsingContext.getCurrentName();
/*  144 */           int nameLen = name.length();
/*  145 */           if (this._nameCopyBuffer == null)
/*  146 */             this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  147 */           else if (this._nameCopyBuffer.length < nameLen) {
/*  148 */             this._nameCopyBuffer = new char[nameLen];
/*      */           }
/*  150 */           name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  151 */           this._nameCopied = true;
/*      */         }
/*  153 */         return this._nameCopyBuffer;
/*      */       case 2:
/*  156 */         if (!this._tokenIncomplete) break;
/*  157 */         this._tokenIncomplete = false;
/*  158 */         _finishString();
/*      */       case 3:
/*      */       case 4:
/*  163 */         return this._textBuffer.getTextBuffer();
/*      */       }
/*      */ 
/*  166 */       return this._currToken.asCharArray();
/*      */     }
/*      */ 
/*  169 */     return null;
/*      */   }
/*      */ 
/*      */   public int getTextLength()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  176 */     if (this._currToken != null) {
/*  177 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
/*      */       {
/*      */       case 1:
/*  180 */         return this._parsingContext.getCurrentName().length();
/*      */       case 2:
/*  182 */         if (!this._tokenIncomplete) break;
/*  183 */         this._tokenIncomplete = false;
/*  184 */         _finishString();
/*      */       case 3:
/*      */       case 4:
/*  189 */         return this._textBuffer.size();
/*      */       }
/*      */ 
/*  192 */       return this._currToken.asCharArray().length;
/*      */     }
/*      */ 
/*  195 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getTextOffset()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  202 */     if (this._currToken != null) {
/*  203 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*      */       case 1:
/*  205 */         return 0;
/*      */       case 2:
/*  207 */         if (!this._tokenIncomplete) break;
/*  208 */         this._tokenIncomplete = false;
/*  209 */         _finishString();
/*      */       case 3:
/*      */       case 4:
/*  214 */         return this._textBuffer.getTextOffset();
/*      */       }
/*      */     }
/*  217 */     return 0;
/*      */   }
/*      */ 
/*      */   public byte[] getBinaryValue(Base64Variant b64variant)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  224 */     if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
/*      */     {
/*  226 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */ 
/*  231 */     if (this._tokenIncomplete) {
/*      */       try {
/*  233 */         this._binaryValue = _decodeBase64(b64variant);
/*      */       } catch (IllegalArgumentException iae) {
/*  235 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       }
/*      */ 
/*  240 */       this._tokenIncomplete = false;
/*      */     }
/*  242 */     return this._binaryValue;
/*      */   }
/*      */ 
/*      */   public JsonToken nextToken()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  263 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  264 */       return _nextAfterName();
/*      */     }
/*  266 */     if (this._tokenIncomplete) {
/*  267 */       _skipString();
/*      */     }
/*      */ 
/*  270 */     int i = _skipWSOrEnd();
/*  271 */     if (i < 0)
/*      */     {
/*  275 */       close();
/*  276 */       return this._currToken = null;
/*      */     }
/*      */ 
/*  282 */     this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
/*  283 */     this._tokenInputRow = this._currInputRow;
/*  284 */     this._tokenInputCol = (this._inputPtr - this._currInputRowStart - 1);
/*      */ 
/*  287 */     this._binaryValue = null;
/*      */ 
/*  290 */     if (i == 93) {
/*  291 */       if (!this._parsingContext.inArray()) {
/*  292 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  294 */       this._parsingContext = this._parsingContext.getParent();
/*  295 */       return this._currToken = JsonToken.END_ARRAY;
/*      */     }
/*  297 */     if (i == 125) {
/*  298 */       if (!this._parsingContext.inObject()) {
/*  299 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  301 */       this._parsingContext = this._parsingContext.getParent();
/*  302 */       return this._currToken = JsonToken.END_OBJECT;
/*      */     }
/*      */ 
/*  306 */     if (this._parsingContext.expectComma()) {
/*  307 */       if (i != 44) {
/*  308 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
/*      */       }
/*  310 */       i = _skipWS();
/*      */     }
/*      */ 
/*  317 */     if (!this._parsingContext.inObject()) {
/*  318 */       return _nextTokenNotInObject(i);
/*      */     }
/*      */ 
/*  321 */     Name n = _parseFieldName(i);
/*  322 */     this._parsingContext.setCurrentName(n.getName());
/*  323 */     this._currToken = JsonToken.FIELD_NAME;
/*  324 */     i = _skipWS();
/*  325 */     if (i != 58) {
/*  326 */       _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */     }
/*  328 */     i = _skipWS();
/*      */ 
/*  331 */     if (i == 34) {
/*  332 */       this._tokenIncomplete = true;
/*  333 */       this._nextToken = JsonToken.VALUE_STRING;
/*  334 */       return this._currToken;
/*      */     }
/*      */     JsonToken t;
/*  338 */     switch (i) {
/*      */     case 91:
/*  340 */       t = JsonToken.START_ARRAY;
/*  341 */       break;
/*      */     case 123:
/*  343 */       t = JsonToken.START_OBJECT;
/*  344 */       break;
/*      */     case 93:
/*      */     case 125:
/*  349 */       _reportUnexpectedChar(i, "expected a value");
/*      */     case 116:
/*  351 */       _matchToken(JsonToken.VALUE_TRUE);
/*  352 */       t = JsonToken.VALUE_TRUE;
/*  353 */       break;
/*      */     case 102:
/*  355 */       _matchToken(JsonToken.VALUE_FALSE);
/*  356 */       t = JsonToken.VALUE_FALSE;
/*  357 */       break;
/*      */     case 110:
/*  359 */       _matchToken(JsonToken.VALUE_NULL);
/*  360 */       t = JsonToken.VALUE_NULL;
/*  361 */       break;
/*      */     case 45:
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*  378 */       t = parseNumberText(i);
/*  379 */       break;
/*      */     default:
/*  381 */       t = _handleUnexpectedValue(i);
/*      */     }
/*  383 */     this._nextToken = t;
/*  384 */     return this._currToken;
/*      */   }
/*      */ 
/*      */   private final JsonToken _nextTokenNotInObject(int i)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  390 */     if (i == 34) {
/*  391 */       this._tokenIncomplete = true;
/*  392 */       return this._currToken = JsonToken.VALUE_STRING;
/*      */     }
/*  394 */     switch (i) {
/*      */     case 91:
/*  396 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  397 */       return this._currToken = JsonToken.START_ARRAY;
/*      */     case 123:
/*  399 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*  400 */       return this._currToken = JsonToken.START_OBJECT;
/*      */     case 93:
/*      */     case 125:
/*  405 */       _reportUnexpectedChar(i, "expected a value");
/*      */     case 116:
/*  407 */       _matchToken(JsonToken.VALUE_TRUE);
/*  408 */       return this._currToken = JsonToken.VALUE_TRUE;
/*      */     case 102:
/*  410 */       _matchToken(JsonToken.VALUE_FALSE);
/*  411 */       return this._currToken = JsonToken.VALUE_FALSE;
/*      */     case 110:
/*  413 */       _matchToken(JsonToken.VALUE_NULL);
/*  414 */       return this._currToken = JsonToken.VALUE_NULL;
/*      */     case 45:
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*  430 */       return this._currToken = parseNumberText(i);
/*      */     }
/*  432 */     return this._currToken = _handleUnexpectedValue(i);
/*      */   }
/*      */ 
/*      */   private final JsonToken _nextAfterName()
/*      */   {
/*  437 */     this._nameCopied = false;
/*  438 */     JsonToken t = this._nextToken;
/*  439 */     this._nextToken = null;
/*      */ 
/*  441 */     if (t == JsonToken.START_ARRAY)
/*  442 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  443 */     else if (t == JsonToken.START_OBJECT) {
/*  444 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     }
/*  446 */     return this._currToken = t;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  452 */     super.close();
/*      */ 
/*  454 */     this._symbols.release();
/*      */   }
/*      */ 
/*      */   protected final JsonToken parseNumberText(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  482 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*  483 */     int outPtr = 0;
/*  484 */     boolean negative = c == 45;
/*      */ 
/*  487 */     if (negative) {
/*  488 */       outBuf[(outPtr++)] = '-';
/*      */ 
/*  490 */       if (this._inputPtr >= this._inputEnd) {
/*  491 */         loadMoreGuaranteed();
/*      */       }
/*  493 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */ 
/*  495 */       if ((c < 48) || (c > 57)) {
/*  496 */         reportInvalidNumber("Missing integer part (next char " + _getCharDesc(c) + ")");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  501 */     if (c == 48) {
/*  502 */       _verifyNoLeadingZeroes();
/*      */     }
/*      */ 
/*  506 */     outBuf[(outPtr++)] = (char)c;
/*  507 */     int intLen = 1;
/*      */ 
/*  510 */     int end = this._inputPtr + outBuf.length;
/*  511 */     if (end > this._inputEnd) {
/*  512 */       end = this._inputEnd;
/*      */     }
/*      */ 
/*      */     while (true)
/*      */     {
/*  517 */       if (this._inputPtr >= end)
/*      */       {
/*  519 */         return _parserNumber2(outBuf, outPtr, negative, intLen);
/*      */       }
/*  521 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  522 */       if ((c < 48) || (c > 57)) {
/*      */         break;
/*      */       }
/*  525 */       intLen++;
/*  526 */       outBuf[(outPtr++)] = (char)c;
/*      */     }
/*  528 */     if ((c == 46) || (c == 101) || (c == 69)) {
/*  529 */       return _parseFloatText(outBuf, outPtr, c, negative, intLen);
/*      */     }
/*      */ 
/*  532 */     this._inputPtr -= 1;
/*  533 */     this._textBuffer.setCurrentLength(outPtr);
/*      */ 
/*  536 */     return resetInt(negative, intLen);
/*      */   }
/*      */ 
/*      */   private final JsonToken _parserNumber2(char[] outBuf, int outPtr, boolean negative, int intPartLength)
/*      */     throws IOException, JsonParseException
/*      */   {
/*      */     while (true)
/*      */     {
/*  549 */       if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*  550 */         this._textBuffer.setCurrentLength(outPtr);
/*  551 */         return resetInt(negative, intPartLength);
/*      */       }
/*  553 */       int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  554 */       if ((c > 57) || (c < 48)) {
/*  555 */         if ((c != 46) && (c != 101) && (c != 69)) break;
/*  556 */         return _parseFloatText(outBuf, outPtr, c, negative, intPartLength);
/*      */       }
/*      */ 
/*  560 */       if (outPtr >= outBuf.length) {
/*  561 */         outBuf = this._textBuffer.finishCurrentSegment();
/*  562 */         outPtr = 0;
/*      */       }
/*  564 */       outBuf[(outPtr++)] = (char)c;
/*  565 */       intPartLength++;
/*      */     }
/*  567 */     this._inputPtr -= 1;
/*  568 */     this._textBuffer.setCurrentLength(outPtr);
/*      */ 
/*  571 */     return resetInt(negative, intPartLength);
/*      */   }
/*      */ 
/*      */   private final void _verifyNoLeadingZeroes()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  583 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*  584 */       return;
/*      */     }
/*  586 */     if (this._inputBuffer[this._inputPtr] == 0)
/*  587 */       reportInvalidNumber("Leading zeroes not allowed");
/*      */   }
/*      */ 
/*      */   private final JsonToken _parseFloatText(char[] outBuf, int outPtr, int c, boolean negative, int integerPartLength)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  595 */     int fractLen = 0;
/*  596 */     boolean eof = false;
/*      */ 
/*  599 */     if (c == 46) {
/*  600 */       outBuf[(outPtr++)] = (char)c;
/*      */       while (true)
/*      */       {
/*  604 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*  605 */           eof = true;
/*  606 */           break;
/*      */         }
/*  608 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  609 */         if ((c < 48) || (c > 57)) {
/*      */           break;
/*      */         }
/*  612 */         fractLen++;
/*  613 */         if (outPtr >= outBuf.length) {
/*  614 */           outBuf = this._textBuffer.finishCurrentSegment();
/*  615 */           outPtr = 0;
/*      */         }
/*  617 */         outBuf[(outPtr++)] = (char)c;
/*      */       }
/*      */ 
/*  620 */       if (fractLen == 0) {
/*  621 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*      */       }
/*      */     }
/*      */ 
/*  625 */     int expLen = 0;
/*  626 */     if ((c == 101) || (c == 69)) {
/*  627 */       if (outPtr >= outBuf.length) {
/*  628 */         outBuf = this._textBuffer.finishCurrentSegment();
/*  629 */         outPtr = 0;
/*      */       }
/*  631 */       outBuf[(outPtr++)] = (char)c;
/*      */ 
/*  633 */       if (this._inputPtr >= this._inputEnd) {
/*  634 */         loadMoreGuaranteed();
/*      */       }
/*  636 */       c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */ 
/*  638 */       if ((c == 45) || (c == 43)) {
/*  639 */         if (outPtr >= outBuf.length) {
/*  640 */           outBuf = this._textBuffer.finishCurrentSegment();
/*  641 */           outPtr = 0;
/*      */         }
/*  643 */         outBuf[(outPtr++)] = (char)c;
/*      */ 
/*  645 */         if (this._inputPtr >= this._inputEnd) {
/*  646 */           loadMoreGuaranteed();
/*      */         }
/*  648 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       }
/*      */ 
/*  652 */       while ((c <= 57) && (c >= 48)) {
/*  653 */         expLen++;
/*  654 */         if (outPtr >= outBuf.length) {
/*  655 */           outBuf = this._textBuffer.finishCurrentSegment();
/*  656 */           outPtr = 0;
/*      */         }
/*  658 */         outBuf[(outPtr++)] = (char)c;
/*  659 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*  660 */           eof = true;
/*  661 */           break;
/*      */         }
/*  663 */         c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */       }
/*      */ 
/*  666 */       if (expLen == 0) {
/*  667 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  672 */     if (!eof) {
/*  673 */       this._inputPtr -= 1;
/*      */     }
/*  675 */     this._textBuffer.setCurrentLength(outPtr);
/*      */ 
/*  678 */     return resetFloat(negative, integerPartLength, fractLen, expLen);
/*      */   }
/*      */ 
/*      */   protected final Name _parseFieldName(int i)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  690 */     if (i != 34) {
/*  691 */       return _handleUnusualFieldName(i);
/*      */     }
/*      */ 
/*  694 */     if (this._inputPtr + 9 > this._inputEnd) {
/*  695 */       return slowParseFieldName();
/*      */     }
/*      */ 
/*  704 */     byte[] input = this._inputBuffer;
/*  705 */     int[] codes = sInputCodesLatin1;
/*      */ 
/*  707 */     int q = input[(this._inputPtr++)] & 0xFF;
/*      */ 
/*  709 */     if (codes[q] == 0) {
/*  710 */       i = input[(this._inputPtr++)] & 0xFF;
/*  711 */       if (codes[i] == 0) {
/*  712 */         q = q << 8 | i;
/*  713 */         i = input[(this._inputPtr++)] & 0xFF;
/*  714 */         if (codes[i] == 0) {
/*  715 */           q = q << 8 | i;
/*  716 */           i = input[(this._inputPtr++)] & 0xFF;
/*  717 */           if (codes[i] == 0) {
/*  718 */             q = q << 8 | i;
/*  719 */             i = input[(this._inputPtr++)] & 0xFF;
/*  720 */             if (codes[i] == 0) {
/*  721 */               this._quad1 = q;
/*  722 */               return parseMediumFieldName(i, codes);
/*      */             }
/*  724 */             if (i == 34) {
/*  725 */               return findName(q, 4);
/*      */             }
/*  727 */             return parseFieldName(q, i, 4);
/*      */           }
/*  729 */           if (i == 34) {
/*  730 */             return findName(q, 3);
/*      */           }
/*  732 */           return parseFieldName(q, i, 3);
/*      */         }
/*  734 */         if (i == 34) {
/*  735 */           return findName(q, 2);
/*      */         }
/*  737 */         return parseFieldName(q, i, 2);
/*      */       }
/*  739 */       if (i == 34) {
/*  740 */         return findName(q, 1);
/*      */       }
/*  742 */       return parseFieldName(q, i, 1);
/*      */     }
/*  744 */     if (q == 34) {
/*  745 */       return BytesToNameCanonicalizer.getEmptyName();
/*      */     }
/*  747 */     return parseFieldName(0, q, 0);
/*      */   }
/*      */ 
/*      */   protected final Name parseMediumFieldName(int q2, int[] codes)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  754 */     int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  755 */     if (codes[i] != 0) {
/*  756 */       if (i == 34) {
/*  757 */         return findName(this._quad1, q2, 1);
/*      */       }
/*  759 */       return parseFieldName(this._quad1, q2, i, 1);
/*      */     }
/*  761 */     q2 = q2 << 8 | i;
/*  762 */     i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  763 */     if (codes[i] != 0) {
/*  764 */       if (i == 34) {
/*  765 */         return findName(this._quad1, q2, 2);
/*      */       }
/*  767 */       return parseFieldName(this._quad1, q2, i, 2);
/*      */     }
/*  769 */     q2 = q2 << 8 | i;
/*  770 */     i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  771 */     if (codes[i] != 0) {
/*  772 */       if (i == 34) {
/*  773 */         return findName(this._quad1, q2, 3);
/*      */       }
/*  775 */       return parseFieldName(this._quad1, q2, i, 3);
/*      */     }
/*  777 */     q2 = q2 << 8 | i;
/*  778 */     i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  779 */     if (codes[i] != 0) {
/*  780 */       if (i == 34) {
/*  781 */         return findName(this._quad1, q2, 4);
/*      */       }
/*  783 */       return parseFieldName(this._quad1, q2, i, 4);
/*      */     }
/*  785 */     this._quadBuffer[0] = this._quad1;
/*  786 */     this._quadBuffer[1] = q2;
/*  787 */     return parseLongFieldName(i);
/*      */   }
/*      */ 
/*      */   protected Name parseLongFieldName(int q)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  794 */     int[] codes = sInputCodesLatin1;
/*  795 */     int qlen = 2;
/*      */     while (true)
/*      */     {
/*  802 */       if (this._inputEnd - this._inputPtr < 4) {
/*  803 */         return parseEscapedFieldName(this._quadBuffer, qlen, 0, q, 0);
/*      */       }
/*      */ 
/*  807 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  808 */       if (codes[i] != 0) {
/*  809 */         if (i == 34) {
/*  810 */           return findName(this._quadBuffer, qlen, q, 1);
/*      */         }
/*  812 */         return parseEscapedFieldName(this._quadBuffer, qlen, q, i, 1);
/*      */       }
/*      */ 
/*  815 */       q = q << 8 | i;
/*  816 */       i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  817 */       if (codes[i] != 0) {
/*  818 */         if (i == 34) {
/*  819 */           return findName(this._quadBuffer, qlen, q, 2);
/*      */         }
/*  821 */         return parseEscapedFieldName(this._quadBuffer, qlen, q, i, 2);
/*      */       }
/*      */ 
/*  824 */       q = q << 8 | i;
/*  825 */       i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  826 */       if (codes[i] != 0) {
/*  827 */         if (i == 34) {
/*  828 */           return findName(this._quadBuffer, qlen, q, 3);
/*      */         }
/*  830 */         return parseEscapedFieldName(this._quadBuffer, qlen, q, i, 3);
/*      */       }
/*      */ 
/*  833 */       q = q << 8 | i;
/*  834 */       i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  835 */       if (codes[i] != 0) {
/*  836 */         if (i == 34) {
/*  837 */           return findName(this._quadBuffer, qlen, q, 4);
/*      */         }
/*  839 */         return parseEscapedFieldName(this._quadBuffer, qlen, q, i, 4);
/*      */       }
/*      */ 
/*  843 */       if (qlen >= this._quadBuffer.length) {
/*  844 */         this._quadBuffer = growArrayBy(this._quadBuffer, qlen);
/*      */       }
/*  846 */       this._quadBuffer[(qlen++)] = q;
/*  847 */       q = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Name slowParseFieldName()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  859 */     if ((this._inputPtr >= this._inputEnd) && 
/*  860 */       (!loadMore())) {
/*  861 */       _reportInvalidEOF(": was expecting closing '\"' for name");
/*      */     }
/*      */ 
/*  864 */     int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*  865 */     if (i == 34) {
/*  866 */       return BytesToNameCanonicalizer.getEmptyName();
/*      */     }
/*  868 */     return parseEscapedFieldName(this._quadBuffer, 0, 0, i, 0);
/*      */   }
/*      */ 
/*      */   private final Name parseFieldName(int q1, int ch, int lastQuadBytes)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  874 */     return parseEscapedFieldName(this._quadBuffer, 0, q1, ch, lastQuadBytes);
/*      */   }
/*      */ 
/*      */   private final Name parseFieldName(int q1, int q2, int ch, int lastQuadBytes)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  880 */     this._quadBuffer[0] = q1;
/*  881 */     return parseEscapedFieldName(this._quadBuffer, 1, q2, ch, lastQuadBytes);
/*      */   }
/*      */ 
/*      */   protected Name parseEscapedFieldName(int[] quads, int qlen, int currQuad, int ch, int currQuadBytes)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  900 */     int[] codes = sInputCodesLatin1;
/*      */     while (true)
/*      */     {
/*  903 */       if (codes[ch] != 0) {
/*  904 */         if (ch == 34)
/*      */         {
/*      */           break;
/*      */         }
/*  908 */         if (ch != 92)
/*      */         {
/*  910 */           _throwUnquotedSpace(ch, "name");
/*      */         }
/*      */         else {
/*  913 */           ch = _decodeEscaped();
/*      */         }
/*      */ 
/*  920 */         if (ch > 127)
/*      */         {
/*  922 */           if (currQuadBytes >= 4) {
/*  923 */             if (qlen >= quads.length) {
/*  924 */               this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */             }
/*  926 */             quads[(qlen++)] = currQuad;
/*  927 */             currQuad = 0;
/*  928 */             currQuadBytes = 0;
/*      */           }
/*  930 */           if (ch < 2048) {
/*  931 */             currQuad = currQuad << 8 | (0xC0 | ch >> 6);
/*  932 */             currQuadBytes++;
/*      */           }
/*      */           else {
/*  935 */             currQuad = currQuad << 8 | (0xE0 | ch >> 12);
/*  936 */             currQuadBytes++;
/*      */ 
/*  938 */             if (currQuadBytes >= 4) {
/*  939 */               if (qlen >= quads.length) {
/*  940 */                 this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */               }
/*  942 */               quads[(qlen++)] = currQuad;
/*  943 */               currQuad = 0;
/*  944 */               currQuadBytes = 0;
/*      */             }
/*  946 */             currQuad = currQuad << 8 | (0x80 | ch >> 6 & 0x3F);
/*  947 */             currQuadBytes++;
/*      */           }
/*      */ 
/*  950 */           ch = 0x80 | ch & 0x3F;
/*      */         }
/*      */       }
/*      */ 
/*  954 */       if (currQuadBytes < 4) {
/*  955 */         currQuadBytes++;
/*  956 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/*  958 */         if (qlen >= quads.length) {
/*  959 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/*  961 */         quads[(qlen++)] = currQuad;
/*  962 */         currQuad = ch;
/*  963 */         currQuadBytes = 1;
/*      */       }
/*  965 */       if ((this._inputPtr >= this._inputEnd) && 
/*  966 */         (!loadMore())) {
/*  967 */         _reportInvalidEOF(" in field name");
/*      */       }
/*      */ 
/*  970 */       ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     }
/*      */ 
/*  973 */     if (currQuadBytes > 0) {
/*  974 */       if (qlen >= quads.length) {
/*  975 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/*  977 */       quads[(qlen++)] = currQuad;
/*      */     }
/*  979 */     Name name = this._symbols.findName(quads, qlen);
/*  980 */     if (name == null) {
/*  981 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/*  983 */     return name;
/*      */   }
/*      */ 
/*      */   protected final Name _handleUnusualFieldName(int ch)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  996 */     if ((ch == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/*  997 */       return _parseApostropheFieldName();
/*      */     }
/*      */ 
/* 1000 */     if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
/* 1001 */       _reportUnexpectedChar(ch, "was expecting double-quote to start field name");
/*      */     }
/*      */ 
/* 1007 */     int[] codes = CharTypes.getInputCodeUtf8JsNames();
/*      */ 
/* 1009 */     if (codes[ch] != 0) {
/* 1010 */       _reportUnexpectedChar(ch, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/*      */ 
/* 1017 */     int[] quads = this._quadBuffer;
/* 1018 */     int qlen = 0;
/* 1019 */     int currQuad = 0;
/* 1020 */     int currQuadBytes = 0;
/*      */     while (true)
/*      */     {
/* 1024 */       if (currQuadBytes < 4) {
/* 1025 */         currQuadBytes++;
/* 1026 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 1028 */         if (qlen >= quads.length) {
/* 1029 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 1031 */         quads[(qlen++)] = currQuad;
/* 1032 */         currQuad = ch;
/* 1033 */         currQuadBytes = 1;
/*      */       }
/* 1035 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1036 */         (!loadMore())) {
/* 1037 */         _reportInvalidEOF(" in field name");
/*      */       }
/*      */ 
/* 1040 */       ch = this._inputBuffer[this._inputPtr] & 0xFF;
/* 1041 */       if (codes[ch] != 0) {
/*      */         break;
/*      */       }
/* 1044 */       this._inputPtr += 1;
/*      */     }
/*      */ 
/* 1047 */     if (currQuadBytes > 0) {
/* 1048 */       if (qlen >= quads.length) {
/* 1049 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 1051 */       quads[(qlen++)] = currQuad;
/*      */     }
/* 1053 */     Name name = this._symbols.findName(quads, qlen);
/* 1054 */     if (name == null) {
/* 1055 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 1057 */     return name;
/*      */   }
/*      */ 
/*      */   protected final Name _parseApostropheFieldName()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1068 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1069 */       (!loadMore())) {
/* 1070 */       _reportInvalidEOF(": was expecting closing ''' for name");
/*      */     }
/*      */ 
/* 1073 */     int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1074 */     if (ch == 39) {
/* 1075 */       return BytesToNameCanonicalizer.getEmptyName();
/*      */     }
/* 1077 */     int[] quads = this._quadBuffer;
/* 1078 */     int qlen = 0;
/* 1079 */     int currQuad = 0;
/* 1080 */     int currQuadBytes = 0;
/*      */ 
/* 1084 */     int[] codes = sInputCodesLatin1;
/*      */ 
/* 1087 */     while (ch != 39)
/*      */     {
/* 1091 */       if ((ch != 34) && (codes[ch] != 0)) {
/* 1092 */         if (ch != 92)
/*      */         {
/* 1095 */           _throwUnquotedSpace(ch, "name");
/*      */         }
/*      */         else {
/* 1098 */           ch = _decodeEscaped();
/*      */         }
/*      */ 
/* 1105 */         if (ch > 127)
/*      */         {
/* 1107 */           if (currQuadBytes >= 4) {
/* 1108 */             if (qlen >= quads.length) {
/* 1109 */               this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */             }
/* 1111 */             quads[(qlen++)] = currQuad;
/* 1112 */             currQuad = 0;
/* 1113 */             currQuadBytes = 0;
/*      */           }
/* 1115 */           if (ch < 2048) {
/* 1116 */             currQuad = currQuad << 8 | (0xC0 | ch >> 6);
/* 1117 */             currQuadBytes++;
/*      */           }
/*      */           else {
/* 1120 */             currQuad = currQuad << 8 | (0xE0 | ch >> 12);
/* 1121 */             currQuadBytes++;
/*      */ 
/* 1123 */             if (currQuadBytes >= 4) {
/* 1124 */               if (qlen >= quads.length) {
/* 1125 */                 this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */               }
/* 1127 */               quads[(qlen++)] = currQuad;
/* 1128 */               currQuad = 0;
/* 1129 */               currQuadBytes = 0;
/*      */             }
/* 1131 */             currQuad = currQuad << 8 | (0x80 | ch >> 6 & 0x3F);
/* 1132 */             currQuadBytes++;
/*      */           }
/*      */ 
/* 1135 */           ch = 0x80 | ch & 0x3F;
/*      */         }
/*      */       }
/*      */ 
/* 1139 */       if (currQuadBytes < 4) {
/* 1140 */         currQuadBytes++;
/* 1141 */         currQuad = currQuad << 8 | ch;
/*      */       } else {
/* 1143 */         if (qlen >= quads.length) {
/* 1144 */           this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */         }
/* 1146 */         quads[(qlen++)] = currQuad;
/* 1147 */         currQuad = ch;
/* 1148 */         currQuadBytes = 1;
/*      */       }
/* 1150 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1151 */         (!loadMore())) {
/* 1152 */         _reportInvalidEOF(" in field name");
/*      */       }
/*      */ 
/* 1155 */       ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */     }
/*      */ 
/* 1158 */     if (currQuadBytes > 0) {
/* 1159 */       if (qlen >= quads.length) {
/* 1160 */         this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */       }
/* 1162 */       quads[(qlen++)] = currQuad;
/*      */     }
/* 1164 */     Name name = this._symbols.findName(quads, qlen);
/* 1165 */     if (name == null) {
/* 1166 */       name = addName(quads, qlen, currQuadBytes);
/*      */     }
/* 1168 */     return name;
/*      */   }
/*      */ 
/*      */   private final Name findName(int q1, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 1181 */     Name name = this._symbols.findName(q1);
/* 1182 */     if (name != null) {
/* 1183 */       return name;
/*      */     }
/*      */ 
/* 1186 */     this._quadBuffer[0] = q1;
/* 1187 */     return addName(this._quadBuffer, 1, lastQuadBytes);
/*      */   }
/*      */ 
/*      */   private final Name findName(int q1, int q2, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 1194 */     Name name = this._symbols.findName(q1, q2);
/* 1195 */     if (name != null) {
/* 1196 */       return name;
/*      */     }
/*      */ 
/* 1199 */     this._quadBuffer[0] = q1;
/* 1200 */     this._quadBuffer[1] = q2;
/* 1201 */     return addName(this._quadBuffer, 2, lastQuadBytes);
/*      */   }
/*      */ 
/*      */   private final Name findName(int[] quads, int qlen, int lastQuad, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 1207 */     if (qlen >= quads.length) {
/* 1208 */       this._quadBuffer = (quads = growArrayBy(quads, quads.length));
/*      */     }
/* 1210 */     quads[(qlen++)] = lastQuad;
/* 1211 */     Name name = this._symbols.findName(quads, qlen);
/* 1212 */     if (name == null) {
/* 1213 */       return addName(quads, qlen, lastQuadBytes);
/*      */     }
/* 1215 */     return name;
/*      */   }
/*      */ 
/*      */   private final Name addName(int[] quads, int qlen, int lastQuadBytes)
/*      */     throws JsonParseException
/*      */   {
/* 1232 */     int byteLen = (qlen << 2) - 4 + lastQuadBytes;
/*      */     int lastQuad;
/* 1241 */     if (lastQuadBytes < 4) {
/* 1242 */       int lastQuad = quads[(qlen - 1)];
/*      */ 
/* 1244 */       quads[(qlen - 1)] = (lastQuad << (4 - lastQuadBytes << 3));
/*      */     } else {
/* 1246 */       lastQuad = 0;
/*      */     }
/*      */ 
/* 1250 */     char[] cbuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1251 */     int cix = 0;
/*      */ 
/* 1253 */     for (int ix = 0; ix < byteLen; ) {
/* 1254 */       int ch = quads[(ix >> 2)];
/* 1255 */       int byteIx = ix & 0x3;
/* 1256 */       ch = ch >> (3 - byteIx << 3) & 0xFF;
/* 1257 */       ix++;
/*      */ 
/* 1259 */       if (ch > 127)
/*      */       {
/*      */         int needed;
/*      */         int needed;
/* 1261 */         if ((ch & 0xE0) == 192) {
/* 1262 */           ch &= 31;
/* 1263 */           needed = 1;
/*      */         }
/*      */         else
/*      */         {
/*      */           int needed;
/* 1264 */           if ((ch & 0xF0) == 224) {
/* 1265 */             ch &= 15;
/* 1266 */             needed = 2;
/*      */           }
/*      */           else
/*      */           {
/*      */             int needed;
/* 1267 */             if ((ch & 0xF8) == 240) {
/* 1268 */               ch &= 7;
/* 1269 */               needed = 3;
/*      */             } else {
/* 1271 */               _reportInvalidInitial(ch);
/* 1272 */               needed = ch = 1;
/*      */             }
/*      */           }
/*      */         }
/* 1274 */         if (ix + needed > byteLen) {
/* 1275 */           _reportInvalidEOF(" in field name");
/*      */         }
/*      */ 
/* 1279 */         int ch2 = quads[(ix >> 2)];
/* 1280 */         byteIx = ix & 0x3;
/* 1281 */         ch2 >>= 3 - byteIx << 3;
/* 1282 */         ix++;
/*      */ 
/* 1284 */         if ((ch2 & 0xC0) != 128) {
/* 1285 */           _reportInvalidOther(ch2);
/*      */         }
/* 1287 */         ch = ch << 6 | ch2 & 0x3F;
/* 1288 */         if (needed > 1) {
/* 1289 */           ch2 = quads[(ix >> 2)];
/* 1290 */           byteIx = ix & 0x3;
/* 1291 */           ch2 >>= 3 - byteIx << 3;
/* 1292 */           ix++;
/*      */ 
/* 1294 */           if ((ch2 & 0xC0) != 128) {
/* 1295 */             _reportInvalidOther(ch2);
/*      */           }
/* 1297 */           ch = ch << 6 | ch2 & 0x3F;
/* 1298 */           if (needed > 2) {
/* 1299 */             ch2 = quads[(ix >> 2)];
/* 1300 */             byteIx = ix & 0x3;
/* 1301 */             ch2 >>= 3 - byteIx << 3;
/* 1302 */             ix++;
/* 1303 */             if ((ch2 & 0xC0) != 128) {
/* 1304 */               _reportInvalidOther(ch2 & 0xFF);
/*      */             }
/* 1306 */             ch = ch << 6 | ch2 & 0x3F;
/*      */           }
/*      */         }
/* 1309 */         if (needed > 2) {
/* 1310 */           ch -= 65536;
/* 1311 */           if (cix >= cbuf.length) {
/* 1312 */             cbuf = this._textBuffer.expandCurrentSegment();
/*      */           }
/* 1314 */           cbuf[(cix++)] = (char)(55296 + (ch >> 10));
/* 1315 */           ch = 0xDC00 | ch & 0x3FF;
/*      */         }
/*      */       }
/* 1318 */       if (cix >= cbuf.length) {
/* 1319 */         cbuf = this._textBuffer.expandCurrentSegment();
/*      */       }
/* 1321 */       cbuf[(cix++)] = (char)ch;
/*      */     }
/*      */ 
/* 1328 */     String baseName = new String(cbuf, 0, cix);
/*      */ 
/* 1330 */     if (lastQuadBytes < 4) {
/* 1331 */       quads[(qlen - 1)] = lastQuad;
/*      */     }
/* 1333 */     return this._symbols.addName(baseName, quads, qlen);
/*      */   }
/*      */ 
/*      */   protected void _finishString()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1347 */     int ptr = this._inputPtr;
/* 1348 */     if (ptr >= this._inputEnd) {
/* 1349 */       loadMoreGuaranteed();
/* 1350 */       ptr = this._inputPtr;
/*      */     }
/* 1352 */     int outPtr = 0;
/* 1353 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1354 */     int[] codes = sInputCodesUtf8;
/*      */ 
/* 1356 */     int max = Math.min(this._inputEnd, ptr + outBuf.length);
/* 1357 */     byte[] inputBuffer = this._inputBuffer;
/* 1358 */     while (ptr < max) {
/* 1359 */       int c = inputBuffer[ptr] & 0xFF;
/* 1360 */       if (codes[c] != 0) {
/* 1361 */         if (c != 34) break;
/* 1362 */         this._inputPtr = (ptr + 1);
/* 1363 */         this._textBuffer.setCurrentLength(outPtr);
/* 1364 */         return;
/*      */       }
/*      */ 
/* 1368 */       ptr++;
/* 1369 */       outBuf[(outPtr++)] = (char)c;
/*      */     }
/* 1371 */     this._inputPtr = ptr;
/* 1372 */     _finishString2(outBuf, outPtr);
/*      */   }
/*      */ 
/*      */   private final void _finishString2(char[] outBuf, int outPtr)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1381 */     int[] codes = sInputCodesUtf8;
/* 1382 */     byte[] inputBuffer = this._inputBuffer;
/*      */     while (true)
/*      */     {
/* 1389 */       int ptr = this._inputPtr;
/* 1390 */       if (ptr >= this._inputEnd) {
/* 1391 */         loadMoreGuaranteed();
/* 1392 */         ptr = this._inputPtr;
/*      */       }
/* 1394 */       if (outPtr >= outBuf.length) {
/* 1395 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1396 */         outPtr = 0;
/*      */       }
/* 1398 */       int max = Math.min(this._inputEnd, ptr + (outBuf.length - outPtr));
/*      */       while (true) if (ptr < max) {
/* 1400 */           int c = inputBuffer[(ptr++)] & 0xFF;
/* 1401 */           if (codes[c] != 0) {
/* 1402 */             this._inputPtr = ptr;
/*      */           }
/*      */           else {
/* 1405 */             outBuf[(outPtr++)] = (char)c; continue;
/*      */           }
/*      */         } else {
/* 1407 */           this._inputPtr = ptr;
/* 1408 */           break;
/*      */         }
/*      */       int c;
/* 1410 */       if (c == 34)
/*      */       {
/*      */         break;
/*      */       }
/* 1414 */       switch (codes[c]) {
/*      */       case 1:
/* 1416 */         c = _decodeEscaped();
/* 1417 */         break;
/*      */       case 2:
/* 1419 */         c = _decodeUtf8_2(c);
/* 1420 */         break;
/*      */       case 3:
/* 1422 */         if (this._inputEnd - this._inputPtr >= 2)
/* 1423 */           c = _decodeUtf8_3fast(c);
/*      */         else {
/* 1425 */           c = _decodeUtf8_3(c);
/*      */         }
/* 1427 */         break;
/*      */       case 4:
/* 1429 */         c = _decodeUtf8_4(c);
/*      */ 
/* 1431 */         outBuf[(outPtr++)] = (char)(0xD800 | c >> 10);
/* 1432 */         if (outPtr >= outBuf.length) {
/* 1433 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1434 */           outPtr = 0;
/*      */         }
/* 1436 */         c = 0xDC00 | c & 0x3FF;
/*      */ 
/* 1438 */         break;
/*      */       default:
/* 1440 */         if (c < 32)
/*      */         {
/* 1442 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         else {
/* 1445 */           _reportInvalidChar(c);
/*      */         }
/*      */       }
/*      */ 
/* 1449 */       if (outPtr >= outBuf.length) {
/* 1450 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1451 */         outPtr = 0;
/*      */       }
/*      */ 
/* 1454 */       outBuf[(outPtr++)] = (char)c;
/*      */     }
/* 1456 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */ 
/*      */   protected void _skipString()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1467 */     this._tokenIncomplete = false;
/*      */ 
/* 1470 */     int[] codes = sInputCodesUtf8;
/* 1471 */     byte[] inputBuffer = this._inputBuffer;
/*      */     while (true)
/*      */     {
/* 1479 */       int ptr = this._inputPtr;
/* 1480 */       int max = this._inputEnd;
/* 1481 */       if (ptr >= max) {
/* 1482 */         loadMoreGuaranteed();
/* 1483 */         ptr = this._inputPtr;
/* 1484 */         max = this._inputEnd;
/*      */       }
/* 1486 */       while (ptr < max) {
/* 1487 */         int c = inputBuffer[(ptr++)] & 0xFF;
/* 1488 */         if (codes[c] != 0) {
/* 1489 */           this._inputPtr = ptr;
/* 1490 */           break label92;
/*      */         }
/*      */       }
/* 1493 */       this._inputPtr = ptr;
/* 1494 */       continue;
/*      */       label92: int c;
/* 1496 */       if (c == 34)
/*      */       {
/*      */         break;
/*      */       }
/* 1500 */       switch (codes[c]) {
/*      */       case 1:
/* 1502 */         _decodeEscaped();
/* 1503 */         break;
/*      */       case 2:
/* 1505 */         _skipUtf8_2(c);
/* 1506 */         break;
/*      */       case 3:
/* 1508 */         _skipUtf8_3(c);
/* 1509 */         break;
/*      */       case 4:
/* 1511 */         _skipUtf8_4(c);
/* 1512 */         break;
/*      */       default:
/* 1514 */         if (c < 32)
/*      */         {
/* 1516 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */         else
/* 1519 */           _reportInvalidChar(c);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final JsonToken _handleUnexpectedValue(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1535 */     if ((c != 39) || (!isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/* 1536 */       _reportUnexpectedChar(c, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
/*      */     }
/*      */ 
/* 1540 */     int outPtr = 0;
/* 1541 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*      */ 
/* 1544 */     int[] codes = sInputCodesUtf8;
/* 1545 */     byte[] inputBuffer = this._inputBuffer;
/*      */     while (true)
/*      */     {
/* 1552 */       if (this._inputPtr >= this._inputEnd) {
/* 1553 */         loadMoreGuaranteed();
/*      */       }
/* 1555 */       if (outPtr >= outBuf.length) {
/* 1556 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1557 */         outPtr = 0;
/*      */       }
/* 1559 */       int max = this._inputEnd;
/*      */ 
/* 1561 */       int max2 = this._inputPtr + (outBuf.length - outPtr);
/* 1562 */       if (max2 < max) {
/* 1563 */         max = max2;
/*      */       }
/*      */       while (true) {
/* 1566 */         if (this._inputPtr < max) {
/* 1567 */           c = inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1568 */           if ((c != 39) && (codes[c] == 0))
/*      */           {
/* 1571 */             outBuf[(outPtr++)] = (char)c; continue;
/*      */           }
/*      */         } else {
/* 1573 */           break;
/*      */         }
/*      */       }
/* 1576 */       if (c == 39)
/*      */       {
/*      */         break;
/*      */       }
/* 1580 */       switch (codes[c]) {
/*      */       case 1:
/* 1582 */         if (c == 34) break;
/* 1583 */         c = _decodeEscaped(); break;
/*      */       case 2:
/* 1587 */         c = _decodeUtf8_2(c);
/* 1588 */         break;
/*      */       case 3:
/* 1590 */         if (this._inputEnd - this._inputPtr >= 2)
/* 1591 */           c = _decodeUtf8_3fast(c);
/*      */         else {
/* 1593 */           c = _decodeUtf8_3(c);
/*      */         }
/* 1595 */         break;
/*      */       case 4:
/* 1597 */         c = _decodeUtf8_4(c);
/*      */ 
/* 1599 */         outBuf[(outPtr++)] = (char)(0xD800 | c >> 10);
/* 1600 */         if (outPtr >= outBuf.length) {
/* 1601 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1602 */           outPtr = 0;
/*      */         }
/* 1604 */         c = 0xDC00 | c & 0x3FF;
/*      */ 
/* 1606 */         break;
/*      */       default:
/* 1608 */         if (c < 32) {
/* 1609 */           _throwUnquotedSpace(c, "string value");
/*      */         }
/*      */ 
/* 1612 */         _reportInvalidChar(c);
/*      */       }
/*      */ 
/* 1615 */       if (outPtr >= outBuf.length) {
/* 1616 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1617 */         outPtr = 0;
/*      */       }
/*      */ 
/* 1620 */       outBuf[(outPtr++)] = (char)c;
/*      */     }
/* 1622 */     this._textBuffer.setCurrentLength(outPtr);
/*      */ 
/* 1624 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */ 
/*      */   protected void _matchToken(JsonToken token)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1637 */     byte[] matchBytes = token.asByteArray();
/* 1638 */     int i = 1;
/*      */ 
/* 1640 */     for (int len = matchBytes.length; i < len; i++) {
/* 1641 */       if (this._inputPtr >= this._inputEnd) {
/* 1642 */         loadMoreGuaranteed();
/*      */       }
/* 1644 */       if (matchBytes[i] != this._inputBuffer[this._inputPtr]) {
/* 1645 */         _reportInvalidToken(token.asString().substring(0, i));
/*      */       }
/* 1647 */       this._inputPtr += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _reportInvalidToken(String matchedPart)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1659 */     StringBuilder sb = new StringBuilder(matchedPart);
/*      */ 
/* 1665 */     while ((this._inputPtr < this._inputEnd) || (loadMore()))
/*      */     {
/* 1668 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1669 */       char c = (char)_decodeCharForError(i);
/* 1670 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 1673 */       this._inputPtr += 1;
/* 1674 */       sb.append(c);
/*      */     }
/*      */ 
/* 1677 */     _reportError("Unrecognized token '" + sb.toString() + "': was expecting 'null', 'true' or 'false'");
/*      */   }
/*      */ 
/*      */   private final int _skipWS()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1689 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1690 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1691 */       if (i > 32) {
/* 1692 */         if (i != 47) {
/* 1693 */           return i;
/*      */         }
/* 1695 */         _skipComment();
/* 1696 */       } else if (i != 32) {
/* 1697 */         if (i == 10)
/* 1698 */           _skipLF();
/* 1699 */         else if (i == 13)
/* 1700 */           _skipCR();
/* 1701 */         else if (i != 9) {
/* 1702 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/* 1706 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
/*      */   }
/*      */ 
/*      */   private final int _skipWSOrEnd()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1712 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1713 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1714 */       if (i > 32) {
/* 1715 */         if (i != 47) {
/* 1716 */           return i;
/*      */         }
/* 1718 */         _skipComment();
/* 1719 */       } else if (i != 32) {
/* 1720 */         if (i == 10)
/* 1721 */           _skipLF();
/* 1722 */         else if (i == 13)
/* 1723 */           _skipCR();
/* 1724 */         else if (i != 9) {
/* 1725 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1730 */     _handleEOF();
/* 1731 */     return -1;
/*      */   }
/*      */ 
/*      */   private final void _skipComment()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1737 */     if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
/* 1738 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */ 
/* 1741 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 1742 */       _reportInvalidEOF(" in a comment");
/*      */     }
/* 1744 */     int c = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1745 */     if (c == 47)
/* 1746 */       _skipCppComment();
/* 1747 */     else if (c == 42)
/* 1748 */       _skipCComment();
/*      */     else
/* 1750 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */   }
/*      */ 
/*      */   private final void _skipCComment()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1758 */     int[] codes = CharTypes.getInputCodeComment();
/*      */ 
/* 1761 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1762 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1763 */       int code = codes[i];
/* 1764 */       if (code != 0) {
/* 1765 */         switch (code) {
/*      */         case 42:
/* 1767 */           if (this._inputBuffer[this._inputPtr] != 47) break;
/* 1768 */           this._inputPtr += 1;
/* 1769 */           return;
/*      */         case 10:
/* 1773 */           _skipLF();
/* 1774 */           break;
/*      */         case 13:
/* 1776 */           _skipCR();
/* 1777 */           break;
/*      */         default:
/* 1780 */           _reportInvalidChar(i);
/*      */         }
/*      */       }
/*      */     }
/* 1784 */     _reportInvalidEOF(" in a comment");
/*      */   }
/*      */ 
/*      */   private final void _skipCppComment()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1791 */     int[] codes = CharTypes.getInputCodeComment();
/* 1792 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1793 */       int i = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 1794 */       int code = codes[i];
/* 1795 */       if (code != 0)
/* 1796 */         switch (code) {
/*      */         case 10:
/* 1798 */           _skipLF();
/* 1799 */           return;
/*      */         case 13:
/* 1801 */           _skipCR();
/* 1802 */           return;
/*      */         case 42:
/* 1804 */           break;
/*      */         default:
/* 1807 */           _reportInvalidChar(i);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final char _decodeEscaped()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1816 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1817 */       (!loadMore())) {
/* 1818 */       _reportInvalidEOF(" in character escape sequence");
/*      */     }
/*      */ 
/* 1821 */     int c = this._inputBuffer[(this._inputPtr++)];
/*      */ 
/* 1823 */     switch (c)
/*      */     {
/*      */     case 98:
/* 1826 */       return '\b';
/*      */     case 116:
/* 1828 */       return '\t';
/*      */     case 110:
/* 1830 */       return '\n';
/*      */     case 102:
/* 1832 */       return '\f';
/*      */     case 114:
/* 1834 */       return '\r';
/*      */     case 34:
/*      */     case 47:
/*      */     case 92:
/* 1840 */       return (char)c;
/*      */     case 117:
/* 1843 */       break;
/*      */     default:
/* 1846 */       return _handleUnrecognizedCharacterEscape((char)_decodeCharForError(c));
/*      */     }
/*      */ 
/* 1850 */     int value = 0;
/* 1851 */     for (int i = 0; i < 4; i++) {
/* 1852 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1853 */         (!loadMore())) {
/* 1854 */         _reportInvalidEOF(" in character escape sequence");
/*      */       }
/*      */ 
/* 1857 */       int ch = this._inputBuffer[(this._inputPtr++)];
/* 1858 */       int digit = CharTypes.charToHex(ch);
/* 1859 */       if (digit < 0) {
/* 1860 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 1862 */       value = value << 4 | digit;
/*      */     }
/* 1864 */     return (char)value;
/*      */   }
/*      */ 
/*      */   protected int _decodeCharForError(int firstByte)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1870 */     int c = firstByte;
/* 1871 */     if (c < 0)
/*      */     {
/*      */       int needed;
/*      */       int needed;
/* 1875 */       if ((c & 0xE0) == 192) {
/* 1876 */         c &= 31;
/* 1877 */         needed = 1;
/*      */       }
/*      */       else
/*      */       {
/*      */         int needed;
/* 1878 */         if ((c & 0xF0) == 224) {
/* 1879 */           c &= 15;
/* 1880 */           needed = 2;
/*      */         }
/*      */         else
/*      */         {
/*      */           int needed;
/* 1881 */           if ((c & 0xF8) == 240)
/*      */           {
/* 1883 */             c &= 7;
/* 1884 */             needed = 3;
/*      */           } else {
/* 1886 */             _reportInvalidInitial(c & 0xFF);
/* 1887 */             needed = 1;
/*      */           }
/*      */         }
/*      */       }
/* 1890 */       int d = nextByte();
/* 1891 */       if ((d & 0xC0) != 128) {
/* 1892 */         _reportInvalidOther(d & 0xFF);
/*      */       }
/* 1894 */       c = c << 6 | d & 0x3F;
/*      */ 
/* 1896 */       if (needed > 1) {
/* 1897 */         d = nextByte();
/* 1898 */         if ((d & 0xC0) != 128) {
/* 1899 */           _reportInvalidOther(d & 0xFF);
/*      */         }
/* 1901 */         c = c << 6 | d & 0x3F;
/* 1902 */         if (needed > 2) {
/* 1903 */           d = nextByte();
/* 1904 */           if ((d & 0xC0) != 128) {
/* 1905 */             _reportInvalidOther(d & 0xFF);
/*      */           }
/* 1907 */           c = c << 6 | d & 0x3F;
/*      */         }
/*      */       }
/*      */     }
/* 1911 */     return c;
/*      */   }
/*      */ 
/*      */   private final int _decodeUtf8_2(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1923 */     if (this._inputPtr >= this._inputEnd) {
/* 1924 */       loadMoreGuaranteed();
/*      */     }
/* 1926 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 1927 */     if ((d & 0xC0) != 128) {
/* 1928 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1930 */     return (c & 0x1F) << 6 | d & 0x3F;
/*      */   }
/*      */ 
/*      */   private final int _decodeUtf8_3(int c1)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1936 */     if (this._inputPtr >= this._inputEnd) {
/* 1937 */       loadMoreGuaranteed();
/*      */     }
/* 1939 */     c1 &= 15;
/* 1940 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 1941 */     if ((d & 0xC0) != 128) {
/* 1942 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1944 */     int c = c1 << 6 | d & 0x3F;
/* 1945 */     if (this._inputPtr >= this._inputEnd) {
/* 1946 */       loadMoreGuaranteed();
/*      */     }
/* 1948 */     d = this._inputBuffer[(this._inputPtr++)];
/* 1949 */     if ((d & 0xC0) != 128) {
/* 1950 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1952 */     c = c << 6 | d & 0x3F;
/* 1953 */     return c;
/*      */   }
/*      */ 
/*      */   private final int _decodeUtf8_3fast(int c1)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1959 */     c1 &= 15;
/* 1960 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 1961 */     if ((d & 0xC0) != 128) {
/* 1962 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1964 */     int c = c1 << 6 | d & 0x3F;
/* 1965 */     d = this._inputBuffer[(this._inputPtr++)];
/* 1966 */     if ((d & 0xC0) != 128) {
/* 1967 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1969 */     c = c << 6 | d & 0x3F;
/* 1970 */     return c;
/*      */   }
/*      */ 
/*      */   private final int _decodeUtf8_4(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1980 */     if (this._inputPtr >= this._inputEnd) {
/* 1981 */       loadMoreGuaranteed();
/*      */     }
/* 1983 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 1984 */     if ((d & 0xC0) != 128) {
/* 1985 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1987 */     c = (c & 0x7) << 6 | d & 0x3F;
/*      */ 
/* 1989 */     if (this._inputPtr >= this._inputEnd) {
/* 1990 */       loadMoreGuaranteed();
/*      */     }
/* 1992 */     d = this._inputBuffer[(this._inputPtr++)];
/* 1993 */     if ((d & 0xC0) != 128) {
/* 1994 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 1996 */     c = c << 6 | d & 0x3F;
/* 1997 */     if (this._inputPtr >= this._inputEnd) {
/* 1998 */       loadMoreGuaranteed();
/*      */     }
/* 2000 */     d = this._inputBuffer[(this._inputPtr++)];
/* 2001 */     if ((d & 0xC0) != 128) {
/* 2002 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/*      */ 
/* 2008 */     return (c << 6 | d & 0x3F) - 65536;
/*      */   }
/*      */ 
/*      */   private final void _skipUtf8_2(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 2014 */     if (this._inputPtr >= this._inputEnd) {
/* 2015 */       loadMoreGuaranteed();
/*      */     }
/* 2017 */     c = this._inputBuffer[(this._inputPtr++)];
/* 2018 */     if ((c & 0xC0) != 128)
/* 2019 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */   }
/*      */ 
/*      */   private final void _skipUtf8_3(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 2029 */     if (this._inputPtr >= this._inputEnd) {
/* 2030 */       loadMoreGuaranteed();
/*      */     }
/*      */ 
/* 2033 */     c = this._inputBuffer[(this._inputPtr++)];
/* 2034 */     if ((c & 0xC0) != 128) {
/* 2035 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */     }
/* 2037 */     if (this._inputPtr >= this._inputEnd) {
/* 2038 */       loadMoreGuaranteed();
/*      */     }
/* 2040 */     c = this._inputBuffer[(this._inputPtr++)];
/* 2041 */     if ((c & 0xC0) != 128)
/* 2042 */       _reportInvalidOther(c & 0xFF, this._inputPtr);
/*      */   }
/*      */ 
/*      */   private final void _skipUtf8_4(int c)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 2049 */     if (this._inputPtr >= this._inputEnd) {
/* 2050 */       loadMoreGuaranteed();
/*      */     }
/* 2052 */     int d = this._inputBuffer[(this._inputPtr++)];
/* 2053 */     if ((d & 0xC0) != 128) {
/* 2054 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 2056 */     if (this._inputPtr >= this._inputEnd) {
/* 2057 */       loadMoreGuaranteed();
/*      */     }
/* 2059 */     if ((d & 0xC0) != 128) {
/* 2060 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */     }
/* 2062 */     if (this._inputPtr >= this._inputEnd) {
/* 2063 */       loadMoreGuaranteed();
/*      */     }
/* 2065 */     d = this._inputBuffer[(this._inputPtr++)];
/* 2066 */     if ((d & 0xC0) != 128)
/* 2067 */       _reportInvalidOther(d & 0xFF, this._inputPtr);
/*      */   }
/*      */ 
/*      */   protected final void _skipCR()
/*      */     throws IOException
/*      */   {
/* 2083 */     if (((this._inputPtr < this._inputEnd) || (loadMore())) && 
/* 2084 */       (this._inputBuffer[this._inputPtr] == 10)) {
/* 2085 */       this._inputPtr += 1;
/*      */     }
/*      */ 
/* 2088 */     this._currInputRow += 1;
/* 2089 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */ 
/*      */   protected final void _skipLF() throws IOException
/*      */   {
/* 2094 */     this._currInputRow += 1;
/* 2095 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */ 
/*      */   private int nextByte()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 2101 */     if (this._inputPtr >= this._inputEnd) {
/* 2102 */       loadMoreGuaranteed();
/*      */     }
/* 2104 */     return this._inputBuffer[(this._inputPtr++)] & 0xFF;
/*      */   }
/*      */ 
/*      */   protected void _reportInvalidChar(int c)
/*      */     throws JsonParseException
/*      */   {
/* 2117 */     if (c < 32) {
/* 2118 */       _throwInvalidSpace(c);
/*      */     }
/* 2120 */     _reportInvalidInitial(c);
/*      */   }
/*      */ 
/*      */   protected void _reportInvalidInitial(int mask)
/*      */     throws JsonParseException
/*      */   {
/* 2126 */     _reportError("Invalid UTF-8 start byte 0x" + Integer.toHexString(mask));
/*      */   }
/*      */ 
/*      */   protected void _reportInvalidOther(int mask)
/*      */     throws JsonParseException
/*      */   {
/* 2132 */     _reportError("Invalid UTF-8 middle byte 0x" + Integer.toHexString(mask));
/*      */   }
/*      */ 
/*      */   protected void _reportInvalidOther(int mask, int ptr)
/*      */     throws JsonParseException
/*      */   {
/* 2138 */     this._inputPtr = ptr;
/* 2139 */     _reportInvalidOther(mask);
/*      */   }
/*      */ 
/*      */   public static int[] growArrayBy(int[] arr, int more)
/*      */   {
/* 2144 */     if (arr == null) {
/* 2145 */       return new int[more];
/*      */     }
/* 2147 */     int[] old = arr;
/* 2148 */     int len = arr.length;
/* 2149 */     arr = new int[len + more];
/* 2150 */     System.arraycopy(old, 0, arr, 0, len);
/* 2151 */     return arr;
/*      */   }
/*      */ 
/*      */   protected byte[] _decodeBase64(Base64Variant b64variant)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 2164 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */     while (true)
/*      */     {
/* 2177 */       if (this._inputPtr >= this._inputEnd) {
/* 2178 */         loadMoreGuaranteed();
/*      */       }
/* 2180 */       int ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2181 */       if (ch > 32) {
/* 2182 */         int bits = b64variant.decodeBase64Char(ch);
/* 2183 */         if (bits < 0) {
/* 2184 */           if (ch == 34) {
/* 2185 */             return builder.toByteArray();
/*      */           }
/* 2187 */           throw reportInvalidChar(b64variant, ch, 0);
/*      */         }
/* 2189 */         int decodedData = bits;
/*      */ 
/* 2193 */         if (this._inputPtr >= this._inputEnd) {
/* 2194 */           loadMoreGuaranteed();
/*      */         }
/* 2196 */         ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2197 */         bits = b64variant.decodeBase64Char(ch);
/* 2198 */         if (bits < 0) {
/* 2199 */           throw reportInvalidChar(b64variant, ch, 1);
/*      */         }
/* 2201 */         decodedData = decodedData << 6 | bits;
/*      */ 
/* 2204 */         if (this._inputPtr >= this._inputEnd) {
/* 2205 */           loadMoreGuaranteed();
/*      */         }
/* 2207 */         ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2208 */         bits = b64variant.decodeBase64Char(ch);
/*      */ 
/* 2211 */         if (bits < 0) {
/* 2212 */           if (bits != -2) {
/* 2213 */             throw reportInvalidChar(b64variant, ch, 2);
/*      */           }
/*      */ 
/* 2216 */           if (this._inputPtr >= this._inputEnd) {
/* 2217 */             loadMoreGuaranteed();
/*      */           }
/* 2219 */           ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2220 */           if (!b64variant.usesPaddingChar(ch)) {
/* 2221 */             throw reportInvalidChar(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */           }
/*      */ 
/* 2224 */           decodedData >>= 4;
/* 2225 */           builder.append(decodedData);
/* 2226 */           continue;
/*      */         }
/*      */ 
/* 2229 */         decodedData = decodedData << 6 | bits;
/*      */ 
/* 2231 */         if (this._inputPtr >= this._inputEnd) {
/* 2232 */           loadMoreGuaranteed();
/*      */         }
/* 2234 */         ch = this._inputBuffer[(this._inputPtr++)] & 0xFF;
/* 2235 */         bits = b64variant.decodeBase64Char(ch);
/* 2236 */         if (bits < 0) {
/* 2237 */           if (bits != -2) {
/* 2238 */             throw reportInvalidChar(b64variant, ch, 3);
/*      */           }
/*      */ 
/* 2246 */           decodedData >>= 2;
/* 2247 */           builder.appendTwoBytes(decodedData);
/*      */         }
/*      */         else {
/* 2250 */           decodedData = decodedData << 6 | bits;
/* 2251 */           builder.appendThreeBytes(decodedData);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex) throws IllegalArgumentException
/*      */   {
/* 2259 */     return reportInvalidChar(b64variant, ch, bindex, null);
/*      */   }
/*      */ 
/*      */   protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, int ch, int bindex, String msg)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     String base;
/*      */     String base;
/* 2270 */     if (ch <= 32) {
/* 2271 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
/*      */     }
/*      */     else
/*      */     {
/*      */       String base;
/* 2272 */       if (b64variant.usesPaddingChar(ch)) {
/* 2273 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
/*      */       }
/*      */       else
/*      */       {
/*      */         String base;
/* 2274 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*      */         {
/* 2276 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*      */         }
/* 2278 */         else base = "Illegal character '" + (char)ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content"; 
/*      */       }
/*      */     }
/* 2280 */     if (msg != null) {
/* 2281 */       base = base + ": " + msg;
/*      */     }
/* 2283 */     return new IllegalArgumentException(base);
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.Utf8StreamParser
 * JD-Core Version:    0.6.0
 */