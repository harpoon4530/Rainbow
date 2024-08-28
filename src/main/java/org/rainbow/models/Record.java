package org.rainbow.models;

import com.google.inject.Inject;
import org.rainbow.db.DbModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Record {

    private static final Logger logger = LoggerFactory.getLogger(Record.class);


    private int id;
    private String data;

    @Inject
    public Record(int id, String data) {
        this.id = id;
        this.data = data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;
        Record record = (Record) o;
        return id == record.id && data.equals(record.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }
}
