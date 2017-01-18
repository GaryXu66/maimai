package maimai.lockUtil;

import maimai.app.util.lock.LockUtil;

public class TestLockUtil {
	int i = 0;
	
	public void test(){
		try {
			LockUtil.getInstance().lock("xuheng");
			System.out.println("进入");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
			System.out.println("出来");
		}catch(Exception e){}
		finally{
			LockUtil.getInstance().unlock("xuheng");
		}
	}
	
	public static void main(String[] args) {
		TestLockUtil tu = new TestLockUtil();
		MyThread t1 = new MyThread(tu);
		MyThread t2 = new MyThread(tu);
		MyThread t3 = new MyThread(tu);
		t1.start();
		t2.start();
		t3.start();
	}
}

class MyThread extends Thread{
	private TestLockUtil testLock;
	MyThread(TestLockUtil tu){
		this.testLock = tu;
	}
	
	@Override
	public void run() {
		testLock.test();
	}
	
}
