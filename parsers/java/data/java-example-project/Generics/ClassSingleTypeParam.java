public class ClassSingleTypeParam<T> {

  T field;

  public T method1() {

  }

  public <K> K method2() {

  }

  public <X, Y> X method3(Y param) {

  }

  public <X extends Number & Comparable> X method4() {

  }
}
