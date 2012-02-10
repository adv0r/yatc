/*     */ package org.codehaus.jackson;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.codehaus.jackson.annotate.JsonCreator;
/*     */ import org.codehaus.jackson.annotate.JsonProperty;
/*     */ 
/*     */ public class JsonLocation
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  22 */   public static final JsonLocation NA = new JsonLocation("N/A", -1L, -1L, -1, -1);
/*     */   final long _totalBytes;
/*     */   final long _totalChars;
/*     */   final int _lineNr;
/*     */   final int _columnNr;
/*     */   final Object _sourceRef;
/*     */ 
/*     */   public JsonLocation(Object srcRef, long totalChars, int lineNr, int colNr)
/*     */   {
/*  42 */     this(srcRef, -1L, totalChars, lineNr, colNr);
/*     */   }
/*     */ 
/*     */   @JsonCreator
/*     */   public JsonLocation(@JsonProperty("sourceRef") Object sourceRef, @JsonProperty("byteOffset") long totalBytes, @JsonProperty("charOffset") long totalChars, @JsonProperty("lineNr") int lineNr, @JsonProperty("columnNr") int columnNr)
/*     */   {
/*  52 */     this._sourceRef = sourceRef;
/*  53 */     this._totalBytes = totalBytes;
/*  54 */     this._totalChars = totalChars;
/*  55 */     this._lineNr = lineNr;
/*  56 */     this._columnNr = columnNr;
/*     */   }
/*     */ 
/*     */   public Object getSourceRef()
/*     */   {
/*  67 */     return this._sourceRef;
/*     */   }
/*     */ 
/*     */   public int getLineNr()
/*     */   {
/*  72 */     return this._lineNr;
/*     */   }
/*     */ 
/*     */   public int getColumnNr()
/*     */   {
/*  77 */     return this._columnNr;
/*     */   }
/*     */ 
/*     */   public long getCharOffset()
/*     */   {
/*  83 */     return this._totalChars;
/*     */   }
/*     */ 
/*     */   public long getByteOffset()
/*     */   {
/*  91 */     return this._totalBytes;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     StringBuilder sb = new StringBuilder(80);
/*  98 */     sb.append("[Source: ");
/*  99 */     if (this._sourceRef == null)
/* 100 */       sb.append("UNKNOWN");
/*     */     else {
/* 102 */       sb.append(this._sourceRef.toString());
/*     */     }
/* 104 */     sb.append("; line: ");
/* 105 */     sb.append(this._lineNr);
/* 106 */     sb.append(", column: ");
/* 107 */     sb.append(this._columnNr);
/* 108 */     sb.append(']');
/* 109 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 115 */     int hash = this._sourceRef == null ? 1 : this._sourceRef.hashCode();
/* 116 */     hash ^= this._lineNr;
/* 117 */     hash += this._columnNr;
/* 118 */     hash ^= (int)this._totalChars;
/* 119 */     hash += (int)this._totalBytes;
/* 120 */     return hash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 126 */     if (other == this) return true;
/* 127 */     if (other == null) return false;
/* 128 */     if (!(other instanceof JsonLocation)) return false;
/* 129 */     JsonLocation otherLoc = (JsonLocation)other;
/*     */ 
/* 131 */     if (this._sourceRef == null) {
/* 132 */       if (otherLoc._sourceRef != null) return false; 
/*     */     }
/* 133 */     else if (!this._sourceRef.equals(otherLoc._sourceRef)) return false;
/*     */ 
/* 135 */     return (this._lineNr == otherLoc._lineNr) && (this._columnNr == otherLoc._columnNr) && (this._totalChars == otherLoc._totalChars) && (getByteOffset() == otherLoc.getByteOffset());
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonLocation
 * JD-Core Version:    0.6.0
 */