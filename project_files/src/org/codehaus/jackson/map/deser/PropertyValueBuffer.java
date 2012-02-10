/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ 
/*    */ public final class PropertyValueBuffer
/*    */ {
/*    */   final JsonParser _parser;
/*    */   final DeserializationContext _context;
/*    */   final Object[] _creatorParameters;
/*    */   private int _paramsNeeded;
/*    */   private PropertyValue _buffered;
/*    */ 
/*    */   public PropertyValueBuffer(JsonParser jp, DeserializationContext ctxt, int paramCount)
/*    */   {
/* 41 */     this._parser = jp;
/* 42 */     this._context = ctxt;
/* 43 */     this._paramsNeeded = paramCount;
/* 44 */     this._creatorParameters = new Object[paramCount];
/*    */   }
/*    */ 
/*    */   protected final Object[] getParameters(Object[] defaults)
/*    */   {
/* 54 */     if (defaults != null) {
/* 55 */       int i = 0; for (int len = this._creatorParameters.length; i < len; i++) {
/* 56 */         if (this._creatorParameters[i] == null) {
/* 57 */           Object value = defaults[i];
/* 58 */           if (value != null) {
/* 59 */             this._creatorParameters[i] = value;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 64 */     return this._creatorParameters;
/*    */   }
/*    */   protected PropertyValue buffered() {
/* 67 */     return this._buffered;
/*    */   }
/*    */ 
/*    */   public boolean assignParameter(int index, Object value)
/*    */   {
/* 73 */     this._creatorParameters[index] = value;
/* 74 */     return --this._paramsNeeded <= 0;
/*    */   }
/*    */ 
/*    */   public void bufferProperty(SettableBeanProperty prop, Object value) {
/* 78 */     this._buffered = new PropertyValue.Regular(this._buffered, value, prop);
/*    */   }
/*    */ 
/*    */   public void bufferAnyProperty(SettableAnyProperty prop, String propName, Object value) {
/* 82 */     this._buffered = new PropertyValue.Any(this._buffered, value, prop, propName);
/*    */   }
/*    */ 
/*    */   public void bufferMapProperty(Object key, Object value) {
/* 86 */     this._buffered = new PropertyValue.Map(this._buffered, value, key);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.PropertyValueBuffer
 * JD-Core Version:    0.6.0
 */