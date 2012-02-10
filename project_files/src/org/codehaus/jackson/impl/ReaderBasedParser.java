/*      */ package org.codehaus.jackson.impl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import org.codehaus.jackson.Base64Variant;
/*      */ import org.codehaus.jackson.JsonParseException;
/*      */ import org.codehaus.jackson.JsonParser.Feature;
/*      */ import org.codehaus.jackson.JsonToken;
/*      */ import org.codehaus.jackson.ObjectCodec;
/*      */ import org.codehaus.jackson.io.IOContext;
/*      */ import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
/*      */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*      */ import org.codehaus.jackson.util.CharTypes;
/*      */ import org.codehaus.jackson.util.TextBuffer;
/*      */ 
/*      */ public final class ReaderBasedParser extends ReaderBasedNumericParser
/*      */ {
/*      */   protected ObjectCodec _objectCodec;
/*      */   protected final CharsToNameCanonicalizer _symbols;
/*   39 */   protected boolean _tokenIncomplete = false;
/*      */ 
/*      */   public ReaderBasedParser(IOContext ioCtxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st)
/*      */   {
/*   50 */     super(ioCtxt, features, r);
/*   51 */     this._objectCodec = codec;
/*   52 */     this._symbols = st;
/*      */   }
/*      */ 
/*      */   public ObjectCodec getCodec()
/*      */   {
/*   57 */     return this._objectCodec;
/*      */   }
/*      */ 
/*      */   public void setCodec(ObjectCodec c)
/*      */   {
/*   62 */     this._objectCodec = c;
/*      */   }
/*      */ 
/*      */   public final String getText()
/*      */     throws IOException, JsonParseException
/*      */   {
/*   81 */     JsonToken t = this._currToken;
/*   82 */     if (t == JsonToken.VALUE_STRING) {
/*   83 */       if (this._tokenIncomplete) {
/*   84 */         this._tokenIncomplete = false;
/*   85 */         _finishString();
/*      */       }
/*   87 */       return this._textBuffer.contentsAsString();
/*      */     }
/*   89 */     return _getText2(t);
/*      */   }
/*      */ 
/*      */   protected final String _getText2(JsonToken t)
/*      */   {
/*   94 */     if (t == null) {
/*   95 */       return null;
/*      */     }
/*   97 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
/*      */     case 1:
/*   99 */       return this._parsingContext.getCurrentName();
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  105 */       return this._textBuffer.contentsAsString();
/*      */     }
/*  107 */     return t.asString();
/*      */   }
/*      */ 
/*      */   public char[] getTextCharacters()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  114 */     if (this._currToken != null) {
/*  115 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
/*      */       {
/*      */       case 1:
/*  118 */         if (!this._nameCopied) {
/*  119 */           String name = this._parsingContext.getCurrentName();
/*  120 */           int nameLen = name.length();
/*  121 */           if (this._nameCopyBuffer == null)
/*  122 */             this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  123 */           else if (this._nameCopyBuffer.length < nameLen) {
/*  124 */             this._nameCopyBuffer = new char[nameLen];
/*      */           }
/*  126 */           name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  127 */           this._nameCopied = true;
/*      */         }
/*  129 */         return this._nameCopyBuffer;
/*      */       case 2:
/*  132 */         if (!this._tokenIncomplete) break;
/*  133 */         this._tokenIncomplete = false;
/*  134 */         _finishString();
/*      */       case 3:
/*      */       case 4:
/*  139 */         return this._textBuffer.getTextBuffer();
/*      */       }
/*      */ 
/*  142 */       return this._currToken.asCharArray();
/*      */     }
/*      */ 
/*  145 */     return null;
/*      */   }
/*      */ 
/*      */   public int getTextLength()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  152 */     if (this._currToken != null) {
/*  153 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
/*      */       {
/*      */       case 1:
/*  156 */         return this._parsingContext.getCurrentName().length();
/*      */       case 2:
/*  158 */         if (!this._tokenIncomplete) break;
/*  159 */         this._tokenIncomplete = false;
/*  160 */         _finishString();
/*      */       case 3:
/*      */       case 4:
/*  165 */         return this._textBuffer.size();
/*      */       }
/*      */ 
/*  168 */       return this._currToken.asCharArray().length;
/*      */     }
/*      */ 
/*  171 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getTextOffset()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  178 */     if (this._currToken != null) {
/*  179 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*      */       case 1:
/*  181 */         return 0;
/*      */       case 2:
/*  183 */         if (!this._tokenIncomplete) break;
/*  184 */         this._tokenIncomplete = false;
/*  185 */         _finishString();
/*      */       case 3:
/*      */       case 4:
/*  190 */         return this._textBuffer.getTextOffset();
/*      */       }
/*      */     }
/*  193 */     return 0;
/*      */   }
/*      */ 
/*      */   public byte[] getBinaryValue(Base64Variant b64variant)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  200 */     if ((this._currToken != JsonToken.VALUE_STRING) && ((this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT) || (this._binaryValue == null)))
/*      */     {
/*  202 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */ 
/*  207 */     if (this._tokenIncomplete) {
/*      */       try {
/*  209 */         this._binaryValue = _decodeBase64(b64variant);
/*      */       } catch (IllegalArgumentException iae) {
/*  211 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       }
/*      */ 
/*  216 */       this._tokenIncomplete = false;
/*      */     }
/*  218 */     return this._binaryValue;
/*      */   }
/*      */ 
/*      */   public JsonToken nextToken()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  239 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  240 */       return _nextAfterName();
/*      */     }
/*  242 */     if (this._tokenIncomplete) {
/*  243 */       _skipString();
/*      */     }
/*  245 */     int i = _skipWSOrEnd();
/*  246 */     if (i < 0)
/*      */     {
/*  250 */       close();
/*  251 */       return this._currToken = null;
/*      */     }
/*      */ 
/*  257 */     this._tokenInputTotal = (this._currInputProcessed + this._inputPtr - 1L);
/*  258 */     this._tokenInputRow = this._currInputRow;
/*  259 */     this._tokenInputCol = (this._inputPtr - this._currInputRowStart - 1);
/*      */ 
/*  262 */     this._binaryValue = null;
/*      */ 
/*  265 */     if (i == 93) {
/*  266 */       if (!this._parsingContext.inArray()) {
/*  267 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/*  269 */       this._parsingContext = this._parsingContext.getParent();
/*  270 */       return this._currToken = JsonToken.END_ARRAY;
/*      */     }
/*  272 */     if (i == 125) {
/*  273 */       if (!this._parsingContext.inObject()) {
/*  274 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/*  276 */       this._parsingContext = this._parsingContext.getParent();
/*  277 */       return this._currToken = JsonToken.END_OBJECT;
/*      */     }
/*      */ 
/*  281 */     if (this._parsingContext.expectComma()) {
/*  282 */       if (i != 44) {
/*  283 */         _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.getTypeDesc() + " entries");
/*      */       }
/*  285 */       i = _skipWS();
/*      */     }
/*      */ 
/*  292 */     boolean inObject = this._parsingContext.inObject();
/*  293 */     if (inObject)
/*      */     {
/*  295 */       String name = _parseFieldName(i);
/*  296 */       this._parsingContext.setCurrentName(name);
/*  297 */       this._currToken = JsonToken.FIELD_NAME;
/*  298 */       i = _skipWS();
/*  299 */       if (i != 58) {
/*  300 */         _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */       }
/*  302 */       i = _skipWS();
/*      */     }
/*      */     JsonToken t;
/*  309 */     switch (i) {
/*      */     case 34:
/*  311 */       this._tokenIncomplete = true;
/*  312 */       t = JsonToken.VALUE_STRING;
/*  313 */       break;
/*      */     case 91:
/*  315 */       if (!inObject) {
/*  316 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  318 */       t = JsonToken.START_ARRAY;
/*  319 */       break;
/*      */     case 123:
/*  321 */       if (!inObject) {
/*  322 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       }
/*  324 */       t = JsonToken.START_OBJECT;
/*  325 */       break;
/*      */     case 93:
/*      */     case 125:
/*  330 */       _reportUnexpectedChar(i, "expected a value");
/*      */     case 116:
/*  332 */       _matchToken(JsonToken.VALUE_TRUE);
/*  333 */       t = JsonToken.VALUE_TRUE;
/*  334 */       break;
/*      */     case 102:
/*  336 */       _matchToken(JsonToken.VALUE_FALSE);
/*  337 */       t = JsonToken.VALUE_FALSE;
/*  338 */       break;
/*      */     case 110:
/*  340 */       _matchToken(JsonToken.VALUE_NULL);
/*  341 */       t = JsonToken.VALUE_NULL;
/*  342 */       break;
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
/*  359 */       t = parseNumberText(i);
/*  360 */       break;
/*      */     default:
/*  362 */       t = _handleUnexpectedValue(i);
/*      */     }
/*      */ 
/*  366 */     if (inObject) {
/*  367 */       this._nextToken = t;
/*  368 */       return this._currToken;
/*      */     }
/*  370 */     this._currToken = t;
/*  371 */     return t;
/*      */   }
/*      */ 
/*      */   private final JsonToken _nextAfterName()
/*      */   {
/*  376 */     this._nameCopied = false;
/*  377 */     JsonToken t = this._nextToken;
/*  378 */     this._nextToken = null;
/*      */ 
/*  380 */     if (t == JsonToken.START_ARRAY)
/*  381 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  382 */     else if (t == JsonToken.START_OBJECT) {
/*  383 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     }
/*  385 */     return this._currToken = t;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  391 */     super.close();
/*  392 */     this._symbols.release();
/*      */   }
/*      */ 
/*      */   protected final String _parseFieldName(int i)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  404 */     if (i != 34) {
/*  405 */       return _handleUnusualFieldName(i);
/*      */     }
/*      */ 
/*  411 */     int ptr = this._inputPtr;
/*  412 */     int hash = 0;
/*  413 */     int inputLen = this._inputEnd;
/*      */ 
/*  415 */     if (ptr < inputLen) {
/*  416 */       int[] codes = CharTypes.getInputCodeLatin1();
/*  417 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/*  420 */         int ch = this._inputBuffer[ptr];
/*  421 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/*  422 */           if (ch != 34) break;
/*  423 */           int start = this._inputPtr;
/*  424 */           this._inputPtr = (ptr + 1);
/*  425 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/*      */ 
/*  429 */         hash = hash * 31 + ch;
/*  430 */         ptr++;
/*  431 */       }while (ptr < inputLen);
/*      */     }
/*      */ 
/*  434 */     int start = this._inputPtr;
/*  435 */     this._inputPtr = ptr;
/*  436 */     return _parseFieldName2(start, hash, 34);
/*      */   }
/*      */ 
/*      */   private String _parseFieldName2(int startPtr, int hash, int endChar)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  442 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/*      */ 
/*  447 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/*  448 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     while (true)
/*      */     {
/*  451 */       if ((this._inputPtr >= this._inputEnd) && 
/*  452 */         (!loadMore())) {
/*  453 */         _reportInvalidEOF(": was expecting closing '" + (char)endChar + "' for name");
/*      */       }
/*      */ 
/*  456 */       char c = this._inputBuffer[(this._inputPtr++)];
/*  457 */       int i = c;
/*  458 */       if (i <= 92) {
/*  459 */         if (i == 92)
/*      */         {
/*  464 */           c = _decodeEscaped();
/*  465 */         } else if (i <= endChar) {
/*  466 */           if (i == endChar) {
/*      */             break;
/*      */           }
/*  469 */           if (i < 32) {
/*  470 */             _throwUnquotedSpace(i, "name");
/*      */           }
/*      */         }
/*      */       }
/*  474 */       hash = hash * 31 + i;
/*      */ 
/*  476 */       outBuf[(outPtr++)] = c;
/*      */ 
/*  479 */       if (outPtr >= outBuf.length) {
/*  480 */         outBuf = this._textBuffer.finishCurrentSegment();
/*  481 */         outPtr = 0;
/*      */       }
/*      */     }
/*  484 */     this._textBuffer.setCurrentLength(outPtr);
/*      */ 
/*  486 */     TextBuffer tb = this._textBuffer;
/*  487 */     char[] buf = tb.getTextBuffer();
/*  488 */     int start = tb.getTextOffset();
/*  489 */     int len = tb.size();
/*      */ 
/*  491 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */ 
/*      */   protected final String _handleUnusualFieldName(int i)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  507 */     if ((i == 39) && (isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/*  508 */       return _parseApostropheFieldName();
/*      */     }
/*      */ 
/*  511 */     if (!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
/*  512 */       _reportUnexpectedChar(i, "was expecting double-quote to start field name");
/*      */     }
/*  514 */     int[] codes = CharTypes.getInputCodeLatin1JsNames();
/*  515 */     int maxCode = codes.length;
/*      */     boolean firstOk;
/*      */     boolean firstOk;
/*  520 */     if (i < maxCode)
/*  521 */       firstOk = (codes[i] == 0) && ((i < 48) || (i > 57));
/*      */     else {
/*  523 */       firstOk = Character.isJavaIdentifierPart((char)i);
/*      */     }
/*  525 */     if (!firstOk) {
/*  526 */       _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/*  528 */     int ptr = this._inputPtr;
/*  529 */     int hash = 0;
/*  530 */     int inputLen = this._inputEnd;
/*      */ 
/*  532 */     if (ptr < inputLen) {
/*      */       do {
/*  534 */         int ch = this._inputBuffer[ptr];
/*  535 */         if (ch < maxCode) {
/*  536 */           if (codes[ch] != 0) {
/*  537 */             int start = this._inputPtr - 1;
/*  538 */             this._inputPtr = ptr;
/*  539 */             return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */           }
/*  541 */         } else if (!Character.isJavaIdentifierPart((char)ch)) {
/*  542 */           int start = this._inputPtr - 1;
/*  543 */           this._inputPtr = ptr;
/*  544 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/*  546 */         hash = hash * 31 + ch;
/*  547 */         ptr++;
/*  548 */       }while (ptr < inputLen);
/*      */     }
/*  550 */     int start = this._inputPtr - 1;
/*  551 */     this._inputPtr = ptr;
/*  552 */     return _parseUnusualFieldName2(start, hash, codes);
/*      */   }
/*      */ 
/*      */   protected final String _parseApostropheFieldName()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  559 */     int ptr = this._inputPtr;
/*  560 */     int hash = 0;
/*  561 */     int inputLen = this._inputEnd;
/*      */ 
/*  563 */     if (ptr < inputLen) {
/*  564 */       int[] codes = CharTypes.getInputCodeLatin1();
/*  565 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/*  568 */         int ch = this._inputBuffer[ptr];
/*  569 */         if (ch == 39) {
/*  570 */           int start = this._inputPtr;
/*  571 */           this._inputPtr = (ptr + 1);
/*  572 */           return this._symbols.findSymbol(this._inputBuffer, start, ptr - start, hash);
/*      */         }
/*  574 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/*      */           break;
/*      */         }
/*  577 */         hash = hash * 31 + ch;
/*  578 */         ptr++;
/*  579 */       }while (ptr < inputLen);
/*      */     }
/*      */ 
/*  582 */     int start = this._inputPtr;
/*  583 */     this._inputPtr = ptr;
/*      */ 
/*  585 */     return _parseFieldName2(start, hash, 39);
/*      */   }
/*      */ 
/*      */   protected final JsonToken _handleUnexpectedValue(int i)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  598 */     if ((i != 39) || (!isEnabled(JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
/*  599 */       _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
/*      */     }
/*      */ 
/*  609 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/*  610 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     while (true)
/*      */     {
/*  613 */       if ((this._inputPtr >= this._inputEnd) && 
/*  614 */         (!loadMore())) {
/*  615 */         _reportInvalidEOF(": was expecting closing quote for a string value");
/*      */       }
/*      */ 
/*  618 */       char c = this._inputBuffer[(this._inputPtr++)];
/*  619 */       i = c;
/*  620 */       if (i <= 92) {
/*  621 */         if (i == 92)
/*      */         {
/*  626 */           c = _decodeEscaped();
/*  627 */         } else if (i <= 39) {
/*  628 */           if (i == 39) {
/*      */             break;
/*      */           }
/*  631 */           if (i < 32) {
/*  632 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  637 */       if (outPtr >= outBuf.length) {
/*  638 */         outBuf = this._textBuffer.finishCurrentSegment();
/*  639 */         outPtr = 0;
/*      */       }
/*      */ 
/*  642 */       outBuf[(outPtr++)] = c;
/*      */     }
/*  644 */     this._textBuffer.setCurrentLength(outPtr);
/*  645 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */ 
/*      */   private String _parseUnusualFieldName2(int startPtr, int hash, int[] codes)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  654 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/*  655 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/*  656 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*  657 */     int maxCode = codes.length;
/*      */ 
/*  660 */     while ((this._inputPtr < this._inputEnd) || 
/*  661 */       (loadMore()))
/*      */     {
/*  665 */       char c = this._inputBuffer[this._inputPtr];
/*  666 */       int i = c;
/*  667 */       if (i <= maxCode ? 
/*  668 */         codes[i] != 0 : 
/*  671 */         !Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/*  674 */       this._inputPtr += 1;
/*  675 */       hash = hash * 31 + i;
/*      */ 
/*  677 */       outBuf[(outPtr++)] = c;
/*      */ 
/*  680 */       if (outPtr >= outBuf.length) {
/*  681 */         outBuf = this._textBuffer.finishCurrentSegment();
/*  682 */         outPtr = 0;
/*      */       }
/*      */     }
/*  685 */     this._textBuffer.setCurrentLength(outPtr);
/*      */ 
/*  687 */     TextBuffer tb = this._textBuffer;
/*  688 */     char[] buf = tb.getTextBuffer();
/*  689 */     int start = tb.getTextOffset();
/*  690 */     int len = tb.size();
/*      */ 
/*  692 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */ 
/*      */   protected void _finishString()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  704 */     int ptr = this._inputPtr;
/*  705 */     int inputLen = this._inputEnd;
/*      */ 
/*  707 */     if (ptr < inputLen) {
/*  708 */       int[] codes = CharTypes.getInputCodeLatin1();
/*  709 */       int maxCode = codes.length;
/*      */       do
/*      */       {
/*  712 */         int ch = this._inputBuffer[ptr];
/*  713 */         if ((ch < maxCode) && (codes[ch] != 0)) {
/*  714 */           if (ch != 34) break;
/*  715 */           this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/*  716 */           this._inputPtr = (ptr + 1);
/*      */ 
/*  718 */           return;
/*      */         }
/*      */ 
/*  722 */         ptr++;
/*  723 */       }while (ptr < inputLen);
/*      */     }
/*      */ 
/*  729 */     this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/*  730 */     this._inputPtr = ptr;
/*  731 */     _finishString2();
/*      */   }
/*      */ 
/*      */   protected void _finishString2()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  737 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/*  738 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     while (true)
/*      */     {
/*  741 */       if ((this._inputPtr >= this._inputEnd) && 
/*  742 */         (!loadMore())) {
/*  743 */         _reportInvalidEOF(": was expecting closing quote for a string value");
/*      */       }
/*      */ 
/*  746 */       char c = this._inputBuffer[(this._inputPtr++)];
/*  747 */       int i = c;
/*  748 */       if (i <= 92) {
/*  749 */         if (i == 92)
/*      */         {
/*  754 */           c = _decodeEscaped();
/*  755 */         } else if (i <= 34) {
/*  756 */           if (i == 34) {
/*      */             break;
/*      */           }
/*  759 */           if (i < 32) {
/*  760 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  765 */       if (outPtr >= outBuf.length) {
/*  766 */         outBuf = this._textBuffer.finishCurrentSegment();
/*  767 */         outPtr = 0;
/*      */       }
/*      */ 
/*  770 */       outBuf[(outPtr++)] = c;
/*      */     }
/*  772 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */ 
/*      */   protected void _skipString()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  783 */     this._tokenIncomplete = false;
/*      */ 
/*  785 */     int inputPtr = this._inputPtr;
/*  786 */     int inputLen = this._inputEnd;
/*  787 */     char[] inputBuffer = this._inputBuffer;
/*      */     while (true)
/*      */     {
/*  790 */       if (inputPtr >= inputLen) {
/*  791 */         this._inputPtr = inputPtr;
/*  792 */         if (!loadMore()) {
/*  793 */           _reportInvalidEOF(": was expecting closing quote for a string value");
/*      */         }
/*  795 */         inputPtr = this._inputPtr;
/*  796 */         inputLen = this._inputEnd;
/*      */       }
/*  798 */       char c = inputBuffer[(inputPtr++)];
/*  799 */       int i = c;
/*  800 */       if (i <= 92)
/*  801 */         if (i == 92)
/*      */         {
/*  806 */           this._inputPtr = inputPtr;
/*  807 */           c = _decodeEscaped();
/*  808 */           inputPtr = this._inputPtr;
/*  809 */           inputLen = this._inputEnd;
/*  810 */         } else if (i <= 34) {
/*  811 */           if (i == 34) {
/*  812 */             this._inputPtr = inputPtr;
/*  813 */             break;
/*      */           }
/*  815 */           if (i < 32) {
/*  816 */             this._inputPtr = inputPtr;
/*  817 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void _matchToken(JsonToken token)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  831 */     String matchStr = token.asString();
/*  832 */     int i = 1;
/*      */ 
/*  834 */     for (int len = matchStr.length(); i < len; i++) {
/*  835 */       if ((this._inputPtr >= this._inputEnd) && 
/*  836 */         (!loadMore())) {
/*  837 */         _reportInvalidEOF(" in a value");
/*      */       }
/*      */ 
/*  840 */       char c = this._inputBuffer[this._inputPtr];
/*  841 */       if (c != matchStr.charAt(i)) {
/*  842 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/*  844 */       this._inputPtr += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _reportInvalidToken(String matchedPart)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  856 */     StringBuilder sb = new StringBuilder(matchedPart);
/*      */ 
/*  862 */     while ((this._inputPtr < this._inputEnd) || 
/*  863 */       (loadMore()))
/*      */     {
/*  867 */       char c = this._inputBuffer[this._inputPtr];
/*  868 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/*  871 */       this._inputPtr += 1;
/*  872 */       sb.append(c);
/*      */     }
/*      */ 
/*  875 */     _reportError("Unrecognized token '" + sb.toString() + "': was expecting 'null', 'true' or 'false'");
/*      */   }
/*      */ 
/*      */   protected final void _skipCR()
/*      */     throws IOException
/*      */   {
/*  890 */     if (((this._inputPtr < this._inputEnd) || (loadMore())) && 
/*  891 */       (this._inputBuffer[this._inputPtr] == '\n')) {
/*  892 */       this._inputPtr += 1;
/*      */     }
/*      */ 
/*  895 */     this._currInputRow += 1;
/*  896 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */ 
/*      */   protected final void _skipLF() throws IOException
/*      */   {
/*  901 */     this._currInputRow += 1;
/*  902 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */ 
/*      */   private final int _skipWS()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  908 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/*  909 */       int i = this._inputBuffer[(this._inputPtr++)];
/*  910 */       if (i > 32) {
/*  911 */         if (i != 47) {
/*  912 */           return i;
/*      */         }
/*  914 */         _skipComment();
/*  915 */       } else if (i != 32) {
/*  916 */         if (i == 10)
/*  917 */           _skipLF();
/*  918 */         else if (i == 13)
/*  919 */           _skipCR();
/*  920 */         else if (i != 9) {
/*  921 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*  925 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.getTypeDesc() + " entries");
/*      */   }
/*      */ 
/*      */   private final int _skipWSOrEnd()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  931 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/*  932 */       int i = this._inputBuffer[(this._inputPtr++)];
/*  933 */       if (i > 32) {
/*  934 */         if (i != 47) {
/*  935 */           return i;
/*      */         }
/*  937 */         _skipComment();
/*  938 */       } else if (i != 32) {
/*  939 */         if (i == 10)
/*  940 */           _skipLF();
/*  941 */         else if (i == 13)
/*  942 */           _skipCR();
/*  943 */         else if (i != 9) {
/*  944 */           _throwInvalidSpace(i);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  949 */     _handleEOF();
/*  950 */     return -1;
/*      */   }
/*      */ 
/*      */   private final void _skipComment()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  956 */     if (!isEnabled(JsonParser.Feature.ALLOW_COMMENTS)) {
/*  957 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */ 
/*  960 */     if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*  961 */       _reportInvalidEOF(" in a comment");
/*      */     }
/*  963 */     char c = this._inputBuffer[(this._inputPtr++)];
/*  964 */     if (c == '/')
/*  965 */       _skipCppComment();
/*  966 */     else if (c == '*')
/*  967 */       _skipCComment();
/*      */     else
/*  969 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */   }
/*      */ 
/*      */   private final void _skipCComment()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  978 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/*  979 */       int i = this._inputBuffer[(this._inputPtr++)];
/*  980 */       if (i <= 42) {
/*  981 */         if (i == 42) {
/*  982 */           if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/*      */             break;
/*      */           }
/*  985 */           if (this._inputBuffer[this._inputPtr] == '/') {
/*  986 */             this._inputPtr += 1;
/*  987 */             return;
/*      */           }
/*      */         }
/*      */ 
/*  991 */         if (i < 32) {
/*  992 */           if (i == 10)
/*  993 */             _skipLF();
/*  994 */           else if (i == 13)
/*  995 */             _skipCR();
/*  996 */           else if (i != 9) {
/*  997 */             _throwInvalidSpace(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1002 */     _reportInvalidEOF(" in a comment");
/*      */   }
/*      */ 
/*      */   private final void _skipCppComment()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1009 */     while ((this._inputPtr < this._inputEnd) || (loadMore())) {
/* 1010 */       int i = this._inputBuffer[(this._inputPtr++)];
/* 1011 */       if (i < 32) {
/* 1012 */         if (i == 10) {
/* 1013 */           _skipLF();
/* 1014 */           break;
/* 1015 */         }if (i == 13) {
/* 1016 */           _skipCR();
/* 1017 */           break;
/* 1018 */         }if (i != 9)
/* 1019 */           _throwInvalidSpace(i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final char _decodeEscaped()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1028 */     if ((this._inputPtr >= this._inputEnd) && 
/* 1029 */       (!loadMore())) {
/* 1030 */       _reportInvalidEOF(" in character escape sequence");
/*      */     }
/*      */ 
/* 1033 */     char c = this._inputBuffer[(this._inputPtr++)];
/*      */ 
/* 1035 */     switch (c)
/*      */     {
/*      */     case 'b':
/* 1038 */       return '\b';
/*      */     case 't':
/* 1040 */       return '\t';
/*      */     case 'n':
/* 1042 */       return '\n';
/*      */     case 'f':
/* 1044 */       return '\f';
/*      */     case 'r':
/* 1046 */       return '\r';
/*      */     case '"':
/*      */     case '/':
/*      */     case '\\':
/* 1052 */       return c;
/*      */     case 'u':
/* 1055 */       break;
/*      */     default:
/* 1058 */       return _handleUnrecognizedCharacterEscape(c);
/*      */     }
/*      */ 
/* 1062 */     int value = 0;
/* 1063 */     for (int i = 0; i < 4; i++) {
/* 1064 */       if ((this._inputPtr >= this._inputEnd) && 
/* 1065 */         (!loadMore())) {
/* 1066 */         _reportInvalidEOF(" in character escape sequence");
/*      */       }
/*      */ 
/* 1069 */       int ch = this._inputBuffer[(this._inputPtr++)];
/* 1070 */       int digit = CharTypes.charToHex(ch);
/* 1071 */       if (digit < 0) {
/* 1072 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 1074 */       value = value << 4 | digit;
/*      */     }
/* 1076 */     return (char)value;
/*      */   }
/*      */ 
/*      */   protected byte[] _decodeBase64(Base64Variant b64variant)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1089 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */     while (true)
/*      */     {
/* 1102 */       if (this._inputPtr >= this._inputEnd) {
/* 1103 */         loadMoreGuaranteed();
/*      */       }
/* 1105 */       char ch = this._inputBuffer[(this._inputPtr++)];
/* 1106 */       if (ch > ' ') {
/* 1107 */         int bits = b64variant.decodeBase64Char(ch);
/* 1108 */         if (bits < 0) {
/* 1109 */           if (ch == '"') {
/* 1110 */             return builder.toByteArray();
/*      */           }
/* 1112 */           throw reportInvalidChar(b64variant, ch, 0);
/*      */         }
/* 1114 */         int decodedData = bits;
/*      */ 
/* 1118 */         if (this._inputPtr >= this._inputEnd) {
/* 1119 */           loadMoreGuaranteed();
/*      */         }
/* 1121 */         ch = this._inputBuffer[(this._inputPtr++)];
/* 1122 */         bits = b64variant.decodeBase64Char(ch);
/* 1123 */         if (bits < 0) {
/* 1124 */           throw reportInvalidChar(b64variant, ch, 1);
/*      */         }
/* 1126 */         decodedData = decodedData << 6 | bits;
/*      */ 
/* 1129 */         if (this._inputPtr >= this._inputEnd) {
/* 1130 */           loadMoreGuaranteed();
/*      */         }
/* 1132 */         ch = this._inputBuffer[(this._inputPtr++)];
/* 1133 */         bits = b64variant.decodeBase64Char(ch);
/*      */ 
/* 1136 */         if (bits < 0) {
/* 1137 */           if (bits != -2) {
/* 1138 */             throw reportInvalidChar(b64variant, ch, 2);
/*      */           }
/*      */ 
/* 1141 */           if (this._inputPtr >= this._inputEnd) {
/* 1142 */             loadMoreGuaranteed();
/*      */           }
/* 1144 */           ch = this._inputBuffer[(this._inputPtr++)];
/* 1145 */           if (!b64variant.usesPaddingChar(ch)) {
/* 1146 */             throw reportInvalidChar(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */           }
/*      */ 
/* 1149 */           decodedData >>= 4;
/* 1150 */           builder.append(decodedData);
/* 1151 */           continue;
/*      */         }
/*      */ 
/* 1154 */         decodedData = decodedData << 6 | bits;
/*      */ 
/* 1156 */         if (this._inputPtr >= this._inputEnd) {
/* 1157 */           loadMoreGuaranteed();
/*      */         }
/* 1159 */         ch = this._inputBuffer[(this._inputPtr++)];
/* 1160 */         bits = b64variant.decodeBase64Char(ch);
/* 1161 */         if (bits < 0) {
/* 1162 */           if (bits != -2) {
/* 1163 */             throw reportInvalidChar(b64variant, ch, 3);
/*      */           }
/*      */ 
/* 1171 */           decodedData >>= 2;
/* 1172 */           builder.appendTwoBytes(decodedData);
/*      */         }
/*      */         else {
/* 1175 */           decodedData = decodedData << 6 | bits;
/* 1176 */           builder.appendThreeBytes(decodedData);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, char ch, int bindex) throws IllegalArgumentException
/*      */   {
/* 1184 */     return reportInvalidChar(b64variant, ch, bindex, null);
/*      */   }
/*      */ 
/*      */   protected IllegalArgumentException reportInvalidChar(Base64Variant b64variant, char ch, int bindex, String msg)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     String base;
/*      */     String base;
/* 1195 */     if (ch <= ' ') {
/* 1196 */       base = "Illegal white space character (code 0x" + Integer.toHexString(ch) + ") as character #" + (bindex + 1) + " of 4-char base64 unit: can only used between units";
/*      */     }
/*      */     else
/*      */     {
/*      */       String base;
/* 1197 */       if (b64variant.usesPaddingChar(ch)) {
/* 1198 */         base = "Unexpected padding character ('" + b64variant.getPaddingChar() + "') as character #" + (bindex + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
/*      */       }
/*      */       else
/*      */       {
/*      */         String base;
/* 1199 */         if ((!Character.isDefined(ch)) || (Character.isISOControl(ch)))
/*      */         {
/* 1201 */           base = "Illegal character (code 0x" + Integer.toHexString(ch) + ") in base64 content";
/*      */         }
/* 1203 */         else base = "Illegal character '" + ch + "' (code 0x" + Integer.toHexString(ch) + ") in base64 content"; 
/*      */       }
/*      */     }
/* 1205 */     if (msg != null) {
/* 1206 */       base = base + ": " + msg;
/*      */     }
/* 1208 */     return new IllegalArgumentException(base);
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedParser
 * JD-Core Version:    0.6.0
 */