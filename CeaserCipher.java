//A Java Program to illustrate Caesar Cipher Technique
import java.util.*;
class CaesarCipher
{
// Encrypts text using a shift od s
public static StringBuffer decrypt(String text, int s)
{
StringBuffer result= new StringBuffer();

for (int i=0; i<text.length(); i++)
{
if (Character.isUpperCase(text.charAt(i)))
{
char ch = (char)(((int)text.charAt(i) -
s - 65) % 26 + 65);
result.append(ch);
}
else
{
char ch = (char)(((int)text.charAt(i) -
s - 97) % 26 + 97);
result.append(ch);
}
}
return result;
}

// Driver code
public static void main(String[] args)
{
//String text = "ATTACKATONCE";
//int s = 4;
Scanner sc = new Scanner(System.in);
System.out.println("Enter Cipher Text : ");
String cipherText = sc.nextLine();
System.out.println("Possible Plain Text"+"\n")  ;
   for(int s=0;s<26;s++){
   System.out.println("Message is: "+decrypt(cipherText, s)+" and the key is "+s);
   }
       
}

} 

