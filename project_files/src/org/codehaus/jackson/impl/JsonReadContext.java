/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import org.codehaus.jackson.JsonLocation;
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ import org.codehaus.jackson.util.CharTypes;
/*     */ 
/*     */ public final class JsonReadContext extends JsonStreamContext
/*     */ {
/*     */   protected final JsonReadContext _parent;
/*     */   protected int _lineNr;
/*     */   protected int _columnNr;
/*     */   protected String _currentName;
/*  34 */   protected JsonReadContext _child = null;
/*     */ 
/*     */   public JsonReadContext(JsonReadContext parent, int type, int lineNr, int colNr)
/*     */   {
/*  45 */     this._type = type;
/*  46 */     this._parent = parent;
/*  47 */     this._lineNr = lineNr;
/*  48 */     this._columnNr = colNr;
/*  49 */     this._index = -1;
/*     */   }
/*     */ 
/*     */   protected final void reset(int type, int lineNr, int colNr)
/*     */   {
/*  54 */     this._type = type;
/*  55 */     this._index = -1;
/*  56 */     this._lineNr = lineNr;
/*  57 */     this._columnNr = colNr;
/*  58 */     this._currentName = null;
/*     */   }
/*     */ 
/*     */   public static JsonReadContext createRootContext(int lineNr, int colNr)
/*     */   {
/*  65 */     return new JsonReadContext(null, 0, lineNr, colNr);
/*     */   }
/*     */ 
/*     */   public final JsonReadContext createChildArrayContext(int lineNr, int colNr)
/*     */   {
/*  70 */     JsonReadContext ctxt = this._child;
/*  71 */     if (ctxt == null) {
/*  72 */       this._child = (ctxt = new JsonReadContext(this, 1, lineNr, colNr));
/*  73 */       return ctxt;
/*     */     }
/*  75 */     ctxt.reset(1, lineNr, colNr);
/*  76 */     return ctxt;
/*     */   }
/*     */ 
/*     */   public final JsonReadContext createChildObjectContext(int lineNr, int colNr)
/*     */   {
/*  81 */     JsonReadContext ctxt = this._child;
/*  82 */     if (ctxt == null) {
/*  83 */       this._child = (ctxt = new JsonReadContext(this, 2, lineNr, colNr));
/*  84 */       return ctxt;
/*     */     }
/*  86 */     ctxt.reset(2, lineNr, colNr);
/*  87 */     return ctxt;
/*     */   }
/*     */ 
/*     */   public final String getCurrentName()
/*     */   {
/*  97 */     return this._currentName;
/*     */   }
/*     */   public final JsonReadContext getParent() {
/* 100 */     return this._parent;
/*     */   }
/*     */ 
/*     */   public final JsonLocation getStartLocation(Object srcRef)
/*     */   {
/* 117 */     long totalChars = -1L;
/*     */ 
/* 119 */     return new JsonLocation(srcRef, totalChars, this._lineNr, this._columnNr);
/*     */   }
/*     */ 
/*     */   public final boolean expectComma()
/*     */   {
/* 134 */     int ix = ++this._index;
/* 135 */     return (this._type != 0) && (ix > 0);
/*     */   }
/*     */ 
/*     */   public void setCurrentName(String name)
/*     */   {
/* 140 */     this._currentName = name;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 156 */     StringBuilder sb = new StringBuilder(64);
/* 157 */     switch (this._type) {
/*     */     case 0:
/* 159 */       sb.append("/");
/* 160 */       break;
/*     */     case 1:
/* 162 */       sb.append('[');
/* 163 */       sb.append(getCurrentIndex());
/* 164 */       sb.append(']');
/* 165 */       break;
/*     */     case 2:
/* 167 */       sb.append('{');
/* 168 */       if (this._currentName != null) {
/* 169 */         sb.append('"');
/* 170 */         CharTypes.appendQuoted(sb, this._currentName);
/* 171 */         sb.append('"');
/*     */       } else {
/* 173 */         sb.append('?');
/*     */       }
/* 175 */       sb.append(']');
/*     */     }
/*     */ 
/* 178 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonReadContext
 * JD-Core Version:    0.6.0
 */