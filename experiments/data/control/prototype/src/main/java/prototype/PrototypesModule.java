package prototype;

class PrototypesModule {                  // 2. "registry" of prototypical objs
   private static Prototype[] prototypes = new Prototype[9];
   private static int         total      = 0;
   public static void addPrototype( Prototype obj ) {
      prototypes[total++] = obj;
   }
   public static Object findAndClone( String name ) {  // 4. The "virtual ctor"
      for (int i=0; i < total; i++)
         if (prototypes[i].getName().equals( name ))
            return prototypes[i].clone();
      System.out.println( name + " not found" );
      return null;
}  }