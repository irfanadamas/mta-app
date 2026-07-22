package adamas.traccs.mta_20_06;

import com.google.android.material.circularreveal.CircularRevealHelper;

class Recipient_Strategy {
    private String PersonId;
    private String RecordNumber;
    private String Name;
    private String Strategy;

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String personId) {
        PersonId = personId;
    }

    public String getRecordNumber() {
        return RecordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        RecordNumber = recordNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStrategy() {
        return Strategy;
    }

    public void setStrategy(String strategy) {
        Strategy = strategy;
    }
}
