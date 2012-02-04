package de.merksoft.alleswirdgut;

import java.util.ArrayList;
import java.util.List;

public class TextSammlung {

	public final static int DE = 1; 
	public final static int EN = 2; 
	
	private List<String> textListeDE; 
	private List<String> textListeEN; 
	
	private static TextSammlung instance; 
	
	private TextSammlung() {
		this.textListeDE = new ArrayList<String>();
		this.textListeEN = new ArrayList<String>();
		
		this.textListeDE.add(	"\n\n        Alles \n" +
								"        wird \n" +
								"        gut!");
		this.textListeDE.add(	"\n\n" +
								"    Keine Fehler \n" +
								"    machen nur \n" +
								"    Leute, die \n" +
							 	"    nichts tun.");
		this.textListeDE.add(	"\n\n     Schlechte \n" +
								"     Tage gehen\n" +
							 	"     auch vorbei.");
//		this.textListeDE.add("Testtext \n" +
//							 "Testtext1");
//		this.textListeDE.add("Testtext \n" +
//							 "Testtext2");
//		this.textListeDE.add("Testtext \n" +
//							 "Testtext3");
//		this.textListeDE.add("Testtext \n" +
//							 "Testtext4");
//		this.textListeDE.add("Testtext \n" +
//							 "Testtext5");
//		this.textListeDE.add("Testtext \n" +
//							 "Testtext6");

		this.textListeEN.add("Everything is going to \n" +
				  		     "be all right!");
		this.textListeEN.add("Nobody is perfect!");
		this.textListeEN.add("Testing \n" +
		     				 "Testing1");
		this.textListeEN.add("Testing \n" +
							 "Testing1");
		this.textListeEN.add("Testing \n" +
							 "Testing2");
		this.textListeEN.add("Testing \n" +
							 "Testing3");
		this.textListeEN.add("Testing \n" +
							 "Testing4");
		this.textListeEN.add("Testing \n" +
							 "Testing5");
		this.textListeEN.add("Testing \n" +
							 "Testing6");
		this.textListeEN.add("Testing \n" +
							 "Testing7");
		
	}
	
	public static TextSammlung getInstance(){
		if ( instance == null ){
			instance = new TextSammlung();
		}
		return instance;
	}
	
	public String getRandomText(int sprache){
		double index = Math.random() * this.getAnzahl(sprache);
		return this.getText(sprache, (int)index);
	}
	
	public String getText(int sprache, int index){
		switch (sprache) {
		case TextSammlung.DE:
			if (index > this.textListeDE.size())
				return "Alles wird gut!!";
			else 
				return this.textListeDE.get(index);
		case TextSammlung.EN:
			if (index > this.textListeEN.size())
				return "Alles wird gut!!";
			else 
				return this.textListeEN.get(index);
		default:
			return "Alles wird gut!!";
		}
	}
	
	public int getAnzahl(int sprache){
		switch (sprache) {
		case TextSammlung.DE:
			return this.textListeDE.size();
		case TextSammlung.EN:
			return this.textListeEN.size();
		default:
			return 0;
		}
	}

}
