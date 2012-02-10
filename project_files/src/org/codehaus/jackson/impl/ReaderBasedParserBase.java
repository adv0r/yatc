/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser.Feature;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ 
/*     */ public abstract class ReaderBasedParserBase extends JsonNumericParserBase
/*     */ {
/*     */   protected Reader _reader;
/*     */   protected char[] _inputBuffer;
/*     */ 
/*     */   protected ReaderBasedParserBase(IOContext ctxt, int features, Reader r)
/*     */   {
/*  53 */     super(ctxt, features);
/*  54 */     this._reader = r;
/*  55 */     this._inputBuffer = ctxt.allocTokenBuffer();
/*     */   }
/*     */ 
/*     */   public int releaseBuffered(Writer w)
/*     */     throws IOException
/*     */   {
/*  67 */     int count = this._inputEnd - this._inputPtr;
/*  68 */     if (count < 1) {
/*  69 */       return 0;
/*     */     }
/*     */ 
/*  72 */     int origPtr = this._inputPtr;
/*  73 */     w.write(this._inputBuffer, origPtr, count);
/*  74 */     return count;
/*     */   }
/*     */ 
/*     */   protected final boolean loadMore()
/*     */     throws IOException
/*     */   {
/*  87 */     this._currInputProcessed += this._inputEnd;
/*  88 */     this._currInputRowStart -= this._inputEnd;
/*     */ 
/*  90 */     if (this._reader != null) {
/*  91 */       int count = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
/*  92 */       if (count > 0) {
/*  93 */         this._inputPtr = 0;
/*  94 */         this._inputEnd = count;
/*  95 */         return true;
/*     */       }
/*     */ 
/*  98 */       _closeInput();
/*     */ 
/* 100 */       if (count == 0) {
/* 101 */         throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
/*     */       }
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   protected char getNextChar(String eofMsg)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 110 */     if ((this._inputPtr >= this._inputEnd) && 
/* 111 */       (!loadMore())) {
/* 112 */       _reportInvalidEOF(eofMsg);
/*     */     }
/*     */ 
/* 115 */     return this._inputBuffer[(this._inputPtr++)];
/*     */   }
/*     */ 
/*     */   protected void _closeInput()
/*     */     throws IOException
/*     */   {
/* 128 */     if (this._reader != null) {
/* 129 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
/* 130 */         this._reader.close();
/*     */       }
/* 132 */       this._reader = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _releaseBuffers()
/*     */     throws IOException
/*     */   {
/* 146 */     super._releaseBuffers();
/* 147 */     char[] buf = this._inputBuffer;
/* 148 */     if (buf != null) {
/* 149 */       this._inputBuffer = null;
/* 150 */       this._ioContext.releaseTokenBuffer(buf);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedParserBase
 * JD-Core Version:    0.6.0
 */