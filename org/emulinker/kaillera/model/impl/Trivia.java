package org.emulinker.kaillera.model.impl;
import java.util.*;
import java.io.*;

public class Trivia implements Runnable {
	private boolean exitThread = false;
	private boolean triviaPaused = false;
	private KailleraServerImpl server;
	
	private boolean newQuestion = true;
	private String answer;
	private char hint[];
	private boolean hint1 = false;
	private boolean hint2 = false;
	private boolean answered = false;
	private int questions_count = 0;
	private String ip_streak = "";
	private int score_streak = 0;
	private int questionTime= 10;
	private int s;
	
	
	ArrayList <Questions> questions = new ArrayList<Questions>();
	ArrayList <Integer> questions_num = new ArrayList<Integer>();
	ArrayList <Scores> scores = new ArrayList<Scores>();
	ArrayList <String> unscramblewords = new ArrayList<String>();

	public boolean isAnswered(){
		return answered;
	}
	public void setQuestionTime(int questionTime){
		this.questionTime = questionTime;
	}
	
	public List<Scores> getScores(){
		return scores;
	}

	public void setTriviaPaused(boolean triviaPaused){
		this.triviaPaused = triviaPaused;
	}
	
	private class Scores{
		private String ip;
		private int score;
		private String nick;
		
		public Scores(String nick, String ip, int score){
			this.nick = nick;
			this.ip = ip;
			this.score = score;
		}
		
		public void setScore(int score){
			this.score = score;
		}
		public void setIP(String ip){
			this.ip = ip;
		}
		public void setNick(String nick){
			this.nick = nick;
		}
		
		
		public int getScore(){
			return score;
		}
		public String getIP(){
			return ip;
		}
		public String getNick(){
			return nick;
		}
	}
	
	
	private class Questions{
		String question;
		String answer;
		
		public Questions(String question, String answer){
			this.question = question;
			this.answer = answer;
		}
		
		public String getQuestion(){
			return question;
		}
		public String getAnswer(){
			return answer;
		}
	}

	
	public Trivia(KailleraServerImpl server){
		this.server = server;
		try
		{
        	server.announce("<Unscramble> " + "Loading EmuXScramble Words...", false, null);
        	server.announce("<Unscramble> " + "Loading Previous Scores...", false, null);
			File file = new File("wordlist.txt");
			Scanner scan = new Scanner(file);
			
			/*
			"This is a test"
			would return
			{this,is,a,test}
			*/
			while (scan.hasNext()) 
			{							// read line by line
				String[] tempStr = scan.nextLine().split(" ");
				for(int j = 0; j < tempStr.length; j++)
				{
					unscramblewords.add(tempStr[j]);
				}
        	}
			scan.close();
		

		
		
			Random generator = new Random();
			for (int j = 0; j <unscramblewords.size(); j++)
			{
				String tempword = unscramblewords.get(j);
				char[] c = tempword.toCharArray();			
				for (int k = 0; k < tempword.length(); k++)
					{
						int r = generator.nextInt(tempword.length());	//generate two random integers for place values
						int q = generator.nextInt(tempword.length());
						char temp = c[q];				//swap starts here
						c[q] = c[r];			
						c[r] = temp;
					}		
			String scrambledword = new String(c);
			Questions tempQuestion = new Questions(scrambledword, unscramblewords.get(j).toString());
			questions.add(tempQuestion);
			questions_num.add(j);
			
        	//##################
        	//######SCORES######
        	//##################

            InputStream ist = new FileInputStream("scores_scrambler.txt"); 
            BufferedReader istream = new BufferedReader(new InputStreamReader(ist));
            
            String str = istream.readLine();//First Score         
        	while(str != null){           	
            	if(str.startsWith("ip:") || str.startsWith("IP:") || str.startsWith("Ip:") || str.startsWith("iP:")){
            		String ip;
            		String s;
            		String n;
            		
            		ip = str.substring("ip:".length()).trim();
            		str = istream.readLine();
            		s = str.substring("s:".length()).trim();
            		str = istream.readLine();
            		n = str.substring("n:".length()).trim();
            		
            		scores.add(new Scores(n, ip, Integer.parseInt(s)));
            	}
            	str = istream.readLine();//New Score
        	}
        	

        	ist.close();
        	}
        	server.announce("<Unscramble> " + questions.size() + " words have been loaded!", false, null);
        	server.announce("<Unscramble> " + scores.size() + " scores have been loaded!", false, null);
        	server.announce("<Unscramble> " + "EmuXUnscramble will begin in 10s!", false, null);
			}
			
    		catch(Exception e)
    		{
    			server.announce("<Unscramble> Error loading words!", false, null);
    		}
			
        
	}

	
	
	public void run(){
		int count = 0;
		int temp;
		Random generator = new Random();
		
		temp = generator.nextInt(questions_num.size() - 1);
		questions_count = (Integer)questions_num.get(temp);
		questions_num.remove(temp);
		try{Thread.sleep(10000);}catch(Exception e){}
		
		while(!exitThread){
			if(!triviaPaused){
				if(newQuestion){
					count++;
					if(count % 15 == 0){
						saveScores(false);
						displayHighScores(false);
					}															
					newQuestion = false;
					hint1 = true;
					hint2 = false;
					Questions thisquestion = (Questions)questions.get(questions_count);
					server.announce("<Unscramble> " + "Unscramble this word: " + thisquestion.getQuestion(), false, null);
					if(!answered)try{Thread.sleep(10000);}catch(Exception e){}
					
					if(!answered){
						server.announce("<Unscramble> " + "35 seconds left...", false, null);
						try{Thread.sleep(5000);}catch(Exception e){}
					}

				}
				if(hint1 && !answered){
					newQuestion = false;
					hint1 = false;
					hint2 = true;
					Questions tempquestion2 = (Questions)questions.get(questions_count);
					answer = tempquestion2.getAnswer().toLowerCase();
					answer = answer.replace(" ", "    ");
					hint = answer.toCharArray();
					for(int w = 0; w < hint.length; w++){
						if((w+1) % 2 == 0 && hint[w] != ' '){
							hint[w] = '_';
						}
					}
					
					answer = String.valueOf(hint);
					answer = answer.replace("_", " _ ");
					
					server.announce("<Unscramble> " + "Hint 1: " + answer, false, null);
					if(!answered)try{Thread.sleep(10000);}catch(Exception e){}
					
					if(!answered){
						server.announce("<Unscramble> " + "20 seconds left...", false, null);
						try{Thread.sleep(5000);}catch(Exception e){}
					}
				}
				
				if(hint2 && !answered){
					newQuestion = false;
					hint1 = false;
					hint2 = false;
					Questions tempquestion2 = (Questions)questions.get(questions_count);
					answer = tempquestion2.getAnswer().toLowerCase();
					answer = answer.replace(" ", "    ");
					hint = answer.toCharArray();
					
					for(int w = 0; w < hint.length; w++){
						if((w+1) % 4 == 0 && hint[w] != ' '){
							hint[w] = '_';
						}
					}
					
					answer = String.valueOf(hint);
					answer = answer.replace("_", " _ ");
					
					server.announce("<Unscramble> " + "Hint 2: " + answer, false, null);
					if(!answered)try{Thread.sleep(10000);}catch(Exception e){}
					
					if(!answered){
						server.announce("<Unscramble> " + "5 seconds left...", false, null);
						try{Thread.sleep(5000);}catch(Exception e){}
					}
					
				}
				
				if(!answered){
                                        questions.get(questions_count);
					server.announce("<Unscramble> " + "Time's up! The answer is: " + questions.get(questions_count).getAnswer(), false, null);
				}				
				
				//Find questions not repeated
				if(questions_num.size() == 1){
					questions_count = (Integer)questions_num.get(0);
					questions_num.clear();
					count = 0;
					server.announce("<Unscramble> " + "***All questions have been exhaused! Restarting list...***", false, null);
					for(int w = 0; w < questions.size(); w++){
						questions_num.add(w);
					}
				}
				else{
					temp = generator.nextInt(questions_num.size() - 1);
					questions_count = questions_num.get(temp);
					questions_num.remove(temp);
				}			
				try{Thread.sleep(5000);}catch(Exception e){}
				
				
				server.announce("<Unscramble> " + (questionTime/1000) + " seconds until the next question. Get ready for question " + (count + 1) + " of " + questions.size(), false, null);
				try{Thread.sleep(questionTime);}catch(Exception e){}

				
				newQuestion = true;
				hint1 = false;
				hint2 = false;
				answered = false;
			}
			else{
				try{Thread.sleep(1000);}catch(Exception e){}
			}
		}
	}
	
	public boolean isCorrect(String message){
		String numbers0[] = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
		String numbers1[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		String placement0[] = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "nineth", "tenth"};
		String placement1[] = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th"};
		Questions tempquestion2 = (Questions)questions.get(questions_count);
		if(message.toLowerCase().equals(tempquestion2.getAnswer().toLowerCase())){
			answered = true;
			return true;
		}
		else{
			for(int i = 0; i < numbers0.length; i++){
				if(tempquestion2.getAnswer().toLowerCase().contains(numbers0[i])){
					message = message.replace(numbers1[i], numbers0[i]);
				}
				else if(tempquestion2.getAnswer().toLowerCase().contains(numbers1[i])){
					message = message.replace(numbers0[i], numbers1[i]);
				}
			}	

			if(message.toLowerCase().equals(tempquestion2.getAnswer().toLowerCase())){
				answered = true;
				return true;
			}
			else{
				for(int i = 0; i < placement0.length; i++){
					if(tempquestion2.getAnswer().toLowerCase().contains(placement0[i])){
						message = message.replace(placement1[i], placement0[i]);
					}
					else if(tempquestion2.getAnswer().toLowerCase().contains(placement1[i])){
						message = message.replace(placement0[i], placement1[i]);
					}
				}
				
				if(message.toLowerCase().equals(tempquestion2.getAnswer().toLowerCase())){
					answered = true;
					return true;
				}
			}
		}
		
		return false;
	}
	public boolean updateIP(String ip, String ip_update){
		for(int i = 0; i < scores.size(); i++){
			Scores tempscore = (Scores)scores.get(i);
			if(tempscore.getIP().equals(ip)){
				tempscore.setIP(ip_update);
				return true;
			}
		}
		
		return false;
	}
		
	public void addScore(String nick, String ip, String answer){
		answered = true;
		
		for(int i = 0; i < scores.size(); i++)
		{
			Scores tempscore = (Scores)scores.get(i);
			if(tempscore.getIP().equals(ip))
			{
				if (ip_streak.equals(ip))
				{
					score_streak++;
					if(score_streak > 1)
					{
						try{Thread.sleep(20);}catch(Exception e){}
                                                server.announce("<Unscramble> ***" + nick + " has answered " + score_streak + " in a row!***", false, null);
						tempscore.setNick(nick);
						s = tempscore.getScore();
						s = s + score_streak;
						tempscore.setScore(s);
					}
					else
					{
						score_streak = 1;
						ip_streak = ip;
					}
                                        
                                        try{Thread.sleep(20);}catch(Exception e){}
				}
				else
				{			
					tempscore.setNick(nick);
					s = tempscore.getScore();
					s++;
					tempscore.setScore(s);
				}
				questions.get(questions_count);
				server.announce("<Unscramble> Congratulations " + nick + "! You have answered the question correctly with (" + answer + ")! Your score is: " +s, false, null);
				return;
			}
		}
		
		scores.add(new Scores(nick, ip, 1));
		score_streak = 1;
		ip_streak = ip;
		server.announce("<Unscramble> Congratulations " + nick + "! You have answered the question correctly with (" + answer + ")! Your score is: 1", false, null);
	}
	
	public void displayHighScores(boolean winner){
		String first_nick = "";
		String second_nick = "";
		String third_nick = "";
		int first_score = 0;
		int second_score = 0;
		int third_score = 0;
		String temp_nick;
		int temp_score = 0;
		
		for(int i = 0; i < scores.size(); i++){
			Scores tempscore = (Scores)scores.get(i);
			if(tempscore.getScore() > first_score){
				temp_nick = first_nick;
				temp_score = first_score;
				
				first_score = tempscore.getScore();
				first_nick = tempscore.getNick();
				
				second_score = temp_score;
				second_nick = temp_nick;
			}
			else if(tempscore.getScore() > second_score){
				temp_nick = second_nick;
				temp_score = second_score;
				
				second_score = tempscore.getScore();
				second_nick = tempscore.getNick();
				
				third_score = temp_score;
				third_nick = temp_nick;
			}
			else if(tempscore.getScore() > third_score){
				third_score = tempscore.getScore();
				third_nick = tempscore.getNick();
			}
		}	
		
		String str;
		
		if(!winner){
			str = first_nick + " = " + first_score + ", ";
			str = str + second_nick + " = " + second_score + ", ";
			str = str + third_nick + " = " + third_score;
		}
		else{
			str = "The Winner is: " + first_nick + " with " + first_score + " points!";
		}
		
		server.announce("<Unscramble> " + "(Top 3 Scores of " + scores.size() + ") " + str, false, null);
	}	
	
	
	public void saveScores(boolean display){
		try{
	        BufferedWriter out = new BufferedWriter(new FileWriter("scores_scrambler.txt", false));
	        
	        for(int i = 0; i < scores.size(); i++){
				Scores tempscore = (Scores)scores.get(i);
	        	out.write("ip:" + tempscore.getIP());
	        	out.newLine();
	        	out.write("s:" + tempscore.getScore());
	        	out.newLine();
	        	out.write("n:" + tempscore.getNick());
	        	out.newLine();
	        	out.newLine();
	        }
	        out.close();
	        
	        if(display)
	        	server.announce("<Unscramble> " + "EmuXUnscramble Scores were Saved Successfully!", false, null);
		}
        catch(Exception e){
        	server.announce("<Unscramble> " + "Error Saving EmuXUnscramble Scores!", false, null);
        }
        }
}

