/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.util.GregorianCalendar;
/*    */ import java.util.HashMap;
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.map.type.TypeFactory;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ class StdDeserializers
/*    */ {
/* 14 */   final HashMap<JavaType, JsonDeserializer<Object>> _deserializers = new HashMap();
/*    */ 
/*    */   private StdDeserializers()
/*    */   {
/* 19 */     add(new UntypedObjectDeserializer());
/*    */ 
/* 22 */     StdDeserializer strDeser = new StdDeserializer.StringDeserializer();
/* 23 */     add(strDeser, String.class);
/* 24 */     add(strDeser, CharSequence.class);
/* 25 */     add(new StdDeserializer.ClassDeserializer());
/*    */ 
/* 28 */     add(new StdDeserializer.BooleanDeserializer(Boolean.class, null));
/* 29 */     add(new StdDeserializer.ByteDeserializer(Byte.class, null));
/* 30 */     add(new StdDeserializer.ShortDeserializer(Short.class, null));
/* 31 */     add(new StdDeserializer.CharacterDeserializer(Character.class, null));
/* 32 */     add(new StdDeserializer.IntegerDeserializer(Integer.class, null));
/* 33 */     add(new StdDeserializer.LongDeserializer(Long.class, null));
/* 34 */     add(new StdDeserializer.FloatDeserializer(Float.class, null));
/* 35 */     add(new StdDeserializer.DoubleDeserializer(Double.class, null));
/*    */ 
/* 40 */     add(new StdDeserializer.BooleanDeserializer(Boolean.TYPE, Boolean.FALSE));
/* 41 */     add(new StdDeserializer.ByteDeserializer(Byte.TYPE, Byte.valueOf(0)));
/* 42 */     add(new StdDeserializer.ShortDeserializer(Short.TYPE, Short.valueOf(0)));
/* 43 */     add(new StdDeserializer.CharacterDeserializer(Character.TYPE, Character.valueOf('\000')));
/* 44 */     add(new StdDeserializer.IntegerDeserializer(Integer.TYPE, Integer.valueOf(0)));
/* 45 */     add(new StdDeserializer.LongDeserializer(Long.TYPE, Long.valueOf(0L)));
/* 46 */     add(new StdDeserializer.FloatDeserializer(Float.TYPE, Float.valueOf(0.0F)));
/* 47 */     add(new StdDeserializer.DoubleDeserializer(Double.TYPE, Double.valueOf(0.0D)));
/*    */ 
/* 50 */     add(new StdDeserializer.NumberDeserializer());
/* 51 */     add(new StdDeserializer.BigDecimalDeserializer());
/* 52 */     add(new StdDeserializer.BigIntegerDeserializer());
/*    */ 
/* 54 */     add(new DateDeserializer());
/* 55 */     add(new StdDeserializer.SqlDateDeserializer());
/* 56 */     add(new TimestampDeserializer());
/* 57 */     add(new StdDeserializer.CalendarDeserializer());
/*    */ 
/* 61 */     add(new StdDeserializer.CalendarDeserializer(GregorianCalendar.class), GregorianCalendar.class);
/*    */ 
/* 65 */     for (StdDeserializer deser : FromStringDeserializer.all()) {
/* 66 */       add(deser);
/*    */     }
/*    */ 
/* 72 */     add(new StdDeserializer.StackTraceElementDeserializer());
/*    */ 
/* 75 */     add(new StdDeserializer.TokenBufferDeserializer());
/*    */ 
/* 77 */     add(new StdDeserializer.AtomicBooleanDeserializer());
/*    */   }
/*    */ 
/*    */   public static HashMap<JavaType, JsonDeserializer<Object>> constructAll()
/*    */   {
/* 85 */     return new StdDeserializers()._deserializers;
/*    */   }
/*    */ 
/*    */   private void add(StdDeserializer<?> stdDeser)
/*    */   {
/* 90 */     add(stdDeser, stdDeser.getValueClass());
/*    */   }
/*    */ 
/*    */   private void add(StdDeserializer<?> stdDeser, Class<?> valueClass)
/*    */   {
/* 97 */     JsonDeserializer deser = stdDeser;
/* 98 */     this._deserializers.put(TypeFactory.type(valueClass), deser);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializers
 * JD-Core Version:    0.6.0
 */