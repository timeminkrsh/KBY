package com.about.kby.Interface;

import androidx.annotation.NonNull;

import java.util.Date;

public interface DateSelectionListener {


    void dateSelected(@NonNull Date date);

    void dateUnselected(@NonNull Date date);
}
