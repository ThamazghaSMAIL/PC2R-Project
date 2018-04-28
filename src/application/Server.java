package application;

//Assumptions made (based on what was mentioned by lecturer in class) 
//Client only has a maximum of two chances to answer a question			

import java.io.*;
import java.net.*;

class Server{

	private static final String[] String = null;

	public static void main(String[] args) throws Exception {
		//String variables that store client and server responses as well as track client score card
		String clientAns,serverResp,track,trackAll="";
		//variables keep track of client score and questions answered
		int score=0,count=0,contd=0,ans=0,wrong=0;

		String[] words= new String[10];//Question array- list of questions

		//	Create question with question,answer and status 
		words[0]= "A1A2A3";
		words[1]= "B1B2B3";
		words[2]= "C1C2C3";

		try{ 	  
			ServerSocket welcomeSocket = new ServerSocket(6789);// écoute de la socket du serveur
			System.out.println("Server running....\n");

			while(true) {

				Socket connectionSocket = welcomeSocket.accept();// Rend la connexion avec le socket

				// Crée un lecteur tamponné pour lire les réponses du client
				BufferedReader inFromClient =new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

				// Crée un flux de sortie de données pour envoyer les réponses du serveur au client
				DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

				while( count<words.length){

					clientAns = inFromClient.readLine(); 
					serverResp="reponse correcte";
					outToClient.writeBytes(serverResp);
					//If client's response is correct increase their total score
					if(clientAns.trim().equals(words[count])){
						score+=1;
						serverResp="reponse correcte";
						outToClient.writeBytes(serverResp);
					}
				}
				count++;//Increments to next question

				serverResp="Your Score is " + score +". You Answered "+ ans + " questions correctly." + '\n';
				outToClient.writeBytes(serverResp);
				System.out.println("\nClient total score is: " +score);
			}
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

}


