import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author wanghanyu
 * @create 2017-09-11 12:44
 */
public class FutureTaskTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor wareSearcherExcutor = new ThreadPoolExecutor(15, 15, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        List<Future<Boolean>> futureTasks = new ArrayList<Future<Boolean>>();
        final List<Person> persons = new ArrayList<Person>();

        for(int i = 0; i < 10; i++) {
            Person p = new Person();
            Student s = new Student();
            s.setAge(i + 1);
            p.setStudent(s);
            persons.add(p);
        }

        final List<Person2> person2s = Lists.transform(persons, new Function<Person, Person2>() {
            @Nullable
            public Person2 apply(@Nullable Person person) {
                Person2 person2 = new Person2();
                person2.setStudent(person.getStudent());
                return person2;
            }
        });

        for(int i = 0; i < 10; i++) {
            final int j = i;

            Callable<Boolean> callable = new Callable<Boolean>() {

                public Boolean call() throws Exception {
                    System.out.println("Thread " + j);
                    Person2 p = person2s.get(j);
                    Student s = new Student();
                    //Thread.sleep(5000);
                    s.setAge((j + 1) * 100);
                    p.setStudent(s);
                    return Boolean.TRUE;
                }
            };
            Future<Boolean> futureTask = wareSearcherExcutor.submit(callable);
            futureTasks.add(futureTask);
        }

        for(int i = 0; i < futureTasks.size(); i++) {
            System.out.println("futureTasks " + i);
            futureTasks.get(i).get();
        }
        System.out.println("--------Main------");
        wareSearcherExcutor.shutdown();
        for(int i = 0; i < 10; i++) {
            System.out.println(person2s.get(i).getStudent().getAge());
        }
    }
}

class Person {
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}

class Person2 {
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}

class Student {
    private Integer age;

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}
