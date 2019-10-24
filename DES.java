import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class DES {

    public static Scanner s = new Scanner(System.in);

    public static void main(String[] args)
    {
        System.out.println("Enter the plain text format : 1.String 2.Hexadecimal");
        int z = s.nextInt();
        char[][] c = new char[8][8];
        if(z == 1) {
            System.out.println("Enter the plain string");
            String st = s.next();
            String str = "";
            char[] ch = st.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                str = "0" + Integer.toBinaryString(ch[i]);
                c[i] = str.toCharArray();
            }
        }
        else if(z == 2)
        {
            System.out.println("Enter the plain hexadecimal value");
            String hex = s.next();
            while(hex.length() != 16)
            {
                System.out.println("Enter the hexadecimal value of 16 digits");
                hex = s.nextLine();
            }
            for(int i=0;i<8;i++)
            {
                StringBuilder builder = new StringBuilder("00000000");
                String binary = Integer.toBinaryString(Integer.parseInt(Character.toString(hex.charAt(2*i)) + Character.toString(hex.charAt(2*i+1)),16));
                builder.replace(8 - binary.length(), 8, binary);
                c[i] = builder.toString().toCharArray();
            }
        }

        encrypt(c);

    }

    public static void encrypt(char[][] c)
    {
        int index = 0;
        char[][] cs;
        char[] csl = new char[32],csr = new char[32],cstemp1 = new char[32],cstemp2 = new char[32];
        //initial permutation
        cs = initp(c);
        //dividing into left and right
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(i<4)
                    csl[index++] = cs[i][j];
                else
                {
                    index = index%32;
                    csr[index++] = cs[i][j];
                }
            }
        }

//        printing initial permutation
//        for(int i=0;i<8;i++)
//        {
//            for(int j=0;j<8;j++)
//            {
//                System.out.print(cs[i][j] + " ");
//            }
//            System.out.println();
//        }
//        for(int i=0;i<32;i++)
//        {
//            System.out.print(csl[i]);
//        }
//        System.out.println();
//        for(int i=0;i<32;i++)
//        {
//            System.out.print(csr[i]);
//        }

        System.out.println("Key input type :");
        System.out.println("1. String, 2. Hexadecimal");
        int d = s.nextInt();

        char[][] key = new char[8][8];
        if(d == 1) {
            System.out.println("Enter the key string");
            String st = s.next();
            while (st.length() != 8) {
                System.out.println("Enter the key string of length 8");
                st = s.nextLine();
            }
            String s1 = "";
            for (int i = 0; i < st.length(); i++) {
                s1 = "0" + Integer.toBinaryString(st.charAt(i));
                key[i] = s1.toCharArray();
            }
        }
        else if(d == 2)
        {
            System.out.println("Enter the hexadecimal value");
            String hex = s.next();
            while(hex.length() != 16)
            {
                System.out.println("Enter the hexadecimal value of 16 digits");
                hex = s.nextLine();
            }
            for(int i=0;i<8;i++)
            {
                StringBuilder builder = new StringBuilder("00000000");
                String binary = Integer.toBinaryString(Integer.parseInt(Character.toString(hex.charAt(2*i)) + Character.toString(hex.charAt(2*i+1)),16));
                builder.replace(8 - binary.length(), 8, binary);
                key[i] = builder.toString().toCharArray();
            }
        }
        Key k = new Key();
        char[][] chars = k.findKey(key);
        for(int i=0;i<16;i++)
        {
            System.out.print("Round-" + (i+1) + " subkey : ");
            for(int j=0;j<48;j++)
            {
                System.out.print(chars[i][j]);
            }
            System.out.println();
        }

        //rounds
        for(int i = 0;i<16;i++)
        {
            cstemp1 = funcbox(csr,chars[i]);
            for(int j = 0;j<32;j++)
            {
                cstemp2[j] = (char)((csl[j] ^ cstemp1[j]) + 48);
            }
            for(int p = 0;p<32;p++)
                csl[p] = csr[p];
            for(int p = 0;p<32;p++)
                csr[p] = cstemp2[p];

            System.out.print("Left round " + (i+1) + ":");
            for(int j=0;j<32;j++)
            {
                System.out.print(csl[j]);
            }
            System.out.println();
            System.out.print("Right round " + (i+1) + ":");
            for(int j=0;j<32;j++)
            {
                System.out.print(csr[j]);
            }
            System.out.println();
        }
        cstemp2 = csl;
        csl = csr;
        csr = cstemp2;

//        for(int i=0;i<32;i++)
//        {
//            System.out.print(csl[i]);
//        }
//        for(int i=0;i<32;i++)
//        {
//            System.out.print(csr[i]);
//        }
//        System.out.println();


//        cstemp1 = funcbox(csr,chars[15]);
//        for(int j = 0;j<32;j++)
//        {
//            cstemp2[j] = (char)((csl[j] ^ cstemp1[j]) + 48);
//        }
//        csl = cstemp2;
//        csr = csr;           //for 16th round no swapping is required

        index = 0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(i<4)
                {
                    cs[i][j] = csl[index++];
                }
                else
                {
                    index = index%32;
                    cs[i][j] = csr[index++];
                }
            }
        }

//        for(int i=0;i<8;i++)
//        {
//            for(int j=0;j<8;j++)
//            {
//                System.out.print(cs[i][j]);
//            }
//        }
        //final permutation
        c = finp(cs);

        //printing the cipher text
        int val = 128,sum = 0;
        System.out.print("The encrypted text is : ");
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                sum = sum + (Character.getNumericValue(c[i][j])*val);
                val = val/2;
            }
            System.out.print(Integer.toHexString(sum));
            sum = 0;
            val = 128;
        }
//        for(int i=0;i<8;i++)
//        {
//            for(int j=0;j<8;j++)
//            {
//                System.out.print(c[i][j]);
//            }
//            System.out.println();
//        }
    }

    public static char[][] initp(char[][] c)
    {
        char[][] cs = new char[8][8];
        int[][] ip = {{58, 50, 42, 34, 26, 18, 10, 2}, {60, 52, 44, 36, 28, 20, 12, 4},
                {62, 54, 46, 38, 30, 22, 14, 6}, {64, 56, 48, 40, 32, 24, 16, 8},
                {57, 49, 41, 33, 25, 17, 9, 1}, {59, 51, 43, 35, 27, 19, 11, 3},
                {61, 53, 45, 37, 29, 21, 13, 5}, {63, 55, 47, 39, 31, 23, 15, 7}};
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                cs[i][j] = c[(ip[i][j]%8 == 0)?(ip[i][j]/8-1):ip[i][j]/8][(ip[i][j]%8 == 0)?7:(ip[i][j]%8-1)];
            }
        }
        return cs;
    }

    public static char[][] finp(char[][] cs)
    {
        char[][] c = new char[8][8];
        int[][] fp = {{40,8,48,16,56,24,64,32}, {39,7,47,15,55,23,63,31}, {38,6,46,14,54,22,62,30},
                {37,5,45,13,53,21,61,29}, {36,4,44,12,52,20,60,28}, {35,3,43,11,51,19,59,27},
                {34,2,42,10,50,18,58,26}, {33,1,41,9,49,17,57,25}};
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                c[i][j] = cs[(fp[i][j]%8 == 0)?(fp[i][j]/8-1):fp[i][j]/8][(fp[i][j]%8 == 0)?7:(fp[i][j]%8-1)];
            }
        }
        return c;
    }

    public static char[] funcbox(char[] cha, char[] roukey)
    {
        //expansion pbox - start
        char[] exch = new char[48];
        char[] ep = {32,1,2,3,4,5,4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,17,18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1};
        for(int i=0;i<48;i++)
        {
            exch[i] = cha[ep[i]-1];
        }
//        int index = 0;
//        exch[index++] = cha[31];exch[47] = cha[0];
//        for(int i=1;i<47;i++)
//        {
//            if(i%6 == 0) {
//                index = index-2;
//                exch[i] = cha[index++];
//            }
//            else
//                exch[i] = cha[index++];
//        }
//        boolean flag = true;
//        for(int i=0;i<32;i++)
//        {
//            if(i%4 == 0 && i != 0 && flag)
//            {
//                exch[index++] = cha[i];
//                exch[index++] = cha[--i];
//                flag = false;
//            }
//            else {
//                exch[index++] = cha[i];
//                flag = true;
//            }
//        }
        //expansion pbox - end

        //adding key
        for(int i=0;i<48;i++)
        {
            exch[i] = (char)((exch[i] ^ roukey[i]) + 48);
        }

        //s-box
        int[][] s1 = {{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7}, {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
                {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0}, {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}};
        int[][] s2 = {{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
                {3 ,13  , 4  ,7  ,15 , 2  , 8 ,14,12,0,1,10,6,9,11,5},
                {0 ,14 ,  7 ,11 , 10 , 4 , 13 , 1 ,  5 , 8 , 12 , 6 ,  9 , 3 ,  2 ,15},
                {13 , 8  ,10 , 1  , 3 ,15  , 4 , 2  ,11 , 6   ,7 ,12  , 0 , 5  ,14 , 9}};
        int[][] s3 = {{10 , 0  , 9 ,14  , 6  ,3  ,15 , 5 ,  1, 13 , 12 , 7  ,11  ,4  , 2 , 8},
                {13 , 7  , 0 , 9  , 3 , 4  , 6, 10  , 2 , 8  , 5 ,14  ,12 ,11 , 15 , 1},
                {13 , 6 ,  4 , 9  , 8, 15 ,  3 , 0  ,11 , 1,   2, 12,   5 ,10 , 14 , 7},
                {1 ,10  ,13 , 0 ,  6,  9  , 8 , 7 ,  4, 15,  14,  3,  11 , 5  , 2 ,12}};
        int[][] s4 = {{7, 13 , 14 , 3  , 0 , 6  , 9, 10,   1 , 2  , 8 , 5 , 11 ,12  , 4 ,15},
                {13 , 8 , 11 , 5  , 6 ,15 ,  0 , 3  , 4  ,7  , 2 ,12 ,  1 ,10  ,14 , 9},
                {10 , 6 ,  9 , 0 , 12, 11  , 7 ,13 , 15,  1 ,  3 ,14 ,  5,  2 ,  8,  4},
                {3, 15   ,0 , 6 , 10 , 1 , 13 , 8  , 9 , 4 ,  5, 11 , 12 , 7  , 2 ,14}};
        int[][] s5 = {{2 ,12,   4 , 1  , 7 ,10 , 11 , 6  , 8 , 5 ,  3 ,15  ,13 , 0  ,14  ,9},
                {14 ,11  , 2 ,12  , 4,  7  ,13 , 1  , 5 , 0 , 15 ,10  , 3 , 9   ,8  ,6},
                {4  ,2  , 1 ,11 , 10 ,13 ,  7  ,8  ,15 , 9 , 12 , 5  , 6 , 3 ,  0, 14},
                {11  ,8  ,12 , 7 ,  1 ,14 ,  2 ,13 ,  6 ,15  , 0 , 9 , 10  ,4   ,5  ,3}};
        int[][] s6 = {{12  ,1  ,10 ,15 ,  9 , 2 ,  6 , 8 ,  0, 13,   3 , 4 , 14 , 7 ,  5 ,11},
                {10, 15  , 4 , 2  , 7 ,12  , 9  ,5 ,  6 , 1 , 13, 14 ,  0, 11   ,3  ,8},
                {9 ,14 , 15 , 5 ,  2  ,8  ,12,  3 ,  7 , 0 ,  4 ,10 ,  1 ,13 , 11 , 6},
                {4 , 3  , 2 ,12  , 9 , 5  ,15 ,10  ,11, 14  , 1 , 7  , 6 , 0  , 8 ,13}};
        int[][] s7 = {{4 ,11  , 2 ,14  ,15 , 0 ,  8 ,13 ,  3 ,12  , 9 , 7 ,  5 ,10 ,  6 , 1},
                {13  ,0  ,11  ,7  , 4 , 9  , 1 ,10 , 14 , 3 ,  5, 12  , 2 ,15  , 8  ,6},
                {1 , 4  ,11 ,13  ,12  ,3  , 7 ,14  ,10, 15 ,  6 , 8,   0,  5 ,  9,  2},
                {6 ,11 , 13 , 8 ,  1 , 4 , 10,  7  , 9 , 5 ,  0, 15 , 14 , 2  , 3, 12}};
        int[][] s8 = {{13 , 2  , 8 , 4 ,  6, 15 , 11,  1 , 10 , 9 ,  3 ,14 ,  5 , 0,  12,  7},
                {1, 15 , 13 , 8 , 10 , 3 ,  7 , 4  ,12 , 5 ,  6, 11  , 0 ,14  , 9  ,2},
                {7 ,11 ,  4,  1,   9, 12 , 14 , 2  , 0 , 6 , 10 ,13  ,15 , 3 ,  5 , 8},
                {2 , 1 , 14 , 7  , 4 ,10 ,  8, 13 , 15 ,12  , 9,  0  , 3 , 5 ,  6 ,11}};
        char[] sout = new char[32],ca;
        String str1 = "",str2 = "",st = "";
        int b1,b2,ind = 0,ti = 1;
        for(int i=0;i<8;i++)
        {
//            str1 = "" + exch[i] + exch[i+5];
//            str2 = "" + exch[i+1] + exch[i+2] + exch[i+3] + exch[i+4];
//            b1 = Integer.parseInt(str1,2);
//            b2 = Integer.parseInt(str2,2);
//            switch(ti)
//            {
//                case 1:st = "" + s1[b1][b2];break;
//                case 2:st = "" + s2[b1][b2];break;
//                case 3:st = "" + s3[b1][b2];break;
//                case 4:st = "" + s4[b1][b2];break;
//                case 5:st = "" + s5[b1][b2];break;
//                case 6:st = "" + s6[b1][b2];break;
//                case 7:st = "" + s7[b1][b2];break;
//                case 8:st = "" + s8[b1][b2];break;
//            }
//            ti++;
//            StringBuilder builder = new StringBuilder("0000");
//            String binary = Integer.toBinaryString(Integer.parseInt(st));
//            builder.replace(4 - binary.length(), 4, binary);
//            ca = builder.toString().toCharArray();
//            sout[ind++] = ca[0];sout[ind++] = ca[1];sout[ind++] = ca[2];sout[ind++] = ca[3];

            char[] row = new char[2];
            row[0] = exch[6*i];
            row[1] = exch[(6*i)+5];
            String sRow ="" + row[0] + "" + row[1];
            // Similarly column bits are found, which are the 4 bits between
            // the two row bits (i.e. bits 1,2,3,4)
            char[] column = new char[4];
            column[0] = exch[(6*i)+1];
            column[1] = exch[(6*i)+2];
            column[2] = exch[(6*i)+3];
            column[3] = exch[(6*i)+4];
            String sColumn ="" + column[0] +""+ column[1] +""+ column[2] +""+ column[3];
            // Converting binary into decimal value, to be given into the
            // array as input
            int x = 0;
            int iRow = Integer.parseInt(sRow, 2);
            int iColumn = Integer.parseInt(sColumn, 2);
            switch(i)
            {
                case 0 : x = s1[iRow][iColumn];break;
                case 1 : x = s2[iRow][iColumn];break;
                case 2 : x = s3[iRow][iColumn];break;
                case 3 : x = s4[iRow][iColumn];break;
                case 4 : x = s5[iRow][iColumn];break;
                case 5 : x = s6[iRow][iColumn];break;
                case 6 : x = s7[iRow][iColumn];break;
                case 7 : x = s8[iRow][iColumn];break;
            }
            // We get decimal value of the S-box here, but we need to convert
            // it into binary:
            String s = Integer.toBinaryString(x);
            // Padding is required since Java returns a decimal '5' as '111' in
            // binary, when we require '0111'.
            while(s.length() < 4) {
                s = "0" + s;
            }
            // The binary bits are appended to the output
            for(int j=0 ; j < 4 ; j++) {
                sout[(i*4) + j] = s.charAt(j);
            }
        }

        //straight p-box
        int[] sp = {16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
        char[] spout = new char[32];
        for(int i=0;i<32;i++)
        {
            spout[i] = sout[sp[i]-1];
        }
        return spout;
    }
}
