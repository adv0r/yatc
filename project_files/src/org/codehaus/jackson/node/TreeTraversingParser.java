/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.JsonLocation;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.ObjectCodec;
/*     */ import org.codehaus.jackson.impl.JsonParserMinimalBase;
/*     */ 
/*     */ public class TreeTraversingParser extends JsonParserMinimalBase
/*     */ {
/*     */   protected ObjectCodec _objectCodec;
/*     */   protected NodeCursor _nodeCursor;
/*     */   protected JsonToken _nextToken;
/*     */   protected boolean _startContainer;
/*     */   protected boolean _closed;
/*     */ 
/*     */   public TreeTraversingParser(JsonNode n)
/*     */   {
/*  66 */     this(n, null);
/*     */   }
/*     */ 
/*     */   public TreeTraversingParser(JsonNode n, ObjectCodec codec) {
/*  70 */     super(0);
/*  71 */     this._objectCodec = codec;
/*  72 */     if (n.isArray()) {
/*  73 */       this._nextToken = JsonToken.START_ARRAY;
/*  74 */       this._nodeCursor = new NodeCursor.Array(n, null);
/*  75 */     } else if (n.isObject()) {
/*  76 */       this._nextToken = JsonToken.START_OBJECT;
/*  77 */       this._nodeCursor = new NodeCursor.Object(n, null);
/*     */     } else {
/*  79 */       this._nodeCursor = new NodeCursor.RootValue(n, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setCodec(ObjectCodec c)
/*     */   {
/*  85 */     this._objectCodec = c;
/*     */   }
/*     */ 
/*     */   public ObjectCodec getCodec()
/*     */   {
/*  90 */     return this._objectCodec;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 102 */     if (!this._closed) {
/* 103 */       this._closed = true;
/* 104 */       this._nodeCursor = null;
/* 105 */       this._currToken = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public JsonToken nextToken()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 118 */     if (this._nextToken != null) {
/* 119 */       this._currToken = this._nextToken;
/* 120 */       this._nextToken = null;
/* 121 */       return this._currToken;
/*     */     }
/*     */ 
/* 124 */     if (this._startContainer) {
/* 125 */       this._startContainer = false;
/*     */ 
/* 127 */       if (!this._nodeCursor.currentHasChildren()) {
/* 128 */         this._currToken = (this._currToken == JsonToken.START_OBJECT ? JsonToken.END_OBJECT : JsonToken.END_ARRAY);
/*     */ 
/* 130 */         return this._currToken;
/*     */       }
/* 132 */       this._nodeCursor = this._nodeCursor.iterateChildren();
/* 133 */       this._currToken = this._nodeCursor.nextToken();
/* 134 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 135 */         this._startContainer = true;
/*     */       }
/* 137 */       return this._currToken;
/*     */     }
/*     */ 
/* 140 */     if (this._nodeCursor == null) {
/* 141 */       this._closed = true;
/* 142 */       return null;
/*     */     }
/*     */ 
/* 145 */     this._currToken = this._nodeCursor.nextToken();
/* 146 */     if (this._currToken != null) {
/* 147 */       if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 148 */         this._startContainer = true;
/*     */       }
/* 150 */       return this._currToken;
/*     */     }
/*     */ 
/* 153 */     this._currToken = this._nodeCursor.endToken();
/* 154 */     this._nodeCursor = this._nodeCursor.getParent();
/* 155 */     return this._currToken;
/*     */   }
/*     */ 
/*     */   public JsonParser skipChildren()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 164 */     if (this._currToken == JsonToken.START_OBJECT) {
/* 165 */       this._startContainer = false;
/* 166 */       this._currToken = JsonToken.END_OBJECT;
/* 167 */     } else if (this._currToken == JsonToken.START_ARRAY) {
/* 168 */       this._startContainer = false;
/* 169 */       this._currToken = JsonToken.END_ARRAY;
/*     */     }
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 176 */     return this._closed;
/*     */   }
/*     */ 
/*     */   public String getCurrentName()
/*     */   {
/* 187 */     return this._nodeCursor == null ? null : this._nodeCursor.getCurrentName();
/*     */   }
/*     */ 
/*     */   public JsonStreamContext getParsingContext()
/*     */   {
/* 192 */     return this._nodeCursor;
/*     */   }
/*     */ 
/*     */   public JsonLocation getTokenLocation()
/*     */   {
/* 197 */     return JsonLocation.NA;
/*     */   }
/*     */ 
/*     */   public JsonLocation getCurrentLocation()
/*     */   {
/* 202 */     return JsonLocation.NA;
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 214 */     if (this._closed) {
/* 215 */       return null;
/*     */     }
/*     */ 
/* 218 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*     */     case 1:
/* 220 */       return this._nodeCursor.getCurrentName();
/*     */     case 2:
/* 222 */       return currentNode().getTextValue();
/*     */     case 3:
/*     */     case 4:
/* 225 */       return String.valueOf(currentNode().getNumberValue());
/*     */     case 5:
/* 227 */       JsonNode n = currentNode();
/* 228 */       if ((n == null) || (!n.isBinary()))
/*     */         break;
/* 230 */       return n.getValueAsText();
/*     */     }
/*     */ 
/* 234 */     return this._currToken == null ? null : this._currToken.asString();
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters() throws IOException, JsonParseException
/*     */   {
/* 239 */     return getText().toCharArray();
/*     */   }
/*     */ 
/*     */   public int getTextLength() throws IOException, JsonParseException
/*     */   {
/* 244 */     return getText().length();
/*     */   }
/*     */ 
/*     */   public int getTextOffset() throws IOException, JsonParseException
/*     */   {
/* 249 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean hasTextCharacters()
/*     */   {
/* 255 */     return false;
/*     */   }
/*     */ 
/*     */   public JsonParser.NumberType getNumberType()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 268 */     JsonNode n = currentNumericNode();
/* 269 */     return n == null ? null : n.getNumberType();
/*     */   }
/*     */ 
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 275 */     return currentNumericNode().getBigIntegerValue();
/*     */   }
/*     */ 
/*     */   public BigDecimal getDecimalValue() throws IOException, JsonParseException
/*     */   {
/* 280 */     return currentNumericNode().getDecimalValue();
/*     */   }
/*     */ 
/*     */   public double getDoubleValue() throws IOException, JsonParseException
/*     */   {
/* 285 */     return currentNumericNode().getDoubleValue();
/*     */   }
/*     */ 
/*     */   public float getFloatValue() throws IOException, JsonParseException
/*     */   {
/* 290 */     return (float)currentNumericNode().getDoubleValue();
/*     */   }
/*     */ 
/*     */   public long getLongValue() throws IOException, JsonParseException
/*     */   {
/* 295 */     return currentNumericNode().getLongValue();
/*     */   }
/*     */ 
/*     */   public int getIntValue() throws IOException, JsonParseException
/*     */   {
/* 300 */     return currentNumericNode().getIntValue();
/*     */   }
/*     */ 
/*     */   public Number getNumberValue() throws IOException, JsonParseException
/*     */   {
/* 305 */     return currentNumericNode().getNumberValue();
/*     */   }
/*     */ 
/*     */   public Object getEmbeddedObject()
/*     */   {
/* 310 */     if (!this._closed) {
/* 311 */       JsonNode n = currentNode();
/* 312 */       if ((n != null) && (n.isPojo())) {
/* 313 */         return ((POJONode)n).getPojo();
/*     */       }
/*     */     }
/* 316 */     return null;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue(Base64Variant b64variant)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 330 */     JsonNode n = currentNode();
/* 331 */     if (n != null) {
/* 332 */       byte[] data = n.getBinaryValue();
/*     */ 
/* 334 */       if (data != null) {
/* 335 */         return data;
/*     */       }
/*     */ 
/* 338 */       if (n.isPojo()) {
/* 339 */         Object ob = ((POJONode)n).getPojo();
/* 340 */         if ((ob instanceof byte[])) {
/* 341 */           return (byte[])(byte[])ob;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */   protected JsonNode currentNode()
/*     */   {
/* 356 */     if ((this._closed) || (this._nodeCursor == null)) {
/* 357 */       return null;
/*     */     }
/* 359 */     return this._nodeCursor.currentNode();
/*     */   }
/*     */ 
/*     */   protected JsonNode currentNumericNode()
/*     */     throws JsonParseException
/*     */   {
/* 365 */     JsonNode n = currentNode();
/* 366 */     if ((n == null) || (!n.isNumber())) {
/* 367 */       JsonToken t = n == null ? null : n.asToken();
/* 368 */       throw _constructError("Current token (" + t + ") not numeric, can not use numeric value accessors");
/*     */     }
/* 370 */     return n;
/*     */   }
/*     */ 
/*     */   protected void _handleEOF() throws JsonParseException
/*     */   {
/* 375 */     _throwInternal();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.TreeTraversingParser
 * JD-Core Version:    0.6.0
 */