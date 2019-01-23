package project1;

import java.io.File;

public class MSMC { // ---MSC---

	private static void execution(long start, long end) { // υπολογισμος μεσου χρονου
		double ex_time = ((end - start) / 1000000000.0) / 100.0; // υπολογιζει το μεσου χρονου εκτελεσης της cover για τις 100 επαναληψεις	
		System.out.println("Cover’s average execution time --> " +ex_time  +" sec\n"); 	
	}
	
	private static String[] filesInDir(int file_in_use, File aDirectory){ // δημιουργια ενος πινακα στον οποιο αποθηκευονται ολα τα αρχεια που θα χρησιμοποιησουμε
		String[] filesInDir = null; 
		
		if(file_in_use == 0)
			filesInDir = aDirectory.list(); // αποθηκευιολα τα αρχεια τον πινακα filesInDir
		else
			filesInDir = aDirectory.list(); // αποθηκευιολα τα αρχεια τον πινακα filesInDir
		
		return filesInDir;
	}
	
	private static void run(int file_in_use, File aDirectory){ // εκτελεση του αλγοριθμου για το αρχειο που εχει επιλεχθει
		for (int y = 0; y < filesInDir(file_in_use, aDirectory).length; y++) { // ο αλγοριθμος θα τρεξει για ολα τα αρχεια που βρισκονται μεσα στον πινακα filesInDir
			String fileName = filesInDir(file_in_use, aDirectory)[y]; // αποθηκευει το ονομα του αρχειου για το οποιο θα τρεξει τον αλγοριθμο
			
			System.out.println("File: " +(y+1)); // εκτυπωνει τον αριθμο του αρχειου για το οποιο τρεχει τον αλγοριθμο
			System.out.println("File Name --> " +fileName); // εκτυπωνει το ονομα του αρχειου για το οποιο τρεχει τον αλγοριθμο
			
			MSetList generateList = new MSetList(); // δημιουργει μια νεα κενη λιστα
			MultiSet target = generateList.readFile(aDirectory.getPath() + "/" +fileName); // δημιουργει το αντικειμενο target το οποιο ειναι το αρχειο με που διαβαζεται στην παρενθεση
			int coverCost = generateList.value; // το δοσμενο κοστος της cover απο το αρχειο
			MSetList estimatedCost = null; // αρχικοποιηση λιστας

			long start = System.nanoTime(); // η εναρξη μετρησης του χρονου για το loop

			for (int i = 0; i < 100; i++) { // ο αλγοριθμος θα τρεξει 100 φορες
				MSetList copyList = generateList.copy(); // δημιουργει ενα αντιγραφο της λιστας καλοντας τη μεθοδο copy
				estimatedCost = copyList.cover(target); // το εκτιμομενο κοστος της cover (επανω στο αντιγραφο που δημιουργησαμε προηγουμενος)
			}

			long end = System.nanoTime(); // η ληξη μετρησης του χρονου

			System.out.println("Cover's Cost --> " + coverCost);
			System.out.println("Cost estimation of the best solution --> " + estimatedCost.value);
			execution(start, end); // καλειται η συναρτηση για την εμφανιση του μεσου χρονου εκτελεσης της cover
		}
	}

	public static void main(String[] args) {	 
		File aDirectory = null; // το directory μεσα στο οποιο βρισκονται τα αρχεια

		for (int file_in_use = 0; file_in_use < 2; file_in_use++) { // η μεταβλητη y κραταει δυο νουμερα...το 0 για το πρωτο και το 1 για το δευτερο αρχειο
			if(file_in_use == 0){ // 0 --> αρα θα χρησιμοποιηθει το πρωτο αρχειο
				aDirectory = new File("m500"); // αναθετει στη μεταβλητη το directory ε τα πρωτα 5 αρχει (το "m500")
				filesInDir(file_in_use, aDirectory); // αποθηκευιολα τα αρχεια τον πινακα filesInDir
			} 
			else { // 1 --> αρα θα χρησιμοποιηθει το δευτερο αρχειο
				aDirectory = new File("n5000"); // αναθετει στη μεταβλητη το directory ε τα πρωτα 5 αρχεια (το "n5000")
				filesInDir(file_in_use, aDirectory); 
			}
			
			run( file_in_use, aDirectory);			
		}
	}
}


class Node { // ---Node---
	public int cost;
	public MultiSet mset;
	public Node next;

	public Node(MultiSet mset, int cost) { // Node constructor
		this.cost = cost;
		this.mset = new MultiSet(mset.toArray());
	}
}


class MultiSet { // ---MiltiSet---
	private int[] item; // αποθηκευει την πολλαπλοτητα του i στο πολυσυνολο
	private int card; // αποδηκευει την πληθυκοτητα του πολυσυνολου

	public MultiSet(int[] item) { // multiSet constructor
		card = 0;
		this.item = item.clone();

	}

	public int cardinality() { // υπολογιζει και επιστρεφει την πληθηκοτητα		
		for (int i = 0; i < item.length; i++) { // για καθε στοιχειο του πινακα item ου περιλαμβανει τις πολλαπλοτητες
			card = card + item[i]; // αυξανει κατα 1 την πληθυκοτητα
		}
		return card; // επιστρεφει την πληθυκοτητα
	}

	public int[] toArray() { // επιστρεφει ενα αντιγραφο του πινακα item το		
		return item.clone();
	}

	public MultiSet isect(MultiSet mset) { // υπολογιζει την τομη του item με το
		
		if (this.item.length != mset.item.length) // ελεγχει αν το μήκος του this.item διαφέρει από το μήκος του mset.item
			return null; // αν ναι επιστρεφει null
		int[] min_isec = new int[this.item.length]; // αποθηκευει την τομης του πολυσυνόλου με το πολυσύνολο mset
		
		for(int i = 0; i < item.length; i++){
			min_isec[i] = Math.min(item[i], mset.item[i]); // εφαρμογη του τυπου για την ευρεση της τομης
		}
		
		return new MultiSet(min_isec); // επιστρεφει το νέο πολυσύνολο με το αποτέλεσμα
	}

	public MultiSet minus(MultiSet mset) { // υπολογιζει τη διαφορα του item με
		if (this.item.length != mset.item.length)  // ελεγχει αν το μήκος του this.item διαφέρει από το μήκος του mset.item
			return null; // αν ναι επιστρεφει null
		int[] max_isec = new int[this.item.length]; // αποθηκευει τη διαφορα του πολυσυνόλου με το πολυσύνολο mset
		
		for(int i = 0; i < item.length; i++){
			max_isec[i] = Math.max(0, item[i] - mset.item[i]);// εφαρμογη του τυπου για την ευρεση της διαφορας
		}
		
		return new MultiSet(max_isec);	// επιστρεφει το νέο πολυσύνολο με το αποτέλεσμα								
	}
}

class MSetList { // --- MSetList---
	public int value;
	private int nbNodes;
	private Node first;
	private Node last;

	public MSetList() { // constructor
		first = null;
		last = null;
		nbNodes = 0;
		value = 0;
	}

	public MultiSet readFile(String filename) {
		java.io.BufferedReader br = null;
		int[] target = null;
		try {
			br = new java.io.BufferedReader(new java.io.FileReader(filename));

			// Read dimensions
			String line = br.readLine();
			String[] data = line.split(" ");
			int m = Integer.parseInt(data[0]);
			int n = Integer.parseInt(data[1]);
			this.value = Integer.parseInt(data[2]);

			// Read target
			line = br.readLine();
			if (line == null)
				return (null);
			data = line.split(" ");
			target = new int[data.length];
			for (int i = 0; i < data.length; i++)
				target[i] = Integer.parseInt(data[i]);

			// Read multisets
			while ((line = br.readLine()) != null) {
				data = line.split(" ");
				// Read multiset cost
				int cost = Integer.parseInt(data[0]);
				// Read multiset contents
				int[] item = new int[data.length - 1];
				for (int i = 1; i < data.length; i++)
					item[i - 1] = Integer.parseInt(data[i]);
				this.append(new MultiSet(item), cost);
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (java.io.IOException ex) {
				ex.printStackTrace();
			}
		}
		return (new MultiSet(target));
	}

	public int length() { // επιστρεφει το πληθος των κομβων της λιστας
		return nbNodes;
	}

	public boolean isEmpty() { // ελεγχει αν η λιστα ειναι αδεια
		if(first == null)
			return true;
		else 
			return false;
	}

	public int append(MultiSet mset, int cost) { // προσθετει ενα νεο κομβο στη λιστα
		Node nd1 = new Node(mset, cost); // δημιουργια νεου κομβου
		
		if(isEmpty()){ // ελεγχος αν η λιστα ειναι αδεια...αρα ο νεος κομβος θα ειναι και ο μοναδικος σε αυτη
			first = nd1; // ο δεικτης first δειχνει στο νεο κομβο
			last = nd1; // ο δεικτης last δειχνει στο νεο κομβο
			last.next = null; // μετα τον δεικτη last δεν υποαρχει αλλος κομβος	
		}
		else{ // αν στη λιστα υπαρχουν και αλλοι κομβοι
			last.next = nd1; // ο δεικτης last μετακινειται στο νεο κομβο
			last = nd1;	// ο δεικτης last δειχνει στο νεο κομβο
			last.next = null; // ο δεικτης last δειχνει στο νεο κομβο
		}
		nbNodes++; // αυξανεται το πληθος των κομβων κατα 1
			
		return nbNodes; // επιστρεφει το πληθος των κομβων
	}

	public MSetList copy() { // δημνιουργια αντιγραφου της λιστας που καλειται
		MSetList lst2 = new MSetList(); // δημιουργια νεας λιστας
		Node current = first; //ο τρεχον κομβος δειχνει στο πρωτο στοιχειο/κομβο της λιστας
		
		while(current != null){ // αν η λιστα δεν ειναι αδεια
			lst2.append(current.mset, current.cost); //προσθετει νεο κομβο
			current = current.next; // ο δεικτης current δειχνει μια θεση μπροστα
		}
		return lst2; //επιστρεφει τη λιστα αντιγραφο
	}

	private Node rmBestNode(MultiSet target) { //Εντοπίζει και διαγράφει από τη λίστα στην οποία καλείται, 
												//τον κόμβο που αποθηκεύει το πολυσύνολο με το ελάχιστο κλάσμα κόστους προς πληθυκότητα
												//τομής με το πολυσύνολο target.
		if(isEmpty()) // ελεγχει αν η λιστα ειναι αδεια
			return null;
		
		Node current = first; // ο τρεεχον κομβος δειχνει στον πρωτο κομβο της λιστας
		Node previous = first; // ο προηγουμενος κομβος δειχνει στον πρωτο κομβο της λιστας
		Node minNode = first; // ο κομβος με την ελαχιστη τιμη του κλασματος της εκφωνησης
		Node minNodePrev = first; //ο προηγουμενος κομβος του minNode
		double min = Double.MAX_VALUE; // κραταει την ελαχιστη τιμη
		
		while(current != null){ // βεβαιωνει πως η λιστα δεν ειναι αδεια
			double m = (double) current.cost/current.mset.isect(target).cardinality();

			if(m < min){ // αν βρεθει κομβος με μικριτερη τιμη απο το min τοτε αυτος γινεται ο minNode
				min = m; // αλλαγη της τιμης min με την τρεχουσα μικροτερη 
				minNodePrev = previous; //  ο νεος προηγουμενος του μικροτερου κομβου
				minNode = current; // ο νεος μικροτερος κομβος ειναι ο τρεχον
			}					
			
			previous = current; // ο κανονικος δεικτης "προηγουμενος" δειχνει στον τρεχον
			current = current.next; // ο τρεχον προχωραει μια θεση
		}
		Node tmp = minNode; // δημιουργια νεου κομβου
		
		if(minNode == first) // ο κομβος min ειναι ο πρωτος κομβος της λιστας
			first = first.next;
		else if(minNode == last){ // ο κομβος min ειναι ο τελευταιος κομβος της λιστας
			minNodePrev.next = null;	
			last = minNodePrev;
		}
		else{ // ο κομβος ειναι στο ενδιαμεσο της λιστας
			minNodePrev.next = minNode.next;
			minNode.next = null;
		}		
		
		return tmp; // επιστροφη του ζητουμενου κομβου
	}

	public MSetList cover(MultiSet target) { // υλοποιηση του απλιστου αλγοριθμου
		MSetList lst1 = new MSetList();
		
		while(target.cardinality() != 0){ // ελεγχει αν η λιστα ειναι αδεια		
			Node rm = rmBestNode(target); // δημιουργει νεο κομβο και καλει την rmBestNode για να βρει και να διαγραει 
											//τον κόμβο που αποθηκεύει το πολυσύνολο με το ελάχιστο κλάσμα κόστους προς πληθυκότητα
											//τομής με το πολυσύνολο target.
			if(rm != null){	// ελεγχει αν υπαρχουν αλλοι κομβοι		
				lst1.append(rm.mset, rm.cost); // προσθετει νεο κομβο
				lst1.value += rm.cost; // αυξανει το κοστος 
				target = target.minus(rm.mset); // βρισκει την τομη του target ε το πολυσυνολο mset
			}			
		}
		
		if(lst1.isEmpty()) // λεγχει αν η λιστα ειναι αδεια
			return null;
		else
			return lst1;		
	}
}

