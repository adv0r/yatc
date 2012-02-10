/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ 
/*     */ public class ObjectNode extends ContainerNode
/*     */ {
/*  16 */   protected LinkedHashMap<String, JsonNode> _children = null;
/*     */ 
/*  18 */   public ObjectNode(JsonNodeFactory nc) { super(nc);
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  26 */     return JsonToken.START_OBJECT;
/*     */   }
/*     */   public boolean isObject() {
/*  29 */     return true;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  33 */     return this._children == null ? 0 : this._children.size();
/*     */   }
/*     */ 
/*     */   public Iterator<JsonNode> getElements()
/*     */   {
/*  39 */     return this._children == null ? ContainerNode.NoNodesIterator.instance() : this._children.values().iterator();
/*     */   }
/*     */ 
/*     */   public JsonNode get(int index) {
/*  43 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonNode get(String fieldName)
/*     */   {
/*  48 */     if (this._children != null) {
/*  49 */       return (JsonNode)this._children.get(fieldName);
/*     */     }
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator<String> getFieldNames()
/*     */   {
/*  57 */     return this._children == null ? ContainerNode.NoStringsIterator.instance() : this._children.keySet().iterator();
/*     */   }
/*     */ 
/*     */   public JsonNode path(int index)
/*     */   {
/*  63 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */   public JsonNode path(String fieldName)
/*     */   {
/*  69 */     if (this._children != null) {
/*  70 */       JsonNode n = (JsonNode)this._children.get(fieldName);
/*  71 */       if (n != null) {
/*  72 */         return n;
/*     */       }
/*     */     }
/*  75 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */   public JsonNode findValue(String fieldName)
/*     */   {
/*  87 */     if (this._children != null) {
/*  88 */       for (Map.Entry entry : this._children.entrySet()) {
/*  89 */         if (fieldName.equals(entry.getKey())) {
/*  90 */           return (JsonNode)entry.getValue();
/*     */         }
/*  92 */         JsonNode value = ((JsonNode)entry.getValue()).findValue(fieldName);
/*  93 */         if (value != null) {
/*  94 */           return value;
/*     */         }
/*     */       }
/*     */     }
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 104 */     if (this._children != null) {
/* 105 */       for (Map.Entry entry : this._children.entrySet()) {
/* 106 */         if (fieldName.equals(entry.getKey())) {
/* 107 */           if (foundSoFar == null) {
/* 108 */             foundSoFar = new ArrayList();
/*     */           }
/* 110 */           foundSoFar.add(entry.getValue());
/*     */         } else {
/* 112 */           foundSoFar = ((JsonNode)entry.getValue()).findValues(fieldName, foundSoFar);
/*     */         }
/*     */       }
/*     */     }
/* 116 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 122 */     if (this._children != null) {
/* 123 */       for (Map.Entry entry : this._children.entrySet()) {
/* 124 */         if (fieldName.equals(entry.getKey())) {
/* 125 */           if (foundSoFar == null) {
/* 126 */             foundSoFar = new ArrayList();
/*     */           }
/* 128 */           foundSoFar.add(((JsonNode)entry.getValue()).getValueAsText());
/*     */         } else {
/* 130 */           foundSoFar = ((JsonNode)entry.getValue()).findValuesAsText(fieldName, foundSoFar);
/*     */         }
/*     */       }
/*     */     }
/* 134 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/* 140 */     if (this._children != null) {
/* 141 */       for (Map.Entry entry : this._children.entrySet()) {
/* 142 */         if (fieldName.equals(entry.getKey())) {
/* 143 */           return this;
/*     */         }
/* 145 */         JsonNode value = ((JsonNode)entry.getValue()).findParent(fieldName);
/* 146 */         if (value != null) {
/* 147 */           return (ObjectNode)value;
/*     */         }
/*     */       }
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 157 */     if (this._children != null) {
/* 158 */       for (Map.Entry entry : this._children.entrySet()) {
/* 159 */         if (fieldName.equals(entry.getKey())) {
/* 160 */           if (foundSoFar == null) {
/* 161 */             foundSoFar = new ArrayList();
/*     */           }
/* 163 */           foundSoFar.add(this);
/*     */         } else {
/* 165 */           foundSoFar = ((JsonNode)entry.getValue()).findParents(fieldName, foundSoFar);
/*     */         }
/*     */       }
/*     */     }
/* 169 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 186 */     jg.writeStartObject();
/* 187 */     if (this._children != null) {
/* 188 */       for (Map.Entry en : this._children.entrySet()) {
/* 189 */         jg.writeFieldName((String)en.getKey());
/*     */ 
/* 195 */         ((BaseJsonNode)en.getValue()).serialize(jg, provider);
/*     */       }
/*     */     }
/* 198 */     jg.writeEndObject();
/*     */   }
/*     */ 
/*     */   public Iterator<Map.Entry<String, JsonNode>> getFields()
/*     */   {
/* 213 */     if (this._children == null) {
/* 214 */       return NoFieldsIterator.instance;
/*     */     }
/* 216 */     return this._children.entrySet().iterator();
/*     */   }
/*     */ 
/*     */   public JsonNode put(String fieldName, JsonNode value)
/*     */   {
/* 238 */     if (value == null) {
/* 239 */       value = nullNode();
/*     */     }
/* 241 */     return _put(fieldName, value);
/*     */   }
/*     */ 
/*     */   public JsonNode remove(String fieldName)
/*     */   {
/* 251 */     if (this._children != null) {
/* 252 */       return (JsonNode)this._children.remove(fieldName);
/*     */     }
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   public ObjectNode remove(Collection<String> fieldNames)
/*     */   {
/* 269 */     if (this._children != null) {
/* 270 */       for (String fieldName : fieldNames) {
/* 271 */         this._children.remove(fieldName);
/*     */       }
/*     */     }
/* 274 */     return this;
/*     */   }
/*     */ 
/*     */   public ObjectNode removeAll()
/*     */   {
/* 284 */     this._children = null;
/* 285 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonNode putAll(Map<String, JsonNode> properties)
/*     */   {
/* 300 */     if (this._children == null)
/* 301 */       this._children = new LinkedHashMap(properties);
/*     */     else {
/* 303 */       for (Map.Entry en : properties.entrySet()) {
/* 304 */         JsonNode n = (JsonNode)en.getValue();
/* 305 */         if (n == null) {
/* 306 */           n = nullNode();
/*     */         }
/* 308 */         this._children.put(en.getKey(), n);
/*     */       }
/*     */     }
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonNode putAll(ObjectNode other)
/*     */   {
/* 326 */     int len = other.size();
/* 327 */     if (len > 0) {
/* 328 */       if (this._children == null) {
/* 329 */         this._children = new LinkedHashMap(len);
/*     */       }
/* 331 */       other.putContentsTo(this._children);
/*     */     }
/* 333 */     return this;
/*     */   }
/*     */ 
/*     */   public ObjectNode retain(Collection<String> fieldNames)
/*     */   {
/* 348 */     if (this._children != null) {
/* 349 */       Iterator entries = this._children.entrySet().iterator();
/* 350 */       while (entries.hasNext()) {
/* 351 */         Map.Entry entry = (Map.Entry)entries.next();
/* 352 */         if (!fieldNames.contains(entry.getKey())) {
/* 353 */           entries.remove();
/*     */         }
/*     */       }
/*     */     }
/* 357 */     return this;
/*     */   }
/*     */ 
/*     */   public ObjectNode retain(String[] fieldNames)
/*     */   {
/* 371 */     return retain(Arrays.asList(fieldNames));
/*     */   }
/*     */ 
/*     */   public ArrayNode putArray(String fieldName)
/*     */   {
/* 389 */     ArrayNode n = arrayNode();
/* 390 */     _put(fieldName, n);
/* 391 */     return n;
/*     */   }
/*     */ 
/*     */   public ObjectNode putObject(String fieldName)
/*     */   {
/* 403 */     ObjectNode n = objectNode();
/* 404 */     _put(fieldName, n);
/* 405 */     return n;
/*     */   }
/*     */ 
/*     */   public void putPOJO(String fieldName, Object pojo)
/*     */   {
/* 410 */     _put(fieldName, POJONode(pojo));
/*     */   }
/*     */ 
/*     */   public void putNull(String fieldName)
/*     */   {
/* 415 */     _put(fieldName, nullNode());
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, int v)
/*     */   {
/* 421 */     _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, long v)
/*     */   {
/* 426 */     _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, float v)
/*     */   {
/* 431 */     _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, double v)
/*     */   {
/* 436 */     _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, BigDecimal v)
/*     */   {
/* 442 */     if (v == null)
/* 443 */       putNull(fieldName);
/*     */     else
/* 445 */       _put(fieldName, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, String v)
/*     */   {
/* 453 */     if (v == null)
/* 454 */       putNull(fieldName);
/*     */     else
/* 456 */       _put(fieldName, textNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, boolean v)
/*     */   {
/* 463 */     _put(fieldName, booleanNode(v));
/*     */   }
/*     */ 
/*     */   public void put(String fieldName, byte[] v)
/*     */   {
/* 469 */     if (v == null)
/* 470 */       putNull(fieldName);
/*     */     else
/* 472 */       _put(fieldName, binaryNode(v));
/*     */   }
/*     */ 
/*     */   protected void putContentsTo(Map<String, JsonNode> dst)
/*     */   {
/* 487 */     if (this._children != null)
/* 488 */       for (Map.Entry en : this._children.entrySet())
/* 489 */         dst.put(en.getKey(), en.getValue());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 503 */     if (o == this) return true;
/* 504 */     if (o == null) return false;
/* 505 */     if (o.getClass() != getClass()) {
/* 506 */       return false;
/*     */     }
/* 508 */     ObjectNode other = (ObjectNode)o;
/* 509 */     if (other.size() != size()) {
/* 510 */       return false;
/*     */     }
/* 512 */     if (this._children != null) {
/* 513 */       for (Map.Entry en : this._children.entrySet()) {
/* 514 */         String key = (String)en.getKey();
/* 515 */         JsonNode value = (JsonNode)en.getValue();
/*     */ 
/* 517 */         JsonNode otherValue = other.get(key);
/*     */ 
/* 519 */         if ((otherValue == null) || (!otherValue.equals(value))) {
/* 520 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 524 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 530 */     return this._children == null ? -1 : this._children.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 536 */     StringBuilder sb = new StringBuilder(32 + (size() << 4));
/* 537 */     sb.append("{");
/*     */     int count;
/* 538 */     if (this._children != null) {
/* 539 */       count = 0;
/* 540 */       for (Map.Entry en : this._children.entrySet()) {
/* 541 */         if (count > 0) {
/* 542 */           sb.append(",");
/*     */         }
/* 544 */         count++;
/* 545 */         TextNode.appendQuoted(sb, (String)en.getKey());
/* 546 */         sb.append(':');
/* 547 */         sb.append(((JsonNode)en.getValue()).toString());
/*     */       }
/*     */     }
/* 550 */     sb.append("}");
/* 551 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private final JsonNode _put(String fieldName, JsonNode value)
/*     */   {
/* 562 */     if (this._children == null) {
/* 563 */       this._children = new LinkedHashMap();
/*     */     }
/* 565 */     return (JsonNode)this._children.put(fieldName, value);
/*     */   }
/*     */ 
/*     */   protected static class NoFieldsIterator
/*     */     implements Iterator<Map.Entry<String, JsonNode>>
/*     */   {
/* 580 */     static final NoFieldsIterator instance = new NoFieldsIterator();
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 584 */       return false; } 
/* 585 */     public Map.Entry<String, JsonNode> next() { throw new NoSuchElementException(); }
/*     */ 
/*     */     public void remove() {
/* 588 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.ObjectNode
 * JD-Core Version:    0.6.0
 */