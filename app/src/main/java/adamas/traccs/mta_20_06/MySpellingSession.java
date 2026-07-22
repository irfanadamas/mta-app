package adamas.traccs.mta_20_06;

import android.service.textservice.SpellCheckerService;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;

import java.util.ArrayList;
import java.util.List;

class MySpellingSession extends SpellCheckerService.Session {
    @Override
    public void onCreate() {
    }

    @Override
    public SuggestionsInfo onGetSuggestions(TextInfo textInfo, int suggestionsLimit) {
        String word = textInfo.getText();
        String[] suggestions = null;
        if(word.equals("Peter")){
            suggestions = new String[]{"Pedro", "Pietro", "Petar", "Pierre", "Petrus"};
        }else{
            suggestions = new String[]{};
        }
        SuggestionsInfo suggestionsInfo = new
                SuggestionsInfo(SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO, suggestions);
        return suggestionsInfo;
    }

    @Override
    public SentenceSuggestionsInfo[] onGetSentenceSuggestionsMultiple(TextInfo[] textInfos, int suggestionsLimit) {

        List<SuggestionsInfo> suggestionsInfos = new ArrayList<>();

        for(int i=0; i<textInfos.length; i++){
            TextInfo cur = textInfos[i];

            // Convert the sentence into an array of words
            String[] words = cur.getText().split("\\s+");
            for(String word:words){
                TextInfo tmp = new TextInfo(word);
                // Generate suggestions for each word
                suggestionsInfos.add(onGetSuggestions(tmp, suggestionsLimit));
            }
        }
        return new SentenceSuggestionsInfo[]{
                new SentenceSuggestionsInfo(
                        suggestionsInfos.toArray(new SuggestionsInfo[suggestionsInfos.size()]),
                        new int[suggestionsInfos.size()],
                        new int[suggestionsInfos.size()]
                )
        };
    }
}