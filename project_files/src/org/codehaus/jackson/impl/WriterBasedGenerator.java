/*      */ package org.codehaus.jackson.impl;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
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
/*      */ public final class WriterBasedGenerator extends JsonGeneratorBase
/*      */ {
/*      */   protected static final int SHORT_WRITE = 32;
/*   20 */   protected static final char[] HEX_CHARS = CharTypes.copyHexChars();
/*      */   protected final IOContext _ioContext;
/*      */   protected final Writer _writer;
/*      */   protected char[] _outputBuffer;
/*   47 */   protected int _outputHead = 0;
/*      */ 
/*   53 */   protected int _outputTail = 0;
/*      */   protected int _outputEnd;
/*      */   protected char[] _entityBuffer;
/*      */ 
/*      */   public WriterBasedGenerator(IOContext ctxt, int features, ObjectCodec codec, Writer w)
/*      */   {
/*   76 */     super(features, codec);
/*   77 */     this._ioContext = ctxt;
/*   78 */     this._writer = w;
/*   79 */     this._outputBuffer = ctxt.allocConcatBuffer();
/*   80 */     this._outputEnd = this._outputBuffer.length;
/*      */   }
/*      */ 
/*      */   public final void writeFieldName(String name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*   96 */     int status = this._writeContext.writeFieldName(name);
/*   97 */     if (status == 4) {
/*   98 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  100 */     _writeFieldName(name, status == 1);
/*      */   }
/*      */ 
/*      */   public final void writeStringField(String fieldName, String value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  107 */     writeFieldName(fieldName);
/*  108 */     writeString(value);
/*      */   }
/*      */ 
/*      */   public final void writeFieldName(SerializedString name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  116 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  117 */     if (status == 4) {
/*  118 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  120 */     _writeFieldName(name, status == 1);
/*      */   }
/*      */ 
/*      */   public final void writeFieldName(SerializableString name)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  128 */     int status = this._writeContext.writeFieldName(name.getValue());
/*  129 */     if (status == 4) {
/*  130 */       _reportError("Can not write a field name, expecting a value");
/*      */     }
/*  132 */     _writeFieldName(name, status == 1);
/*      */   }
/*      */ 
/*      */   public final void writeStartArray()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  144 */     _verifyValueWrite("start an array");
/*  145 */     this._writeContext = this._writeContext.createChildArrayContext();
/*  146 */     if (this._cfgPrettyPrinter != null) {
/*  147 */       this._cfgPrettyPrinter.writeStartArray(this);
/*      */     } else {
/*  149 */       if (this._outputTail >= this._outputEnd) {
/*  150 */         _flushBuffer();
/*      */       }
/*  152 */       this._outputBuffer[(this._outputTail++)] = '[';
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeEndArray()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  159 */     if (!this._writeContext.inArray()) {
/*  160 */       _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
/*      */     }
/*  162 */     if (this._cfgPrettyPrinter != null) {
/*  163 */       this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  165 */       if (this._outputTail >= this._outputEnd) {
/*  166 */         _flushBuffer();
/*      */       }
/*  168 */       this._outputBuffer[(this._outputTail++)] = ']';
/*      */     }
/*  170 */     this._writeContext = this._writeContext.getParent();
/*      */   }
/*      */ 
/*      */   public final void writeStartObject()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  176 */     _verifyValueWrite("start an object");
/*  177 */     this._writeContext = this._writeContext.createChildObjectContext();
/*  178 */     if (this._cfgPrettyPrinter != null) {
/*  179 */       this._cfgPrettyPrinter.writeStartObject(this);
/*      */     } else {
/*  181 */       if (this._outputTail >= this._outputEnd) {
/*  182 */         _flushBuffer();
/*      */       }
/*  184 */       this._outputBuffer[(this._outputTail++)] = '{';
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeEndObject()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  191 */     if (!this._writeContext.inObject()) {
/*  192 */       _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
/*      */     }
/*  194 */     this._writeContext = this._writeContext.getParent();
/*  195 */     if (this._cfgPrettyPrinter != null) {
/*  196 */       this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
/*      */     } else {
/*  198 */       if (this._outputTail >= this._outputEnd) {
/*  199 */         _flushBuffer();
/*      */       }
/*  201 */       this._outputBuffer[(this._outputTail++)] = '}';
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void _writeFieldName(String name, boolean commaBefore)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  208 */     if (this._cfgPrettyPrinter != null) {
/*  209 */       _writePPFieldName(name, commaBefore);
/*  210 */       return;
/*      */     }
/*      */ 
/*  213 */     if (this._outputTail + 1 >= this._outputEnd) {
/*  214 */       _flushBuffer();
/*      */     }
/*  216 */     if (commaBefore) {
/*  217 */       this._outputBuffer[(this._outputTail++)] = ',';
/*      */     }
/*      */ 
/*  223 */     if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  224 */       _writeString(name);
/*  225 */       return;
/*      */     }
/*      */ 
/*  229 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */ 
/*  231 */     _writeString(name);
/*      */ 
/*  233 */     if (this._outputTail >= this._outputEnd) {
/*  234 */       _flushBuffer();
/*      */     }
/*  236 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void _writeFieldName(SerializableString name, boolean commaBefore)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  242 */     if (this._cfgPrettyPrinter != null) {
/*  243 */       _writePPFieldName(name, commaBefore);
/*  244 */       return;
/*      */     }
/*      */ 
/*  247 */     if (this._outputTail + 1 >= this._outputEnd) {
/*  248 */       _flushBuffer();
/*      */     }
/*  250 */     if (commaBefore) {
/*  251 */       this._outputBuffer[(this._outputTail++)] = ',';
/*      */     }
/*      */ 
/*  256 */     char[] quoted = name.asQuotedChars();
/*  257 */     if (!isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  258 */       writeRaw(quoted, 0, quoted.length);
/*  259 */       return;
/*      */     }
/*      */ 
/*  262 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */ 
/*  264 */     int qlen = quoted.length;
/*  265 */     if (this._outputTail + qlen + 1 >= this._outputEnd) {
/*  266 */       writeRaw(quoted, 0, qlen);
/*      */ 
/*  268 */       if (this._outputTail >= this._outputEnd) {
/*  269 */         _flushBuffer();
/*      */       }
/*  271 */       this._outputBuffer[(this._outputTail++)] = '"';
/*      */     } else {
/*  273 */       System.arraycopy(quoted, 0, this._outputBuffer, this._outputTail, qlen);
/*  274 */       this._outputTail += qlen;
/*  275 */       this._outputBuffer[(this._outputTail++)] = '"';
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _writePPFieldName(String name, boolean commaBefore)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  286 */     if (commaBefore)
/*  287 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     else {
/*  289 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */ 
/*  292 */     if (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  293 */       if (this._outputTail >= this._outputEnd) {
/*  294 */         _flushBuffer();
/*      */       }
/*  296 */       this._outputBuffer[(this._outputTail++)] = '"';
/*  297 */       _writeString(name);
/*  298 */       if (this._outputTail >= this._outputEnd) {
/*  299 */         _flushBuffer();
/*      */       }
/*  301 */       this._outputBuffer[(this._outputTail++)] = '"';
/*      */     } else {
/*  303 */       _writeString(name);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _writePPFieldName(SerializableString name, boolean commaBefore)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  310 */     if (commaBefore)
/*  311 */       this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
/*      */     else {
/*  313 */       this._cfgPrettyPrinter.beforeObjectEntries(this);
/*      */     }
/*      */ 
/*  316 */     char[] quoted = name.asQuotedChars();
/*  317 */     if (isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES)) {
/*  318 */       if (this._outputTail >= this._outputEnd) {
/*  319 */         _flushBuffer();
/*      */       }
/*  321 */       this._outputBuffer[(this._outputTail++)] = '"';
/*  322 */       writeRaw(quoted, 0, quoted.length);
/*  323 */       if (this._outputTail >= this._outputEnd) {
/*  324 */         _flushBuffer();
/*      */       }
/*  326 */       this._outputBuffer[(this._outputTail++)] = '"';
/*      */     } else {
/*  328 */       writeRaw(quoted, 0, quoted.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeString(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  342 */     _verifyValueWrite("write text value");
/*  343 */     if (text == null) {
/*  344 */       _writeNull();
/*  345 */       return;
/*      */     }
/*  347 */     if (this._outputTail >= this._outputEnd) {
/*  348 */       _flushBuffer();
/*      */     }
/*  350 */     this._outputBuffer[(this._outputTail++)] = '"';
/*  351 */     _writeString(text);
/*      */ 
/*  353 */     if (this._outputTail >= this._outputEnd) {
/*  354 */       _flushBuffer();
/*      */     }
/*  356 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void writeString(char[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  363 */     _verifyValueWrite("write text value");
/*  364 */     if (this._outputTail >= this._outputEnd) {
/*  365 */       _flushBuffer();
/*      */     }
/*  367 */     this._outputBuffer[(this._outputTail++)] = '"';
/*  368 */     _writeString(text, offset, len);
/*      */ 
/*  370 */     if (this._outputTail >= this._outputEnd) {
/*  371 */       _flushBuffer();
/*      */     }
/*  373 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public final void writeString(SerializableString sstr)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  380 */     _verifyValueWrite("write text value");
/*  381 */     if (this._outputTail >= this._outputEnd) {
/*  382 */       _flushBuffer();
/*      */     }
/*  384 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */ 
/*  386 */     char[] text = sstr.asQuotedChars();
/*  387 */     int len = text.length;
/*      */ 
/*  389 */     if (len < 32) {
/*  390 */       int room = this._outputEnd - this._outputTail;
/*  391 */       if (len > room) {
/*  392 */         _flushBuffer();
/*      */       }
/*  394 */       System.arraycopy(text, 0, this._outputBuffer, this._outputTail, len);
/*  395 */       this._outputTail += len;
/*      */     }
/*      */     else {
/*  398 */       _flushBuffer();
/*  399 */       this._writer.write(text, 0, len);
/*      */     }
/*  401 */     if (this._outputTail >= this._outputEnd) {
/*  402 */       _flushBuffer();
/*      */     }
/*  404 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  412 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   public void writeUTF8String(byte[] text, int offset, int length)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  420 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   public void writeRaw(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  434 */     int len = text.length();
/*  435 */     int room = this._outputEnd - this._outputTail;
/*      */ 
/*  437 */     if (room == 0) {
/*  438 */       _flushBuffer();
/*  439 */       room = this._outputEnd - this._outputTail;
/*      */     }
/*      */ 
/*  442 */     if (room >= len) {
/*  443 */       text.getChars(0, len, this._outputBuffer, this._outputTail);
/*  444 */       this._outputTail += len;
/*      */     } else {
/*  446 */       writeRawLong(text);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeRaw(String text, int start, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  455 */     int room = this._outputEnd - this._outputTail;
/*      */ 
/*  457 */     if (room < len) {
/*  458 */       _flushBuffer();
/*  459 */       room = this._outputEnd - this._outputTail;
/*      */     }
/*      */ 
/*  462 */     if (room >= len) {
/*  463 */       text.getChars(start, start + len, this._outputBuffer, this._outputTail);
/*  464 */       this._outputTail += len;
/*      */     } else {
/*  466 */       writeRawLong(text.substring(start, start + len));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void writeRaw(char[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  475 */     if (len < 32) {
/*  476 */       int room = this._outputEnd - this._outputTail;
/*  477 */       if (len > room) {
/*  478 */         _flushBuffer();
/*      */       }
/*  480 */       System.arraycopy(text, offset, this._outputBuffer, this._outputTail, len);
/*  481 */       this._outputTail += len;
/*  482 */       return;
/*      */     }
/*      */ 
/*  485 */     _flushBuffer();
/*  486 */     this._writer.write(text, offset, len);
/*      */   }
/*      */ 
/*      */   public void writeRaw(char c)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  493 */     if (this._outputTail >= this._outputEnd) {
/*  494 */       _flushBuffer();
/*      */     }
/*  496 */     this._outputBuffer[(this._outputTail++)] = c;
/*      */   }
/*      */ 
/*      */   private void writeRawLong(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  502 */     int room = this._outputEnd - this._outputTail;
/*      */ 
/*  504 */     text.getChars(0, room, this._outputBuffer, this._outputTail);
/*  505 */     this._outputTail += room;
/*  506 */     _flushBuffer();
/*  507 */     int offset = room;
/*  508 */     int len = text.length() - room;
/*      */ 
/*  510 */     while (len > this._outputEnd) {
/*  511 */       int amount = this._outputEnd;
/*  512 */       text.getChars(offset, offset + amount, this._outputBuffer, 0);
/*  513 */       this._outputHead = 0;
/*  514 */       this._outputTail = amount;
/*  515 */       _flushBuffer();
/*  516 */       offset += amount;
/*  517 */       len -= amount;
/*      */     }
/*      */ 
/*  520 */     text.getChars(offset, offset + len, this._outputBuffer, 0);
/*  521 */     this._outputHead = 0;
/*  522 */     this._outputTail = len;
/*      */   }
/*      */ 
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  535 */     _verifyValueWrite("write binary value");
/*      */ 
/*  537 */     if (this._outputTail >= this._outputEnd) {
/*  538 */       _flushBuffer();
/*      */     }
/*  540 */     this._outputBuffer[(this._outputTail++)] = '"';
/*  541 */     _writeBinary(b64variant, data, offset, offset + len);
/*      */ 
/*  543 */     if (this._outputTail >= this._outputEnd) {
/*  544 */       _flushBuffer();
/*      */     }
/*  546 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void writeNumber(int i)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  559 */     _verifyValueWrite("write number");
/*      */ 
/*  561 */     if (this._outputTail + 11 >= this._outputEnd) {
/*  562 */       _flushBuffer();
/*      */     }
/*  564 */     if (this._cfgNumbersAsStrings) {
/*  565 */       _writeQuotedInt(i);
/*  566 */       return;
/*      */     }
/*  568 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*      */   }
/*      */ 
/*      */   private final void _writeQuotedInt(int i) throws IOException {
/*  572 */     if (this._outputTail + 13 >= this._outputEnd) {
/*  573 */       _flushBuffer();
/*      */     }
/*  575 */     this._outputBuffer[(this._outputTail++)] = '"';
/*  576 */     this._outputTail = NumberOutput.outputInt(i, this._outputBuffer, this._outputTail);
/*  577 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void writeNumber(long l)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  584 */     _verifyValueWrite("write number");
/*  585 */     if (this._cfgNumbersAsStrings) {
/*  586 */       _writeQuotedLong(l);
/*  587 */       return;
/*      */     }
/*  589 */     if (this._outputTail + 21 >= this._outputEnd)
/*      */     {
/*  591 */       _flushBuffer();
/*      */     }
/*  593 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*      */   }
/*      */ 
/*      */   private final void _writeQuotedLong(long l) throws IOException {
/*  597 */     if (this._outputTail + 23 >= this._outputEnd) {
/*  598 */       _flushBuffer();
/*      */     }
/*  600 */     this._outputBuffer[(this._outputTail++)] = '"';
/*  601 */     this._outputTail = NumberOutput.outputLong(l, this._outputBuffer, this._outputTail);
/*  602 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void writeNumber(BigInteger value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  611 */     _verifyValueWrite("write number");
/*  612 */     if (value == null)
/*  613 */       _writeNull();
/*  614 */     else if (this._cfgNumbersAsStrings)
/*  615 */       _writeQuotedRaw(value);
/*      */     else
/*  617 */       writeRaw(value.toString());
/*      */   }
/*      */ 
/*      */   public void writeNumber(double d)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  626 */     if ((this._cfgNumbersAsStrings) || (((Double.isNaN(d)) || (Double.isInfinite(d))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
/*      */     {
/*  630 */       writeString(String.valueOf(d));
/*  631 */       return;
/*      */     }
/*      */ 
/*  634 */     _verifyValueWrite("write number");
/*  635 */     writeRaw(String.valueOf(d));
/*      */   }
/*      */ 
/*      */   public void writeNumber(float f)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  642 */     if ((this._cfgNumbersAsStrings) || (((Float.isNaN(f)) || (Float.isInfinite(f))) && (isEnabled(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS))))
/*      */     {
/*  646 */       writeString(String.valueOf(f));
/*  647 */       return;
/*      */     }
/*      */ 
/*  650 */     _verifyValueWrite("write number");
/*  651 */     writeRaw(String.valueOf(f));
/*      */   }
/*      */ 
/*      */   public void writeNumber(BigDecimal value)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  659 */     _verifyValueWrite("write number");
/*  660 */     if (value == null)
/*  661 */       _writeNull();
/*  662 */     else if (this._cfgNumbersAsStrings)
/*  663 */       _writeQuotedRaw(value);
/*      */     else
/*  665 */       writeRaw(value.toString());
/*      */   }
/*      */ 
/*      */   public void writeNumber(String encodedValue)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  673 */     _verifyValueWrite("write number");
/*  674 */     if (this._cfgNumbersAsStrings)
/*  675 */       _writeQuotedRaw(encodedValue);
/*      */     else
/*  677 */       writeRaw(encodedValue);
/*      */   }
/*      */ 
/*      */   private final void _writeQuotedRaw(Object value)
/*      */     throws IOException
/*      */   {
/*  683 */     if (this._outputTail >= this._outputEnd) {
/*  684 */       _flushBuffer();
/*      */     }
/*  686 */     this._outputBuffer[(this._outputTail++)] = '"';
/*  687 */     writeRaw(value.toString());
/*  688 */     if (this._outputTail >= this._outputEnd) {
/*  689 */       _flushBuffer();
/*      */     }
/*  691 */     this._outputBuffer[(this._outputTail++)] = '"';
/*      */   }
/*      */ 
/*      */   public void writeBoolean(boolean state)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  698 */     _verifyValueWrite("write boolean value");
/*  699 */     if (this._outputTail + 5 >= this._outputEnd) {
/*  700 */       _flushBuffer();
/*      */     }
/*  702 */     int ptr = this._outputTail;
/*  703 */     char[] buf = this._outputBuffer;
/*  704 */     if (state) {
/*  705 */       buf[ptr] = 't';
/*  706 */       ptr++; buf[ptr] = 'r';
/*  707 */       ptr++; buf[ptr] = 'u';
/*  708 */       ptr++; buf[ptr] = 'e';
/*      */     } else {
/*  710 */       buf[ptr] = 'f';
/*  711 */       ptr++; buf[ptr] = 'a';
/*  712 */       ptr++; buf[ptr] = 'l';
/*  713 */       ptr++; buf[ptr] = 's';
/*  714 */       ptr++; buf[ptr] = 'e';
/*      */     }
/*  716 */     this._outputTail = (ptr + 1);
/*      */   }
/*      */ 
/*      */   public void writeNull()
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  723 */     _verifyValueWrite("write null value");
/*  724 */     _writeNull();
/*      */   }
/*      */ 
/*      */   protected final void _verifyValueWrite(String typeMsg)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  737 */     int status = this._writeContext.writeValue();
/*  738 */     if (status == 5) {
/*  739 */       _reportError("Can not " + typeMsg + ", expecting field name");
/*      */     }
/*  741 */     if (this._cfgPrettyPrinter == null)
/*      */     {
/*      */       char c;
/*  743 */       switch (status) {
/*      */       case 1:
/*  745 */         c = ',';
/*  746 */         break;
/*      */       case 2:
/*  748 */         c = ':';
/*  749 */         break;
/*      */       case 3:
/*  751 */         c = ' ';
/*  752 */         break;
/*      */       case 0:
/*      */       default:
/*  755 */         return;
/*      */       }
/*  757 */       if (this._outputTail >= this._outputEnd) {
/*  758 */         _flushBuffer();
/*      */       }
/*  760 */       this._outputBuffer[this._outputTail] = c;
/*  761 */       this._outputTail += 1;
/*  762 */       return;
/*      */     }
/*      */ 
/*  765 */     _verifyPrettyValueWrite(typeMsg, status);
/*      */   }
/*      */ 
/*      */   protected final void _verifyPrettyValueWrite(String typeMsg, int status)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  772 */     switch (status) {
/*      */     case 1:
/*  774 */       this._cfgPrettyPrinter.writeArrayValueSeparator(this);
/*  775 */       break;
/*      */     case 2:
/*  777 */       this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
/*  778 */       break;
/*      */     case 3:
/*  780 */       this._cfgPrettyPrinter.writeRootValueSeparator(this);
/*  781 */       break;
/*      */     case 0:
/*  784 */       if (this._writeContext.inArray()) {
/*  785 */         this._cfgPrettyPrinter.beforeArrayValues(this); } else {
/*  786 */         if (!this._writeContext.inObject()) break;
/*  787 */         this._cfgPrettyPrinter.beforeObjectEntries(this); } break;
/*      */     default:
/*  791 */       _cantHappen();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void flush()
/*      */     throws IOException
/*      */   {
/*  806 */     _flushBuffer();
/*  807 */     if ((this._writer != null) && 
/*  808 */       (isEnabled(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)))
/*  809 */       this._writer.flush();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  818 */     super.close();
/*      */ 
/*  824 */     if ((this._outputBuffer != null) && (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT))) {
/*      */       while (true)
/*      */       {
/*  827 */         JsonStreamContext ctxt = getOutputContext();
/*  828 */         if (ctxt.inArray()) {
/*  829 */           writeEndArray(); } else {
/*  830 */           if (!ctxt.inObject()) break;
/*  831 */           writeEndObject();
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  837 */     _flushBuffer();
/*      */ 
/*  845 */     if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonGenerator.Feature.AUTO_CLOSE_TARGET))) {
/*  846 */       this._writer.close();
/*      */     }
/*      */     else {
/*  849 */       this._writer.flush();
/*      */     }
/*      */ 
/*  852 */     _releaseBuffers();
/*      */   }
/*      */ 
/*      */   protected void _releaseBuffers()
/*      */   {
/*  858 */     char[] buf = this._outputBuffer;
/*  859 */     if (buf != null) {
/*  860 */       this._outputBuffer = null;
/*  861 */       this._ioContext.releaseConcatBuffer(buf);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _writeString(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  879 */     int len = text.length();
/*  880 */     if (len > this._outputEnd) {
/*  881 */       _writeLongString(text);
/*  882 */       return;
/*      */     }
/*      */ 
/*  887 */     if (this._outputTail + len > this._outputEnd) {
/*  888 */       _flushBuffer();
/*      */     }
/*  890 */     text.getChars(0, len, this._outputBuffer, this._outputTail);
/*      */ 
/*  893 */     int end = this._outputTail + len;
/*  894 */     int[] escCodes = CharTypes.getOutputEscapes();
/*  895 */     int escLen = escCodes.length;
/*      */ 
/*  898 */     while (this._outputTail < end)
/*      */     {
/*      */       while (true)
/*      */       {
/*  902 */         char c = this._outputBuffer[this._outputTail];
/*  903 */         if ((c < escLen) && (escCodes[c] != 0)) {
/*      */           break;
/*      */         }
/*  906 */         if (++this._outputTail >= end)
/*      */         {
/*      */           return;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  915 */       int flushLen = this._outputTail - this._outputHead;
/*  916 */       if (flushLen > 0) {
/*  917 */         this._writer.write(this._outputBuffer, this._outputHead, flushLen);
/*      */       }
/*      */ 
/*  923 */       int escCode = escCodes[this._outputBuffer[this._outputTail]];
/*  924 */       this._outputTail += 1;
/*  925 */       int needLen = escCode < 0 ? 6 : 2;
/*      */ 
/*  927 */       if (needLen > this._outputTail) {
/*  928 */         this._outputHead = this._outputTail;
/*  929 */         _writeSingleEscape(escCode);
/*      */       }
/*      */       else {
/*  932 */         int ptr = this._outputTail - needLen;
/*  933 */         this._outputHead = ptr;
/*  934 */         _appendSingleEscape(escCode, this._outputBuffer, ptr);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _writeLongString(String text)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  948 */     _flushBuffer();
/*      */ 
/*  951 */     int textLen = text.length();
/*  952 */     int offset = 0;
/*      */     do {
/*  954 */       int max = this._outputEnd;
/*  955 */       int segmentLen = offset + max > textLen ? textLen - offset : max;
/*      */ 
/*  957 */       text.getChars(offset, offset + segmentLen, this._outputBuffer, 0);
/*  958 */       _writeSegment(segmentLen);
/*  959 */       offset += segmentLen;
/*  960 */     }while (offset < textLen);
/*      */   }
/*      */ 
/*      */   private final void _writeSegment(int end)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/*  974 */     int[] escCodes = CharTypes.getOutputEscapes();
/*  975 */     int escLen = escCodes.length;
/*      */ 
/*  977 */     int ptr = 0;
/*      */ 
/*  980 */     while (ptr < end)
/*      */     {
/*  982 */       int start = ptr;
/*      */       while (true) {
/*  984 */         char c = this._outputBuffer[ptr];
/*  985 */         if ((c < escLen) && (escCodes[c] != 0)) {
/*      */           break;
/*      */         }
/*  988 */         ptr++; if (ptr >= end)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  997 */       int flushLen = ptr - start;
/*  998 */       if (flushLen > 0) {
/*  999 */         this._writer.write(this._outputBuffer, start, flushLen);
/* 1000 */         if (ptr >= end)
/*      */         {
/*      */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1008 */       int escCode = escCodes[this._outputBuffer[ptr]];
/* 1009 */       ptr++;
/* 1010 */       int needLen = escCode < 0 ? 6 : 2;
/*      */ 
/* 1012 */       if (needLen > this._outputTail) {
/* 1013 */         _writeSingleEscape(escCode);
/*      */       }
/*      */       else {
/* 1016 */         ptr -= needLen;
/* 1017 */         _appendSingleEscape(escCode, this._outputBuffer, ptr);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _writeString(char[] text, int offset, int len)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1034 */     len += offset;
/* 1035 */     int[] escCodes = CharTypes.getOutputEscapes();
/* 1036 */     int escLen = escCodes.length;
/* 1037 */     while (offset < len) {
/* 1038 */       int start = offset;
/*      */       while (true)
/*      */       {
/* 1041 */         char c = text[offset];
/* 1042 */         if ((c < escLen) && (escCodes[c] != 0)) {
/*      */           break;
/*      */         }
/* 1045 */         offset++; if (offset >= len)
/*      */         {
/*      */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1051 */       int newAmount = offset - start;
/* 1052 */       if (newAmount < 32)
/*      */       {
/* 1054 */         if (this._outputTail + newAmount > this._outputEnd) {
/* 1055 */           _flushBuffer();
/*      */         }
/* 1057 */         if (newAmount > 0) {
/* 1058 */           System.arraycopy(text, start, this._outputBuffer, this._outputTail, newAmount);
/* 1059 */           this._outputTail += newAmount;
/*      */         }
/*      */       } else {
/* 1062 */         _flushBuffer();
/* 1063 */         this._writer.write(text, start, newAmount);
/*      */       }
/*      */ 
/* 1066 */       if (offset >= len)
/*      */       {
/*      */         break;
/*      */       }
/* 1070 */       int escCode = escCodes[text[offset]];
/* 1071 */       offset++;
/* 1072 */       int needLen = escCode < 0 ? 6 : 2;
/* 1073 */       if (this._outputTail + needLen > this._outputEnd) {
/* 1074 */         _flushBuffer();
/*      */       }
/* 1076 */       _appendSingleEscape(escCode, this._outputBuffer, this._outputTail);
/* 1077 */       this._outputTail += needLen;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void _writeBinary(Base64Variant b64variant, byte[] input, int inputPtr, int inputEnd)
/*      */     throws IOException, JsonGenerationException
/*      */   {
/* 1085 */     int safeInputEnd = inputEnd - 3;
/*      */ 
/* 1087 */     int safeOutputEnd = this._outputEnd - 6;
/* 1088 */     int chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */ 
/* 1091 */     while (inputPtr <= safeInputEnd) {
/* 1092 */       if (this._outputTail > safeOutputEnd) {
/* 1093 */         _flushBuffer();
/*      */       }
/*      */ 
/* 1096 */       int b24 = input[(inputPtr++)] << 8;
/* 1097 */       b24 |= input[(inputPtr++)] & 0xFF;
/* 1098 */       b24 = b24 << 8 | input[(inputPtr++)] & 0xFF;
/* 1099 */       this._outputTail = b64variant.encodeBase64Chunk(b24, this._outputBuffer, this._outputTail);
/* 1100 */       chunksBeforeLF--; if (chunksBeforeLF <= 0)
/*      */       {
/* 1102 */         this._outputBuffer[(this._outputTail++)] = '\\';
/* 1103 */         this._outputBuffer[(this._outputTail++)] = 'n';
/* 1104 */         chunksBeforeLF = b64variant.getMaxLineLength() >> 2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1109 */     int inputLeft = inputEnd - inputPtr;
/* 1110 */     if (inputLeft > 0) {
/* 1111 */       if (this._outputTail > safeOutputEnd) {
/* 1112 */         _flushBuffer();
/*      */       }
/* 1114 */       int b24 = input[(inputPtr++)] << 16;
/* 1115 */       if (inputLeft == 2) {
/* 1116 */         b24 |= (input[(inputPtr++)] & 0xFF) << 8;
/*      */       }
/* 1118 */       this._outputTail = b64variant.encodeBase64Partial(b24, inputLeft, this._outputBuffer, this._outputTail);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void _writeNull() throws IOException
/*      */   {
/* 1124 */     if (this._outputTail + 4 >= this._outputEnd) {
/* 1125 */       _flushBuffer();
/*      */     }
/* 1127 */     int ptr = this._outputTail;
/* 1128 */     char[] buf = this._outputBuffer;
/* 1129 */     buf[ptr] = 'n';
/* 1130 */     ptr++; buf[ptr] = 'u';
/* 1131 */     ptr++; buf[ptr] = 'l';
/* 1132 */     ptr++; buf[ptr] = 'l';
/* 1133 */     this._outputTail = (ptr + 1);
/*      */   }
/*      */ 
/*      */   private void _writeSingleEscape(int escCode)
/*      */     throws IOException
/*      */   {
/* 1143 */     char[] buf = this._entityBuffer;
/* 1144 */     if (buf == null) {
/* 1145 */       buf = new char[6];
/* 1146 */       buf[0] = '\\';
/* 1147 */       buf[2] = '0';
/* 1148 */       buf[3] = '0';
/*      */     }
/*      */ 
/* 1151 */     if (escCode < 0) {
/* 1152 */       int value = -(escCode + 1);
/* 1153 */       buf[1] = 'u';
/*      */ 
/* 1155 */       buf[4] = HEX_CHARS[(value >> 4)];
/* 1156 */       buf[5] = HEX_CHARS[(value & 0xF)];
/* 1157 */       this._writer.write(buf, 0, 6);
/*      */     } else {
/* 1159 */       buf[1] = (char)escCode;
/* 1160 */       this._writer.write(buf, 0, 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _appendSingleEscape(int escCode, char[] buf, int ptr)
/*      */   {
/* 1166 */     if (escCode < 0) {
/* 1167 */       int value = -(escCode + 1);
/* 1168 */       buf[ptr] = '\\';
/* 1169 */       ptr++; buf[ptr] = 'u';
/*      */ 
/* 1171 */       ptr++; buf[ptr] = '0';
/* 1172 */       ptr++; buf[ptr] = '0';
/* 1173 */       ptr++; buf[ptr] = HEX_CHARS[(value >> 4)];
/* 1174 */       ptr++; buf[ptr] = HEX_CHARS[(value & 0xF)];
/*      */     } else {
/* 1176 */       buf[ptr] = '\\';
/* 1177 */       buf[(ptr + 1)] = (char)escCode;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void _flushBuffer() throws IOException
/*      */   {
/* 1183 */     int len = this._outputTail - this._outputHead;
/* 1184 */     if (len > 0) {
/* 1185 */       int offset = this._outputHead;
/* 1186 */       this._outputTail = (this._outputHead = 0);
/* 1187 */       this._writer.write(this._outputBuffer, offset, len);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.WriterBasedGenerator
 * JD-Core Version:    0.6.0
 */