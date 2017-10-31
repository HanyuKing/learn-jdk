package context;

import java.lang.reflect.Field;

public class ContextUtil {

	private static MyThreadLocal<User> currentUser = new MyThreadLocal<User>();

	public static void main(String[] args) throws InterruptedException {
		String user = new String("Hanyu");
		User user2 = new User();
		user2.setName(user);
		setCurrentUser(user2);

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				String user = new String("King");
				User user1 = new User();
				user1.setName(user);
				currentUser.set(user1);
			}
		});
		t1.start();

		Thread t2 = new Thread(new Runnable() {
			public void run() {
				String user = new String("王");
				User user1 = new User();
				user1.setName(user);
				currentUser.set(user1);
			}
		});
		t2.start();

		Thread.sleep(1000);

		//printThread();


		System.gc();
		//printThread();
	}

	public static void setCurrentUser(User user){
		currentUser.set(user);
		System.out.println(Thread.currentThread().getName() + "-" + Thread.currentThread().getId() + currentUser.get());
	}

	private static void printThread() {
		Thread t = Thread.currentThread();
		String tName = t.getName() + t.getId();
		System.out.println();
		System.out.println("-----------------------------------start-----------------------------------");
		try {
			Field field = Thread.class.getDeclaredField("threadLocals");
			field.setAccessible(true);

			Class<?> obj = field.get(t).getClass();
			Field[] f = obj.getDeclaredFields();
			int size = 0;
			Object[] table = null;
			for(Field field2 : f){
				if(field2.getName().equals("size")) {
					field2.setAccessible(true);
					size = field2.getInt(field.get(t));
					//System.out.println("size->" + field2.get(field.get(t)));
				}
				if(field2.getName().equals("table")) {
					field2.setAccessible(true);
					table = (Object[]) field2.get(field.get(t));
				}
			}
			for(int i = 0; i < size; i++) {
				System.out.print(tName + "-table-" + i + "->" + table[i]);
				if(table[i] != null) {
					Field[] f2 = table[i].getClass().getDeclaredFields();
					Field[] superFS = table[i].getClass().getSuperclass().getSuperclass().getDeclaredFields();
					Object value = null;
					ThreadLocal<String> key = null;
					for(Field field2 : f2){
						if(field2.getName().equals("value")) {
							field2.setAccessible(true);
							value = field2.get(table[i]);
							//System.out.println("size->" + field2.get(field.get(t)));
						}
					}
					for(Field superF : superFS) {
						if(superF.getName().equals("referent")) {
							superF.setAccessible(true);
							key = (ThreadLocal<String>) superF.get(table[i]);
						}
					}
					System.out.println("[key: " + key + " value: " + value + "]");
					key = null;
				} else {
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("-----------------------------------end-----------------------------------");
			System.out.println();
		}
	}
}

class MyThreadLocal<T> extends ThreadLocal<T> {
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println(Thread.currentThread().getName() + "-" + Thread.currentThread().getId() + "-" + this + " finalize");
	}
}

class User {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				'}';
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println(Thread.currentThread().getName() + "-" + Thread.currentThread().getId() + "-" + name + " finalize");
	}
}
