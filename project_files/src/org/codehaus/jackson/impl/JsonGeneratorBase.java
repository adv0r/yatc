/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonGenerator.Feature;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.ObjectCodec;
/*     */ import org.codehaus.jackson.PrettyPrinter;
/*     */ import org.codehaus.jackson.Version;
/*     */ import org.codehaus.jackson.util.DefaultPrettyPrinter;
/*     */ import org.codehaus.jackson.util.VersionUtil;
/*     */ 
/*     */ public abstract class JsonGeneratorBase extends JsonGenerator
/*     */ {
/*     */   protected ObjectCodec _objectCodec;
/*     */   protected int _features;
/*     */   protected boolean _cfgNumbersAsStrings;
/*     */   protected JsonWriteContext _writeContext;
/*     */   protected boolean _closed;
/*     */ 
/*     */   protected JsonGeneratorBase(int features, ObjectCodec codec)
/*     */   {
/*  72 */     this._features = features;
/*  73 */     this._writeContext = JsonWriteContext.createRootContext();
/*  74 */     this._objectCodec = codec;
/*  75 */     this._cfgNumbersAsStrings = isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
/*     */   }
/*     */ 
/*     */   public Version version()
/*     */   {
/*  80 */     return VersionUtil.versionFor(getClass());
/*     */   }
/*     */ 
/*     */   public JsonGenerator enable(JsonGenerator.Feature f)
/*     */   {
/*  91 */     this._features |= f.getMask();
/*  92 */     if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
/*  93 */       this._cfgNumbersAsStrings = true;
/*     */     }
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonGenerator disable(JsonGenerator.Feature f)
/*     */   {
/* 100 */     this._features &= (f.getMask() ^ 0xFFFFFFFF);
/* 101 */     if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
/* 102 */       this._cfgNumbersAsStrings = false;
/*     */     }
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled(JsonGenerator.Feature f)
/*     */   {
/* 111 */     return (this._features & f.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   public JsonGenerator useDefaultPrettyPrinter()
/*     */   {
/* 117 */     return setPrettyPrinter(new DefaultPrettyPrinter());
/*     */   }
/*     */ 
/*     */   public JsonGenerator setCodec(ObjectCodec oc)
/*     */   {
/* 122 */     this._objectCodec = oc;
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public final ObjectCodec getCodec() {
/* 127 */     return this._objectCodec;
/*     */   }
/*     */ 
/*     */   public final JsonWriteContext getOutputContext()
/*     */   {
/* 139 */     return this._writeContext;
/*     */   }
/*     */ 
/*     */   public void writeStartArray()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 151 */     _verifyValueWrite("start an array");
/* 152 */     this._writeContext = this._writeContext.createChildArrayContext();
/* 153 */     if (this._cfgPrettyPrinter != null)
/* 154 */       this._cfgPrettyPrinter.writeStartArray(this);
/*     */     else
/* 156 */       _writeStartArray();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void _writeStartArray()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeEndArray()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 172 */     if (!this._writeContext.inArray()) {
/* 173 */       _reportError("Current context not an ARRAY but " + this._writeContext.getTypeDesc());
/*     */     }
/* 175 */     if (this._cfgPrettyPrinter != null)
/* 176 */       this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
/*     */     else {
/* 178 */       _writeEndArray();
/*     */     }
/* 180 */     this._writeContext = this._writeContext.getParent();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void _writeEndArray()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeStartObject()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 195 */     _verifyValueWrite("start an object");
/* 196 */     this._writeContext = this._writeContext.createChildObjectContext();
/* 197 */     if (this._cfgPrettyPrinter != null)
/* 198 */       this._cfgPrettyPrinter.writeStartObject(this);
/*     */     else
/* 200 */       _writeStartObject();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void _writeStartObject()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeEndObject()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 216 */     if (!this._writeContext.inObject()) {
/* 217 */       _reportError("Current context not an object but " + this._writeContext.getTypeDesc());
/*     */     }
/* 219 */     this._writeContext = this._writeContext.getParent();
/* 220 */     if (this._cfgPrettyPrinter != null)
/* 221 */       this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
/*     */     else
/* 223 */       _writeEndObject();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected void _writeEndObject()
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeRawValue(String text)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 254 */     _verifyValueWrite("write raw value");
/* 255 */     writeRaw(text);
/*     */   }
/*     */ 
/*     */   public void writeRawValue(String text, int offset, int len)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 262 */     _verifyValueWrite("write raw value");
/* 263 */     writeRaw(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeRawValue(char[] text, int offset, int len)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 270 */     _verifyValueWrite("write raw value");
/* 271 */     writeRaw(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeObject(Object value)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 305 */     if (value == null)
/*     */     {
/* 307 */       writeNull();
/*     */     }
/*     */     else
/*     */     {
/* 314 */       if (this._objectCodec != null) {
/* 315 */         this._objectCodec.writeValue(this, value);
/* 316 */         return;
/*     */       }
/* 318 */       _writeSimpleObject(value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTree(JsonNode rootNode)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 327 */     if (rootNode == null) {
/* 328 */       writeNull();
/*     */     } else {
/* 330 */       if (this._objectCodec == null) {
/* 331 */         throw new IllegalStateException("No ObjectCodec defined for the generator, can not serialize JsonNode-based trees");
/*     */       }
/* 333 */       this._objectCodec.writeTree(this, rootNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 349 */     this._closed = true;
/*     */   }
/*     */ 
/*     */   public boolean isClosed() {
/* 353 */     return this._closed;
/*     */   }
/*     */ 
/*     */   public final void copyCurrentEvent(JsonParser jp)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 365 */     JsonToken t = jp.getCurrentToken();
/*     */ 
/* 367 */     if (t == null) {
/* 368 */       _reportError("No current event to copy");
/*     */     }
/* 370 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
/*     */     case 1:
/* 372 */       writeStartObject();
/* 373 */       break;
/*     */     case 2:
/* 375 */       writeEndObject();
/* 376 */       break;
/*     */     case 3:
/* 378 */       writeStartArray();
/* 379 */       break;
/*     */     case 4:
/* 381 */       writeEndArray();
/* 382 */       break;
/*     */     case 5:
/* 384 */       writeFieldName(jp.getCurrentName());
/* 385 */       break;
/*     */     case 6:
/* 387 */       if (jp.hasTextCharacters())
/* 388 */         writeString(jp.getTextCharacters(), jp.getTextOffset(), jp.getTextLength());
/*     */       else {
/* 390 */         writeString(jp.getText());
/*     */       }
/* 392 */       break;
/*     */     case 7:
/* 394 */       switch (jp.getNumberType()) {
/*     */       case INT:
/* 396 */         writeNumber(jp.getIntValue());
/* 397 */         break;
/*     */       case BIG_INTEGER:
/* 399 */         writeNumber(jp.getBigIntegerValue());
/* 400 */         break;
/*     */       default:
/* 402 */         writeNumber(jp.getLongValue());
/*     */       }
/* 404 */       break;
/*     */     case 8:
/* 406 */       switch (jp.getNumberType()) {
/*     */       case BIG_DECIMAL:
/* 408 */         writeNumber(jp.getDecimalValue());
/* 409 */         break;
/*     */       case FLOAT:
/* 411 */         writeNumber(jp.getFloatValue());
/* 412 */         break;
/*     */       default:
/* 414 */         writeNumber(jp.getDoubleValue());
/*     */       }
/* 416 */       break;
/*     */     case 9:
/* 418 */       writeBoolean(true);
/* 419 */       break;
/*     */     case 10:
/* 421 */       writeBoolean(false);
/* 422 */       break;
/*     */     case 11:
/* 424 */       writeNull();
/* 425 */       break;
/*     */     case 12:
/* 427 */       writeObject(jp.getEmbeddedObject());
/* 428 */       break;
/*     */     default:
/* 430 */       _cantHappen();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void copyCurrentStructure(JsonParser jp)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 438 */     JsonToken t = jp.getCurrentToken();
/*     */ 
/* 441 */     if (t == JsonToken.FIELD_NAME) {
/* 442 */       writeFieldName(jp.getCurrentName());
/* 443 */       t = jp.nextToken();
/*     */     }
/*     */ 
/* 447 */     switch (t) {
/*     */     case START_ARRAY:
/* 449 */       writeStartArray();
/* 450 */       while (jp.nextToken() != JsonToken.END_ARRAY) {
/* 451 */         copyCurrentStructure(jp);
/*     */       }
/* 453 */       writeEndArray();
/* 454 */       break;
/*     */     case START_OBJECT:
/* 456 */       writeStartObject();
/* 457 */       while (jp.nextToken() != JsonToken.END_OBJECT) {
/* 458 */         copyCurrentStructure(jp);
/*     */       }
/* 460 */       writeEndObject();
/* 461 */       break;
/*     */     default:
/* 463 */       copyCurrentEvent(jp);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void _releaseBuffers();
/*     */ 
/*     */   protected abstract void _verifyValueWrite(String paramString)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   protected void _reportError(String msg)
/*     */     throws JsonGenerationException
/*     */   {
/* 481 */     throw new JsonGenerationException(msg);
/*     */   }
/*     */ 
/*     */   protected void _cantHappen()
/*     */   {
/* 486 */     throw new RuntimeException("Internal error: should never end up through this code path");
/*     */   }
/*     */ 
/*     */   protected void _writeSimpleObject(Object value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 503 */     if (value == null) {
/* 504 */       writeNull();
/* 505 */       return;
/*     */     }
/* 507 */     if ((value instanceof String)) {
/* 508 */       writeString((String)value);
/* 509 */       return;
/*     */     }
/* 511 */     if ((value instanceof Number)) {
/* 512 */       Number n = (Number)value;
/* 513 */       if ((n instanceof Integer)) {
/* 514 */         writeNumber(n.intValue());
/* 515 */         return;
/* 516 */       }if ((n instanceof Long)) {
/* 517 */         writeNumber(n.longValue());
/* 518 */         return;
/* 519 */       }if ((n instanceof Double)) {
/* 520 */         writeNumber(n.doubleValue());
/* 521 */         return;
/* 522 */       }if ((n instanceof Float)) {
/* 523 */         writeNumber(n.floatValue());
/* 524 */         return;
/* 525 */       }if ((n instanceof Short)) {
/* 526 */         writeNumber(n.shortValue());
/* 527 */         return;
/* 528 */       }if ((n instanceof Byte)) {
/* 529 */         writeNumber(n.byteValue());
/* 530 */         return;
/* 531 */       }if ((n instanceof BigInteger)) {
/* 532 */         writeNumber((BigInteger)n);
/* 533 */         return;
/* 534 */       }if ((n instanceof BigDecimal)) {
/* 535 */         writeNumber((BigDecimal)n);
/* 536 */         return;
/*     */       }
/*     */ 
/* 540 */       if ((n instanceof AtomicInteger)) {
/* 541 */         writeNumber(((AtomicInteger)n).get());
/* 542 */         return;
/* 543 */       }if ((n instanceof AtomicLong)) {
/* 544 */         writeNumber(((AtomicLong)n).get());
/* 545 */         return;
/*     */       }
/*     */     } else {
/* 547 */       if ((value instanceof byte[])) {
/* 548 */         writeBinary((byte[])(byte[])value);
/* 549 */         return;
/* 550 */       }if ((value instanceof Boolean)) {
/* 551 */         writeBoolean(((Boolean)value).booleanValue());
/* 552 */         return;
/* 553 */       }if ((value instanceof AtomicBoolean)) {
/* 554 */         writeBoolean(((AtomicBoolean)value).get());
/* 555 */         return;
/*     */       }
/*     */     }
/* 557 */     throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + value.getClass().getName() + ")");
/*     */   }
/*     */ 
/*     */   protected final void _throwInternal()
/*     */   {
/* 562 */     throw new RuntimeException("Internal error: this code path should never get executed");
/*     */   }
/*     */ 
/*     */   protected void _reportUnsupportedOperation()
/*     */   {
/* 569 */     throw new UnsupportedOperationException("Operation not supported by generator of type " + getClass().getName());
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonGeneratorBase
 * JD-Core Version:    0.6.0
 */