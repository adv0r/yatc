/*    */ package org.codehaus.jackson.map.module;
/*    */ 
/*    */ import org.codehaus.jackson.Version;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.JsonSerializer;
/*    */ import org.codehaus.jackson.map.Module;
/*    */ import org.codehaus.jackson.map.Module.SetupContext;
/*    */ 
/*    */ public class SimpleModule extends Module
/*    */ {
/*    */   protected final String _name;
/*    */   protected final Version _version;
/* 20 */   protected SimpleSerializers _serializers = null;
/* 21 */   protected SimpleDeserializers _deserializers = null;
/*    */ 
/*    */   public SimpleModule(String name, Version version)
/*    */   {
/* 31 */     this._name = name;
/* 32 */     this._version = version;
/*    */   }
/*    */ 
/*    */   public SimpleModule addSerializer(JsonSerializer<?> ser)
/*    */   {
/* 37 */     if (this._serializers == null) {
/* 38 */       this._serializers = new SimpleSerializers();
/*    */     }
/* 40 */     this._serializers.addSerializer(ser);
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   public <T> SimpleModule addSerializer(Class<? extends T> type, JsonSerializer<T> ser)
/*    */   {
/* 46 */     if (this._serializers == null) {
/* 47 */       this._serializers = new SimpleSerializers();
/*    */     }
/* 49 */     this._serializers.addSerializer(type, ser);
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   public <T> SimpleModule addDeserializer(Class<T> type, JsonDeserializer<? extends T> deser)
/*    */   {
/* 55 */     if (this._deserializers == null) {
/* 56 */       this._deserializers = new SimpleDeserializers();
/*    */     }
/* 58 */     this._deserializers.addDeserializer(type, deser);
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */   public String getModuleName()
/*    */   {
/* 70 */     return this._name;
/*    */   }
/*    */ 
/*    */   public void setupModule(Module.SetupContext context)
/*    */   {
/* 76 */     if (this._serializers != null) {
/* 77 */       context.addSerializers(this._serializers);
/*    */     }
/* 79 */     if (this._deserializers != null)
/* 80 */       context.addDeserializers(this._deserializers);
/*    */   }
/*    */ 
/*    */   public Version version()
/*    */   {
/* 86 */     return this._version;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.module.SimpleModule
 * JD-Core Version:    0.6.0
 */