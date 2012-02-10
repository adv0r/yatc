/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonLocation;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ 
/*     */ public class JsonMappingException extends JsonProcessingException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   static final int MAX_REFS_TO_LIST = 1000;
/*     */   protected LinkedList<Reference> _path;
/*     */ 
/*     */   public JsonMappingException(String msg)
/*     */   {
/* 143 */     super(msg);
/*     */   }
/*     */ 
/*     */   public JsonMappingException(String msg, Throwable rootCause)
/*     */   {
/* 148 */     super(msg, rootCause);
/*     */   }
/*     */ 
/*     */   public JsonMappingException(String msg, JsonLocation loc)
/*     */   {
/* 153 */     super(msg, loc);
/*     */   }
/*     */ 
/*     */   public JsonMappingException(String msg, JsonLocation loc, Throwable rootCause)
/*     */   {
/* 158 */     super(msg, loc, rootCause);
/*     */   }
/*     */ 
/*     */   public static JsonMappingException from(JsonParser jp, String msg)
/*     */   {
/* 163 */     return new JsonMappingException(msg, jp.getTokenLocation());
/*     */   }
/*     */ 
/*     */   public static JsonMappingException from(JsonParser jp, String msg, Throwable problem)
/*     */   {
/* 169 */     return new JsonMappingException(msg, jp.getTokenLocation(), problem);
/*     */   }
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, String refFieldName)
/*     */   {
/* 183 */     return wrapWithPath(src, new Reference(refFrom, refFieldName));
/*     */   }
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, int index)
/*     */   {
/* 197 */     return wrapWithPath(src, new Reference(refFrom, index));
/*     */   }
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Reference ref)
/*     */   {
/*     */     JsonMappingException jme;
/*     */     JsonMappingException jme;
/* 208 */     if ((src instanceof JsonMappingException)) {
/* 209 */       jme = (JsonMappingException)src;
/*     */     } else {
/* 211 */       String msg = src.getMessage();
/*     */ 
/* 215 */       if ((msg == null) || (msg.length() == 0)) {
/* 216 */         msg = "(was " + src.getClass().getName() + ")";
/*     */       }
/* 218 */       jme = new JsonMappingException(msg, null, src);
/*     */     }
/* 220 */     jme.prependPath(ref);
/* 221 */     return jme;
/*     */   }
/*     */ 
/*     */   public List<Reference> getPath()
/*     */   {
/* 232 */     if (this._path == null) {
/* 233 */       return Collections.emptyList();
/*     */     }
/* 235 */     return Collections.unmodifiableList(this._path);
/*     */   }
/*     */ 
/*     */   public void prependPath(Object referrer, String fieldName)
/*     */   {
/* 244 */     Reference ref = new Reference(referrer, fieldName);
/* 245 */     prependPath(ref);
/*     */   }
/*     */ 
/*     */   public void prependPath(Object referrer, int index)
/*     */   {
/* 253 */     Reference ref = new Reference(referrer, index);
/* 254 */     prependPath(ref);
/*     */   }
/*     */ 
/*     */   public void prependPath(Reference r)
/*     */   {
/* 259 */     if (this._path == null) {
/* 260 */       this._path = new LinkedList();
/*     */     }
/*     */ 
/* 266 */     if (this._path.size() < 1000)
/* 267 */       this._path.addFirst(r);
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 287 */     String msg = super.getMessage();
/* 288 */     if (this._path == null) {
/* 289 */       return msg;
/*     */     }
/*     */ 
/* 295 */     StringBuilder sb = msg == null ? new StringBuilder() : new StringBuilder(msg);
/*     */ 
/* 301 */     sb.append(" (through reference chain: ");
/* 302 */     _appendPathDesc(sb);
/* 303 */     sb.append(')');
/* 304 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 310 */     return getClass().getName() + ": " + getMessage();
/*     */   }
/*     */ 
/*     */   protected void _appendPathDesc(StringBuilder sb)
/*     */   {
/* 321 */     Iterator it = this._path.iterator();
/* 322 */     while (it.hasNext()) {
/* 323 */       sb.append(((Reference)it.next()).toString());
/* 324 */       if (it.hasNext())
/* 325 */         sb.append("->");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Reference
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     protected Object _from;
/*     */     protected String _fieldName;
/*  64 */     protected int _index = -1;
/*     */ 
/*     */     protected Reference()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Reference(Object from) {
/*  71 */       this._from = from;
/*     */     }
/*     */     public Reference(Object from, String fieldName) {
/*  74 */       this._from = from;
/*  75 */       if (fieldName == null) {
/*  76 */         throw new NullPointerException("Can not pass null fieldName");
/*     */       }
/*  78 */       this._fieldName = fieldName;
/*     */     }
/*     */ 
/*     */     public Reference(Object from, int index) {
/*  82 */       this._from = from;
/*  83 */       this._index = index;
/*     */     }
/*     */     public void setFrom(Object o) {
/*  86 */       this._from = o; } 
/*  87 */     public void setFieldName(String n) { this._fieldName = n; } 
/*  88 */     public void setIndex(int ix) { this._index = ix; } 
/*     */     public Object getFrom() {
/*  90 */       return this._from; } 
/*  91 */     public String getFieldName() { return this._fieldName; } 
/*  92 */     public int getIndex() { return this._index; }
/*     */ 
/*     */     public String toString() {
/*  95 */       StringBuilder sb = new StringBuilder();
/*  96 */       Class cls = (this._from instanceof Class) ? (Class)this._from : this._from.getClass();
/*     */ 
/* 102 */       Package pkg = cls.getPackage();
/* 103 */       if (pkg != null) {
/* 104 */         sb.append(pkg.getName());
/* 105 */         sb.append('.');
/*     */       }
/* 107 */       sb.append(cls.getSimpleName());
/* 108 */       sb.append('[');
/* 109 */       if (this._fieldName != null) {
/* 110 */         sb.append('"');
/* 111 */         sb.append(this._fieldName);
/* 112 */         sb.append('"');
/* 113 */       } else if (this._index >= 0) {
/* 114 */         sb.append(this._index);
/*     */       } else {
/* 116 */         sb.append('?');
/*     */       }
/* 118 */       sb.append(']');
/* 119 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.JsonMappingException
 * JD-Core Version:    0.6.0
 */