import java.util.*;

public class Key {

    public char[][] findKey(char[][] c)
    {
//        for(int i=0;i<8;i++)
//        {
//            for(int j=0;j<8;j++)
//            {
//                System.out.print(c[i][j] + " ");
//            }
//            System.out.println();
//        }

        char a, b, d, e;
        int k = 0;
        char[][] ch = new char[8][7];
        char[][] skeys = new char[16][48];

        //permutation pc1
        int[][] cp1 = {{57,  49,   41,  33,   25,   17,   9},
                {1,   58,   50,  42,   34,   26,  18},
                {10,   2,   59,  51,   43,   35,  27},
                {19,  11,    3,  60,   52,   44,   36},
                {63,  55,   47,  39,   31,   23,  15},
                {7,   62,   54,  46,   38,   30,  22},
                {14,  6,    61,  53,   45,   37,  29},
                {21,  13,    5,  28,   20,   12,   4}};
        int[][] cp2 = {{14, 17, 11, 24, 1,  5},
                    {3,    28,  15,    6,   21,  10},
                    {23,   19,  12,    4,   26,   8},
                    {16,    7,  27,   20,   13,   2},
                    {41,   52,  31,   37,   47,  55},
                    {30,   40,  51,   45,   33,  48},
                    {44,   49,  39,   56,   34,  53},
                    {46,   42,  50,   36,   29,  32}};

        for(int i=0;i<8;i++)
        {
            for(int j=0;j<7;j++)
            {
                ch[i][j] = c[cp1[i][j]/8][cp1[i][j]%8-1];
            }
        }

        //dividing into left half and right half
        char[] chl = new char[28],chr = new char[28];
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<7;j++)
            {
                chl[k++] = ch[i][j];
            }
        }
        k=0;
        for(int i=4;i<8;i++)
        {
            for(int j=0;j<7;j++)
            {
                chr[k++] = ch[i][j];
            }
        }

        //creating subkeys
        for(int i=1;i<=16;i++)
        {
            if(i == 1 || i == 2 || i == 9 || i == 16)
            {
                //for left and right - 1 shift
                a = chl[0];
                b = chr[0];
                for(int j=1;j<28;j++)
                {
                    chl[j-1] = chl[j];
                    chr[j-1] = chr[j];
                }
                chl[27] = a;
                chr[27] = b;
            }
            else
            {
                //for left and right - 2 shift
                a = chl[0];d = chl[1];
                b = chr[0];e = chr[1];
                for(int j=2;j<28;j++)
                {
                    chl[j-2] = chl[j];
                    chr[j-2] = chr[j];
                }
                chl[26] = a;chl[27] = d;
                chr[26] = b;chr[27] = e;
            }

            k=0;
            for(int p=0;p<8;p++)
            {
                for(int q=0;q<6;q++)
                {
                    if(cp2[p][q] <= 28)
                        skeys[i-1][k++] = chl[cp2[p][q]-1];
                    else
                        skeys[i-1][k++] = chr[cp2[p][q]-29];
                }
            }
        }
        return skeys;
    }
}
