/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ 
/*     */ public class JsonWriteContext extends JsonStreamContext
/*     */ {
/*     */   public static final int STATUS_OK_AS_IS = 0;
/*     */   public static final int STATUS_OK_AFTER_COMMA = 1;
/*     */   public static final int STATUS_OK_AFTER_COLON = 2;
/*     */   public static final int STATUS_OK_AFTER_SPACE = 3;
/*     */   public static final int STATUS_EXPECT_VALUE = 4;
/*     */   public static final int STATUS_EXPECT_NAME = 5;
/*     */   protected final JsonWriteContext _parent;
/*     */   protected String _currentName;
/*  38 */   protected JsonWriteContext _child = null;
/*     */ 
/*     */   protected JsonWriteContext(int type, JsonWriteContext parent)
/*     */   {
/*  49 */     this._type = type;
/*  50 */     this._parent = parent;
/*  51 */     this._index = -1;
/*     */   }
/*     */ 
/*     */   public static JsonWriteContext createRootContext()
/*     */   {
/*  58 */     return new JsonWriteContext(0, null);
/*     */   }
/*     */ 
/*     */   private final JsonWriteContext reset(int type) {
/*  62 */     this._type = type;
/*  63 */     this._index = -1;
/*  64 */     this._currentName = null;
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   public final JsonWriteContext createChildArrayContext()
/*     */   {
/*  70 */     JsonWriteContext ctxt = this._child;
/*  71 */     if (ctxt == null) {
/*  72 */       this._child = (ctxt = new JsonWriteContext(1, this));
/*  73 */       return ctxt;
/*     */     }
/*  75 */     return ctxt.reset(1);
/*     */   }
/*     */ 
/*     */   public final JsonWriteContext createChildObjectContext()
/*     */   {
/*  80 */     JsonWriteContext ctxt = this._child;
/*  81 */     if (ctxt == null) {
/*  82 */       this._child = (ctxt = new JsonWriteContext(2, this));
/*  83 */       return ctxt;
/*     */     }
/*  85 */     return ctxt.reset(2);
/*     */   }
/*     */ 
/*     */   public final JsonWriteContext getParent()
/*     */   {
/*  91 */     return this._parent;
/*     */   }
/*     */   public final String getCurrentName() {
/*  94 */     return this._currentName;
/*     */   }
/*     */ 
/*     */   public final int writeFieldName(String name)
/*     */   {
/* 105 */     if (this._type == 2) {
/* 106 */       if (this._currentName != null) {
/* 107 */         return 4;
/*     */       }
/* 109 */       this._currentName = name;
/* 110 */       return this._index < 0 ? 0 : 1;
/*     */     }
/* 112 */     return 4;
/*     */   }
/*     */ 
/*     */   public final int writeValue()
/*     */   {
/* 118 */     if (this._type == 2) {
/* 119 */       if (this._currentName == null) {
/* 120 */         return 5;
/*     */       }
/* 122 */       this._currentName = null;
/* 123 */       this._index += 1;
/* 124 */       return 2;
/*     */     }
/*     */ 
/* 128 */     if (this._type == 1) {
/* 129 */       int ix = this._index;
/* 130 */       this._index += 1;
/* 131 */       return ix < 0 ? 0 : 1;
/*     */     }
/*     */ 
/* 136 */     this._index += 1;
/* 137 */     return this._index == 0 ? 0 : 3;
/*     */   }
/*     */ 
/*     */   protected final void appendDesc(StringBuilder sb)
/*     */   {
/* 144 */     if (this._type == 2) {
/* 145 */       sb.append('{');
/* 146 */       if (this._currentName != null) {
/* 147 */         sb.append('"');
/*     */ 
/* 149 */         sb.append(this._currentName);
/* 150 */         sb.append('"');
/*     */       } else {
/* 152 */         sb.append('?');
/*     */       }
/* 154 */       sb.append(']');
/* 155 */     } else if (this._type == 1) {
/* 156 */       sb.append('[');
/* 157 */       sb.append(getCurrentIndex());
/* 158 */       sb.append(']');
/*     */     }
/*     */     else {
/* 161 */       sb.append("/");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 174 */     StringBuilder sb = new StringBuilder(64);
/* 175 */     appendDesc(sb);
/* 176 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonWriteContext
 * JD-Core Version:    0.6.0
 */