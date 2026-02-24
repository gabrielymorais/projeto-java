public class PayableInterfaceTest
{
	public static void main(String[] args)
	{
		Payable[] payableObjects = new Payable[6];
		
		payableObjects[0] = new Invoice("01234","seat",2,375.00);
		payableObjects[1] = new Invoice("56789","tire",4,79.95);
		payableObjects[2] = new HourlyEmployee("Karen","Price","222-22-2222",new Date(6,28,1989),16.75,40);
		payableObjects[3] = new CommissionEmployee("Sue","Jones","333-33-3333",new Date(1,26,2018),10000,0.06);
		payableObjects[4] = new BasePlusCommissionEmployee("Bob","Lewis","444-44-4444",new Date(1,25,2023),0.04,5000,300);
		payableObjects[5] = new PieceWorker("Michael","Jackson","555-55-5555",new Date(2,26,2024),10.0,100);
		
		System.out.printf("Payable objects processed polymorphically:%n%n");
		
		for (Payable payable : payableObjects)
		{
			System.out.println(payable);
			System.out.printf("earnings: $%,.2f%n",payable.getPaymentAmount());
			System.out.println();		
		}		
	}
}
