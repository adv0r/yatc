/*     */ package org.codehaus.jackson;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class JsonNode
/*     */   implements Iterable<JsonNode>
/*     */ {
/*  30 */   protected static final List<JsonNode> NO_NODES = Collections.emptyList();
/*  31 */   protected static final List<String> NO_STRINGS = Collections.emptyList();
/*     */ 
/*     */   public boolean isValueNode()
/*     */   {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isContainerNode()
/*     */   {
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isMissingNode()
/*     */   {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isArray()
/*     */   {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isObject()
/*     */   {
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isPojo()
/*     */   {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isNumber()
/*     */   {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isIntegralNumber()
/*     */   {
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isFloatingPointNumber()
/*     */   {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isInt()
/*     */   {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isLong()
/*     */   {
/* 127 */     return false;
/*     */   }
/* 129 */   public boolean isDouble() { return false; } 
/* 130 */   public boolean isBigDecimal() { return false; } 
/* 131 */   public boolean isBigInteger() { return false; } 
/*     */   public boolean isTextual() {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isBoolean()
/*     */   {
/* 139 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isNull()
/*     */   {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isBinary()
/*     */   {
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract JsonToken asToken();
/*     */ 
/*     */   public abstract JsonParser.NumberType getNumberType();
/*     */ 
/*     */   public String getTextValue()
/*     */   {
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue()
/*     */     throws IOException
/*     */   {
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getBooleanValue()
/*     */   {
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */   public Number getNumberValue()
/*     */   {
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/* 238 */     return 0;
/*     */   }
/* 240 */   public long getLongValue() { return 0L; } 
/* 241 */   public double getDoubleValue() { return 0.0D; } 
/* 242 */   public BigDecimal getDecimalValue() { return BigDecimal.ZERO; } 
/* 243 */   public BigInteger getBigIntegerValue() { return BigInteger.ZERO;
/*     */   }
/*     */ 
/*     */   public JsonNode get(int index)
/*     */   {
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonNode get(String fieldName)
/*     */   {
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract String getValueAsText();
/*     */ 
/*     */   public int getValueAsInt()
/*     */   {
/* 304 */     return getValueAsInt(0);
/*     */   }
/*     */ 
/*     */   public int getValueAsInt(int defaultValue)
/*     */   {
/* 320 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public long getValueAsLong()
/*     */   {
/* 336 */     return getValueAsInt(0);
/*     */   }
/*     */ 
/*     */   public long getValueAsLong(long defaultValue)
/*     */   {
/* 352 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public double getValueAsDouble()
/*     */   {
/* 368 */     return getValueAsDouble(0.0D);
/*     */   }
/*     */ 
/*     */   public double getValueAsDouble(double defaultValue)
/*     */   {
/* 384 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public boolean getValueAsBoolean()
/*     */   {
/* 400 */     return getValueAsBoolean(false);
/*     */   }
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */   {
/* 416 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public boolean has(String fieldName)
/*     */   {
/* 445 */     return get(fieldName) != null;
/*     */   }
/*     */ 
/*     */   public boolean has(int index)
/*     */   {
/* 470 */     return get(index) != null;
/*     */   }
/*     */ 
/*     */   public abstract JsonNode findValue(String paramString);
/*     */ 
/*     */   public final List<JsonNode> findValues(String fieldName)
/*     */   {
/* 499 */     List result = findValues(fieldName, null);
/* 500 */     if (result == null) {
/* 501 */       return Collections.emptyList();
/*     */     }
/* 503 */     return result;
/*     */   }
/*     */ 
/*     */   public final List<String> findValuesAsText(String fieldName)
/*     */   {
/* 514 */     List result = findValuesAsText(fieldName, null);
/* 515 */     if (result == null) {
/* 516 */       return Collections.emptyList();
/*     */     }
/* 518 */     return result;
/*     */   }
/*     */ 
/*     */   public abstract JsonNode findPath(String paramString);
/*     */ 
/*     */   public abstract JsonNode findParent(String paramString);
/*     */ 
/*     */   public final List<JsonNode> findParents(String fieldName)
/*     */   {
/* 563 */     List result = findParents(fieldName, null);
/* 564 */     if (result == null) {
/* 565 */       return Collections.emptyList();
/*     */     }
/* 567 */     return result;
/*     */   }
/*     */ 
/*     */   public abstract List<JsonNode> findValues(String paramString, List<JsonNode> paramList);
/*     */ 
/*     */   public abstract List<String> findValuesAsText(String paramString, List<String> paramList);
/*     */ 
/*     */   public abstract List<JsonNode> findParents(String paramString, List<JsonNode> paramList);
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonNode getFieldValue(String fieldName)
/*     */   {
/* 586 */     return get(fieldName);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonNode getElementValue(int index)
/*     */   {
/* 594 */     return get(index);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 610 */     return 0;
/*     */   }
/*     */ 
/*     */   public final Iterator<JsonNode> iterator()
/*     */   {
/* 617 */     return getElements();
/*     */   }
/*     */ 
/*     */   public Iterator<JsonNode> getElements()
/*     */   {
/* 625 */     return NO_NODES.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<String> getFieldNames()
/*     */   {
/* 631 */     return NO_STRINGS.iterator();
/*     */   }
/*     */ 
/*     */   public abstract JsonNode path(String paramString);
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonNode getPath(String fieldName)
/*     */   {
/* 656 */     return path(fieldName);
/*     */   }
/*     */ 
/*     */   public abstract JsonNode path(int paramInt);
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonNode getPath(int index)
/*     */   {
/* 674 */     return path(index);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public abstract void writeTo(JsonGenerator paramJsonGenerator)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract JsonParser traverse();
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonNode
 * JD-Core Version:    0.6.0
 */