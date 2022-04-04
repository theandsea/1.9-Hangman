import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hangman {

    public static void main(String[] args) throws Exception {
        // write your code here
        // "D:\\UCSD course\\CSE 250A - Probabilistic Reason&Learning - Saul\\"
        String str = path_str("hw1_word_counts_05.txt");
        str_wordcount(str);
        sort();
        System.out.println("the fifteen most frequent 5-letter words___" + word[15 - 1] + "\r\n" +
                "the fourteen least frequent 5-letter words___" + word[num - 14] + "\r\n");

        Count_Pw();
        //print();
        System.out.println("Check for the example");
        System.out.println("====================================================");
        PWE("-----",new char[]{'E','O'});
        PLE();
        PWE("D--I-",new char[]{});
        PLE();
        PWE("D--I-",new char[]{'A'});
        PLE();
        PWE("-U---",new char[]{'A','E','I','O','S'});
        PLE();
        System.out.println();
        System.out.println();
        System.out.println("Here are the answer:");
        System.out.println("====================================================");
        PWE("-----",new char[]{});
        PLE();
        PWE("-----",new char[]{'E','A'});
        PLE();
        PWE("A---S",new char[]{});
        PLE();
        PWE("A---S",new char[]{'I'});
        PLE();
        PWE("--O--",new char[]{'A','E','M','N','T'});
        PLE();
    }

    public static String path_str(String path) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bf = new BufferedReader(new FileReader(path));
        String s = null;
        while ((s = bf.readLine()) != null) {//使用readLine方法，一次读一行
            buffer.append(s.trim() + "\r\n");
        }

        return buffer.toString();
    }

    public static int[] count = null;
    public static double[] Pw = null;
    public static String[] word = null;
    public static int num = 0;

    public static void str_wordcount(String str) {
        String strafter = str.replace("\r", "");
        String[] seg = strafter.split("\n");
        num = seg.length;
        count = new int[num];
        word = new String[num];
        String[] tempseg = null;
        for (int i = 0; i < num; i++) {
            tempseg = seg[i].split(" ");
            word[i] = tempseg[0];
            count[i] = Integer.parseInt(tempseg[1]);
        }
    }

    public static void sort() {
        int tempcount = 0;
        String tempword = null;
        for (int i = 0; i < num; i++) {
            for (int j = i + 1; j < num; j++) {
                if (count[i] < count[j]) {
                    tempword = word[j];
                    tempcount = count[j];
                    word[j] = word[i];
                    count[j] = count[i];
                    word[i] = tempword;
                    count[i] = tempcount;
                }
            }
        }
    }

    public static void wordcount_sort(int s, int e) {
        if (s >= e) return;
        int i = s;
        int j = e;
        int key = count[s];
        int tempcount = 0;
        String tempword = null;
        while (i != j) {
            while (count[j] < key) j--;
            tempword = word[j];
            tempcount = count[j];
            word[j] = word[i];
            count[j] = count[i];
            word[i] = tempword;
            count[i] = tempcount;
            while (count[i] > key) i++;
            tempword = word[j];
            tempcount = count[j];
            word[j] = word[i];
            count[j] = count[i];
            word[i] = tempword;
            count[i] = tempcount;
        }
        wordcount_sort(s, i - 1);
        wordcount_sort(i + 1, e);
    }

    public static void print() {
        for (int i = 0; i < num; i++) {
            System.out.println(word[i] + " " + count[i]);
        }
    }

    public static void Count_Pw() {
        Pw = new double[num];
        double sum = 0;
        for (int i = 0; i < num; i++)
            sum += count[i];
        for (int i = 0; i < num; i++) {
            Pw[i] = count[i] / sum;
            //System.out.println(Pw[i]);
        }
    }

    public static double[] PWE = null;
    public static String correct=null;
    public static char[] wrong=null;

    public static void PWE(String thiscorrect,char[] thiswrong) throws Exception {
        // check for the right
        if(thiscorrect.length()!=5)
            throw new Exception("correct answer error !");

        correct=thiscorrect;
        wrong=thiswrong;

        PWE = new double[num];
        double[] interPWE = new double[num];
        boolean compat = true;
        String thisword = null;
        char[] correctchar = correct.toCharArray();
        for (int i = 0; i < num; i++) {
            compat = true;
            thisword = word[i];

            // check for wrong
            for (int j=0;j<wrong.length ;j++ ) {
                if(thisword.indexOf(wrong[j])!=-1){
                    compat=false;
                    break;
                }
            }

            // check for correct
            // also not appear in any other place !!! exclude the already words !!!!
            for (int word_k = 0, kl = thisword.length(); word_k < kl; word_k++) {
                if (correctchar[word_k] != '-') {
                    if (correctchar[word_k] != thisword.charAt(word_k)) {
                        compat = false;
                        break;
                    }
                    // other position
                    for (int j=0,jl=thisword.length();j<jl;j++ ) {
                        if(j!=word_k) {
                            if(thisword.charAt(j)==correctchar[word_k]){
                                compat = false;
                                break;
                            }
                        }
                    }
                    if(!compat)
                        break;
                }
            }


            if (compat)
                interPWE[i] = Pw[i];
            else
                interPWE[i] = 0;
        }


        double sum = 0;
        for (int i = 0; i < num; i++) {
            sum += interPWE[i];
        }
        for (int i = 0; i < num; i++) {
            PWE[i] = interPWE[i] / sum;
        }
    }

    public static double[] PLE=null;
    public static char[] letter=new char[26];
    public static void PLE() {
        PLE=new double[26];

        boolean[] Lif=new boolean[26]; // if the xth letter is possible
        for (int i=0;i<26 ;i++ ) {
            letter[i]=(char)('A'+i);
        }

        for (int i=0;i<correct.length();i++ ) {
            if(correct.charAt(i)!='-'){
                Lif[correct.charAt(i)-'A']=true; // true means avoid
            }
        }
        for (int i=0;i<wrong.length ;i++ ) {
            Lif[wrong[i]-'A']=true;
        }

        double sum=0;
        char thisletter='0';
        boolean compat=false;
        String thisword=null;

        for (int i=0;i<26 ;i++ ) { // each letter
            if(!Lif[i]){ // compatible with situation(Environment)
                sum=0;
                thisletter=letter[i];
                for (int j=0;j<num ;j++ ) { // each word
                    thisword=word[j];
                    // check whether at certain position
                    compat=false;
                    for (int word_k=0,word_l=correct.length();word_k<word_l ;word_k++ ) {
                        if(correct.charAt(word_k)=='-')  // need check
                            if(thisword.charAt(word_k)==thisletter) {
                                compat = true;
                                break;
                            }
                    }

                    if(compat)
                        sum+=PWE[j];
/*
                    if(word[j].indexOf(thisletter)!=-1){
                        sum+=PWE[j];
                    }*/
                }
                PLE[i]=sum;
            }
        }

        // find the maximum
        double max=0;
        int index=0;
        for (int i=0;i<26 ;i++ ) {
            if(max<PLE[i]){
                max=PLE[i];
                index=i;
            }
        }
        System.out.println("----------------------------------------------");
        System.out.println("For the situation___"+correct+"__"+String.valueOf(wrong));
        System.out.println("best next guess l___"+letter[index]+"\r\n"+"probability___"+PLE[index]);
    }
}





