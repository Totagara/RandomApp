package random;

import com.randomapps.randomapp.enums.CardDeckType;
import com.randomapps.randomapp.enums.RockPaperScissorsType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLibrary {

    public ThreadLocalRandom randomObject = ThreadLocalRandom.current();

    /*public String GenerateRandomColor() {
        String colorId = null;
        Random rand = new Random();
        colorId = String.format("#%06x", rand.nextInt(256*256*256));
        return colorId;
    }*/

    /*public ArrayList<String> GenerateRandomColors(int howMany) {
        ArrayList<String> randomColors = null;
        if (howMany > 0) {
            randomColors = new ArrayList<>();
            for (int i = 0; i < howMany; i++) {
                String colorId = String.format("#%06x", randomObject.nextInt(256 * 256 * 256));

                //avoid repeated colors in the list
                while (randomColors.contains(colorId)){
                    colorId = String.format("#%06x", randomObject.nextInt(256 * 256 * 256));
                }
                randomColors.add(colorId);
            }
        }
        return randomColors;
    }*/

    /*public String[] GetRandomString(int minLength, int maxLength, int howMany,
                                    boolean allowalphaCaps, boolean allowalphaSmall, boolean allowNumerics,
                                    boolean allowSpecialChars, String specialCharSet){

        StringBuilder randomCharSet = new StringBuilder();
        if(allowalphaCaps){
            randomCharSet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if(allowalphaSmall){
            randomCharSet.append("abcdefghijklmnopqrstuvwxyz");
        }
        if(allowNumerics){
            randomCharSet.append("0123456789");
        }
        if(allowSpecialChars){
            if(specialCharSet.length() > 0){
                randomCharSet.append(specialCharSet);
            }
        }

        String[] randomStrings = new String[howMany];

        for (int count=0; count < randomStrings.length; count++) {
            int randomLength = (minLength == maxLength)?minLength:ThreadLocalRandom.current().nextInt(minLength, maxLength);
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < randomLength; i++) {
                double ran = Math.random();
                int len = randomCharSet.length();
                int ranInd = (int)(ran * len);
                *//*int index = (int)(randomCharSet.length()
                        * Math.random());*//*

                // add Character one by one in end of sb
                randomString.append(randomCharSet
                        .charAt(ranInd));
            }
            randomStrings[count] = randomString.toString();
        }
        return randomStrings;
    }*/


    /*public ArrayList<String> GetRandomDates(long startDate, long endDate, boolean isRepeatAllowed, int howMany, boolean onlyDates, boolean onlyTimes, SimpleDateFormat format, boolean is24HoursFormat){
        ArrayList<Date> randomDates = new ArrayList<Date>();
        ArrayList<String> randomDatesInFormat = new ArrayList<String>();
        //SimpleDateFormat dateFormat = null;
        SimpleDateFormat resultsFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        if(format != null){
            resultsFormat = format;
        }

        String dateText = null;
        for (int i = 0; i < howMany; i++) {
            long random = ThreadLocalRandom.current().nextLong(startDate, endDate);
            Date date = new Date(random);
            dateText = GetResultInTimeFormat(date, resultsFormat,is24HoursFormat, onlyDates, onlyTimes);
            if(isRepeatAllowed){
                //randomDates.add(date);
                randomDatesInFormat.add(dateText);
            } else {
                while (randomDatesInFormat.contains(dateText)){
                    random = ThreadLocalRandom.current().nextLong(startDate, endDate);
                    date = new Date(random);
                    dateText = GetResultInTimeFormat(date, resultsFormat,is24HoursFormat, onlyDates, onlyTimes);
                }
                randomDatesInFormat.add(dateText);
            }
        }
        return randomDatesInFormat;
    }*/

    /*String GetResultInTimeFormat(Date date, SimpleDateFormat resultsFormat, boolean is24HoursFormat, boolean onlyDates, boolean onlyTimes){
        String timeText = null;
        if(is24HoursFormat){
            return resultsFormat.format(date);
        } else if(onlyDates && !onlyTimes){
            return resultsFormat.format(date);
        } else if(onlyDates && onlyTimes){
            int hour = 0;
            hour = Integer.parseInt(resultsFormat.format(date).split(" ")[1].split(":")[0]);
            if (hour > 12) {
                timeText = (hour - 12) + ":" + resultsFormat.format(date).split(" ")[1].split(":")[1] + " PM";
            } else {
                timeText = hour + ":" + resultsFormat.format(date).split(" ")[1].split(":")[1] + " AM";
            }
            return resultsFormat.format(date).split(" ")[0] + " " + timeText;
        } else if(!onlyDates && onlyTimes){
            int hour = 0;
            hour = Integer.parseInt(resultsFormat.format(date).split(":")[0]);
            if (hour > 12) {
                timeText = (hour - 12) + ":" + resultsFormat.format(date).split(":")[1] + " PM";
            } else {
                timeText = hour + ":" + resultsFormat.format(date).split(":")[1] + " AM";
            }
            return timeText;
        }
        return null;
    }*/

    /*public ArrayList<String> GenerateUUIDs(int quantity) {
        ArrayList<String> randomUUIDs = new ArrayList<String>();
        for (int i = 0; i < quantity; i++) {
            randomUUIDs.add(UUID.randomUUID().toString());
        }
        return randomUUIDs;
    }*/

    /*public BigInteger GetTheDoubleRange(double minVal, double maxVal, int precision) {
        String precisionFormatter = GetPrecisionFormatter(precision);
        DecimalFormat df = new DecimalFormat("#." + precisionFormatter);

        double minimum = Double.parseDouble(df.format(minVal));
        double maximum = Double.parseDouble(df.format(maxVal));

        String[] minDigits = String.valueOf(minimum).split(Pattern.quote("."));
        String[] maxDigits = String.valueOf(maximum).split(Pattern.quote("."));

        if(minDigits[1].length() < precision){
            while(minDigits[1].length() < precision)
                minDigits[1] += "0";
        }
        if(maxDigits[1].length() < precision){
            while(maxDigits[1].length() < precision)
                maxDigits[1] += "0";
        }

        BigInteger minRange = new BigInteger(minDigits[0] + minDigits[1]);
        BigInteger maxRange = new BigInteger(maxDigits[0] + maxDigits[1]);
        BigInteger range =  maxRange.subtract(minRange);
        return range;
    }*/

    /*private String GetPrecisionFormatter(int precision) {
        String formatter = "";
        for (int i = 0; i < precision; i++) {
            formatter += "#";
        }
        return formatter;
    }*/

    /*public ArrayList<Double> GenerateRandomNumbersForDoubles(Double minVal, Double maxVal, int quantityVal, int pricision, boolean repeatAllowed, boolean sortNeeded, Boolean ascSort) {
        ArrayList<Double> randomNumbers = new ArrayList<>();
        boolean needSorting = false, isAscendingSort = false;
        Random randomGenerator = new Random();

        needSorting = sortNeeded | needSorting;
        isAscendingSort = sortNeeded && ascSort?true:false;

        for (int i = 0; i < quantityVal; i++) {
            //For API < 21
            //Double randomNumber = randomGenerator.nextInt(maxVal-minVal) + minVal;

            Double randomNumber = GenerateDoubleRandomNumber(minVal, maxVal, pricision);;
            if(repeatAllowed){
                randomNumbers.add(randomNumber);
            } else {
                int j=0;
                while (randomNumbers.contains(randomNumber)){
                    randomNumber = GenerateDoubleRandomNumber(minVal, maxVal, pricision);
                    j++;
                }
                int x = j;
                randomNumbers.add(randomNumber);
            }
        }

        //sort the array
        if(needSorting){
            if(isAscendingSort){
                Collections.sort(randomNumbers);
            }
            else {
                Collections.sort(randomNumbers, Collections.reverseOrder());
            }
        }
        return randomNumbers;
    }*/

    /*double GenerateDoubleRandomNumber(Double minVal, Double maxVal, int pricision){
        //Double randomNumber = ThreadLocalRandom.current().nextDouble(minVal, maxVal);
        //return RoundUp(randomNumber, pricision);
        return RoundUp(ThreadLocalRandom.current().nextDouble(minVal, maxVal), pricision);
    }*/

    /*private double RoundUp(double val, int precision){
        if (precision < 0)
            precision = 1;

        BigDecimal bd = new BigDecimal(Double.toString(val));
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/

    /*public ArrayList<Integer> GenerateRandomNumbersForIntegers(int minVal, int maxVal, int quantityVal, boolean repeatAllowed, boolean sortNeeded, Boolean ascSort) {
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        boolean needSorting = false, isAscendingSort = false;
        Random randomGenerator = new Random();

        needSorting = sortNeeded | needSorting;
        isAscendingSort = sortNeeded && ascSort?true:false;
        randomNumbers = ProduceRandomNumbers(minVal, maxVal, quantityVal, repeatAllowed);

        //sort the array
        if(needSorting){
            if(isAscendingSort){
                Collections.sort(randomNumbers);
            }
            else {
                Collections.sort(randomNumbers, Collections.reverseOrder());
            }
        }
        return randomNumbers;
    }*/

    /*private ArrayList<Integer> ProduceRandomNumbers(int minVal, int maxVal, int quantityVal, boolean repeatAllowed) {
        ArrayList<Integer> randomNumbers = new ArrayList<Integer>();
        for (int i = 0; i < quantityVal; i++) {
            //Integer randomNumber = randomGenerator.nextInt(maxVal-minVal) + minVal;
            Integer randomNumber = ThreadLocalRandom.current().nextInt(minVal, maxVal);
            if(repeatAllowed){
                randomNumbers.add(randomNumber);
            } else {
                while (randomNumbers.contains(randomNumber)){
                    randomNumber = ThreadLocalRandom.current().nextInt(minVal, maxVal);
                }
                randomNumbers.add(randomNumber);
            }
        }
        return randomNumbers;
    }*/

    /*public char GetRandomCardNumber() {
        char selectedCardChar;
        StringBuilder randomCharSet = new StringBuilder();
        //T is ten as single character need to be selected
        randomCharSet.append("A23456789TJKQ");
        double ran = Math.random();
        int len = randomCharSet.length();
        int ranInd = (int)(ran * len);
        selectedCardChar = randomCharSet.charAt(ranInd);
        return selectedCardChar;
    }

    public char GetRandomCardNumber(CardDeckType deckType) {
        char selectedCardChar;
        StringBuilder randomCharSet = new StringBuilder();

        //T for 10
        //T is ten as single character need to be selected
        randomCharSet.append("A23456789TJKQ");
        double ran = Math.random();
        int len = randomCharSet.length();
        int ranInd = (int)(ran * len);
        selectedCardChar = randomCharSet.charAt(ranInd);
        return selectedCardChar;
    }*/

    /*public String GetRandomCardCategory(boolean includeJokers) {
        ArrayList<String> cardCategories = new ArrayList<>();
        int randIndex;
        cardCategories.add("Spade");
        cardCategories.add("Club");
        cardCategories.add("Diamond");
        cardCategories.add("Heart");

        if (includeJokers){
            cardCategories.add("Joker");
            randIndex = randomObject.nextInt(0,5);
        } else {
            randIndex = randomObject.nextInt(0,4);
        }
        return cardCategories.get(randIndex);
    }*/

    /*public String GetRandomCardCategory(CardDeckType deckType) {
        ArrayList<String> cardCategories = new ArrayList<>();
        int randIndex;
        cardCategories.add("Spade");
        cardCategories.add("Club");
        cardCategories.add("Diamond");
        cardCategories.add("Heart");

        if (deckType == CardDeckType.Cards_54 || deckType == CardDeckType.Cards_56){
            cardCategories.add("Joker");
            randIndex = randomObject.nextInt(0,5);
        } else {
            randIndex = randomObject.nextInt(0,4);
        }
        return cardCategories.get(randIndex);
    }*/

    //Without remove card option
    /*public String GetRandomCard(CardDeckType deckType){
        String selectedCard = null;
        ArrayList<String> cardsSet = new ArrayList<>();

        //Spades
        cardsSet.add("sp_a");
        cardsSet.add("sp_2");
        cardsSet.add("sp_3");
        cardsSet.add("sp_4");
        cardsSet.add("sp_5");
        cardsSet.add("sp_6");
        cardsSet.add("sp_7");
        cardsSet.add("sp_8");
        cardsSet.add("sp_9");
        cardsSet.add("sp_10");
        cardsSet.add("sp_j");
        cardsSet.add("sp_q");
        cardsSet.add("sp_k");

        //Hearts
        cardsSet.add("he_a");
        cardsSet.add("he_2");
        cardsSet.add("he_3");
        cardsSet.add("he_4");
        cardsSet.add("he_5");
        cardsSet.add("he_6");
        cardsSet.add("he_7");
        cardsSet.add("he_8");
        cardsSet.add("he_9");
        cardsSet.add("he_10");
        cardsSet.add("he_j");
        cardsSet.add("he_q");
        cardsSet.add("he_k");

        //Diamonds
        cardsSet.add("di_a");
        cardsSet.add("di_2");
        cardsSet.add("di_3");
        cardsSet.add("di_4");
        cardsSet.add("di_5");
        cardsSet.add("di_6");
        cardsSet.add("di_7");
        cardsSet.add("di_8");
        cardsSet.add("di_9");
        cardsSet.add("di_10");
        cardsSet.add("di_j");
        cardsSet.add("di_q");
        cardsSet.add("di_k");

        //Clubs
        cardsSet.add("cl_a");
        cardsSet.add("cl_2");
        cardsSet.add("cl_3");
        cardsSet.add("cl_4");
        cardsSet.add("cl_5");
        cardsSet.add("cl_6");
        cardsSet.add("cl_7");
        cardsSet.add("cl_8");
        cardsSet.add("cl_9");
        cardsSet.add("cl_10");
        cardsSet.add("cl_j");
        cardsSet.add("cl_q");
        cardsSet.add("cl_k");

        if(deckType == CardDeckType.Cards_54){
            cardsSet.add("joker");
            cardsSet.add("joker");
        } else if(deckType == CardDeckType.Cards_56){
            cardsSet.add("joker");
            cardsSet.add("joker");
            cardsSet.add("joker");
            cardsSet.add("joker");
        }

        double ran = Math.random();
        int len = cardsSet.size();
        int ranIndex = (int)(ran * len);
        selectedCard = cardsSet.get(ranIndex);
        return selectedCard;
    }*/

    //With remove card option
    public String GetRandomCard(CardDeckType deckType, boolean removeGenerated, List<String> selectedCards){
        String selectedCard = null;
        ArrayList<String> cardsSet = new ArrayList<>();

        //Get cardsSet based on the selected decktype
        cardsSet.addAll(GetCardsSet(deckType));

        /*//Spades
        cardsSet.add("sp_a");
        cardsSet.add("sp_2");
        cardsSet.add("sp_3");
        cardsSet.add("sp_4");
        cardsSet.add("sp_5");
        cardsSet.add("sp_6");
        cardsSet.add("sp_7");
        cardsSet.add("sp_8");
        cardsSet.add("sp_9");
        cardsSet.add("sp_10");
        cardsSet.add("sp_j");
        cardsSet.add("sp_q");
        cardsSet.add("sp_k");

        //Hearts
        cardsSet.add("he_a");
        cardsSet.add("he_2");
        cardsSet.add("he_3");
        cardsSet.add("he_4");
        cardsSet.add("he_5");
        cardsSet.add("he_6");
        cardsSet.add("he_7");
        cardsSet.add("he_8");
        cardsSet.add("he_9");
        cardsSet.add("he_10");
        cardsSet.add("he_j");
        cardsSet.add("he_q");
        cardsSet.add("he_k");

        //Diamonds
        cardsSet.add("di_a");
        cardsSet.add("di_2");
        cardsSet.add("di_3");
        cardsSet.add("di_4");
        cardsSet.add("di_5");
        cardsSet.add("di_6");
        cardsSet.add("di_7");
        cardsSet.add("di_8");
        cardsSet.add("di_9");
        cardsSet.add("di_10");
        cardsSet.add("di_j");
        cardsSet.add("di_q");
        cardsSet.add("di_k");

        //Clubs
        cardsSet.add("cl_a");
        cardsSet.add("cl_2");
        cardsSet.add("cl_3");
        cardsSet.add("cl_4");
        cardsSet.add("cl_5");
        cardsSet.add("cl_6");
        cardsSet.add("cl_7");
        cardsSet.add("cl_8");
        cardsSet.add("cl_9");
        cardsSet.add("cl_10");
        cardsSet.add("cl_j");
        cardsSet.add("cl_q");
        cardsSet.add("cl_k");

        if(deckType == CardDeckType.Cards_54){
            cardsSet.add("joker1");
            cardsSet.add("joker2");
        } else if(deckType == CardDeckType.Cards_56){
            cardsSet.add("joker1");
            cardsSet.add("joker2");
            cardsSet.add("joker3");
            cardsSet.add("joker4");
        }*/

        if(removeGenerated && selectedCards != null && selectedCards.size() > 0){
        List<String> removableCards = new ArrayList<>();
        removableCards.addAll(selectedCards);
        cardsSet.removeAll(removableCards);
        }

        double ran = Math.random();
        int len = cardsSet.size();
        int ranIndex = (int)(ran * len);

        if(len != 0) {
            selectedCard = cardsSet.get(ranIndex);
        }

        int selectedCardsSize = (selectedCards == null)?0:selectedCards.size();
        //Log.i("Logs", "selectedCards: " + selectedCardsSize + ", removeGenerated: "+ removeGenerated + ", ranIndex: " + ranIndex +", len: " + len + ", selectedCard: " + selectedCard);
        return selectedCard;
    }

    private Collection<? extends String> GetCardsSet(CardDeckType deckType) {
        ArrayList<String> cardsSet = new ArrayList<>();

        if(deckType == CardDeckType.Cards_52) {
            //Spades
            cardsSet.add("sp_a");
            cardsSet.add("sp_2");
            cardsSet.add("sp_3");
            cardsSet.add("sp_4");
            cardsSet.add("sp_5");
            cardsSet.add("sp_6");
            cardsSet.add("sp_7");
            cardsSet.add("sp_8");
            cardsSet.add("sp_9");
            cardsSet.add("sp_10");
            cardsSet.add("sp_j");
            cardsSet.add("sp_q");
            cardsSet.add("sp_k");

            //Hearts
            cardsSet.add("he_a");
            cardsSet.add("he_2");
            cardsSet.add("he_3");
            cardsSet.add("he_4");
            cardsSet.add("he_5");
            cardsSet.add("he_6");
            cardsSet.add("he_7");
            cardsSet.add("he_8");
            cardsSet.add("he_9");
            cardsSet.add("he_10");
            cardsSet.add("he_j");
            cardsSet.add("he_q");
            cardsSet.add("he_k");

            //Diamonds
            cardsSet.add("di_a");
            cardsSet.add("di_2");
            cardsSet.add("di_3");
            cardsSet.add("di_4");
            cardsSet.add("di_5");
            cardsSet.add("di_6");
            cardsSet.add("di_7");
            cardsSet.add("di_8");
            cardsSet.add("di_9");
            cardsSet.add("di_10");
            cardsSet.add("di_j");
            cardsSet.add("di_q");
            cardsSet.add("di_k");

            //Clubs
            cardsSet.add("cl_a");
            cardsSet.add("cl_2");
            cardsSet.add("cl_3");
            cardsSet.add("cl_4");
            cardsSet.add("cl_5");
            cardsSet.add("cl_6");
            cardsSet.add("cl_7");
            cardsSet.add("cl_8");
            cardsSet.add("cl_9");
            cardsSet.add("cl_10");
            cardsSet.add("cl_j");
            cardsSet.add("cl_q");
            cardsSet.add("cl_k");
        } else if(deckType == CardDeckType.Cards_54){
            //Spades
            cardsSet.add("sp_a");
            cardsSet.add("sp_2");
            cardsSet.add("sp_3");
            cardsSet.add("sp_4");
            cardsSet.add("sp_5");
            cardsSet.add("sp_6");
            cardsSet.add("sp_7");
            cardsSet.add("sp_8");
            cardsSet.add("sp_9");
            cardsSet.add("sp_10");
            cardsSet.add("sp_j");
            cardsSet.add("sp_q");
            cardsSet.add("sp_k");

            //Hearts
            cardsSet.add("he_a");
            cardsSet.add("he_2");
            cardsSet.add("he_3");
            cardsSet.add("he_4");
            cardsSet.add("he_5");
            cardsSet.add("he_6");
            cardsSet.add("he_7");
            cardsSet.add("he_8");
            cardsSet.add("he_9");
            cardsSet.add("he_10");
            cardsSet.add("he_j");
            cardsSet.add("he_q");
            cardsSet.add("he_k");

            //Diamonds
            cardsSet.add("di_a");
            cardsSet.add("di_2");
            cardsSet.add("di_3");
            cardsSet.add("di_4");
            cardsSet.add("di_5");
            cardsSet.add("di_6");
            cardsSet.add("di_7");
            cardsSet.add("di_8");
            cardsSet.add("di_9");
            cardsSet.add("di_10");
            cardsSet.add("di_j");
            cardsSet.add("di_q");
            cardsSet.add("di_k");

            //Clubs
            cardsSet.add("cl_a");
            cardsSet.add("cl_2");
            cardsSet.add("cl_3");
            cardsSet.add("cl_4");
            cardsSet.add("cl_5");
            cardsSet.add("cl_6");
            cardsSet.add("cl_7");
            cardsSet.add("cl_8");
            cardsSet.add("cl_9");
            cardsSet.add("cl_10");
            cardsSet.add("cl_j");
            cardsSet.add("cl_q");
            cardsSet.add("cl_k");

            cardsSet.add("joker1");
            cardsSet.add("joker2");
        } else if(deckType == CardDeckType.Cards_56){
            //Spades
            cardsSet.add("sp_a");
            cardsSet.add("sp_2");
            cardsSet.add("sp_3");
            cardsSet.add("sp_4");
            cardsSet.add("sp_5");
            cardsSet.add("sp_6");
            cardsSet.add("sp_7");
            cardsSet.add("sp_8");
            cardsSet.add("sp_9");
            cardsSet.add("sp_10");
            cardsSet.add("sp_j");
            cardsSet.add("sp_q");
            cardsSet.add("sp_k");

            //Hearts
            cardsSet.add("he_a");
            cardsSet.add("he_2");
            cardsSet.add("he_3");
            cardsSet.add("he_4");
            cardsSet.add("he_5");
            cardsSet.add("he_6");
            cardsSet.add("he_7");
            cardsSet.add("he_8");
            cardsSet.add("he_9");
            cardsSet.add("he_10");
            cardsSet.add("he_j");
            cardsSet.add("he_q");
            cardsSet.add("he_k");

            //Diamonds
            cardsSet.add("di_a");
            cardsSet.add("di_2");
            cardsSet.add("di_3");
            cardsSet.add("di_4");
            cardsSet.add("di_5");
            cardsSet.add("di_6");
            cardsSet.add("di_7");
            cardsSet.add("di_8");
            cardsSet.add("di_9");
            cardsSet.add("di_10");
            cardsSet.add("di_j");
            cardsSet.add("di_q");
            cardsSet.add("di_k");

            //Clubs
            cardsSet.add("cl_a");
            cardsSet.add("cl_2");
            cardsSet.add("cl_3");
            cardsSet.add("cl_4");
            cardsSet.add("cl_5");
            cardsSet.add("cl_6");
            cardsSet.add("cl_7");
            cardsSet.add("cl_8");
            cardsSet.add("cl_9");
            cardsSet.add("cl_10");
            cardsSet.add("cl_j");
            cardsSet.add("cl_q");
            cardsSet.add("cl_k");

            cardsSet.add("joker1");
            cardsSet.add("joker2");
            cardsSet.add("joker3");
            cardsSet.add("joker4");
        }
        return cardsSet;
    }

    /*public ArrayList<ArrayList<String>> GenerateSeries(int howMany, int seriesSize, List<String> seriesItemsList, Boolean excludeGeneratedItems) {
        ArrayList<ArrayList<String>> generatedSeries = new ArrayList<>();
        List<String> itemSet = seriesItemsList;

        for (int i = 0; i < howMany; i++) {
            ArrayList<String> series = GetShuffledSeries(seriesSize, itemSet);
            while (generatedSeries.contains(series)){
                series = GetShuffledSeries(seriesSize, itemSet);
            }
            generatedSeries.add(series);
            if(excludeGeneratedItems){
                itemSet.removeAll(series);
            }
        }
        return generatedSeries;
    }*/

    /*private ArrayList<String> GetShuffledSeries(int seriesSize, List<String> itemSet) {
        ArrayList<String> series = new ArrayList<>();
        Collections.shuffle(itemSet);

        for (int i = 0; i < seriesSize ; i++) {
            String item = itemSet.get(randomObject.nextInt(itemSet.size()));
            while (series.contains(item)){
                item = itemSet.get(randomObject.nextInt(itemSet.size()));
            }
            series.add(item);
        }
        return series;
    }*/

    /*public BigInteger GetPossibleSeriesCount(int seriesSize, int howMany, Boolean excludeSetsWithSameItems, Boolean excludeGeneratedItems, int itemsCount) {
        BigInteger nFact, rFact, n_rFact;
        if(excludeGeneratedItems){ // itemsCount/seriesSize
            return BigInteger.valueOf(itemsCount/seriesSize);
        } else if(excludeSetsWithSameItems){ // nCr = n!/((n-r)! * r!)
            nFact = factorial(itemsCount);
            n_rFact = factorial(itemsCount - seriesSize);
            rFact = factorial(seriesSize);
            BigInteger n_rFact_Mul_rFact = n_rFact.multiply(rFact);
            BigInteger nCr = nFact.divide(n_rFact_Mul_rFact);
            return nCr;
        } else { // nPr = = n!/(n-r)!
            nFact = factorial(itemsCount);
            n_rFact = factorial(itemsCount - seriesSize);
            BigInteger nPr = nFact.divide(n_rFact);
            return nPr;
        }
    }*/

    /*BigInteger factorial(int N)
    {
        // Initialize result
        BigInteger f = new BigInteger("1"); // Or BigInteger.ONE

        // Multiply f with 2, 3, ...N
        for (int i = 2; i <= N; i++)
            f = f.multiply(BigInteger.valueOf(i));

        return f;
    }*/

    public String GetRandomRpsResult(RockPaperScissorsType rpsType) {
        String randomResult = null;
        ArrayList<String> rpsSet = new ArrayList<>();

        //R-P-S
        rpsSet.add("rock");
        rpsSet.add("paper");
        rpsSet.add("scissors");

        //R-P-S-L-S
        if(rpsType == RockPaperScissorsType.RPSLS){
            rpsSet.add("lizard");
            rpsSet.add("spock");
        }

        double ran = Math.random();
        int len = rpsSet.size();
        int ranIndex = (int)(ran * len);
        randomResult = rpsSet.get(ranIndex);
        return randomResult;
    }
}
