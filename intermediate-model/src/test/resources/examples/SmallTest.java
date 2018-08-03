public class Test extends Object implements IFloodlightModule, INetTopologyService, IOFMessageListener {

	public void smallTest(){
		int monthToUse = thisMonth - 1 + months;
		if (thisMonth > 0 && monthToUse < 0) {
			if (Math.signum(months + iMax) == Math.signum(months)) {
				yearToUse--;
				months += iMax;
			} else {
				yearToUse++;
				months -= iMax;
			}
			monthToUse = thisMonth - 1 + months;
		}
	}
	public synchronized void dowhileexample(){
		int count = 1;
		do {
			System.out.println("Count is: " + count);
			count++;
		} while (count < 11);
		if( 1 < 0){
			System.out.print();
		}
		end();
	}

	public void whileExample(){
		int count = 0;
		while (count < 100) {  // test: boolean test within (..)
			System.out.println("count:" + count);  // body: statements within {..}
			count = count + 1;
		}
		System.out.println("all done!");
	}

	@Deprecated
	private Test(int i, int k) {

		int month = 8;
		String monthString;
		switch (month) {
			case 0:
			case 1:  monthString = "January";
				break;
			case 2:  monthString = "February";
				break;
			case 3:  monthString = "March";
				break;
			case 4:  monthString = "April";
				break;
			case 5:  monthString = "May";
				break;
			case 6:
			case 7:
			case 8: monthString = "June";
				break;
			case 10: monthString = "October";
				break;
			case 11: monthString = "November";
				break;
			case 12: monthString = "December";
				break;
			default: monthString = "Invalid month";
				break;
		}
		System.out.println(monthString);

		synchronized(monthString) {
			//inside the
			System.out.print(stmttt.toString());
			int i;
		}

		System.out.println(monthString);

		try
			/*	(

				FileWriter fw = new FileWriter("");
				BufferedWriter bw = new BufferedWriter(fw);
				BufferedWriter aw = new BufferedWriter(fw);
		) */ {

			bw.write("");
		} catch(Exception e){
			throw new Exception("Exc");System.out.print("str");
		} catch(IOException e){
			throw new Exception("IO Exc");System.out.print("str");
		} finally {
			System.err.print("Eccezione lanciata");System.out.print("str");
			System.err.print("Eccezione lanciata");System.out.print("str");
		}

	}

	public void testWhileVar(int a, int b){
		int i = 10;
		while(i > 0){
			i--;
			if(i % 3 == 0)
				a++;
			else
				b++;
		}
	}


}