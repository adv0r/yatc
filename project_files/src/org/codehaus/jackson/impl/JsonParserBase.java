/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.JsonLocation;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.Version;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*     */ import org.codehaus.jackson.util.TextBuffer;
/*     */ import org.codehaus.jackson.util.VersionUtil;
/*     */ 
/*     */ public abstract class JsonParserBase extends JsonParserMinimalBase
/*     */ {
/*     */   protected final IOContext _ioContext;
/*     */   protected boolean _closed;
/*  51 */   protected int _inputPtr = 0;
/*     */ 
/*  56 */   protected int _inputEnd = 0;
/*     */ 
/*  68 */   protected long _currInputProcessed = 0L;
/*     */ 
/*  74 */   protected int _currInputRow = 1;
/*     */ 
/*  82 */   protected int _currInputRowStart = 0;
/*     */ 
/*  98 */   protected long _tokenInputTotal = 0L;
/*     */ 
/* 103 */   protected int _tokenInputRow = 1;
/*     */ 
/* 109 */   protected int _tokenInputCol = 0;
/*     */   protected JsonReadContext _parsingContext;
/*     */   protected JsonToken _nextToken;
/*     */   protected final TextBuffer _textBuffer;
/* 148 */   protected char[] _nameCopyBuffer = null;
/*     */ 
/* 155 */   protected boolean _nameCopied = false;
/*     */ 
/* 161 */   protected ByteArrayBuilder _byteArrayBuilder = null;
/*     */   protected byte[] _binaryValue;
/*     */ 
/*     */   protected JsonParserBase(IOContext ctxt, int features)
/*     */   {
/* 180 */     this._features = features;
/* 181 */     this._ioContext = ctxt;
/* 182 */     this._textBuffer = ctxt.constructTextBuffer();
/* 183 */     this._parsingContext = JsonReadContext.createRootContext(this._tokenInputRow, this._tokenInputCol);
/*     */   }
/*     */ 
/*     */   public Version version()
/*     */   {
/* 188 */     return VersionUtil.versionFor(getClass());
/*     */   }
/*     */ 
/*     */   public String getCurrentName()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 206 */     if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 207 */       JsonReadContext parent = this._parsingContext.getParent();
/* 208 */       return parent.getCurrentName();
/*     */     }
/* 210 */     return this._parsingContext.getCurrentName();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 216 */     if (!this._closed) {
/* 217 */       this._closed = true;
/*     */       try {
/* 219 */         _closeInput();
/*     */       }
/*     */       finally
/*     */       {
/* 223 */         _releaseBuffers();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isClosed() {
/* 229 */     return this._closed;
/*     */   }
/*     */ 
/*     */   public JsonReadContext getParsingContext()
/*     */   {
/* 234 */     return this._parsingContext;
/*     */   }
/*     */ 
/*     */   public JsonLocation getTokenLocation()
/*     */   {
/* 245 */     return new JsonLocation(this._ioContext.getSourceReference(), getTokenCharacterOffset(), getTokenLineNr(), getTokenColumnNr());
/*     */   }
/*     */ 
/*     */   public JsonLocation getCurrentLocation()
/*     */   {
/* 258 */     int col = this._inputPtr - this._currInputRowStart + 1;
/* 259 */     return new JsonLocation(this._ioContext.getSourceReference(), this._currInputProcessed + this._inputPtr - 1L, this._currInputRow, col);
/*     */   }
/*     */ 
/*     */   public boolean hasTextCharacters()
/*     */   {
/* 274 */     if (this._currToken != null) {
/* 275 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*     */       case 1:
/* 277 */         return this._nameCopied;
/*     */       case 2:
/* 279 */         return true;
/*     */       }
/*     */     }
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   public final long getTokenCharacterOffset()
/*     */   {
/* 291 */     return this._tokenInputTotal; } 
/* 292 */   public final int getTokenLineNr() { return this._tokenInputRow; } 
/* 293 */   public final int getTokenColumnNr() { return this._tokenInputCol + 1;
/*     */   }
/*     */ 
/*     */   protected final void loadMoreGuaranteed()
/*     */     throws IOException
/*     */   {
/* 304 */     if (!loadMore())
/* 305 */       _reportInvalidEOF();
/*     */   }
/*     */ 
/*     */   protected abstract boolean loadMore()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void _finishString()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   protected abstract void _closeInput()
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract byte[] _decodeBase64(Base64Variant paramBase64Variant)
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   protected void _releaseBuffers()
/*     */     throws IOException
/*     */   {
/* 337 */     this._textBuffer.releaseBuffers();
/* 338 */     char[] buf = this._nameCopyBuffer;
/* 339 */     if (buf != null) {
/* 340 */       this._nameCopyBuffer = null;
/* 341 */       this._ioContext.releaseNameCopyBuffer(buf);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _handleEOF()
/*     */     throws JsonParseException
/*     */   {
/* 353 */     if (!this._parsingContext.inRoot())
/* 354 */       _reportInvalidEOF(": expected close marker for " + this._parsingContext.getTypeDesc() + " (from " + this._parsingContext.getStartLocation(this._ioContext.getSourceReference()) + ")");
/*     */   }
/*     */ 
/*     */   protected void _reportMismatchedEndMarker(int actCh, char expCh)
/*     */     throws JsonParseException
/*     */   {
/* 367 */     String startDesc = "" + this._parsingContext.getStartLocation(this._ioContext.getSourceReference());
/* 368 */     _reportError("Unexpected close marker '" + (char)actCh + "': expected '" + expCh + "' (for " + this._parsingContext.getTypeDesc() + " starting at " + startDesc + ")");
/*     */   }
/*     */ 
/*     */   public ByteArrayBuilder _getByteArrayBuilder()
/*     */   {
/* 379 */     if (this._byteArrayBuilder == null)
/* 380 */       this._byteArrayBuilder = new ByteArrayBuilder();
/*     */     else {
/* 382 */       this._byteArrayBuilder.reset();
/*     */     }
/* 384 */     return this._byteArrayBuilder;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonParserBase
 * JD-Core Version:    0.6.0
 */