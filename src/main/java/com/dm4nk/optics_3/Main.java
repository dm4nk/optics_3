package com.dm4nk.optics_3;

import com.dm4nk.optics_3.model.Model;
import com.dm4nk.optics_3.utility.ExcelWriter;

import javax.script.ScriptException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ScriptException {
        Model model = new Model();

        ExcelWriter.write("Restored Bessel Amplitude", model.getRestoredBesselAmplitude());
        ExcelWriter.write("Restored Bessel Phase", model.getRestoredBesselPhase());

        ExcelWriter.write("Restored Hankel transformed Amplitude", model.getRestoredHankelTransformedAmplitude());
        ExcelWriter.write("Restored Hankel transformed Phase", model.getRestoredHankelTransformedPhase());

        ExcelWriter.write("FFT transformed Amplitude", model.getFftTransformedAmplitude());
        ExcelWriter.write("FFT transformed Phase", model.getFftTransformedPhase());
    }
}
