package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class AmitRanjan1 {

    public static void main(String[] args) throws IOException {

        String fileLocation = (args.length ==0 || args[0] == null) ? AmitRanjan1.class.getResource("/file1.txt").getPath() : args[0];
        try (BufferedReader in = new BufferedReader(new FileReader(fileLocation))) {
            in.lines()
                    .map(AmitRanjan1::reassemble)
                    .forEach(System.out::println);
        }
    }


    static String  reassemble(String fragmentedline){

        List<String> fragmentedList = Arrays.asList(fragmentedline.split(";"));



        if(fragmentedList.isEmpty()) {
            return "";
        }else if(fragmentedList.size() ==1){
            return fragmentedList.get(0);
        }




        List<String> combinedWordsList = new ArrayList<>(fragmentedList);;
        while(combinedWordsList.size() >1) {
            System.out.println(" initial size " + combinedWordsList.size());
            //List<String> primedList = new ArrayList<>(fragmentedList);
            combinedWordsList = joinRelatedWords(combinedWordsList);
           // primedList.addAll(combinedWordsList);
        }

        combinedWordsList.stream().map(a-> "====---===>>>" + a ).forEach(System.out::println);
       // System.out.println(combinedWords);

       /* for(int i=0; i< fragmentedList.size(); i++) {
         //   System.out.println("i = " + i);
           // if(i+1< fragmentedList.size()) {
               // List<String> remainingFragmentedList = fragmentedList.subList(i + 1, fragmentedList.size() - 1);

            String firstWord = fragmentedList.get(i);

          //  List<String> remainingList = new ArrayList<>(fragmentedList);
            List<String> remainingList = new ArrayList(fragmentedList.subList(i, fragmentedList.size()));
            remainingList.remove(firstWord);
            List<String> relatedWords = findRelatedWords(firstWord, remainingList);
            //relatedWords.add(firstWord);
            // }

            relatedWordsMap.put(firstWord, relatedWords);

        }
*/


        return fragmentedline;
    }

    public static String combineOverlappingWords(String overlap, String firstWord, String secondWord) {


        System.out.print("combineOverlappingWords " + overlap + " -> " + firstWord + " -> " + secondWord );

        String combinedWord = "";
        if(firstWord.startsWith(overlap)){
            firstWord = firstWord.replace(overlap, "");
            combinedWord = secondWord + firstWord;
        }else if(secondWord.startsWith(overlap)){
            secondWord = secondWord.replace(overlap, "");
            combinedWord = firstWord + secondWord ;
        }

        System.out.println( " -> " + combinedWord);
        return combinedWord;
    }

    static class Overlap {

        private String overlap;
        private String firstWord;
        private String secondWord;

        public Overlap(String overlap, String firstWord, String secondWord) {
            this.overlap = overlap;
            this.firstWord = firstWord;
            this.secondWord = secondWord;
        }

        public String getOverlap() {
            return overlap;
        }

        public void setOverlap(String overlap) {
            this.overlap = overlap;
        }


        public String getFirstWord() {
            return firstWord;
        }

        public void setFirstWord(String firstWord) {
            this.firstWord = firstWord;
        }

        public String getSecondWord() {
            return secondWord;
        }

        public void setSecondWord(String secondWord) {
            this.secondWord = secondWord;
        }
    }

    public static  List<String> joinRelatedWords(List<String> words){


        words = removeSmallestWordAlreadyPresentInAnotherWord(words);

        String combinedWord = "";
        List<String> concatenatedWords = new ArrayList<>();
        List<String> primedList = new ArrayList<>(words);
       // System.out.println("Initial size " + primedList.size());

        List<String> visitedWords = new ArrayList<>();
        List<String> combinedWordsList = new ArrayList<>();

        String overlapRecord = "";

        Map<String, Overlap> overlapMap = new HashMap<>();

        for(String word : words){
            visitedWords.add(word);



            List<String> anotherList = new ArrayList<>(words);
            anotherList.remove(word);

            String firstWord = combinedWord.isEmpty()? word : combinedWord;
           // String firstWord = word;
            for(String nestedWord : anotherList){

                if(combinedWordsList.contains(nestedWord))
                    continue;

                String overlap = isAnyOverlap(firstWord, nestedWord);

                if(overlap.length()>0){

                    addOverlappingDetail(word, overlapMap, nestedWord, overlap);

                  //  combinedWordsList.add(word);
                  //  combinedWordsList.add(nestedWord);

                 //    combinedWord = combineOverlappingWords(overlap, firstWord, nestedWord);

             //       if(!concatenatedWords.contains(combinedWord)) {
                      //  System.out.println("->>>" + combinedWord);
             //           concatenatedWords.add(combinedWord);

            //        }
            //        firstWord = combinedWord;
                   // break;
                }

            }


            ///  combine the words here





        }

        System.out.println("Size of overlap Map " + overlapMap.size());


        String combinedString = "";

        for(String word : words){

            Overlap overlap =  overlapMap.get(word);
            if(overlap == null) {

                System.out.println("No overlap found for " + word);
                continue;
            }

            else {
                combinedString = combineOverlappingWords(overlap.getOverlap(), overlap.getFirstWord(), overlap.getSecondWord());

                if(!concatenatedWords.contains(combinedString)) {
                    concatenatedWords.add(combinedString);
                }
               // System.out.println("+++++ " + combinedString);
            }
        }


       // System.out.println("After removal size " + concatenatedWords.size());

       // words = concatenatedWords;
     //   concatenatedWords.stream().map(a-> "=======>" + a ).forEach(System.out::println);
        return concatenatedWords;
    }

    private static void addOverlappingDetail(String word, Map<String, Overlap> overlapMap, String nestedWord, String overlap) {

        Overlap overlaps = overlapMap.get(word);

        Overlap newOverlapObject = new Overlap(overlap, word, nestedWord);

        if(overlaps == null || (newOverlapObject.getOverlap().length() > overlaps.getOverlap().length() || newOverlapObject.getSecondWord().length() > overlaps.getSecondWord().length())) {

            overlapMap.put(word, newOverlapObject);
        }

    }

    public boolean allFragmentsIncluded(final String joinedSentence, final List<String> words){
        boolean allFragmentsIncluded = false;

        for(String word: words){
            allFragmentsIncluded = joinedSentence.contains(word);
            if(!allFragmentsIncluded) break;
        }



        return allFragmentsIncluded;
    }

    public static  List<String> removeSmallestWordAlreadyPresentInAnotherWord(List<String> words){

        List<String> primedList = new ArrayList<>(words);
        System.out.println("Initial size before priming " + primedList.size());
        for(String word : words){
            List<String> anotherList = new ArrayList<>(words);
            anotherList.remove(word);

            for(String nestedWord : anotherList){


            if(nestedWord.contains(word)){
                primedList.remove(word);
                System.out.println("Removed duplicate substring " + word);
               // continue;
            }
            }
        }
        System.out.println("After priming size " + primedList.size());
     //   primedList.stream().map(a-> a + ";").forEach(System.out::println);
        return primedList;
    }

    static Map<String, List<String>> findRelatedWords(String firstWord, List<String> fragmentedList){

        List<String> listOfRelatedWord = new ArrayList<>();


        Map<String, List<String>> overlapMappingList = new HashMap<>();


        for(String word: fragmentedList){
            if(listOfRelatedWord.contains(word)) continue;

            String overlap = isAnyOverlap(firstWord, word);

            if(overlap != "") {
             //   System.out.println(overlap + " -> " + firstWord + " -> " + word);
                listOfRelatedWord.add(word);


            }
        }


        listOfRelatedWord.add(firstWord);


        return overlapMappingList;

    }

    static String isAnyOverlap(String firstString, String secondString){

        boolean overlap = false;




        String matchedWord = getLongestCommonSubstring(firstString, secondString);

       /* if(firstString.contains(firstTwoCharacterOfSecondString)){
             overlappingString = firstString.substring(firstString.indexOf(firstTwoCharacterOfSecondString));


        }else if(secondString.contains(firstTwoCharacterOfFirstString)){
            overlappingString = secondString.substring(secondString.indexOf(firstTwoCharacterOfFirstString));
        }*/

        // We want String upto endIndex, we are using endIndex+1 in substring.

     //   System.out.println("===>" + matchedWord + " -> " + firstString + " -> " + secondString);

        return matchedWord.length()>2 &&
                (firstString.startsWith(matchedWord) && secondString.endsWith(matchedWord)) ||
                        (firstString.endsWith(matchedWord) && secondString.startsWith(matchedWord)) ? matchedWord : "";





    }

    public static String getLongestCommonSubstring(String str1, String str2){
        int m = str1.length();
        int n = str2.length();

        int max = 0;

        int[][] dp = new int[m][n];
        int endIndex=-1;
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                if(str1.charAt(i) == str2.charAt(j)){

                    // If first row or column
                    if(i==0 || j==0){
                        dp[i][j]=1;
                    }else{
                        // Add 1 to the diagonal value
                        dp[i][j] = dp[i-1][j-1]+1;
                    }

                    if(max < dp[i][j])
                    {
                        max = dp[i][j];
                        endIndex=i;
                    }
                }

            }
        }
        // We want String upto endIndex, we are using endIndex+1 in substring.
        return str1.substring(endIndex-max+1,endIndex+1);

    }

}
