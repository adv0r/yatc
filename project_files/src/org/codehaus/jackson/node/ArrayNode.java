/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ 
/*     */ public final class ArrayNode extends ContainerNode
/*     */ {
/*     */   protected ArrayList<JsonNode> _children;
/*     */ 
/*     */   public ArrayNode(JsonNodeFactory nc)
/*     */   {
/*  18 */     super(nc);
/*     */   }
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  26 */     return JsonToken.START_ARRAY;
/*     */   }
/*     */   public boolean isArray() {
/*  29 */     return true;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  34 */     return this._children == null ? 0 : this._children.size();
/*     */   }
/*     */ 
/*     */   public Iterator<JsonNode> getElements()
/*     */   {
/*  40 */     return this._children == null ? ContainerNode.NoNodesIterator.instance() : this._children.iterator();
/*     */   }
/*     */ 
/*     */   public JsonNode get(int index)
/*     */   {
/*  46 */     if ((index >= 0) && (this._children != null) && (index < this._children.size())) {
/*  47 */       return (JsonNode)this._children.get(index);
/*     */     }
/*  49 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonNode get(String fieldName) {
/*  53 */     return null;
/*     */   }
/*     */   public JsonNode path(String fieldName) {
/*  56 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */   public JsonNode path(int index)
/*     */   {
/*  61 */     if ((index >= 0) && (this._children != null) && (index < this._children.size())) {
/*  62 */       return (JsonNode)this._children.get(index);
/*     */     }
/*  64 */     return MissingNode.getInstance();
/*     */   }
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  77 */     jg.writeStartArray();
/*  78 */     if (this._children != null) {
/*  79 */       for (JsonNode n : this._children)
/*     */       {
/*  85 */         ((BaseJsonNode)n).writeTo(jg);
/*     */       }
/*     */     }
/*  88 */     jg.writeEndArray();
/*     */   }
/*     */ 
/*     */   public JsonNode findValue(String fieldName)
/*     */   {
/* 100 */     if (this._children != null) {
/* 101 */       for (JsonNode node : this._children) {
/* 102 */         JsonNode value = node.findValue(fieldName);
/* 103 */         if (value != null) {
/* 104 */           return value;
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 114 */     if (this._children != null) {
/* 115 */       for (JsonNode node : this._children) {
/* 116 */         foundSoFar = node.findValues(fieldName, foundSoFar);
/*     */       }
/*     */     }
/* 119 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 125 */     if (this._children != null) {
/* 126 */       for (JsonNode node : this._children) {
/* 127 */         foundSoFar = node.findValuesAsText(fieldName, foundSoFar);
/*     */       }
/*     */     }
/* 130 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public ObjectNode findParent(String fieldName)
/*     */   {
/* 136 */     if (this._children != null) {
/* 137 */       for (JsonNode node : this._children) {
/* 138 */         JsonNode parent = node.findParent(fieldName);
/* 139 */         if (parent != null) {
/* 140 */           return (ObjectNode)parent;
/*     */         }
/*     */       }
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 150 */     if (this._children != null) {
/* 151 */       for (JsonNode node : this._children) {
/* 152 */         foundSoFar = node.findParents(fieldName, foundSoFar);
/*     */       }
/*     */     }
/* 155 */     return foundSoFar;
/*     */   }
/*     */ 
/*     */   public JsonNode set(int index, JsonNode value)
/*     */   {
/* 177 */     if (value == null) {
/* 178 */       value = nullNode();
/*     */     }
/* 180 */     return _set(index, value);
/*     */   }
/*     */ 
/*     */   public void add(JsonNode value)
/*     */   {
/* 185 */     if (value == null) {
/* 186 */       value = nullNode();
/*     */     }
/* 188 */     _add(value);
/*     */   }
/*     */ 
/*     */   public JsonNode addAll(ArrayNode other)
/*     */   {
/* 203 */     int len = other.size();
/* 204 */     if (len > 0) {
/* 205 */       if (this._children == null) {
/* 206 */         this._children = new ArrayList(len + 2);
/*     */       }
/* 208 */       other.addContentsTo(this._children);
/*     */     }
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonNode addAll(Collection<JsonNode> nodes)
/*     */   {
/* 224 */     int len = nodes.size();
/* 225 */     if (len > 0) {
/* 226 */       if (this._children == null)
/* 227 */         this._children = new ArrayList(nodes);
/*     */       else {
/* 229 */         this._children.addAll(nodes);
/*     */       }
/*     */     }
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */   public void insert(int index, JsonNode value)
/*     */   {
/* 244 */     if (value == null) {
/* 245 */       value = nullNode();
/*     */     }
/* 247 */     _insert(index, value);
/*     */   }
/*     */ 
/*     */   public JsonNode remove(int index)
/*     */   {
/* 257 */     if ((index >= 0) && (this._children != null) && (index < this._children.size())) {
/* 258 */       return (JsonNode)this._children.remove(index);
/*     */     }
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */   public ArrayNode removeAll()
/*     */   {
/* 266 */     this._children = null;
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   public ArrayNode addArray()
/*     */   {
/* 284 */     ArrayNode n = arrayNode();
/* 285 */     _add(n);
/* 286 */     return n;
/*     */   }
/*     */ 
/*     */   public ObjectNode addObject()
/*     */   {
/* 297 */     ObjectNode n = objectNode();
/* 298 */     _add(n);
/* 299 */     return n;
/*     */   }
/*     */ 
/*     */   public void addPOJO(Object value)
/*     */   {
/* 308 */     if (value == null)
/* 309 */       addNull();
/*     */     else
/* 311 */       _add(POJONode(value));
/*     */   }
/*     */ 
/*     */   public void addNull()
/*     */   {
/* 317 */     _add(nullNode());
/*     */   }
/*     */ 
/*     */   public void add(int v)
/*     */   {
/* 323 */     _add(numberNode(v));
/*     */   }
/*     */ 
/*     */   public void add(long v)
/*     */   {
/* 328 */     _add(numberNode(v));
/*     */   }
/*     */ 
/*     */   public void add(float v)
/*     */   {
/* 333 */     _add(numberNode(v));
/*     */   }
/*     */ 
/*     */   public void add(double v)
/*     */   {
/* 338 */     _add(numberNode(v));
/*     */   }
/*     */ 
/*     */   public void add(BigDecimal v)
/*     */   {
/* 344 */     if (v == null)
/* 345 */       addNull();
/*     */     else
/* 347 */       _add(numberNode(v));
/*     */   }
/*     */ 
/*     */   public void add(String v)
/*     */   {
/* 355 */     if (v == null)
/* 356 */       addNull();
/*     */     else
/* 358 */       _add(textNode(v));
/*     */   }
/*     */ 
/*     */   public void add(boolean v)
/*     */   {
/* 365 */     _add(booleanNode(v));
/*     */   }
/*     */ 
/*     */   public void add(byte[] v)
/*     */   {
/* 371 */     if (v == null)
/* 372 */       addNull();
/*     */     else
/* 374 */       _add(binaryNode(v));
/*     */   }
/*     */ 
/*     */   public ArrayNode insertArray(int index)
/*     */   {
/* 380 */     ArrayNode n = arrayNode();
/* 381 */     _insert(index, n);
/* 382 */     return n;
/*     */   }
/*     */ 
/*     */   public ObjectNode insertObject(int index)
/*     */   {
/* 393 */     ObjectNode n = objectNode();
/* 394 */     _insert(index, n);
/* 395 */     return n;
/*     */   }
/*     */ 
/*     */   public void insertPOJO(int index, Object value)
/*     */   {
/* 404 */     if (value == null)
/* 405 */       insertNull(index);
/*     */     else
/* 407 */       _insert(index, POJONode(value));
/*     */   }
/*     */ 
/*     */   public void insertNull(int index)
/*     */   {
/* 413 */     _insert(index, nullNode());
/*     */   }
/*     */ 
/*     */   public void insert(int index, int v)
/*     */   {
/* 419 */     _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, long v)
/*     */   {
/* 424 */     _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, float v)
/*     */   {
/* 429 */     _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, double v)
/*     */   {
/* 434 */     _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, BigDecimal v)
/*     */   {
/* 440 */     if (v == null)
/* 441 */       insertNull(index);
/*     */     else
/* 443 */       _insert(index, numberNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, String v)
/*     */   {
/* 451 */     if (v == null)
/* 452 */       insertNull(index);
/*     */     else
/* 454 */       _insert(index, textNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, boolean v)
/*     */   {
/* 461 */     _insert(index, booleanNode(v));
/*     */   }
/*     */ 
/*     */   public void insert(int index, byte[] v)
/*     */   {
/* 467 */     if (v == null)
/* 468 */       insertNull(index);
/*     */     else
/* 470 */       _insert(index, binaryNode(v));
/*     */   }
/*     */ 
/*     */   protected void addContentsTo(List<JsonNode> dst)
/*     */   {
/* 485 */     if (this._children != null)
/* 486 */       for (JsonNode n : this._children)
/* 487 */         dst.add(n);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 501 */     if (o == this) return true;
/* 502 */     if (o == null) return false;
/* 503 */     if (o.getClass() != getClass()) {
/* 504 */       return false;
/*     */     }
/* 506 */     ArrayNode other = (ArrayNode)o;
/* 507 */     if ((this._children == null) || (this._children.size() == 0)) {
/* 508 */       return other.size() == 0;
/*     */     }
/* 510 */     return other._sameChildren(this._children);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*     */     int hash;
/*     */     int hash;
/* 517 */     if (this._children == null) {
/* 518 */       hash = 1;
/*     */     } else {
/* 520 */       hash = this._children.size();
/* 521 */       for (JsonNode n : this._children) {
/* 522 */         if (n != null) {
/* 523 */           hash ^= n.hashCode();
/*     */         }
/*     */       }
/*     */     }
/* 527 */     return hash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 534 */     StringBuilder sb = new StringBuilder(16 + (size() << 4));
/* 535 */     sb.append('[');
/* 536 */     if (this._children != null) {
/* 537 */       int i = 0; for (int len = this._children.size(); i < len; i++) {
/* 538 */         if (i > 0) {
/* 539 */           sb.append(',');
/*     */         }
/* 541 */         sb.append(((JsonNode)this._children.get(i)).toString());
/*     */       }
/*     */     }
/* 544 */     sb.append(']');
/* 545 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public JsonNode _set(int index, JsonNode value)
/*     */   {
/* 556 */     if ((this._children == null) || (index < 0) || (index >= this._children.size())) {
/* 557 */       throw new IndexOutOfBoundsException("Illegal index " + index + ", array size " + size());
/*     */     }
/* 559 */     return (JsonNode)this._children.set(index, value);
/*     */   }
/*     */ 
/*     */   private void _add(JsonNode node)
/*     */   {
/* 564 */     if (this._children == null) {
/* 565 */       this._children = new ArrayList();
/*     */     }
/* 567 */     this._children.add(node);
/*     */   }
/*     */ 
/*     */   private void _insert(int index, JsonNode node)
/*     */   {
/* 572 */     if (this._children == null) {
/* 573 */       this._children = new ArrayList();
/* 574 */       this._children.add(node);
/* 575 */       return;
/*     */     }
/* 577 */     if (index < 0)
/* 578 */       this._children.add(0, node);
/* 579 */     else if (index >= this._children.size())
/* 580 */       this._children.add(node);
/*     */     else
/* 582 */       this._children.add(index, node);
/*     */   }
/*     */ 
/*     */   private boolean _sameChildren(ArrayList<JsonNode> otherChildren)
/*     */   {
/* 592 */     int len = otherChildren.size();
/* 593 */     if (size() != len) {
/* 594 */       return false;
/*     */     }
/* 596 */     for (int i = 0; i < len; i++) {
/* 597 */       if (!((JsonNode)this._children.get(i)).equals(otherChildren.get(i))) {
/* 598 */         return false;
/*     */       }
/*     */     }
/* 601 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.ArrayNode
 * JD-Core Version:    0.6.0
 */