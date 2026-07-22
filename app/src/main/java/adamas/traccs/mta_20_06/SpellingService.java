package adamas.traccs.mta_20_06;

import android.service.textservice.SpellCheckerService;

public class SpellingService extends SpellCheckerService {

    @Override
    public Session createSession() {

        return  new MySpellingSession();
    }

}