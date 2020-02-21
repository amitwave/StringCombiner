package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class AmitRanjan {

    public static void main(String[] args) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            in.lines()
                    .map(AmitRanjan::reassemble)
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
            combinedWordsList = joinRelatedWords(combinedWordsList);
          }

          if(combinedWordsList.size() ==1) {
              return combinedWordsList.get(0);
          } else{
            return "";
          }
    }

    public static String combineOverlappingWords(String overlap, String firstWord, String secondWord) {


        String combinedWord = "";
        if(firstWord.startsWith(overlap)){
            firstWord = firstWord.replace(overlap, "");
            combinedWord = secondWord + firstWord;
        }else if(secondWord.startsWith(overlap)){
            secondWord = secondWord.replace(overlap, "");
            combinedWord = firstWord + secondWord ;
        }
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


        List<String> visitedWords = new ArrayList<>();
        List<String> combinedWordsList = new ArrayList<>();

         Map<String, Overlap> overlapMap = new HashMap<>();

        for(String word : words){
            visitedWords.add(word);

            List<String> anotherList = new ArrayList<>(words);
            anotherList.remove(word);

            String firstWord = combinedWord.isEmpty()? word : combinedWord;

            for(String nestedWord : anotherList){

                if(combinedWordsList.contains(nestedWord))
                    continue;

                String overlap = isAnyOverlap(firstWord, nestedWord);

                if(overlap.length()>0){
                    addOverlappingDetail(word, overlapMap, nestedWord, overlap);

                }

            }



        }


        for(String word : words){

            Overlap overlap =  overlapMap.get(word);
            if(overlap == null) {
                continue;
            } else {
                String combinedString = combineOverlappingWords(overlap.getOverlap(), overlap.getFirstWord(), overlap.getSecondWord());

                if(!concatenatedWords.contains(combinedString)) {
                    concatenatedWords.add(combinedString);
                }

            }
        }

        return concatenatedWords;
    }

    private static void addOverlappingDetail(String word, Map<String, Overlap> overlapMap, String nestedWord, String overlap) {

        Overlap overlaps = overlapMap.get(word);

        Overlap newOverlapObject = new Overlap(overlap, word, nestedWord);

        if(overlaps == null || (newOverlapObject.getOverlap().length() > overlaps.getOverlap().length() || newOverlapObject.getSecondWord().length() > overlaps.getSecondWord().length())) {

            overlapMap.put(word, newOverlapObject);
        }

    }



    public static  List<String> removeSmallestWordAlreadyPresentInAnotherWord(List<String> words){

        List<String> primedList = new ArrayList<>(words);
        for(String word : words){
            List<String> anotherList = new ArrayList<>(words);
            anotherList.remove(word);

            for(String nestedWord : anotherList){


            if(nestedWord.contains(word)){
                primedList.remove(word);
            }
            }
        }
        return primedList;
    }


    static String isAnyOverlap(String firstString, String secondString){

      String matchedWord = getLongestCommonSubstring(firstString, secondString);


        return matchedWord.length()>=2 &&
                (firstString.startsWith(matchedWord) && secondString.endsWith(matchedWord)) ||
                        (firstString.endsWith(matchedWord) && secondString.startsWith(matchedWord)) ? matchedWord : "";

    }


    public static String getLongestCommonSubstring(String str1, String str2){

        String commonSubstring = "";
        if(str1 == null || str2 == null) {
            return "";
        }else if(str1.equals(str2)){
            return str1;
        }else{



            String match1 = findLongestMatch(str1, str2);

            commonSubstring =  match1;

        }


        return commonSubstring;
    }

    private static String findLongestMatch(String str1, String str2) {
        String mathingString = str2.substring(0, 2);
        int lastIndexOfFirstCharacterOfSecondString = -1;

        List<String> subList = new ArrayList<>();
        String match = "";
        if(str1.contains(mathingString)){
            lastIndexOfFirstCharacterOfSecondString = 0; //str1.indexOf(mathingString);
            while(lastIndexOfFirstCharacterOfSecondString != -1) {
                lastIndexOfFirstCharacterOfSecondString = str1.indexOf(mathingString, lastIndexOfFirstCharacterOfSecondString);

                if(lastIndexOfFirstCharacterOfSecondString == -1) {
                    break;
                }

                String substring = str1.substring(lastIndexOfFirstCharacterOfSecondString);
                if(str2.startsWith(substring) && (match.length() <= substring.length())) {
                    match = substring;
                }
                if(str1.length()-1 > lastIndexOfFirstCharacterOfSecondString) {
                    lastIndexOfFirstCharacterOfSecondString += 1;
                }else{
                    lastIndexOfFirstCharacterOfSecondString = -1;
                }
            }


        }

        return match;
    }


}
