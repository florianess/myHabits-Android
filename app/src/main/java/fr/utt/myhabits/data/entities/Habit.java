package fr.utt.myhabits.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "habits")
public class Habit {
    @PrimaryKey
    @NonNull
    private String name;
    private String description;
    private String category;
    private String repetition;
    @ColumnInfo(name = "repetition_label")
    private String repetitionLabel;

    public Habit(String name, String description, String category, String repetitionLabel, String repetition) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.repetitionLabel = repetitionLabel;
        this.repetition = repetition;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getRepetition() {
        return repetition;
    }

    public String getRepetitionLabel() {
        return repetitionLabel;
    }
}
