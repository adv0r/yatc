/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ 
/*     */ public abstract class ContainerNode extends BaseJsonNode
/*     */ {
/*     */   JsonNodeFactory _nodeFactory;
/*     */ 
/*     */   protected ContainerNode(JsonNodeFactory nc)
/*     */   {
/*  27 */     this._nodeFactory = nc;
/*     */   }
/*     */ 
/*     */   public boolean isContainerNode() {
/*  31 */     return true;
/*     */   }
/*     */   public abstract JsonToken asToken();
/*     */ 
/*     */   public String getValueAsText() {
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract JsonNode findValue(String paramString);
/*     */ 
/*     */   public abstract ObjectNode findParent(String paramString);
/*     */ 
/*     */   public abstract List<JsonNode> findValues(String paramString, List<JsonNode> paramList);
/*     */ 
/*     */   public abstract List<JsonNode> findParents(String paramString, List<JsonNode> paramList);
/*     */ 
/*     */   public abstract List<String> findValuesAsText(String paramString, List<String> paramList);
/*     */ 
/*     */   public abstract int size();
/*     */ 
/*     */   public abstract JsonNode get(int paramInt);
/*     */ 
/*     */   public abstract JsonNode get(String paramString);
/*     */ 
/*     */   public final ArrayNode arrayNode()
/*     */   {
/*  82 */     return this._nodeFactory.arrayNode(); } 
/*  83 */   public final ObjectNode objectNode() { return this._nodeFactory.objectNode(); } 
/*  84 */   public final NullNode nullNode() { return this._nodeFactory.nullNode(); } 
/*     */   public final BooleanNode booleanNode(boolean v) {
/*  86 */     return this._nodeFactory.booleanNode(v);
/*     */   }
/*  88 */   public final NumericNode numberNode(byte v) { return this._nodeFactory.numberNode(v); } 
/*  89 */   public final NumericNode numberNode(short v) { return this._nodeFactory.numberNode(v); } 
/*  90 */   public final NumericNode numberNode(int v) { return this._nodeFactory.numberNode(v); } 
/*  91 */   public final NumericNode numberNode(long v) { return this._nodeFactory.numberNode(v); } 
/*  92 */   public final NumericNode numberNode(float v) { return this._nodeFactory.numberNode(v); } 
/*  93 */   public final NumericNode numberNode(double v) { return this._nodeFactory.numberNode(v); } 
/*  94 */   public final NumericNode numberNode(BigDecimal v) { return this._nodeFactory.numberNode(v); } 
/*     */   public final TextNode textNode(String text) {
/*  96 */     return this._nodeFactory.textNode(text);
/*     */   }
/*  98 */   public final BinaryNode binaryNode(byte[] data) { return this._nodeFactory.binaryNode(data); } 
/*  99 */   public final BinaryNode binaryNode(byte[] data, int offset, int length) { return this._nodeFactory.binaryNode(data, offset, length); } 
/*     */   public final POJONode POJONode(Object pojo) {
/* 101 */     return this._nodeFactory.POJONode(pojo);
/*     */   }
/*     */ 
/*     */   public abstract ContainerNode removeAll();
/*     */ 
/*     */   protected static class NoStringsIterator
/*     */     implements Iterator<String>
/*     */   {
/* 145 */     static final NoStringsIterator instance = new NoStringsIterator();
/*     */ 
/*     */     public static NoStringsIterator instance()
/*     */     {
/* 149 */       return instance;
/*     */     }
/* 151 */     public boolean hasNext() { return false; } 
/* 152 */     public String next() { throw new NoSuchElementException(); }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 156 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class NoNodesIterator
/*     */     implements Iterator<JsonNode>
/*     */   {
/* 127 */     static final NoNodesIterator instance = new NoNodesIterator();
/*     */ 
/*     */     public static NoNodesIterator instance()
/*     */     {
/* 131 */       return instance;
/*     */     }
/* 133 */     public boolean hasNext() { return false; } 
/* 134 */     public JsonNode next() { throw new NoSuchElementException(); }
/*     */ 
/*     */     public void remove()
/*     */     {
/* 138 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.ContainerNode
 * JD-Core Version:    0.6.0
 */