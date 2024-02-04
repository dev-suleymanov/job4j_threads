package differents;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringBuildTest {
    @Test
    public void Test1() {
        StringBuild stringBuild = new StringBuild();
        stringBuild.set("Rustam");
        stringBuild.set(" Suleymanov");
        stringBuild.set(" Ikramovich");
        assertThat(stringBuild.toBuildString()).
                isEqualTo("Rustam Suleymanov Ikramovich");
    }

    @Test
    public void Test2() {
        StringBuild stringBuild = new StringBuild();
        stringBuild.set("Hello");
        stringBuild.set(" my fri");
        stringBuild.set("ends");
        assertThat(stringBuild.toBuildString()).
                isEqualTo("Hello my friends");
    }

    @Test
    public void Test3()  {
        StringBuild stringBuild = new StringBuild();
        Thread thread1 = new Thread(() -> {
            stringBuild.set("Rustam");
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            try {
                thread1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            stringBuild.set(" Suleymanov");
        });
        thread2.start();
        Thread thread3 = new Thread(() -> {
            try {
                thread1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            stringBuild.set(" Ikramovich");
        });
        thread3.start();
        try {
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(stringBuild.toBuildString()).
                isEqualTo("Rustam Suleymanov Ikramovich");
    }
}