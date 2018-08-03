/**
 * Created by giovanni (@thisthatDC) on 18/03/16.
 */
public class HelloWorld {

    public class Foo {
        public int mymethod(int i) {
            return i;
        }

        public int yourmethod(int j) {
            return j - 1;
        }
    };

    public static void main(String[] args) {
        System.out.println("Hello, World"  + test(1,2,3,4));
        int i = 0+4 << 2;
    }
    //single line
    public static String test(int x, int y, int w, int z){

        x = y + w;
        y = y * 2;
        return "";
    }

    public void fie(int a) {
        Foo varfoo = new HelloWorld.Foo() {
            @Override
            public int mymethod(int i) {
                i = i * 2;
                return i * i;
            }
        };


    }

    public void time_method(int x) {
        int a = 10 + x;

        int b = System.currentTimeMillis();
        int c = b + a;

        while (b < c) {
            Thread.sleep(1000);
            b = System.currentTimeMillis();
        }
    }
}