/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import org.codehaus.jackson.map.ser.BeanSerializerModifier;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class SerializerFactory
/*     */ {
/*     */   public abstract Config getConfig();
/*     */ 
/*     */   public abstract SerializerFactory withConfig(Config paramConfig);
/*     */ 
/*     */   public final SerializerFactory withAdditionalSerializers(Serializers additional)
/*     */   {
/*  81 */     return withConfig(getConfig().withAdditionalSerializers(additional));
/*     */   }
/*     */ 
/*     */   public final SerializerFactory withSerializerModifier(BeanSerializerModifier modifier)
/*     */   {
/*  94 */     return withConfig(getConfig().withSerializerModifier(modifier));
/*     */   }
/*     */ 
/*     */   public abstract JsonSerializer<Object> createSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty);
/*     */ 
/*     */   public abstract TypeSerializer createTypeSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty);
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonSerializer<Object> createSerializer(JavaType type, SerializationConfig config)
/*     */   {
/* 139 */     return createSerializer(config, type, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final TypeSerializer createTypeSerializer(JavaType baseType, SerializationConfig config)
/*     */   {
/* 152 */     return createTypeSerializer(config, baseType, null);
/*     */   }
/*     */ 
/*     */   public static abstract class Config
/*     */   {
/*     */     public abstract Config withAdditionalSerializers(Serializers paramSerializers);
/*     */ 
/*     */     public abstract Config withSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier);
/*     */ 
/*     */     public abstract boolean hasSerializers();
/*     */ 
/*     */     public abstract boolean hasSerializerModifiers();
/*     */ 
/*     */     public abstract Iterable<Serializers> serializers();
/*     */ 
/*     */     public abstract Iterable<BeanSerializerModifier> serializerModifiers();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.SerializerFactory
 * JD-Core Version:    0.6.0
 */