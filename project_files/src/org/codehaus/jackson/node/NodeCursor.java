/*     */ package org.codehaus.jackson.node;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ 
/*     */ abstract class NodeCursor extends JsonStreamContext
/*     */ {
/*     */   final NodeCursor _parent;
/*     */ 
/*     */   public NodeCursor(int contextType, NodeCursor p)
/*     */   {
/*  23 */     this._type = contextType;
/*  24 */     this._index = -1;
/*  25 */     this._parent = p;
/*     */   }
/*     */ 
/*     */   public final NodeCursor getParent()
/*     */   {
/*  36 */     return this._parent;
/*     */   }
/*     */ 
/*     */   public abstract String getCurrentName();
/*     */ 
/*     */   public abstract JsonToken nextToken();
/*     */ 
/*     */   public abstract JsonToken nextValue();
/*     */ 
/*     */   public abstract JsonToken endToken();
/*     */ 
/*     */   public abstract JsonNode currentNode();
/*     */ 
/*     */   public abstract boolean currentHasChildren();
/*     */ 
/*     */   public final NodeCursor iterateChildren()
/*     */   {
/*  59 */     JsonNode n = currentNode();
/*  60 */     if (n == null) throw new IllegalStateException("No current node");
/*  61 */     if (n.isArray()) {
/*  62 */       return new Array(n, this);
/*     */     }
/*  64 */     if (n.isObject()) {
/*  65 */       return new Object(n, this);
/*     */     }
/*  67 */     throw new IllegalStateException("Current node of type " + n.getClass().getName());
/*     */   }
/*     */ 
/*     */   protected static final class Object extends NodeCursor
/*     */   {
/*     */     Iterator<Map.Entry<String, JsonNode>> _contents;
/*     */     Map.Entry<String, JsonNode> _current;
/*     */     boolean _needEntry;
/*     */ 
/*     */     public Object(JsonNode n, NodeCursor p)
/*     */     {
/* 172 */       super(p);
/* 173 */       this._contents = ((ObjectNode)n).getFields();
/* 174 */       this._needEntry = true;
/*     */     }
/*     */ 
/*     */     public String getCurrentName()
/*     */     {
/* 179 */       return this._current == null ? null : (String)this._current.getKey();
/*     */     }
/*     */ 
/*     */     public JsonToken nextToken()
/*     */     {
/* 186 */       if (this._needEntry) {
/* 187 */         if (!this._contents.hasNext()) {
/* 188 */           this._current = null;
/* 189 */           return null;
/*     */         }
/* 191 */         this._needEntry = false;
/* 192 */         this._current = ((Map.Entry)this._contents.next());
/* 193 */         return JsonToken.FIELD_NAME;
/*     */       }
/* 195 */       this._needEntry = true;
/* 196 */       return ((JsonNode)this._current.getValue()).asToken();
/*     */     }
/*     */ 
/*     */     public JsonToken nextValue()
/*     */     {
/* 202 */       JsonToken t = nextToken();
/* 203 */       if (t == JsonToken.FIELD_NAME) {
/* 204 */         t = nextToken();
/*     */       }
/* 206 */       return t;
/*     */     }
/*     */ 
/*     */     public JsonToken endToken() {
/* 210 */       return JsonToken.END_OBJECT;
/*     */     }
/*     */ 
/*     */     public JsonNode currentNode() {
/* 214 */       return this._current == null ? null : (JsonNode)this._current.getValue();
/*     */     }
/*     */ 
/*     */     public boolean currentHasChildren()
/*     */     {
/* 219 */       return ((ContainerNode)currentNode()).size() > 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final class Array extends NodeCursor
/*     */   {
/*     */     Iterator<JsonNode> _contents;
/*     */     JsonNode _currentNode;
/*     */ 
/*     */     public Array(JsonNode n, NodeCursor p)
/*     */     {
/* 127 */       super(p);
/* 128 */       this._contents = n.getElements();
/*     */     }
/*     */ 
/*     */     public String getCurrentName() {
/* 132 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonToken nextToken()
/*     */     {
/* 137 */       if (!this._contents.hasNext()) {
/* 138 */         this._currentNode = null;
/* 139 */         return null;
/*     */       }
/* 141 */       this._currentNode = ((JsonNode)this._contents.next());
/* 142 */       return this._currentNode.asToken();
/*     */     }
/*     */ 
/*     */     public JsonToken nextValue() {
/* 146 */       return nextToken();
/*     */     }
/* 148 */     public JsonToken endToken() { return JsonToken.END_ARRAY; }
/*     */ 
/*     */     public JsonNode currentNode() {
/* 151 */       return this._currentNode;
/*     */     }
/*     */ 
/*     */     public boolean currentHasChildren() {
/* 155 */       return ((ContainerNode)currentNode()).size() > 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final class RootValue extends NodeCursor
/*     */   {
/*     */     JsonNode _node;
/*  86 */     protected boolean _done = false;
/*     */ 
/*     */     public RootValue(JsonNode n, NodeCursor p) {
/*  89 */       super(p);
/*  90 */       this._node = n;
/*     */     }
/*     */ 
/*     */     public String getCurrentName() {
/*  94 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonToken nextToken() {
/*  98 */       if (!this._done) {
/*  99 */         this._done = true;
/* 100 */         return this._node.asToken();
/*     */       }
/* 102 */       this._node = null;
/* 103 */       return null;
/*     */     }
/*     */ 
/*     */     public JsonToken nextValue() {
/* 107 */       return nextToken();
/*     */     }
/* 109 */     public JsonToken endToken() { return null; } 
/*     */     public JsonNode currentNode() {
/* 111 */       return this._node;
/*     */     }
/* 113 */     public boolean currentHasChildren() { return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.NodeCursor
 * JD-Core Version:    0.6.0
 */