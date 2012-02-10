/*      */ package org.codehaus.jackson.impl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import org.codehaus.jackson.Base64Variant;
/*      */ import org.codehaus.jackson.JsonGenerationException;
/*      */ import org.codehaus.jackson.JsonGenerator.Feature;
/*      */ import org.codehaus.jackson.JsonStreamContext;
/*      */ import org.codehaus.jackson.ObjectCodec;
/*      */ import org.codehaus.jackson.PrettyPrinter;
/*      */ import org.codehaus.jackson.SerializableString;
/*      */ import org.codehaus.jackson.io.IOContext;
/*      */ import org.codehaus.jackson.io.NumberOutput;
/*      */ import org.codehaus.jackson.io.SerializedString;
/*      */ import org.codehaus.jackson.util.CharTypes;
/*      */ 
/*      */ public class Utf8Generator extends JsonGeneratorBase
/*      */ {
/*      */   private static final byte BYTE_u = 117;
/*      */   private static final byte BYTE_0 = 48;
/*      */   private static final byte BYTE_LBRACKET = 91;
/*      */   private static final byte BYTE_RBRACKET = 93;
/*      */   private static final byte BYTE_LCURLY = 123;
/*      */   private static final byte BYTE_RCURLY = 125;
/*      */   private static final byte BYTE_BACKSLASH = 92;
/*      */   private static final byte BYTE_SPACE = 32;
/*      */   private static final byte BYTE_COMMA = 44;
/*      */   private static final byte BYTE_COLON = 58;
/*      */   private static final byte BYTE_QUOTE = 34;
/*      */   protected static final int SURR1_FIRST = 55296;
/*      */   protected static final int SURR1_LAST = 56319;
/*      */   protected static final int SURR2_FIRST = 56320;
/*      */   protected static final int SURR2_LAST = 57343;
/*      */   private static final int MAX_BYTES_TO_BUFFER = 512;
/*   39 */   static final byte[] HEX_CHARS = CharTypes.copyHexBytes();
/*      */ 
/*   41 */   private static final byte[] NULL_BYTES = { 110, 117, 108, 108 };
/*   42 */   private static final byte[] TRUE_BYTES = { 116, 114, 117, 101 };
/*   43 */   private static final byte[] FALSE_BYTES = { 102, 97, 108, 115, 101 };
/*      */ 
/*   45 */   private static final int[] sOutputEscapes = CharTypes.getOutputEscapes();
/*      */   protected final IOContext _ioContext;
/*      */   protected final OutputStream _outputStream;
/*      */   protected byte[] _outputBuffer;
/*   73 */   protected int _outputTail = 0;
/*      */   protected final int _outputEnd;
/*      */   protected final int _outputMaxContiguous;
/*      */   protected char[] _charBuffer;
/*      */   protected final int _charBufferLength;
/*      */   protected byte[] _entityBuffer;
/*      */   protected boolean _bufferRecyclable;
/*      */ 
/*      */   public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out)
/*      */   {
/*  120 */     super(features, codec);
/*  121 */     this._ioContext = ctxt;
/*  122 */     this._outputStream = out;
/*  123 */     this._bufferRecyclable = true;
/*  124 */     this._outputBuffer = ctxt.allocWriteEncodingBuffer();
/*  125 */     this._outputEnd = this._outputBuffer.length;
/*      */ 
/*  130 */     this._outputMaxContiguous = (this._outputEnd >> 3);
/*  131 */     this._charBuffer = ctxt.allocConcatBuffer();
/*  132 */     this._charBufferLength = this._charBuffer.length;
/*      */   }
/*      */ 
/*      */   public Utf8Generator(IOContext ctxt, int features, ObjectCodec codec, OutputStream out, byte[] outputBuffer, int outputOffset, boolean bufferRecyclable)
/*      */   {
/*  139 */     super(features, codec);
/*  140 */     this._ioContext = ctxt;
/*  141 */     this._outputStream = out;
/*  142 */     this._bufferRecyclable = bufferRecyclable;
/*  143 */     this._outputTail = outputOffset;
/*  144 */     this._outputBuffer = outputBuffer;
/*  145 */     this._outputEnd = this._outputBuffer.length;
/*      */ 
/*  147 */     this._outputMaxContiguous = (this._outputEnd >> 3);
/*  148 */     this._charBuffer = ctxt.allocConcatBuffer();
/*  149 */     this._charBufferLength = this._charBuffer.length;
/*      */   }
/*      */ 
/*      */   public final void writeStringField(String fieldName, String value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  165 */     writeFieldName(fieldName);
/*  166 */     writeString(value);
/*      */   }
/*      */ 
/*      */   public final void writeFieldName(String name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  172 */     int status = this._writeContext.writeFieldName(name);
/*  173 */     if (status == 4) {
/*  174 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  176 */     if (this._cfgPrettyPrinter != null) {
/*  177 */       _writePPFieldName(name, status == 1);
/*  178 */       return;
/*      */     }
/*  180 */     if (status == 1) {
/*  181 */       if (this._outputTail >= this._outputEnd) {
/*  182 */         _flushBuffer();
/*      */       }
/*  184 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*  186 */     _writeFieldName(name);
/*      */   }
/*      */ 
/*      */   public final void writeFieldName(SerializedString name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  194 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  195 */     if (status == 4) {
/*  196 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  198 */     if (this._cfgPrettyPrinter != null) {
/*  199 */       _writePPFieldName(name, status == 1);
/*  200 */       return;
/*      */     }
/*  202 */     if (status == 1) {
/*  203 */       if (this._outputTail >= this._outputEnd) {
/*  204 */         _flushBuffer();
/*      */       }
/*  206 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*  208 */     _writeFieldName(name);
/*      */   }
/*      */ 
/*      */   public final void writeFieldName(SerializableString name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  216 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  217 */     if (status == 4) {
/*  218 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  220 */     if (this._cfgPrettyPrinter != null) {
/*  221 */       _writePPFieldName(name, status == 1);
/*  222 */       return;
/*      */     }
/*  224 */     if (status == 1) {
/*  225 */       if (this._outputTail >= this._outputEnd) {
/*  226 */         _flushBuffer();
/*      */       }
/*  228 */       this._outputBuffer[(this._outputTail++)] = 44;
/*      */     }
/*  230 */     _writeFieldName(name);
/*      */   }
/*      */ 
/*      */   public final void writeStartArray()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  242 */     _verifyValueWrite("start an array");
/*  243 */     this._writeContext = this._writeContext.createChildArrayContext();
/*  244 */     if (this._cfgPrettyPrinter != null) {
/*  245 */       this._cfgPrettyPrinter.writeStartArray(this);
/*      */     } else {
/*  247 */       if (this._outputTail >= this._outputEnd) {
/*  248 */         _flushBuffer();
/*      */       }
/*  250 */       this._outputBuffer[(this._outputTail++)] = 91;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeEndArray()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  257 */     if (!this._writeContext.inArray()) {
/*  258 */       _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
/*      */     }
/*  260 */     if (this._cfgPrettyPrinter != null) {
/*  261 */       this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  263 */       if (this._outputTail >= this._outputEnd) {
/*  264 */         _flushBuffer();
/*      */       }
/*  266 */       this._outputBuffer[(this._outputTail++)] = 93;
/*      */     }
/*  268 */     this._writeContext = this._writeContext.getParent();
/*      */   }
/*      */ 
/*      */   public final void writeStartObject()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  274 */     _verifyValueWrite("start an object");
/*  275 */     this._writeContext = this._writeContext.createChildObjectContext();
/*  276 */     if (this._cfgPrettyPrinter != null) {
/*  277 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  279 */       if (this._outputTail >= this._outputEnd) {
/*  280 */         _flushBuffer();
/*      */       }
/*  282 */       this._outputBuffer[(this._outputTail++)] = 123;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeEndObject()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  289 */     if (!this._writeContext.inObject()) {
/*  290 */       _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
/*      */     }
/*  292 */     this._writeContext = this._writeContext.getParent();
/*  293 */     if (this._cfgPrettyPrinter != null) {
/*  294 */       this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  296 */       if (this._outputTail >= this._outputEnd) {
/*  297 */         _flushBuffer();
/*      */       }
/*  299 */       this._outputBuffer[(this._outputTail++)] = 125;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _writeFieldName(String name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  309 */     if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  310 */       _writeStringSegments(name);
/*  311 */       return;
/*      */     }
/*  313 */     if (this._outputTail >= this._outputEnd) {
/*  314 */       _flushBuffer();
/*      */     }
/*  316 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */ 
/*  318 */     int len = name.length();
/*  319 */     if (len <= this._charBufferLength) {
/*  320 */       name.getChars(0, len, this._charBuffer, 0);
/*      */ 
/*  322 */       if (len <= this._outputMaxContiguous) {
/*  323 */         if (this._outputTail + len > this._outputEnd) {
/*  324 */           _flushBuffer();
/*      */         }
/*  326 */         _writeStringSegment(this._charBuffer, 0, len);
/*      */       } else {
/*  328 */         _writeStringSegments(this._charBuffer, 0, len);
/*      */       }
/*      */     } else {
/*  331 */       _writeStringSegments(name);
/*      */     }
/*      */ 
/*  335 */     if (this._outputTail >= this._outputEnd) {
/*  336 */       _flushBuffer();
/*      */     }
/*  338 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   protected final void _writeFieldName(SerializableString name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  344 */     byte[] raw = name.asQuotedUTF8();
/*  345 */     if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  346 */       _writeBytes(raw);
/*  347 */       return;
/*      */     }
/*  349 */     if (this._outputTail >= this._outputEnd) {
/*  350 */       _flushBuffer();
/*      */     }
/*  352 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */ 
/*  355 */     int len = raw.length;
/*  356 */     if (this._outputTail + len + 1 < this._outputEnd) {
/*  357 */       System.arraycopy(raw, 0, this._outputBuffer, this._outputTail, len);
/*  358 */       this._outputTail += len;
/*  359 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     } else {
/*  361 */       _writeBytes(raw);
/*  362 */       if (this._outputTail >= this._outputEnd) {
/*  363 */         _flushBuffer();
/*      */       }
/*  365 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _writePPFieldName(String name, boolean commaBefore)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  376 */     if (commaBefore)
/*  377 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     else {
/*  379 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */ 
/*  382 */     if (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  383 */       if (this._outputTail >= this._outputEnd) {
/*  384 */         _flushBuffer();
/*      */       }
/*  386 */       this._outputBuffer[(this._outputTail++)] = 34;
/*  387 */       int len = name.length();
/*  388 */       if (len <= this._charBufferLength) {
/*  389 */         name.getChars(0, len, this._charBuffer, 0);
/*      */ 
/*  391 */         if (len <= this._outputMaxContiguous) {
/*  392 */           if (this._outputTail + len > this._outputEnd) {
/*  393 */             _flushBuffer();
/*      */           }
/*  395 */           _writeStringSegment(this._charBuffer, 0, len);
/*      */         } else {
/*  397 */           _writeStringSegments(this._charBuffer, 0, len);
/*      */         }
/*      */       } else {
/*  400 */         _writeStringSegments(name);
/*      */       }
/*  402 */       if (this._outputTail >= this._outputEnd) {
/*  403 */         _flushBuffer();
/*      */       }
/*  405 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     } else {
/*  407 */       _writeStringSegments(name);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _writePPFieldName(SerializableString name, boolean commaBefore)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  414 */     if (commaBefore)
/*  415 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     else {
/*  417 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */ 
/*  420 */     boolean addQuotes = isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
/*  421 */     if (addQuotes) {
/*  422 */       if (this._outputTail >= this._outputEnd) {
/*  423 */         _flushBuffer();
/*      */       }
/*  425 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*  427 */     _writeBytes(name.asQuotedUTF8());
/*  428 */     if (addQuotes) {
/*  429 */       if (this._outputTail >= this._outputEnd) {
/*  430 */         _flushBuffer();
/*      */       }
/*  432 */       this._outputBuffer[(this._outputTail++)] = 34;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeString(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  446 */     _verifyValueWrite("write text value");
/*  447 */     if (text == null) {
/*  448 */       _writeNull();
/*  449 */       return;
/*      */     }
/*      */ 
/*  452 */     int len = text.length();
/*  453 */     if (len > this._charBufferLength) {
/*  454 */       _writeLongString(text);
/*  455 */       return;
/*      */     }
/*      */ 
/*  458 */     text.getChars(0, len, this._charBuffer, 0);
/*      */ 
/*  460 */     if (len > this._outputMaxContiguous) {
/*  461 */       _writeLongString(this._charBuffer, 0, len);
/*  462 */       return;
/*      */     }
/*  464 */     if (this._outputTail + len + 2 > this._outputEnd) {
/*  465 */       _flushBuffer();
/*      */     }
/*  467 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  468 */     _writeStringSegment(this._charBuffer, 0, len);
/*  469 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   private final void _writeLongString(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  475 */     if (this._outputTail >= this._outputEnd) {
/*  476 */       _flushBuffer();
/*      */     }
/*  478 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  479 */     _writeStringSegments(text);
/*  480 */     if (this._outputTail >= this._outputEnd) {
/*  481 */       _flushBuffer();
/*      */     }
/*  483 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   private final void _writeLongString(char[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  489 */     if (this._outputTail >= this._outputEnd) {
/*  490 */       _flushBuffer();
/*      */     }
/*  492 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  493 */     _writeStringSegments(this._charBuffer, 0, len);
/*  494 */     if (this._outputTail >= this._outputEnd) {
/*  495 */       _flushBuffer();
/*      */     }
/*  497 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeString(char[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  504 */     _verifyValueWrite("write text value");
/*  505 */     if (this._outputTail >= this._outputEnd) {
/*  506 */       _flushBuffer();
/*      */     }
/*  508 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */ 
/*  510 */     if (len <= this._outputMaxContiguous) {
/*  511 */       if (this._outputTail + len > this._outputEnd) {
/*  512 */         _flushBuffer();
/*      */       }
/*  514 */       _writeStringSegment(text, offset, len);
/*      */     } else {
/*  516 */       _writeStringSegments(text, offset, len);
/*      */     }
/*      */ 
/*  519 */     if (this._outputTail >= this._outputEnd) {
/*  520 */       _flushBuffer();
/*      */     }
/*  522 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public final void writeString(SerializableString text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  529 */     _verifyValueWrite("write text value");
/*  530 */     if (this._outputTail >= this._outputEnd) {
/*  531 */       _flushBuffer();
/*      */     }
/*  533 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  534 */     _writeBytes(text.asQuotedUTF8());
/*  535 */     if (this._outputTail >= this._outputEnd) {
/*  536 */       _flushBuffer();
/*      */     }
/*  538 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  545 */     _verifyValueWrite("write text value");
/*  546 */     if (this._outputTail >= this._outputEnd) {
/*  547 */       _flushBuffer();
/*      */     }
/*  549 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  550 */     _writeBytes(text, offset, length);
/*  551 */     if (this._outputTail >= this._outputEnd) {
/*  552 */       _flushBuffer();
/*      */     }
/*  554 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeUTF8String(byte[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  561 */     _verifyValueWrite("write text value");
/*  562 */     if (this._outputTail >= this._outputEnd) {
/*  563 */       _flushBuffer();
/*      */     }
/*  565 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */ 
/*  567 */     if (len <= this._outputMaxContiguous)
/*  568 */       _writeUTF8Segment(text, offset, len);
/*      */     else {
/*  570 */       _writeUTF8Segments(text, offset, len);
/*      */     }
/*  572 */     if (this._outputTail >= this._outputEnd) {
/*  573 */       _flushBuffer();
/*      */     }
/*  575 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeRaw(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  588 */     int start = 0;
/*  589 */     int len = text.length();
/*  590 */     while (len > 0) {
/*  591 */       char[] buf = this._charBuffer;
/*  592 */       int blen = buf.length;
/*  593 */       int len2 = len < blen ? len : blen;
/*  594 */       text.getChars(start, start + len2, buf, 0);
/*  595 */       writeRaw(buf, 0, len2);
/*  596 */       start += len2;
/*  597 */       len -= len2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeRaw(String text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  605 */     while (len > 0) {
/*  606 */       char[] buf = this._charBuffer;
/*  607 */       int blen = buf.length;
/*  608 */       int len2 = len < blen ? len : blen;
/*  609 */       text.getChars(offset, offset + len2, buf, 0);
/*  610 */       writeRaw(buf, 0, len2);
/*  611 */       offset += len2;
/*  612 */       len -= len2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeRaw(char[] cbuf, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  623 */     int len3 = len + len + len;
/*  624 */     if (this._outputTail + len3 > this._outputEnd)
/*      */     {
/*  626 */       if (this._outputEnd < len3) {
/*  627 */         _writeSegmentedRaw(cbuf, offset, len);
/*  628 */         return;
/*      */       }
/*      */ 
/*  631 */       _flushBuffer();
/*      */     }
/*      */ 
/*  634 */     len += offset;
/*      */ 
/*  638 */     while (offset < len)
/*      */     {
/*      */       while (true) {
/*  641 */         int ch = cbuf[offset];
/*  642 */         if (ch > 127) {
/*      */           break;
/*      */         }
/*  645 */         this._outputBuffer[(this._outputTail++)] = (byte)ch;
/*  646 */         offset++; if (offset >= len)
/*      */           return;
/*      */       }
/*  650 */       char ch = cbuf[(offset++)];
/*  651 */       if (ch < 'ࠀ') {
/*  652 */         this._outputBuffer[(this._outputTail++)] = (byte)(0xC0 | ch >> '\006');
/*  653 */         this._outputBuffer[(this._outputTail++)] = (byte)(0x80 | ch & 0x3F);
/*      */       } else {
/*  655 */         _outputRawMultiByteChar(ch, cbuf, offset, len);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeRaw(char ch)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  664 */     if (this._outputTail + 3 >= this._outputEnd) {
/*  665 */       _flushBuffer();
/*      */     }
/*  667 */     byte[] bbuf = this._outputBuffer;
/*  668 */     if (ch <= '') {
/*  669 */       bbuf[(this._outputTail++)] = (byte)ch;
/*  670 */     } else if (ch < 'ࠀ') {
/*  671 */       bbuf[(this._outputTail++)] = (byte)(0xC0 | ch >> '\006');
/*  672 */       bbuf[(this._outputTail++)] = (byte)(0x80 | ch & 0x3F);
/*      */     } else {
/*  674 */       _outputRawMultiByteChar(ch, null, 0, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void _writeSegmentedRaw(char[] cbuf, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  685 */     int end = this._outputEnd;
/*  686 */     byte[] bbuf = this._outputBuffer;
/*      */ 
/*  689 */     while (offset < len)
/*      */     {
/*      */       while (true) {
/*  692 */         int ch = cbuf[offset];
/*  693 */         if (ch >= 128)
/*      */         {
/*      */           break;
/*      */         }
/*  697 */         if (this._outputTail >= end) {
/*  698 */           _flushBuffer();
/*      */         }
/*  700 */         bbuf[(this._outputTail++)] = (byte)ch;
/*  701 */         offset++; if (offset >= len)
/*      */           return;
/*      */       }
/*  705 */       if (this._outputTail + 3 >= this._outputEnd) {
/*  706 */         _flushBuffer();
/*      */       }
/*  708 */       char ch = cbuf[(offset++)];
/*  709 */       if (ch < 'ࠀ') {
/*  710 */         bbuf[(this._outputTail++)] = (byte)(0xC0 | ch >> '\006');
/*  711 */         bbuf[(this._outputTail++)] = (byte)(0x80 | ch & 0x3F);
/*      */       } else {
/*  713 */         _outputRawMultiByteChar(ch, cbuf, offset, len);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  728 */     _verifyValueWrite("write binary value");
/*      */ 
/*  730 */     if (this._outputTail >= this._outputEnd) {
/*  731 */       _flushBuffer();
/*      */     }
/*  733 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  734 */     _writeBinary(b64variant, data, offset, offset + len);
/*      */ 
/*  736 */     if (this._outputTail >= this._outputEnd) {
/*  737 */       _flushBuffer();
/*      */     }
/*  739 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeNumber(int i)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  752 */     _verifyValueWrite("write number");
/*      */ 
/*  754 */     if (this._outputTail + 11 >= this._outputEnd) {
/*  755 */       _flushBuffer();
/*      */     }
/*  757 */     if (this._cfgNumbersAsStrings) {
/*  758 */       _writeQuotedInt(i);
/*  759 */       return;
/*      */     }
/*  761 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*      */   }
/*      */ 
/*      */   private final void _writeQuotedInt(int i) throws IOException {
/*  765 */     if (this._outputTail + 13 >= this._outputEnd) {
/*  766 */       _flushBuffer();
/*      */     }
/*  768 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  769 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*  770 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeNumber(long l)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  777 */     _verifyValueWrite("write number");
/*  778 */     if (this._cfgNumbersAsStrings) {
/*  779 */       _writeQuotedLong(l);
/*  780 */       return;
/*      */     }
/*  782 */     if (this._outputTail + 21 >= this._outputEnd)
/*      */     {
/*  784 */       _flushBuffer();
/*      */     }
/*  786 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*      */   }
/*      */ 
/*      */   private final void _writeQuotedLong(long l) throws IOException {
/*  790 */     if (this._outputTail + 23 >= this._outputEnd) {
/*  791 */       _flushBuffer();
/*      */     }
/*  793 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  794 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*  795 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeNumber(BigInteger value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  802 */     _verifyValueWrite("write number");
/*  803 */     if (value == null)
/*  804 */       _writeNull();
/*  805 */     else if (this._cfgNumbersAsStrings)
/*  806 */       _writeQuotedRaw(value);
/*      */     else
/*  808 */       writeRaw(value.toString());
/*      */   }
/*      */ 
/*      */   public void writeNumber(double d)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  817 */     if ((this._cfgNumbersAsStrings) || (((Double.isNaN(d)) || (Double.isInfinite(d))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
/*      */     {
/*  821 */       writeString(String.valueOf(d));
/*  822 */       return;
/*      */     }
/*      */ 
/*  825 */     _verifyValueWrite("write number");
/*  826 */     writeRaw(String.valueOf(d));
/*      */   }
/*      */ 
/*      */   public void writeNumber(float f)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  833 */     if ((this._cfgNumbersAsStrings) || (((Float.isNaN(f)) || (Float.isInfinite(f))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
/*      */     {
/*  837 */       writeString(String.valueOf(f));
/*  838 */       return;
/*      */     }
/*      */ 
/*  841 */     _verifyValueWrite("write number");
/*  842 */     writeRaw(String.valueOf(f));
/*      */   }
/*      */ 
/*      */   public void writeNumber(BigDecimal value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  850 */     _verifyValueWrite("write number");
/*  851 */     if (value == null)
/*  852 */       _writeNull();
/*  853 */     else if (this._cfgNumbersAsStrings)
/*  854 */       _writeQuotedRaw(value);
/*      */     else
/*  856 */       writeRaw(value.toString());
/*      */   }
/*      */ 
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  864 */     _verifyValueWrite("write number");
/*  865 */     if (this._cfgNumbersAsStrings)
/*  866 */       _writeQuotedRaw(encodedValue);
/*      */     else
/*  868 */       writeRaw(encodedValue);
/*      */   }
/*      */ 
/*      */   private final void _writeQuotedRaw(Object value)
/*      */     throws IOException
/*      */   {
/*  874 */     if (this._outputTail >= this._outputEnd) {
/*  875 */       _flushBuffer();
/*      */     }
/*  877 */     this._outputBuffer[(this._outputTail++)] = 34;
/*  878 */     writeRaw(value.toString());
/*  879 */     if (this._outputTail >= this._outputEnd) {
/*  880 */       _flushBuffer();
/*      */     }
/*  882 */     this._outputBuffer[(this._outputTail++)] = 34;
/*      */   }
/*      */ 
/*      */   public void writeBoolean(boolean state)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  889 */     _verifyValueWrite("write boolean value");
/*  890 */     if (this._outputTail + 5 >= this._outputEnd) {
/*  891 */       _flushBuffer();
/*      */     }
/*  893 */     byte[] keyword = state ? TRUE_BYTES : FALSE_BYTES;
/*  894 */     int len = keyword.length;
/*  895 */     System.arraycopy(keyword, 0, this._outputBuffer, this._outputTail, len);
/*  896 */     this._outputTail += len;
/*      */   }
/*      */ 
/*      */   public void writeNull()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  903 */     _verifyValueWrite("write null value");
/*  904 */     _writeNull();
/*      */   }
/*      */ 
/*      */   protected final void _verifyValueWrite(String typeMsg)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  917 */     int status = this._writeContext.writeValue();
/*  918 */     if (status == 5) {
/*  919 */       _reportError("Can not " + typeMsg + ", expecting field name");
/*      */     }
/*  921 */     if (this._cfgPrettyPrinter == null)
/*      */     {
/*      */       byte b;
/*  923 */       switch (status) {
/*      */       case 1:
/*  925 */         b = 44;
/*  926 */         break;
/*      */       case 2:
/*  928 */         b = 58;
/*  929 */         break;
/*      */       case 3:
/*  931 */         b = 32;
/*  932 */         break;
/*      */       case 0:
/*      */       default:
/*  935 */         return;
/*      */       }
/*  937 */       if (this._outputTail >= this._outputEnd) {
/*  938 */         _flushBuffer();
/*      */       }
/*  940 */       this._outputBuffer[this._outputTail] = b;
/*  941 */       this._outputTail += 1;
/*  942 */       return;
/*      */     }
/*      */ 
/*  945 */     _verifyPrettyValueWrite(typeMsg, status);
/*      */   }
/*      */ 
/*      */   protected final void _verifyPrettyValueWrite(String typeMsg, int status)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  952 */     switch (status) {
/*      */     case 1:
/*  954 */       this._cfgPrettyPrinter.writeArrayValueSeparator(this);
/*  955 */       break;
/*      */     case 2:
/*  957 */       this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
/*  958 */       break;
/*      */     case 3:
/*  960 */       this._cfgPrettyPrinter.writeRootValueSeparator(this);
/*  961 */       break;
/*      */     case 0:
/*  964 */       if (this._writeContext.inArray()) {
/*  965 */         this._cfgPrettyPrinter.beforeArrayValues(this); } else {
/*  966 */         if (!this._writeContext.inObject()) break;
/*  967 */         this._cfgPrettyPrinter.beforeObjectEntries(this); } break;
/*      */     default:
/*  971 */       _cantHappen();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void flush()
/*      */     throws IOException
/*      */   {
/*  986 */     _flushBuffer();
/*  987 */     if ((this._outputStream != null) && 
/*  988 */       (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)))
/*  989 */       this._outputStream.flush();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  998 */     super.close();
/*      */ 
/* 1004 */     if ((this._outputBuffer != null) && (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT))) {
/*      */       while (true)
/*      */       {
/* 1007 */         JsonStreamContext ctxt = getOutputContext();
/* 1008 */         if (ctxt.inArray()) {
/* 1009 */           writeEndArray(); } else {
/* 1010 */           if (!ctxt.inObject()) break;
/* 1011 */           writeEndObject();
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1017 */     _flushBuffer();
/*      */ 
/* 1025 */     if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET))) {
/* 1026 */       this._outputStream.close();
/*      */     }
/*      */     else {
/* 1029 */       this._outputStream.flush();
/*      */     }
/*      */ 
/* 1032 */     _releaseBuffers();
/*      */   }
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */   {
/* 1038 */     byte[] buf = this._outputBuffer;
/* 1039 */     if ((buf != null) && (this._bufferRecyclable)) {
/* 1040 */       this._outputBuffer = null;
/* 1041 */       this._ioContext.releaseWriteEncodingBuffer(buf);
/*      */     }
/* 1043 */     char[] cbuf = this._charBuffer;
/* 1044 */     if (cbuf != null) {
/* 1045 */       this._charBuffer = null;
/* 1046 */       this._ioContext.releaseConcatBuffer(cbuf);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void _writeBytes(byte[] bytes)
/*      */     throws IOException
/*      */   {
/* 1058 */     int len = bytes.length;
/* 1059 */     if (this._outputTail + len > this._outputEnd) {
/* 1060 */       _flushBuffer();
/*      */ 
/* 1062 */       if (len > 512) {
/* 1063 */         this._outputStream.write(bytes, 0, len);
/* 1064 */         return;
/*      */       }
/*      */     }
/* 1067 */     System.arraycopy(bytes, 0, this._outputBuffer, this._outputTail, len);
/* 1068 */     this._outputTail += len;
/*      */   }
/*      */ 
/*      */   private final void _writeBytes(byte[] bytes, int offset, int len) throws IOException
/*      */   {
/* 1073 */     if (this._outputTail + len > this._outputEnd) {
/* 1074 */       _flushBuffer();
/*      */ 
/* 1076 */       if (len > 512) {
/* 1077 */         this._outputStream.write(bytes, offset, len);
/* 1078 */         return;
/*      */       }
/*      */     }
/* 1081 */     System.arraycopy(bytes, offset, this._outputBuffer, this._outputTail, len);
/* 1082 */     this._outputTail += len;
/*      */   }
/*      */ 
/*      */   private final void _writeStringSegments(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1095 */     int left = text.length();
/* 1096 */     int offset = 0;
/* 1097 */     char[] cbuf = this._charBuffer;
/*      */ 
/* 1099 */     while (left > 0) {
/* 1100 */       int len = Math.min(this._outputMaxContiguous, left);
/* 1101 */       text.getChars(offset, offset + len, cbuf, 0);
/* 1102 */       if (this._outputTail + len > this._outputEnd) {
/* 1103 */         _flushBuffer();
/*      */       }
/* 1105 */       _writeStringSegment(cbuf, 0, len);
/* 1106 */       offset += len;
/* 1107 */       left -= len;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void _writeStringSegments(char[] cbuf, int offset, int totalLen)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*      */     do
/*      */     {
/* 1121 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1122 */       if (this._outputTail + len > this._outputEnd) {
/* 1123 */         _flushBuffer();
/*      */       }
/* 1125 */       _writeStringSegment(cbuf, offset, len);
/* 1126 */       offset += len;
/* 1127 */       totalLen -= len;
/* 1128 */     }while (totalLen > 0);
/*      */   }
/*      */ 
/*      */   private final void _writeStringSegment(char[] cbuf, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1145 */     len += offset;
/*      */ 
/* 1147 */     int outputPtr = this._outputTail;
/* 1148 */     byte[] outputBuffer = this._outputBuffer;
/* 1149 */     int[] escCodes = sOutputEscapes;
/*      */ 
/* 1151 */     while (offset < len) {
/* 1152 */       int ch = cbuf[offset];
/* 1153 */       if ((ch > 127) || (escCodes[ch] != 0)) {
/*      */         break;
/*      */       }
/* 1156 */       outputBuffer[(outputPtr++)] = (byte)ch;
/* 1157 */       offset++;
/*      */     }
/* 1159 */     this._outputTail = outputPtr;
/* 1160 */     if (offset < len)
/* 1161 */       _writeStringSegment2(cbuf, offset, len);
/*      */   }
/*      */ 
/*      */   private final void _writeStringSegment2(char[] cbuf, int offset, int end)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1173 */     if (this._outputTail + 6 * (end - offset) > this._outputEnd) {
/* 1174 */       _flushBuffer();
/*      */     }
/*      */ 
/* 1177 */     int outputPtr = this._outputTail;
/*      */ 
/* 1179 */     byte[] outputBuffer = this._outputBuffer;
/* 1180 */     int[] escCodes = sOutputEscapes;
/*      */ 
/* 1182 */     while (offset < end) {
/* 1183 */       int ch = cbuf[(offset++)];
/* 1184 */       if (ch <= 127) {
/* 1185 */         if (escCodes[ch] == 0) {
/* 1186 */           outputBuffer[(outputPtr++)] = (byte)ch;
/* 1187 */           continue;
/*      */         }
/* 1189 */         int escape = escCodes[ch];
/* 1190 */         if (escape > 0) {
/* 1191 */           outputBuffer[(outputPtr++)] = 92;
/* 1192 */           outputBuffer[(outputPtr++)] = (byte)escape; continue;
/*      */         }
/*      */ 
/* 1195 */         outputPtr = _writeEscapedControlChar(escape, outputPtr);
/*      */ 
/* 1197 */         continue;
/*      */       }
/* 1199 */       if (ch <= 2047) {
/* 1200 */         outputBuffer[(outputPtr++)] = (byte)(0xC0 | ch >> 6);
/* 1201 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | ch & 0x3F);
/*      */       } else {
/* 1203 */         outputPtr = _outputMultiByteChar(ch, outputPtr);
/*      */       }
/*      */     }
/* 1206 */     this._outputTail = outputPtr;
/*      */   }
/*      */ 
/*      */   private final void _writeUTF8Segments(byte[] utf8, int offset, int totalLen)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*      */     do
/*      */     {
/* 1218 */       int len = Math.min(this._outputMaxContiguous, totalLen);
/* 1219 */       _writeUTF8Segment(utf8, offset, len);
/* 1220 */       offset += len;
/* 1221 */       totalLen -= len;
/* 1222 */     }while (totalLen > 0);
/*      */   }
/*      */ 
/*      */   private final void _writeUTF8Segment(byte[] utf8, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1229 */     int[] escCodes = sOutputEscapes;
/*      */ 
/* 1231 */     int ptr = offset; for (int end = offset + len; ptr < end; ) {
/* 1232 */       int ch = utf8[(ptr++)] & 0xFF;
/* 1233 */       if (escCodes[ch] != 0) {
/* 1234 */         _writeUTFSegment2(utf8, offset, len);
/* 1235 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1240 */     if (this._outputTail + len > this._outputEnd) {
/* 1241 */       _flushBuffer();
/*      */     }
/* 1243 */     System.arraycopy(utf8, offset, this._outputBuffer, this._outputTail, len);
/* 1244 */     this._outputTail += len;
/*      */   }
/*      */ 
/*      */   private final void _writeUTFSegment2(byte[] utf8, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1250 */     int outputPtr = this._outputTail;
/*      */ 
/* 1253 */     if (outputPtr + len * 6 > this._outputEnd) {
/* 1254 */       _flushBuffer();
/* 1255 */       outputPtr = this._outputTail;
/*      */     }
/*      */ 
/* 1258 */     byte[] outputBuffer = this._outputBuffer;
/* 1259 */     int[] escCodes = sOutputEscapes;
/* 1260 */     len += offset;
/*      */ 
/* 1262 */     while (offset < len) {
/* 1263 */       byte b = utf8[(offset++)];
/* 1264 */       int ch = b & 0xFF;
/* 1265 */       if (escCodes[ch] == 0) {
/* 1266 */         outputBuffer[(outputPtr++)] = b;
/* 1267 */         continue;
/*      */       }
/* 1269 */       int escape = sOutputEscapes[ch];
/* 1270 */       if (escape > 0) {
/* 1271 */         outputBuffer[(outputPtr++)] = 92;
/* 1272 */         outputBuffer[(outputPtr++)] = (byte)escape;
/*      */       }
/*      */       else {
/* 1275 */         outputPtr = _writeEscapedControlChar(escape, outputPtr);
/*      */       }
/*      */     }
/* 1278 */     this._outputTail = outputPtr;
/*      */   }
/*      */ 
/*      */   protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1285 */     int safeInputEnd = inputEnd - 3;
/*      */ 
/* 1287 */     int safeOutputEnd = this._outputEnd - 6;
/* 1288 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */ 
/* 1291 */     while (inputPtr <= safeInputEnd) {
/* 1292 */       if (this._outputTail > safeOutputEnd) {
/* 1293 */         _flushBuffer();
/*      */       }
/*      */ 
/* 1296 */       int b24 = input[(inputPtr++)] << 8;
/* 1297 */       b24 |= input[(inputPtr++)] & 0xFF;
/* 1298 */       b24 = b24 << 8 | input[(inputPtr++)] & 0xFF;
/* 1299 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1300 */       chunksBeforeLF--; if (chunksBeforeLF <= 0)
/*      */       {
/* 1302 */         this._outputBuffer[(this._outputTail++)] = 92;
/* 1303 */         this._outputBuffer[(this._outputTail++)] = 110;
/* 1304 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1309 */     int inputLeft = inputEnd - inputPtr;
/* 1310 */     if (inputLeft > 0) {
/* 1311 */       if (this._outputTail > safeOutputEnd) {
/* 1312 */         _flushBuffer();
/*      */       }
/* 1314 */       int b24 = input[(inputPtr++)] << 16;
/* 1315 */       if (inputLeft == 2) {
/* 1316 */         b24 |= (input[(inputPtr++)] & 0xFF) << 8;
/*      */       }
/* 1318 */       this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final int _outputRawMultiByteChar(int ch, char[] cbuf, int inputOffset, int inputLen)
/*      */     throws IOException
/*      */   {
/* 1331 */     if ((ch >= 55296) && 
/* 1332 */       (ch <= 57343))
/*      */     {
/* 1334 */       if (inputOffset >= inputLen) {
/* 1335 */         _reportError("Split surrogate on writeRaw() input (last character)");
/*      */       }
/* 1337 */       _outputSurrogates(ch, cbuf[inputOffset]);
/* 1338 */       return inputOffset + 1;
/*      */     }
/*      */ 
/* 1341 */     byte[] bbuf = this._outputBuffer;
/* 1342 */     bbuf[(this._outputTail++)] = (byte)(0xE0 | ch >> 12);
/* 1343 */     bbuf[(this._outputTail++)] = (byte)(0x80 | ch >> 6 & 0x3F);
/* 1344 */     bbuf[(this._outputTail++)] = (byte)(0x80 | ch & 0x3F);
/* 1345 */     return inputOffset;
/*      */   }
/*      */ 
/*      */   protected final void _outputSurrogates(int surr1, int surr2)
/*      */     throws IOException
/*      */   {
/* 1351 */     int c = _decodeSurrogate(surr1, surr2);
/* 1352 */     if (this._outputTail + 4 > this._outputEnd) {
/* 1353 */       _flushBuffer();
/*      */     }
/* 1355 */     byte[] bbuf = this._outputBuffer;
/* 1356 */     bbuf[(this._outputTail++)] = (byte)(0xF0 | c >> 18);
/* 1357 */     bbuf[(this._outputTail++)] = (byte)(0x80 | c >> 12 & 0x3F);
/* 1358 */     bbuf[(this._outputTail++)] = (byte)(0x80 | c >> 6 & 0x3F);
/* 1359 */     bbuf[(this._outputTail++)] = (byte)(0x80 | c & 0x3F);
/*      */   }
/*      */ 
/*      */   private final int _outputMultiByteChar(int ch, int outputPtr)
/*      */     throws IOException
/*      */   {
/* 1374 */     byte[] bbuf = this._outputBuffer;
/* 1375 */     if ((ch >= 55296) && (ch <= 57343)) {
/* 1376 */       bbuf[(outputPtr++)] = 92;
/* 1377 */       bbuf[(outputPtr++)] = 117;
/*      */ 
/* 1379 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 12 & 0xF)];
/* 1380 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 8 & 0xF)];
/* 1381 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch >> 4 & 0xF)];
/* 1382 */       bbuf[(outputPtr++)] = HEX_CHARS[(ch & 0xF)];
/*      */     } else {
/* 1384 */       bbuf[(outputPtr++)] = (byte)(0xE0 | ch >> 12);
/* 1385 */       bbuf[(outputPtr++)] = (byte)(0x80 | ch >> 6 & 0x3F);
/* 1386 */       bbuf[(outputPtr++)] = (byte)(0x80 | ch & 0x3F);
/*      */     }
/* 1388 */     return outputPtr;
/*      */   }
/*      */ 
/*      */   protected final int _decodeSurrogate(int surr1, int surr2)
/*      */     throws IOException
/*      */   {
/* 1394 */     if ((surr2 < 56320) || (surr2 > 57343)) {
/* 1395 */       String msg = "Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2);
/* 1396 */       _reportError(msg);
/*      */     }
/* 1398 */     int c = 65536 + (surr1 - 55296 << 10) + (surr2 - 56320);
/* 1399 */     return c;
/*      */   }
/*      */ 
/*      */   private final void _writeNull() throws IOException
/*      */   {
/* 1404 */     if (this._outputTail + 4 >= this._outputEnd) {
/* 1405 */       _flushBuffer();
/*      */     }
/* 1407 */     System.arraycopy(NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
/* 1408 */     this._outputTail += 4;
/*      */   }
/*      */ 
/*      */   private int _writeEscapedControlChar(int escCode, int outputPtr)
/*      */     throws IOException
/*      */   {
/* 1418 */     byte[] bbuf = this._outputBuffer;
/* 1419 */     bbuf[(outputPtr++)] = 92;
/* 1420 */     int value = -(escCode + 1);
/* 1421 */     bbuf[(outputPtr++)] = 117;
/* 1422 */     bbuf[(outputPtr++)] = 48;
/* 1423 */     bbuf[(outputPtr++)] = 48;
/*      */ 
/* 1425 */     bbuf[(outputPtr++)] = HEX_CHARS[(value >> 4)];
/* 1426 */     bbuf[(outputPtr++)] = HEX_CHARS[(value & 0xF)];
/* 1427 */     return outputPtr;
/*      */   }
/*      */ 
/*      */   protected final void _flushBuffer() throws IOException
/*      */   {
/* 1432 */     int len = this._outputTail;
/* 1433 */     if (len > 0) {
/* 1434 */       this._outputTail = 0;
/* 1435 */       this._outputStream.write(this._outputBuffer, 0, len);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.Utf8Generator
 * JD-Core Version:    0.6.0
 */