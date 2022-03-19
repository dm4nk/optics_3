package com.dm4nk.optics_3;

import com.dm4nk.optics_3.model.Model;
import com.dm4nk.optics_3.utility.ExcelWriter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Model model = new Model();

        ExcelWriter.write("Restored Bessel Amplitude", model.getRestoredBesselAmplitude());
        ExcelWriter.write("Restored Bessel Phase", model.getRestoredBesselPhase());
    }
}
